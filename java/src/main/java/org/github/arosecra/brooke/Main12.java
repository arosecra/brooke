package org.github.arosecra.brooke;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.thymeleaf.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sourceforge.tess4j.Tesseract;

public class Main12 {

	static Tesseract tesseract = new Tesseract();

	public static void main(String[] args) throws Exception {
		tesseract.setDatapath("D:\\Software\\tessdata");
		
		Collection<File> subFiles = FileUtils.listFiles(new File("D:\\Library\\Anime_Repository"), new String[] {"sub"}, true);
		System.out.println("Starting. Files to check: " + subFiles.size());
		for(File file : subFiles) {
			File folder = file.getParentFile();
			String basename = FilenameUtils.getBaseName(file.getName());
			File tar = new File(folder, basename + ".tar");
			
			if(!tar.exists()) {
				extracted(file);
			}
			
		}
	}

	private static void extracted(File subfile) throws Exception {
		// extract the pngs from the .sub files using bdsup2sub
		System.out.println("Extracting " + subfile.getPath());

		File tempFolder = new File(subfile.getParentFile(), FilenameUtils.getBaseName(subfile.getName()));
		
		if(tempFolder.exists()) {

	//		extractSubtitlePngs(subfile, tempFolder);
	//		Thread.sleep(15000);
			
	
			extractVtt(subfile, tempFolder);
	
			// tar the png, new xml, sub and idx files together for later - vobsub.tar
			// delete the tar'd files, and the other sub and idx files
			tarData(subfile, tempFolder);
	
			// if there's a chapter xml file, create a chapters.vtt file
			createChaptersVtt(subfile);
	
			System.out.println("Done");
		} else {
			System.out.println("Skipped");
		}
	}

	private static void tarData(File subfile, File tempFolder) throws InterruptedException, IOException {
		tar(tempFolder, subfile.getParentFile(), FilenameUtils.getBaseName(subfile.getName()));
		Thread.sleep(15000);
		for (File file : tempFolder.listFiles())
			file.delete();
		tempFolder.delete();
	}

	private static void extractVtt(File subfile, File tempFolder) throws IOException, Exception {
		// use the tesseract output and idx file to create as english.vtt file
		List<Subtitle> subtitles = new ArrayList<>();
		File idxFile = new File(tempFolder, FilenameUtils.getBaseName(subfile.getName()) + ".xml");
		for (String line : FileUtils.readLines(idxFile, StandardCharsets.UTF_8)) {
			if (line.contains("<Event ")) {
				//
				String temp = StringUtils.substringAfter(line, "Event");
				temp = temp.trim();
				temp = temp.replace('>', ' ');
				String[] attributes = temp.split(" ");

				Subtitle sub = new Subtitle();
				subtitles.add(sub);

				for (String attribute : attributes) {
					String name = StringUtils.substringBefore(attribute, "=");
					String value = StringUtils.substringAfter(attribute, "=");
					value = value.replace('"', ' ').trim();

					if (name.equals("InTC")) {
						sub.start = value;
					} else if (name.equals("OutTC")) {
						sub.end = value;
					}
				}

			}
		}

		// run teserract on the pngs

		File[] pngs = tempFolder.listFiles();
		int pngCount = 0;
		for (int i = 0; i < pngs.length; i++) {
			File file = pngs[i];
			if (file.getName().endsWith("png")) {
				String basename = FilenameUtils.getBaseName(file.getName());
				File txtFile = new File(file.getParentFile(), basename + ".txt");

				Subtitle subtitle = subtitles.get(pngCount);
				if(txtFile.exists()) {
					subtitle.text = FileUtils.readFileToString(txtFile);
				} else {
					subtitle.text = doOcr(file);
				}
				System.out.println(file.getName());
				System.out.println("------");
				System.out.println(subtitle.text);

				System.out.println("");
				pngCount++;
			}
		}

		StringBuilder sb = createVttString(subtitles, "captions");

		FileUtils.write(new File(subfile.getParentFile(), FilenameUtils.getBaseName(subfile.getName()) + ".vtt"),
				sb.toString(), StandardCharsets.UTF_8);
	}

	private static StringBuilder createVttString(List<Subtitle> subtitles, String kind) {
		StringBuilder sb = new StringBuilder();

		sb.append("WEBVTT\r\n");
		sb.append("Kind: ");
		sb.append(kind);
		sb.append("\r\n");
		sb.append("Language: en\r\n");
		sb.append("\r\n");
		sb.append("\r\n");

		for (Subtitle sub : subtitles) {
			String start = replaceLastColonWithPeriod(sub.start);
			String stop = replaceLastColonWithPeriod(sub.end);
			start = formatTimestamp(start);
			stop = formatTimestamp(stop);

			sb.append(start);
			sb.append(" --> ");
			sb.append(stop);
			sb.append("\r\n");
			sb.append(sub.text);
			sb.append("\r\n");
		}
		return sb;
	}

