package org.github.arosecra.book.pipeline.book.steps;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.github.arosecra.book.pipeline.model.BoundingBox;
import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.model.Point;
import org.github.arosecra.brooke.util.CommandLine;

public class ModifyBookImageStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		for (File file : job.tempFolder.listFiles()) {

			File outputFile = new File(job.destFolder, file.getName());
			BufferedImage originalImage = ImageIO.read(file);
			if (isColor(originalImage)) {
				Files.move(file.toPath(), Path.of(job.destFolder.toPath().toString(), file.getName()),
						StandardCopyOption.REPLACE_EXISTING);
			} else {
				double skew = imageMagickDeskewRadians(file);
				if (skew != 0) {
					rotateImage(originalImage, skew, outputFile);
					file.delete();
				} else {
					Files.move(file.toPath(), Path.of(job.destFolder.toPath().toString(), file.getName()),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}

	}

	private void rotateImage(BufferedImage image, double skew, File outputFile) throws IOException {
		BufferedImage sourceBI = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		sourceBI.getGraphics().drawImage(image, 0, 0, null);
		AffineTransform at = AffineTransform.getRotateInstance(skew);
		BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage rotated = bio.filter(sourceBI, null);

		rotated.getHeight(null);
		rotated.getWidth(null);

		BoundingBox bb = getTransparencyBoundingBox(image, rotated, skew);

		BufferedImage noTrans = new BufferedImage(bb.width(), bb.height(), BufferedImage.TYPE_BYTE_BINARY);
		for (int x = 0; x < bb.width(); x++) {
			for (int y = 0; y < bb.height(); y++) {
				int oldX = x + bb.tlx();
				int oldY = y + bb.tly();
				noTrans.setRGB(x, y, rotated.getRGB(oldX, oldY));
			}
		}

		ImageIO.write(noTrans, "png", outputFile);
	}

	private BoundingBox getTransparencyBoundingBox(BufferedImage original, BufferedImage image, double skew) {
		int minx = 0;
		int miny = 0;
		int maxx = image.getWidth();
		int maxy = image.getHeight();

//		Point oldTL = new Point(0, 0);
		Point oldBL = new Point(0, original.getHeight());
		Point oldBR = new Point(original.getWidth(), original.getHeight());
		Point oldTR = new Point(original.getWidth(), 0);
//        [   cos(theta)    -sin(theta)    0   ]
//        [   sin(theta)     cos(theta)    0   ]
//        [       0              0         1   ]

//		double skewRadians = Math.PI * skew )

//		Point newTL = calculateNewPoint(oldTL, skew);
		Point newBL = calculateNewPoint(oldBL, skew);
		Point newBR = calculateNewPoint(oldBR, skew);
		Point newTR = calculateNewPoint(oldTR, skew);

//		System.out.println(oldTL + "->" + newTL);
//		System.out.println("BL " + oldBL + "->" + newBL);
//		System.out.println("BR " + oldBR + "->" + newBR);
//		System.out.println("TR " + oldTR + "->" + newTR);
//		System.out.println(rotBR);

		if (minx < newBL.getX())
			minx = newBL.getX();

		if (miny < newTR.getY())
			miny = newTR.getY();

		if (maxx > newBR.getX())
			maxx = newBR.getX();

		if (maxx > newTR.getX())
			maxx = newTR.getX();

		if (maxy > newBR.getY())
			maxy = newBR.getY();

		if (maxy > newBL.getY())
			maxy = newBL.getY();

		Point croppedTL = new Point(minx, miny);
		Point croppedBR = new Point(maxx, maxy);

		return new BoundingBox(croppedTL, croppedBR);
	}

	private Point calculateNewPoint(Point oldPoint, double rads) {

//      [   cos(theta)    -sin(theta)    0   ]
//      [   sin(theta)     cos(theta)    0   ]
//      [       0              0         1   ]

		double xPrime = oldPoint.getX() * Math.cos(rads) + -oldPoint.getY() * Math.sin(rads);

		double yPrime = oldPoint.getX() * Math.sin(rads) + oldPoint.getY() * Math.cos(rads);

		return new Point(xPrime, yPrime);
	}

	private boolean isColor(BufferedImage image) {
		boolean ret = false;

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color pixelColor = new Color(image.getRGB(x, y), true);
				if (pixelColor.getRGB() != Color.black.getRGB() && pixelColor.getRGB() != Color.white.getRGB()) {
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	private double imageMagickDeskewRadians(File file) throws IOException {
		// D:\Software\ImageMagick\magick.exe
		// C:\scans\deskew\Redemption_of_Althalus_RAW\Redemption_of_Althalus-001-001.png
		// -deskew 40% -format '%[deskew:angle]' info:
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final String utf8 = StandardCharsets.UTF_8.name();
		try (PrintStream ps = new PrintStream(baos, true, utf8)) {
			CommandLine.run(new String[] { "D:\\Software\\ImageMagick\\magick.exe", file.getAbsolutePath(), "-deskew",
					"40%", "-format", "%[deskew:angle]", "info:" }, ps);
		}
		double skewDegrees = Double.parseDouble(baos.toString(utf8));
		return (Math.PI / 180) * skewDegrees;
	}

}