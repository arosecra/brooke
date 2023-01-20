package org.github.arosecra.brooke.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.Settings;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.api.CollectionApiModel;

public class RenameAndItemizeExtensionFiles {
	public static void main(String[] args) throws IOException {

		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(new Settings());
		Library library = libraryDao.getLibrary(false);

		for(CollectionApiModel collection : library.getCollections()) {
			Collection<File> itemFiles = listFiles(
				new File(collection.getRemoteDirectory()),
				collection.getItemExtension()
			);
			List<File> extensionFolders = new ArrayList<>();
			
			for(File file : itemFiles) {
				extensionFolders.add(file.getParentFile());
			}

			for(int i = 0; i < extensionFolders.size(); i++) {
				File folder = extensionFolders.get(i);
				renameAndItemize(folder, collection.getItemExtension());
			}
		}
	}

	private static void renameAndItemize(File itemizedFolder, String ext) throws IOException {
		for(File file : itemizedFolder.listFiles()) {
			String folderName = itemizedFolder.getName();
			String filename = FilenameUtils.getBaseName(file.getName());
			String extension = FilenameUtils.getExtension(file.getName());
			
			if((
					 file.getName().endsWith(ext) 
				|| file.getName().endsWith("item")
				) 
				&& !folderName.equals(filename)
			) {
				String newName = filename.replace(filename, folderName) + "." + extension;
				File destFile = new File(itemizedFolder, newName);
				File tempFile = new File(itemizedFolder, "temp." + extension);
				System.out.println(file.getName() + " in " + itemizedFolder + " -> " + destFile.getName());
				
				FileUtils.moveFile(file, tempFile);
				FileUtils.moveFile(tempFile, destFile);
			}
		}

		File itemFile = new File(itemizedFolder, itemizedFolder.getName() + ".item");
		if(!itemFile.exists()) {
			itemFile.createNewFile();
		}
	}

	private static Collection<File> listFiles(File remoteDir, String extension) {
		Collection<File> remoteFiles = FileUtils.listFiles(remoteDir, null, true);

		Set<File> files = new HashSet<>();
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
