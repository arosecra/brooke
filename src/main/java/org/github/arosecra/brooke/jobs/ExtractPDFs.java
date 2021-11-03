package org.github.arosecra.brooke.jobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.util.CommandLine;


public class ExtractPDFs implements BrookeJobStep {
	
	private static final String GS_EXE = "C:\\software\\ghostscript\\bin\\gswin64c.exe";
	
	private String variant = "book";
	
	public ExtractPDFs() {}
	public ExtractPDFs(String variant) {this.variant = variant; }

	private static void tarToRawCbt(File pngFolder, File bookFolder) {
		CommandLine.run(new String[] {
			"D:\\software\\7za\\7za.exe", 
    		"a", 
    		"-ttar", 
    		"-o" + pngFolder.getAbsolutePath(),
    		bookFolder.getAbsolutePath()+"\\"+bookFolder.getName()+"_RAW.tar", 
    		pngFolder.getAbsolutePath() + "\\*.png" 	
		});
	}

	private static void extractPdf(File file, String outputFile, int dpi, String device) {
		String[] args = new String[] {
			GS_EXE,
			"-dBATCH",
			"-dNOPAUSE",
			"-dGraphicsAlphaBits=4",
			"-q",
			"-r"+dpi,
			"-sDEVICE="+device,
			"-dUseCropBox",
			"-sOutputFile="+outputFile,
			file.getAbsolutePath()
		};
		
		org.github.arosecra.brooke.util.CommandLine.run(args);
	}

	@Override
	public boolean required(JobFolder folder) {
		int pdfCount = 0;
		boolean rawCbtExists = false;
		for(File file : folder.remoteFiles) {
			if(this.variant.equals("movie") && file.getName().contains("box"))
				continue;
			if(file.getName().endsWith("pdf"))
				pdfCount++;
			if(file.getName().endsWith("_RAW.tar"))
				rawCbtExists = true;
		}		
		return pdfCount > 0 && !rawCbtExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		if(required(folder)) {
			File pngFolder = new File(folder.workFolder, "png");
			
			if(!pngFolder.exists()) {
				pngFolder.mkdirs();
				
				
				if(variant.equals("book")) {
				
					int pdfCount = 0; 
					for(File file : folder.workFiles) {
						if(file.getName().endsWith("pdf")) {
							pdfCount++;
						}
					}
					
					for(File file : folder.workFiles) {
						if(file.getName().endsWith("pdf") && file.getName().contains("covers")) {
							JobSubStep jss = new JobSubStep("Extract", folder.workFolder, 1, pdfCount);
							jss.startAndPrint();
							extractPdf(file, pngFolder.getAbsolutePath()+"\\"+folder.workFolder.getName()+"-000-%03d.png", 300, "png16m");
							jss.endAndPrint();
						}
					}
					
					for(int i = 0; i < folder.workFiles.size(); i++) {
						File file = folder.workFiles.get(i);
						if(file.getName().endsWith("pdf") && !file.getName().contains("covers")) {
							String prefix = String.format("%03d", i+1);
							String colorSpace = "pngmono";
							int size = 1200;
							if(file.getName().contains("color")) {
								size = 300;
								colorSpace = "png16m";
							}
							JobSubStep jss = new JobSubStep("Extract", folder.workFolder, i+2, pdfCount);
							jss.startAndPrint();
							extractPdf(file, pngFolder.getAbsolutePath()+"\\"+folder.workFolder.getName()+"-"+prefix+"-%03d.png", size, colorSpace);
							jss.endAndPrint();
						}
					}
				} else if(variant.equals("general")) {
					for(int i = 0; i < folder.workFiles.size(); i++) {
						File file = folder.workFiles.get(i);
						if(file.getName().endsWith("pdf")) {
							String prefix = String.format("%03d", i+1);
							JobSubStep jss = new JobSubStep("Extract", folder.workFolder, 1, 1);
							jss.startAndPrint();
							extractPdf(file, pngFolder.getAbsolutePath()+"\\"+folder.workFolder.getName()+"-"+prefix+"-%03d.png", 300, "png16m");
							jss.endAndPrint();
						}
					}
				} else if(variant.equals("movie")) {
					for(int i = 0; i < folder.workFiles.size(); i++) {
						File file = folder.workFiles.get(i);
						if(file.getName().endsWith("pdf") && !file.getName().contains("box")) {
							extractMoviePdf(pngFolder, folder.workFolder, new File(folder.workFolder, file.getName()));
						}
					}
				}
			}
			
			tarToRawCbt(pngFolder, folder.workFolder);
			
			FileUtils.deleteDirectory(pngFolder);
		}
	}

	private void extractMoviePdf(File pngFolder, File folder, File file) throws IOException {
		JobSubStep jss = new JobSubStep("Extract", folder, 1, 1);
		jss.startAndPrint();
		String basename = FilenameUtils.getBaseName(file.getName());
		File outputFile = new File(pngFolder, basename+"_raw.png");
		extractPdf(file, outputFile.getAbsolutePath(), 300, "png16m");
		
		
		BufferedImage orginalImage = ImageIO.read(outputFile);
		
		if(orginalImage.getHeight() > orginalImage.getWidth() && !file.getName().contains("box")) {

			BufferedImage blackAndWhiteImg = new BufferedImage(orginalImage.getWidth(), orginalImage.getHeight(),
					BufferedImage.TYPE_BYTE_BINARY);

			Graphics2D graphics = blackAndWhiteImg.createGraphics();
			graphics.drawImage(orginalImage, 0, 0, null);

			BufferedImage trimmed = trim(blackAndWhiteImg, orginalImage);

			ImageIO.write(blackAndWhiteImg, "png",
					new File(pngFolder, basename+"_bw.png"));

			ImageIO.write(rotate90(trimmed), "png",
					new File(pngFolder, basename+".png"));
			
			outputFile.delete();
			new File(pngFolder, basename+"_bw.png").delete();
		} else {
			FileUtils.moveFile(outputFile, new File(pngFolder, basename+".png"));
		}
		jss.endAndPrint();
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

		BufferedImage newImg = new BufferedImage(width, height, color.getColorModel().getColorSpace().getType());
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

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(JobFolder folder) {
		List<File> result = new ArrayList<>();
		for(File child : folder.remoteFiles) {
			if(child.getName().endsWith("pdf"))
				result.add(child);
		}		
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}
}
