package org.github.arosecra.brooke.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.model.CacheManifest;
import org.github.arosecra.brooke.model.CachedFile;
import org.github.arosecra.brooke.model.JobDetails;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.task.CopyFileTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@Component
public class FileCacheService {
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private LibraryLocationService libraryLocationService;
	
	public File getCachedFile(Library library, String collectionName, String itemName) {
		File cacheFolder = new File("D:\\Library\\Cache");
		CacheManifest cm = this.readCachedFileManifest();
		CachedFile cf = new CachedFile();
		for(CachedFile file : cm.getFiles()) {
			if(file.getCollectionName().equals(collectionName) && file.getItemName().equals(itemName)) {
				cf = file;
			}
		}

		File cacheFile = new File(cacheFolder, cf.getFilename());
		return cacheFile;
	}

	public JobDetails cacheItem(Library library, String collectionName, String itemName) throws IOException {
		File remoteFile = this.libraryLocationService.getRemoteFile(library, collectionName, itemName);
		return cacheRemoteFile(collectionName, itemName, remoteFile);
	}
	
	public List<String> listCachedFiles(Library library) {
//		File cacheFolder = new File("D:\\Library\\Cache");
//		List<File> files = Try.listFilesSafe(cacheFolder);
		List<String> names = new ArrayList<>();
//		for(File file : files) {
//			names.add(file.getName());
//		}
//		
		return names;
	}
	
	public CacheManifest readCachedFileManifest() {
		ObjectMapper mapper = new YAMLMapper();
		try {
			CacheManifest cacheManifest = mapper.readValue(new File("D:\\Library\\cache-manifest.yaml"), CacheManifest.class);
			return cacheManifest;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new CacheManifest();
	}
	
	private void writeCachedFileManifest(CacheManifest manifest) {
		ObjectMapper mapper = new YAMLMapper();
		try {
			mapper.writeValue(new File("D:\\Library\\cache-manifest.yaml"), manifest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void removeCachedFile(File file) {
		file.delete();
	}

	public JobDetails cacheRemoteFile(String collectionName, String itemName, File remoteFile) throws IOException {
		CacheManifest manifest = this.readCachedFileManifest();
//		File cacheFolder = new File("D:\\Library\\Cache");
//		File[] files = Try.listFilesSafe(cacheFolder);
//		int numberCached = files.length;
//		
//		int numberToDelete = 10 - numberCached;
//		for(int i = 0; i < numberToDelete; i++) {
//			this.removeCachedFile(remoteFile);
//		}
//		
//		if(numberCached > 10) {
//			do {
//				files[0].delete();
//			} while (files.length > 10);
//		}

		String uid = java.util.UUID.nameUUIDFromBytes((collectionName + "_"  + itemName).getBytes()).toString();
		CachedFile file = new CachedFile();
		file.setCollectionName(collectionName);
		file.setItemName(itemName);
		file.setFilename(uid);
		
		CopyFileTask task = new CopyFileTask(manifest, remoteFile, file);
		JobDetails jobDetails = jobService.createJob(task);
		jobDetails.setJobDescription("Caching");
		jobDetails.setJobType("Cache");
		jobService.runJob(jobDetails, task);
		return jobDetails;
	}
}
