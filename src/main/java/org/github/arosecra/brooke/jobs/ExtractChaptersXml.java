package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.util.CommandLine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtractChaptersXml implements BrookeJobStep {

	@Override
	public boolean required(JobFolder folder) throws IOException {
		boolean chaptersExists = false;
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith("chapters.vtt"))
				chaptersExists = true;
		}
		return !chaptersExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		for(File file : folder.workFiles) {
			if(file.getName().endsWith("mkv")) {
				CommandLine.run(new String[] {
					"D:\\software\\MKVToolNix\\mkvextract.exe", 
					file.getAbsolutePath(), 
					"chapters",
	    			folder.workFolder.getAbsolutePath() + "\\chapters.xml"	
				});
				
				File chaptersXml = new File(folder.workFolder, "chapters.xml");
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
					FileUtils.write(new File(folder.workFolder, "chapters.vtt"), sb.toString(), StandardCharsets.UTF_8);
					chaptersXml.delete();
				}
			}
		}
	}

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(JobFolder folder) {
		List<File> mkvs = new ArrayList<>();
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith("mkv"))
				mkvs.add(file);
		}
		return mkvs;
	}

	private StringBuilder createVttString(List<Subtitle> subtitles, String kind) {
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

	private String formatTimestamp(String start) {
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

	private String replaceLastColonWithPeriod(String s) {
		StringBuilder sb = new StringBuilder();
		int pos = s.lastIndexOf(':');
		sb.append(s.substring(0, pos));
		sb.append('.');
		sb.append(s.substring(pos + 1));
		return sb.toString();
	}

	private static NodeList xpathForNodeSet(Object src, String xpath) throws IOException {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath x = xPathfactory.newXPath();
		try {
			XPathExpression expr = x.compile(xpath);
			return (NodeList) expr.evaluate(src, XPathConstants.NODESET);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}

	private static String xpathForString(Object src, String xpath) throws IOException {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath x = xPathfactory.newXPath();
		try {
			XPathExpression expr = x.compile(xpath);
			return (String) expr.evaluate(src, XPathConstants.STRING);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}

	private static Document parse(File xmlFile) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	    factory.setValidating(true);
//	    factory.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(xmlFile);
		} catch(Exception e) {
			throw new IOException(e);
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

}
