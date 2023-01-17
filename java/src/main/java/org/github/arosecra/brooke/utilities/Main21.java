package org.github.arosecra.brooke.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

class Node {
	String path;
	File file;

	List<Node> childNodes = new ArrayList<>();
	List<File> childFiles = new ArrayList<>();
}

public class Main21 {
	public static void main(String[] args) throws IOException {
		Collection<Node> animeFolders = listLeafFolders(new File("\\\\drobo5n2\\public\\Anime"), "mp4");
		Collection<File> mkvFiles = listFiles(new File("\\\\drobo5n2\\public\\Videos\\Anime_TV"), "mkv");

		for (File mkvFile : mkvFiles) {
			for (Node node : animeFolders) {
				if (node.childNodes.isEmpty()) {

					String name = FilenameUtils.getBaseName(mkvFile.getName());
					File destFile = new File(node.file, mkvFile.getName());

					if (node.file.getName().equals(name) && !destFile.exists() && mkvFile.exists()) {
						FileUtils.moveFile(mkvFile, destFile);
						System.out.println(mkvFile.getAbsolutePath() + " -> " + node.file.getAbsolutePath());
					}
				}

//				System.out.println(node.path);
			}
		}

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

	private static Collection<Node> listLeafFolders(File remoteDir, String extension) {
		java.util.Collection<File> remoteFiles = FileUtils.listFilesAndDirs(remoteDir, TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);

		Map<String, Node> nodes = new HashMap<String, Node>();

		for (File file : remoteFiles) {
			if (file.isDirectory()) {
				if (!nodes.containsKey(file.getAbsoluteFile().getAbsolutePath())) {
					Node node = new Node();
					node.path = file.getAbsoluteFile().getAbsolutePath();
					node.file = file;
					nodes.put(node.path, node);
				}

				if (nodes.containsKey(file.getAbsoluteFile().getParentFile().getAbsolutePath())) {
					Node parentNode = nodes.get(file.getAbsoluteFile().getParentFile().getAbsolutePath());
					Node fileNode = nodes.get(file.getAbsoluteFile().getAbsolutePath());
					parentNode.childNodes.add(fileNode);
				}
			}
		}

		return nodes.values();
	}
}