	private static String formatTimestamp(String start) {
		String[] parts = start.split("[\\.:]");
		
		StringBuilder result = new StringBuilder();

		for(int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if(i == parts.length - 1) {
				if(part.length() > 3) {
					part = part.substring(0, 3);
				} else if (part.length() < 3) {
					while(part.length() < 3) {
						part = part + "0";
					}
				}
				
				result.append('.');
				result.append(part);
			} else {
				result.append(part);
				if(i != parts.length - 2) 
					result.append(':');
			}
		}
		
		
		return result.toString();
	}

	private static void createChaptersVtt(File subfile) throws Exception, IOException {
		File chaptersXml = new File(subfile.getParentFile(),
				FilenameUtils.getBaseName(subfile.getParentFile().getName()) + "_Chapters.xml");
		if (chaptersXml.exists()) {
			List<Subtitle> chapters = new ArrayList<>();
			Document doc = parse(chaptersXml);

			NodeList nl = xpathForNodeSet(doc, "//ChapterAtom");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);

				String start = xpathForString(n, ".//ChapterTimeStart");
				String stop = xpathForString(n, ".//ChapterTimeEnd");
				String name = xpathForString(n, ".//ChapterString");
				String chapterUuid = xpathForString(n, ".//ChapterUID");

				chapters.add(new Subtitle(name, start, stop, chapterUuid));
			}

			StringBuilder sb = createVttString(chapters, "chapters");
			FileUtils.write(new File(subfile.getParentFile(), "chapters.vtt"), sb.toString(), StandardCharsets.UTF_8);

		}
	}

	@SuppressWarnings("unused")
	private static class Subtitle {
		private String text;
		private String start;
		private String end;
		private String uid;
		public Subtitle() {}
		public Subtitle(String text, String start, String end, String uid) {
			super();
			this.text = text;
			this.start = start;
			this.end = end;
			this.uid = uid;
		}
		
		
	}

	private static NodeList xpathForNodeSet(Object src, String xpath) throws Exception {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath x = xPathfactory.newXPath();
		XPathExpression expr = x.compile(xpath);
		return (NodeList) expr.evaluate(src, XPathConstants.NODESET);
	}

	private static String xpathForString(Object src, String xpath) throws Exception {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath x = xPathfactory.newXPath();
		XPathExpression expr = x.compile(xpath);
		return (String) expr.evaluate(src, XPathConstants.STRING);
	}

	private static Document parse(File xmlFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	    factory.setValidating(true);
//	    factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(xmlFile);
	}

	private static String replaceLastColonWithPeriod(String s) {
		StringBuilder sb = new StringBuilder();
		int pos = s.lastIndexOf(':');
		sb.append(s.substring(0, pos));
		sb.append('.');
		sb.append(s.substring(pos + 1));
		return sb.toString();
	}

	private static String doOcr(File file) throws Exception {
		String tessOcr = tesseract.doOCR(resizeImage(file));
		tessOcr = tessOcr.replace('|', 'I');
		tessOcr = tessOcr.replace("s0", "so");
		tessOcr = tessOcr.replace("sO", "so");
		return tessOcr;
	}

	private static void tar(File inputFolder, File outputFolder, String name) {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture -ttar
		// D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt
		// D:\Scans\temp\3D_Game_Engine_Architecture\*.webp

		Process process;
		try {
			ProcessBuilder pb = new ProcessBuilder("D:\\software\\7za\\7za.exe", "a", "-ttar",
					"-o" + outputFolder.getAbsolutePath(), outputFolder.getAbsolutePath() + "\\" + name + ".tar",
					inputFolder.getAbsolutePath() + "\\*");

			process = pb.start();

			process.waitFor(30, TimeUnit.MINUTES);
			if (process.exitValue() == 0) {
				// Success
				printProcessOutput(process.getInputStream(), System.out);
			} else {
				printProcessOutput(process.getErrorStream(), System.err);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage resizeImage(File file) throws FileNotFoundException, IOException {
		BufferedImage input = ImageIO.read(new BufferedInputStream(new FileInputStream(file)));
		int width = input.getWidth();
		int height = input.getHeight();

		int factor = 20;

		int newWidth = width * factor;
		int newHeight = height * factor;
		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		thumbnail.createGraphics().drawImage(input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0,
				null);

		return thumbnail;
	}

//	public static boolean extractSubtitlePngs(File subfile, File tempFolder) {
//		Process process = null;
//		try {
//			process = new ProcessBuilder("cmd", "/c", "java.exe", "-jar", "D:\\projects\\bdsup2sub.jar", "-o",
//					tempFolder.getAbsolutePath() + "\\" + FilenameUtils.getBaseName(subfile.getName()) + ".xml",
//					subfile.getAbsolutePath(), "&&", "exit").start();
//			process.waitFor(3, TimeUnit.MINUTES);
//			if (process.exitValue() == 0) {
//				// Success
//				printProcessOutput(process.getInputStream(), System.out);
//				return true;
//			} else {
//				printProcessOutput(process.getErrorStream(), System.err);
//				return false;
//			}
//		} catch (Exception e) {
//			try {
//				printProcessOutput(process.getInputStream(), System.out);
//				printProcessOutput(process.getErrorStream(), System.err);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//			return false;
//		}
//	}

	private static void printProcessOutput(InputStream inputStream, PrintStream output) throws IOException {
		try (InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(isr)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				output.println(line);
			}
		}
	}
}
