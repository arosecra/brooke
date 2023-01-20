package org.github.arosecra.brooke.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.api.CollectionApiModel;

public class ListMissingExtensionFiles {
	public static void main(String[] args) throws IOException {

		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(new Settings());
		Library library = libraryDao.getLibrary(false);

		for(CollectionApiModel collection : library.getCollections()) {
			Collection<File> itemFiles = listFiles(
				new File(collection.getRemoteDirectory()),
				"item"
			);
			List<File> extensionFolders = new ArrayList<>();
			
			for(File file : itemFiles) {
				extensionFolders.add(file.getParentFile());
			}

			for(int i = 0; i < extensionFolders.size(); i++) {
				File folder = extensionFolders.get(i);
				checkForExtension(folder, collection.getItemExtension());
			}
		}
	}

	private static void checkForExtension(File folder, String itemExtension) {
		File extFile = new File(folder, folder.getName() + "." + itemExtension);
		if(!extFile.exists()) {
			System.out.println(folder.getAbsolutePath() + " is missing a " + itemExtension);
		}
	}

	private static Collection<File> listFiles(File remoteDir, String extension) {
		Collection<File> remoteFiles = FileUtils.listFiles(remoteDir, null, true);

		java.util.Set<File> files = new HashSet<>();
		for (File file : remoteFiles) {
			if (file.getName().endsWith(extension)) {
				files.add(file);
			}
		}
		List<File> res = new ArrayList<>();
		res.addAll(files);
		Collections.sort(res, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
			}});
		
		return files;
	}
}
