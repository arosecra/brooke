package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.github.arosecra.brooke.services.LibraryCacheService;

public class SynchronizeTask implements IRunnableTask {

	private static class FilePair {
		private File source;
		private File destination;
	}

	private AtomicLong totalProgress = new AtomicLong(0L);
	private AtomicLong copied = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private List<CollectionApiModel> collections;
	private String currentProgressDescription;
	private String totalProgressDescription;
	private String jobDescription;
	private LibraryCacheService libraryCacheService;

	public SynchronizeTask(LibraryCacheService libraryCacheService, List<CollectionApiModel> collections) {
		this.libraryCacheService = libraryCacheService;
		this.collections = collections;
	}

	@Override
	public AtomicBoolean started() {
		return this.started;
	}

	@Override
	public AtomicLong getTotalProgress() {
		return this.totalProgress;
	}

	@Override
	public AtomicLong getCurrentProgress() {
		return this.copied;
	}

	@Override
	public String getJobDescription() {
		return this.jobDescription;
	}

	@Override
	public String getJobType() {
		return "Sync";
	}

	@Override
	public String getCurrentProgressDescription() {
		return this.currentProgressDescription;
	}

	@Override
	public String getTotalProgressDescription() {
		return this.totalProgressDescription;
	}

	@Override
	public void run() {
		this.totalProgress.set(this.collections.size());
		this.started.set(true);
		this.currentProgressDescription = "Collections Prepared";
		this.totalProgressDescription = "Collections";
		this.jobDescription = "Preparing to Sync";
		//determine the list of files to copy
		
		try {
			List<FilePair> filesToCopy = this.getFilesToCopy();

			this.currentProgressDescription = "Copied";
			this.totalProgressDescription = "Copies Required";
			this.jobDescription = "Syncrhonizing";
			this.totalProgress.set(filesToCopy.size()+collections.size());
	
			//once we have the list of files to copy, update the progress total & start copying
			for(FilePair filepair : filesToCopy) {
				FileUtils.copyFile(filepair.source, filepair.destination);
				if(this.copied.get() == this.totalProgress.get()) {
					this.libraryCacheService.reload();
				}
				this.copied.set(this.copied.get()+1);
			}
		} catch (IOException e) {
			this.totalProgress.set(1L);
			this.copied.set(1L);
		}
	}

	private List<FilePair> getFilesToCopy() throws IOException {
		List<FilePair> result = new ArrayList<>();
		for(CollectionApiModel collection : this.collections) {
			result.addAll(this.syncCollection(collection));
			this.copied.set(this.copied.get()+1);
		}

		return result;
	}

	private List<FilePair> syncCollection(CollectionApiModel collection) throws IOException {
		File localBaseDir = new File(collection.getLocalDirectory());
		File remoteBaseDir = new File(collection.getRemoteDirectory());

		// for each remote file, compute the local spot for it
		// create a .item file there if the file doesn't exist
		// copy all other files
		List<FilePair> files = new ArrayList<>();
		for (File extensionFile : FileUtils.listFiles(remoteBaseDir, new String[] { collection.getItemExtension() },
				true)) {

			files.addAll(syncExtensionFile(collection, localBaseDir, remoteBaseDir, extensionFile));
		}
		return files;
	}

	private List<FilePair> syncExtensionFile(CollectionApiModel collection, File localBaseDir, File remoteBaseDir,
			File extensionFile)
			throws IOException {
		List<FilePair> results = new ArrayList<>();
		String relativePath = extensionFile.getAbsolutePath().substring(remoteBaseDir.getAbsolutePath().length());
		// System.out.println(relativePath);
		File localFile = new File(localBaseDir, relativePath);

		results.addAll(copyItemDirectoryFiles(collection, extensionFile, localFile));

		// copy loose files from the parent folder so that we get series thumbnails, etc
		results.addAll(copyItemParentDirectoryFiles(collection, extensionFile, localFile));
		return results;
	}

	private List<FilePair> copyItemDirectoryFiles(CollectionApiModel collection, File extensionFile, File localFile)
			throws IOException {
		List<FilePair> results = new ArrayList<>();
		for (File sibling : extensionFile.getParentFile().listFiles()) {
			File localSibling = new File(localFile.getParentFile(), sibling.getName());
			if (shouldCopyFile(collection, sibling, localSibling)) {
				FilePair filePair = new FilePair();
				filePair.source = sibling;
				filePair.destination = localSibling;
				results.add(filePair);
			}
		}
		return results;
	}

	private List<FilePair> copyItemParentDirectoryFiles(CollectionApiModel collection, File extensionFile, File localFile)
			throws IOException {
				List<FilePair> results = new ArrayList<>();
		for (File parentSibling : extensionFile.getParentFile().getParentFile().listFiles()) {

			File localSibling = new File(localFile.getParentFile().getParentFile(), parentSibling.getName());
			if (shouldCopyFile(collection, parentSibling, localSibling)) {
				FilePair filePair = new FilePair();
				filePair.source = parentSibling;
				filePair.destination = localSibling;
				results.add(filePair);
			}
		}
		return results;
	}

	public boolean shouldCopyFile(CollectionApiModel collection, File sourceFile, File destFile) {
		boolean sourceIsFile = sourceFile.isFile();
		String ext = FilenameUtils.getExtension(sourceFile.getName());
		boolean sourceIsMainFile = ext.equals(collection.getItemExtension());
		boolean extensionIsExcluded = collection.getExcludeExtensions().contains(ext);
		boolean destExists = destFile.exists();
		boolean result = sourceIsFile && !sourceIsMainFile && !extensionIsExcluded && !destExists;

		return result;
	}

}
