package org.github.arosecra.book.pipeline.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

public class Try {

	public static void loadProperties(Properties props, File propertiesFile) {
		try {
			props.load(new BufferedInputStream(new FileInputStream(propertiesFile)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> readLines(File childFile) {
		try {
			return Files.readAllLines(childFile.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] readAllBytes(Path childFile) {
		try {
			return Files.readAllBytes(childFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static BufferedImage imageIORead(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static BufferedImage imageIORead(InputStream is) {
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean imageIOWrite(BufferedImage bi, String type, OutputStream os) {
		try {
			return ImageIO.write(bi, type, os);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File[] listFilesSafe(File file) {
		if (file.listFiles() != null) {
			File[] results = file.listFiles();
			Arrays.sort(results, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return Long.compare(o1.lastModified(), o2.lastModified());
				}

			});
			return results;
		} else {
			return new File[] {};
		}

	}

	public static void deleteFolder(Path p) {
		try {
			Files.walk(p) //
					.sorted(Comparator.reverseOrder()) //
					.forEach(path -> {
						try {
							Files.delete(path);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
