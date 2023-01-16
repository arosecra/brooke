package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


class RenameNode {
	File folderToRename;
	File parentFolder;
	File grandparentFolder;
	String folderOriginalName;
}


public class Main24 {
	public static void main(String[] args) throws IOException {
//		Collection<File> itemFolders = listFiles(new File("\\\\drobo5n2\\public\\Anime"), "item");
//		List<RenameNode> filesWithParentsMatching = new ArrayList<>();
//
//		int total = 0;
//		for (File itemFile : itemFolders) {
//			RenameNode renameNode = new RenameNode();
//			renameNode.folderToRename = itemFile.getParentFile();
//			renameNode.folderOriginalName = renameNode.folderToRename.getName();
//			renameNode.parentFolder = renameNode.folderToRename.getParentFile();
//			renameNode.grandparentFolder = renameNode.parentFolder.getParentFile();
//			
//			boolean parentIsOneChar = renameNode.parentFolder.getName().length() == 1;
//			boolean grandparentIsOnChar = renameNode.grandparentFolder.getName().length() == 1;
//			boolean startsWithParentName = renameNode.folderOriginalName.startsWith(renameNode.parentFolder.getName());
//			boolean equalsParentName = renameNode.folderOriginalName.equals(renameNode.parentFolder.getName());
//			
//			if(!parentIsOneChar && !grandparentIsOnChar && (!startsWithParentName || equalsParentName)) {
////				System.out.println(itemFolder.getName() + " has the same name as its parent");
//				filesWithParentsMatching.add(renameNode);
//				total++;
//			}
//			
//			
////			
////			for (Node node : animeFolders) {
////				if (node.childNodes.isEmpty()) {
////
////					String name = FilenameUtils.getBaseName(mkvFile.getName());
////					File destFile = new File(node.file, mkvFile.getName());
////
////					if (node.file.getName().equals(name) && !destFile.exists() && mkvFile.exists()) {
////						FileUtils.moveFile(mkvFile, destFile);
////						System.out.println(mkvFile.getAbsolutePath() + " -> " + node.file.getAbsolutePath());
////					}
////				}
////
//////				System.out.println(node.path);
////			}
//		}
//		
//		Collections.sort(filesWithParentsMatching, new Comparator<RenameNode>() {
//
//			@Override
//			public int compare(RenameNode o1, RenameNode o2) {
//				return o1.folderOriginalName.compareTo(o2.folderOriginalName);
//			}
//			
//		});
//		
//		for(RenameNode file : filesWithParentsMatching) {
////			System.out.println(file.folderOriginalName);
//			
//			String newName = file.folderOriginalName.replace(file.grandparentFolder.getName(), "");
//			newName = file.parentFolder.getName() + newName;
//			System.out.println(file.folderOriginalName + " -> " + newName);
//			
//			
////			if(newName.contains("Last_Exile")
////					
////					
////					) {
////				FileUtils.moveDirectory(file.folderToRename, new File(file.parentFolder, newName));
////			} else 
//				System.out.println(file.folderOriginalName + " -> " + newName);
//			
//		}
//		
//		System.out.println(total + " total");
//		
		
		
		

		Collection<File> itemFiles = listFiles(new File("\\\\drobo5n2\\public\\Anime"), "item");
		List<File> itemizedFolders = new ArrayList<>();
		
		for(File file : itemFiles) {
			itemizedFolders.add(file.getParentFile());
		}
		
		List<File> missingMkv = new ArrayList<>();
		
		int current = 0;
		for(File itemizedFolder : itemizedFolders) {
			boolean mkvFound = false;
			for(File file : itemizedFolder.listFiles()) {
				if(file.getName().endsWith("mkv"))
					mkvFound = true;
			}
			
			if(!mkvFound)
				missingMkv.add(itemizedFolder);
			
			for(File file : itemizedFolder.listFiles()) {
				String folderName = itemizedFolder.getName();
				String filename = FilenameUtils.getBaseName(file.getName());
				String extension = FilenameUtils.getExtension(file.getName());
				
				if((
					   file.getName().endsWith("mkv") 
					|| file.getName().endsWith("mp4")
					|| file.getName().endsWith("item")
					) 
					&& !folderName.equals(filename)
				) {
					current++;
					String newName = filename.replace(filename, folderName) + "." + extension;
					File destFile = new File(itemizedFolder, newName);
					File tempFile = new File(itemizedFolder, "temp." + extension);
					System.out.println(current + ": " + file.getName() + " in " + itemizedFolder + " -> " + destFile.getName());
					FileUtils.moveFile(file, tempFile);
					FileUtils.moveFile(tempFile, destFile);
				}
			}
		}
		
		Collections.sort(missingMkv, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		
		for(File file : missingMkv)
			System.out.println("No MKV in " + file);

	}

	private static Collection<File> listFiles(File remoteDir, String extension) {
		java.util.Collection<File> remoteFiles = FileUtils.listFiles(remoteDir, null, true);

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
//
//	private static Collection<Node> listLeafFolders(File remoteDir, String extension) {
//		java.util.Collection<File> remoteFiles = FileUtils.listFilesAndDirs(remoteDir, TrueFileFilter.INSTANCE,
//				TrueFileFilter.INSTANCE);
//
//		Map<String, Node> nodes = new HashMap<String, Node>();
//
//		for (File file : remoteFiles) {
//			if (file.isDirectory()) {
//				if (!nodes.containsKey(file.getAbsoluteFile().getAbsolutePath())) {
//					Node node = new Node();
//					node.path = file.getAbsoluteFile().getAbsolutePath();
//					node.file = file;
//					nodes.put(node.path, node);
//				}
//
//				if (nodes.containsKey(file.getAbsoluteFile().getParentFile().getAbsolutePath())) {
//					Node parentNode = nodes.get(file.getAbsoluteFile().getParentFile().getAbsolutePath());
//					Node fileNode = nodes.get(file.getAbsoluteFile().getAbsolutePath());
//					parentNode.childNodes.add(fileNode);
//				}
//			}
//		}
//
//		return nodes.values();
//	}
}
