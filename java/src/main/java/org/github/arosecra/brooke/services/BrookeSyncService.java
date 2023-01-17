package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.model.api.CollectionApiModel;
import org.springframework.stereotype.Component;

@Component
public class BrookeSyncService {

	public void sync(List<CollectionApiModel> collections) throws IOException {
		for (CollectionApiModel collection : collections) {
			syncCollection(collection);
		}
	}

	private void syncCollection(CollectionApiModel collection) throws IOException {
		File localBaseDir = new File(collection.getLocalDirectory());
		File remoteBaseDir = new File(collection.getRemoteDirectory());

		// for each remote file, compute the local spot for it
		// create a .item file there if the file doesn't exist
		// copy all other files

		for (File extensionFile : FileUtils.listFiles(remoteBaseDir, new String[] { collection.getItemExtension() },
				true)) {

			syncExtensionFile(collection, localBaseDir, remoteBaseDir, extensionFile);
		}
	}

	private void syncExtensionFile(CollectionApiModel collection, File localBaseDir, File remoteBaseDir,
			File extensionFile)
			throws IOException {
		String relativePath = extensionFile.getAbsolutePath().substring(remoteBaseDir.getAbsolutePath().length());
		// System.out.println(relativePath);
		File localFile = new File(localBaseDir, relativePath);
		createLocalDirectory(localFile);

		copyItemDirectoryFiles(collection, extensionFile, localFile);

		// copy loose files from the parent folder so that we get series thumbnails, etc
		copyItemParentDirectoryFiles(collection, extensionFile, localFile);
	}

	private void createLocalDirectory(File localFile) throws IOException {
		if (!localFile.exists()) {
			localFile.getParentFile().mkdirs();
			String basename = FilenameUtils.getBaseName(localFile.getName());
			File placeholder = new File(localFile.getParentFile(), basename + ".item");
			placeholder.createNewFile();
		}
	}

	private void copyItemDirectoryFiles(CollectionApiModel collection, File extensionFile, File localFile)
			throws IOException {
		for (File sibling : extensionFile.getParentFile().listFiles()) {
			File localSibling = new File(localFile.getParentFile(), sibling.getName());
			if (shouldCopyFile(collection, sibling, localSibling)) {
				System.out.println("Copying " + sibling.getAbsolutePath() + " to " + localSibling.getAbsolutePath());
				FileUtils.copyFile(sibling, localSibling);
			}
		}
	}

	private void copyItemParentDirectoryFiles(CollectionApiModel collection, File extensionFile, File localFile)
			throws IOException {
		for (File parentSibling : extensionFile.getParentFile().getParentFile().listFiles()) {

			File localSibling = new File(localFile.getParentFile().getParentFile(), parentSibling.getName());
			if (shouldCopyFile(collection, parentSibling, localSibling)) {
				// System.out.println("Copying "+parentSibling.getAbsolutePath() + " to " +
				// localSibling.getAbsolutePath());
				FileUtils.copyFile(parentSibling, localSibling);
			}
		}
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
