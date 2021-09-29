package org.github.arosecra.brooke;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.jobs.ExtractPDFs;
import org.thymeleaf.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main17 {
	public static void main(String[] args) throws Exception {

		processTemp1();
		processTemp2();
//		processTemp3();
	}

	private static void processTemp2() throws Exception {
		File tempDir = new File("D:\\scans\\temp2");
		Map<String, String> docIdsToTitles = new HashMap<>();
		Map<String, String> docIdsToPdfIds = new HashMap<>();
		
		Map<String, File> pdfs = new HashMap<>();
		for(File pdf : tempDir.listFiles()) {
			if(pdf.getName().endsWith("pdf"))
			pdfs.put(StringUtils.substringBefore(pdf.getName(), "-"), pdf);
		}
		
		
		for(File php : tempDir.listFiles()) {
			if(php.getName().endsWith("php")) {
				
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(php);
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("//a");
				
				NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				for(int i = 0; i < nl.getLength(); i++) {
					Node item = nl.item(i);
					String content = item.getTextContent();
					if(item.getAttributes().getNamedItem("href") != null) {
						String href = item.getAttributes().getNamedItem("href").getNodeValue();
						String[] hrefParts = href.split("/");
						if(content.trim().equals("PDF")) {
//							String docId = hrefParts[hrefParts.length-2];
//							docIdsToPdfIds.put(docId, pdfId);
						} else {
							String docId = hrefParts[hrefParts.length-1];
							docIdsToTitles.put(docId, content.trim());
						}
					}
				}
				
				for(String docId : docIdsToTitles.keySet()) {
					String title = docIdsToTitles.get(docId);
					String titleOneLine = title.replaceAll("\\s+", " ").replace(":", "");
					
					
					
					boolean hasFile = pdfs.containsKey(docId);
					if(!hasFile)
						System.out.println(titleOneLine + " " + " " + docId + " " + hasFile);
				}
			}
		}
		
	}

	private static void processTemp3() throws IOException {
		File tempDir = new File("D:\\scans\\temp3");
		ExtractPDFs e = new ExtractPDFs("general");
		e.execute(tempDir);
		for (File file : tempDir.listFiles()) {
		}

		for (File file : new File(tempDir, "temp3_RAW").listFiles()) {
			BufferedImage orginalImage = ImageIO.read(file);

			BufferedImage blackAndWhiteImg = new BufferedImage(orginalImage.getWidth(), orginalImage.getHeight(),
					BufferedImage.TYPE_BYTE_BINARY);

			Graphics2D graphics = blackAndWhiteImg.createGraphics();
			graphics.drawImage(orginalImage, 0, 0, null);

			BufferedImage trimmed = trim(blackAndWhiteImg, orginalImage);

			ImageIO.write(blackAndWhiteImg, "png",
					new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + "_bw.png"));

			ImageIO.write(rotate90(trimmed), "png",
					new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + "_trimmed.png"));
		}
	}
	
	public static BufferedImage rotate90(BufferedImage image) {
        final double rads = Math.toRadians(90);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));

        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);
        return rotatedImage;
	}

	public static BufferedImage trim(BufferedImage img, BufferedImage color) {
		int width = getTrimmedWidth(img);
		int height = getTrimmedHeight(img);

		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImg.createGraphics();
		g.drawImage(color, 0, 0, null);
		return newImg;
	}

	private static int getTrimmedWidth(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int trimmedWidth = 0;
		int padding = height / 4;
		for (int j = width - 1; j >= 0; j--) {
			boolean isBlack = false;
			for(int i =padding; i < height-padding; i++) {
				int pixel = img.getRGB(j,i);
				if (pixel != Color.white.getRGB()) {
					isBlack = true;
				}
			}
			if(isBlack  && j > trimmedWidth) {
				trimmedWidth = j;
				break;
			}
		}

		return trimmedWidth;
	}

	private static int getTrimmedHeight(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int padding = width / 4;
		int trimmedHeight = 0;
		for (int j = height - 1; j >= 0; j--) {
			boolean isBlack = false;
			for(int i =padding; i < width-padding; i++) {
				int pixel = img.getRGB(i,j);
				if (pixel != Color.white.getRGB()) {
					isBlack = true;
				}
			}
			if(isBlack  && j > trimmedHeight) {
				trimmedHeight = j;
				break;
			}
		}

		return trimmedHeight;
	}

	private static void processTemp1() throws IOException {
		List<String> forbiddenFirstWords = Arrays.asList(new String[] { "A", "An", "The" });
		File tempDir = new File("D:\\scans\\temp1");

		for (File file : tempDir.listFiles()) {
			if (file.isDirectory()) {
				new File(file, ".item").createNewFile();
			} else {

				String basename = FilenameUtils.getBaseName(file.getName());
				basename = basename.replace('_', ' ');

				StringBuilder sb = new StringBuilder();
				boolean inParens = false;
				for (int i = 0; i < basename.length(); i++) {
					if (basename.charAt(i) == '(')
						inParens = true;
					if (!inParens)
						sb.append(basename.charAt(i));
					if (basename.charAt(i) == ')')
						inParens = false;
				}
				basename = sb.toString().replace('-', ' ').trim();

				String[] parts = basename.split(" ");
				sb = new StringBuilder();
				for (int i = 0; i < parts.length; i++) {
					String part = parts[i];
					if (part.length() > 0) {
						part = part.replace(",", "");
						if (i == 0 && forbiddenFirstWords.contains(parts[i])) {
							// skip
						} else {
							sb.append(Character.toUpperCase(parts[i].charAt(0)));
							if (parts[i].length() > 1)
								sb.append(parts[i].substring(1));
						}
						sb.append(' ');
					}

				}
				basename = sb.toString().trim().replace(' ', '_');
				// if(basename.length() > 40)
				// basename = basename.substring(0,40);
				// System.out.println(basename);

				File dir = new File(tempDir, basename);
				File newFile = new File(dir, basename + ".pdf");
				if (!dir.exists()) {
					dir.mkdirs();
					FileUtils.moveFile(file, newFile);
				} else {
					// check if the files are the same length
					if (file.length() == newFile.length()) {
						System.out.println("Delete " + file.getName());
					}
				}

			}
		}
	}

}
