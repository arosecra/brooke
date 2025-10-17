package org.github.arosecra.book.pipeline;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.book.pipeline.util.Images;
import org.github.arosecra.brooke.util.CommandLine;

public class Test {
	public static void main(String[] args) throws Exception {
		File baseDir = new File("C:\\Scans\\temp\\Redemption_of_Althalus_RAW");

		File[] files = baseDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			System.out.println("Resizing " + file.getName());
			byte[] img = FileUtils.readFileToByteArray(file);
			byte[] resizedImg = Images.resizeImageToWidth(img, 1920);
			FileUtils.writeByteArrayToFile(new File("C:/Scans/temp/out/", file.getName()), resizedImg);

			if (i > 8) {
				System.out.println("OCRing   " + file.getName());
				CommandLine.run(
						new String[] { "uv", "run", "mineru", "-p", file.getAbsolutePath(), "-o",
								"C:\\Scans\\temp\\Redemption_of_Althalus_MD", "-l", "en", "--source", "local" },
						new File("D:\\Projects\\mineru"), System.out);
			}
		}
	}
}