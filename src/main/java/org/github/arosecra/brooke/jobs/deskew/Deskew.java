package org.github.arosecra.brooke.jobs.deskew;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class Deskew {

	public static void main(String[] args) throws Exception {
		Deskew t = new Deskew();
		File scansFolder = new File("D:/scans/tobeexported");
		
		for(File docFolder : scansFolder.listFiles())
		{
			System.out.println(docFolder.getName());
			File pngsFolder = new File(docFolder, "pngs/");
			File destPngsFolder = new File(docFolder + "/deskew/");
			if(pngsFolder.isDirectory()) {
			destPngsFolder.mkdirs();
			if(pngsFolder.exists())
			{
				for(File imageFile : pngsFolder.listFiles(new FilenameFilter() {
					public boolean accept(File file, String s) {
						return s.toLowerCase().endsWith("png");
					}}))
				{
					try 
					{
						File newImageFile = new File(destPngsFolder, imageFile.getName());
						if(!newImageFile.exists())
						{
							BufferedImage image = ImageIO.read(imageFile);
							
							if(!isColor(image))
							{
								double skew = t.doIt(image);
								System.out.printf("%s %f\n", imageFile.getName(), skew);
								if(skew == 0)
								{
									FileUtils.copyFile(imageFile, newImageFile);
								}
								else
	//							if(skew > .01 || skew < -.01)
								{
	//								AffineTransform at = AffineTransform.getRotateInstance(skew);
	//								AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	//								
	//								BufferedImage rotated = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
	//								
	//								ato.filter(image, rotated);
									
									BufferedImage sourceBI = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
									sourceBI.getGraphics().drawImage(image,0,0,null);
									AffineTransform at = AffineTransform.getRotateInstance(skew);
									BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
									BufferedImage rotated = bio.filter(sourceBI, null);
									
									rotated.getHeight(null);
									rotated.getWidth(null);
									
									BoundingBox bb = getTransparencyBoundingBox(image, rotated, skew);
									
									BufferedImage noTrans = new BufferedImage(bb.width(), bb.height(), BufferedImage.TYPE_BYTE_BINARY);
									for(int x = 0; x < bb.width(); x++)
									{
										for(int y = 0; y < bb.height(); y++)
										{
											int oldX = x + bb.tlx();
											int oldY = y + bb.tly();
											noTrans.setRGB(x, y, rotated.getRGB(oldX, oldY));
										}
									}
									
									ImageIO.write(noTrans, 
							                "png",
							                newImageFile);
								}
							}
							else
							{
								FileUtils.copyFile(imageFile, newImageFile);
							}
						}
					}
					catch(Throwable throwable)
					{
						throwable.printStackTrace();
					}
				}
			}
			}
		}
		
	}
	
//	public void rotate(double angle, int[] pixels)
//	{
//		final double radians = Math.toRadians(angle);
//		final double cos = Math.cos(radians);
//		final double sin = Math.sin(radians);
//		final int[] pixels2 = new int[pixels.length];
//		for (int x = 0; x < width; x++)
//			for (int y = 0; y < height; y++) {
//				final int centerx = this.width / 2;
//				final int centery = this.height / 2;
//				final int m = x - centerx;
//				final int n = y - centery;
//				final int j = ((int) (m * cos + n * sin)) + centerx;
//				final int k = ((int) (n * cos - m * sin)) + centery;
//				if (j >= 0 && j < width && k >= 0 && k < this.height)
//					pixels2[(y * width + x)] = pixels[(k * width + j)];
//			}
//		arraycopy(pixels2, 0, pixels, 0, pixels.length);
//	}

	private static BoundingBox getTransparencyBoundingBox(BufferedImage original, BufferedImage image, double skew) {
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
		System.out.println("BL " + oldBL + "->" + newBL);
		System.out.println("BR " + oldBR + "->" + newBR);
		System.out.println("TR " + oldTR + "->" + newTR);
//		System.out.println(rotBR);
		
		if(minx < newBL.getX())
			minx = newBL.getX();
		
		if(miny < newTR.getY())
			miny = newTR.getY();
		
		if(maxx > newBR.getX())
			maxx = newBR.getX();
		
		if(maxx > newTR.getX())
			maxx = newTR.getX();
		
		if(maxy > newBR.getY())
			maxy = newBR.getY();
		
		if(maxy > newBL.getY())
			maxy = newBL.getY();
		
		Point croppedTL = new Point(minx,miny);
		Point croppedBR = new Point(maxx,maxy);
		System.out.println("CR: " + croppedTL + ", " + croppedBR);
		
		return new BoundingBox(croppedTL, croppedBR);
	}

	private static Point calculateNewPoint(Point oldPoint, double rads) {

//        [   cos(theta)    -sin(theta)    0   ]
//        [   sin(theta)     cos(theta)    0   ]
//        [       0              0         1   ]
		
		double xPrime = oldPoint.getX()*Math.cos(rads) + 
				-oldPoint.getY()*Math.sin(rads);
		
		double yPrime = oldPoint.getX()*Math.sin(rads) +
				oldPoint.getY()*Math.cos(rads);
		
		return new Point(xPrime, yPrime);
	}

	private static boolean isColor(BufferedImage image) {
		boolean ret = false;
		
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				Color pixelColor = new Color(image.getRGB(x, y), true);
				if(pixelColor.getRGB() != Color.black.getRGB() &&
						pixelColor.getRGB() != Color.white.getRGB())
				{
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}

	public double doIt(BufferedImage image) {
		final double skewRadians;
		BufferedImage black = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		final Graphics2D g = black.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		skewRadians = findSkew(black);
		//System.out.println(-57.295779513082320876798154814105 * skewRadians);
		return skewRadians;
	}

	static int getByteWidth(final int width) {
		return (width + 7) / 8;
	}

	static int next_pow2(final int n) {
		int retval = 1;
		while (retval < n) {
			retval <<= 1;
		}
		return retval;
	}

	static class BitUtils {
		static int[] bitcount_ = new int[256];
		static int[] invbits_ = new int[256];

		static {
			for (int i = 0; i < 256; i++) {
				int j = i, cnt = 0;
				do {
					cnt += j & 1;
				} while ((j >>= 1) != 0);
				int x = (i << 4) | (i >> 4);
				x = ((x & 0xCC) >> 2) | ((x & 0x33) << 2);
				x = ((x & 0xAA) >> 1) | ((x & 0x55) << 1);
				bitcount_[i] = cnt;
				invbits_[i] = x;
			}
		}
	}

	static double findSkew(final BufferedImage img) {
		final DataBuffer buffer = img.getRaster().getDataBuffer();
		final int byteWidth = getByteWidth(img.getWidth());
		final int padmask = 0xFF << ((img.getWidth() + 7) % 8);
		int elementIndex = 0;
		for (int row = 0; row < img.getHeight(); row++) {
			for (int col = 0; col < byteWidth; col++) {
				int elem = buffer.getElem(elementIndex);
				elem ^= 0xff;// invert colors
				elem = BitUtils.invbits_[elem]; // Change the bit order
				buffer.setElem(elementIndex, elem);
				elementIndex++;
			}
			final int lastElement = buffer.getElem(elementIndex - 1) & padmask;
			buffer.setElem(elementIndex - 1, lastElement); // Zero trailing bits
		}
		final int w2 = next_pow2(byteWidth);
		final int ssize = 2 * w2 - 1; // Size of sharpness table
		final int sharpness[] = new int[ssize];
		radon(img.getWidth(), img.getHeight(), buffer, 1, sharpness);
		radon(img.getWidth(), img.getHeight(), buffer, -1, sharpness);
		int i, imax = 0;
		int vmax = 0;
		double sum = 0.;
		for (i = 0; i < ssize; i++) {
			final int s = sharpness[i];
			if (s > vmax) {
				imax = i;
				vmax = s;
			}
			sum += s;
		}
		final int h = img.getHeight();
		if (vmax <= 3 * sum / h) { // Heuristics !!!
			return 0;
		}
		final double iskew = imax - w2 + 1;
		return Math.atan(iskew / (8 * w2));
	}

	static void radon(final int width, final int height,
			final DataBuffer buffer, final int sign, final int sharpness[]) {

		int[] p1_, p2_; // Stored columnwise

		final int w2 = next_pow2(getByteWidth(width));
		final int w = getByteWidth(width);
		final int h = height;

		final int s = h * w2;
		p1_ = new int[s];
		p2_ = new int[s];
		// Fill in the first table
		int row, column;
		int scanlinePosition = 0;
		for (row = 0; row < h; row++) {
			scanlinePosition = row * w;
			for (column = 0; column < w; column++) {
				if (sign > 0) {
					final int b = buffer.getElem(0, scanlinePosition + w - 1
							- column);
					p1_[h * column + row] = BitUtils.bitcount_[b];
				} else {
					final int b = buffer.getElem(0, scanlinePosition + column);
					p1_[h * column + row] = BitUtils.bitcount_[b];
				}
			}
		}

		int[] x1 = p1_;
		int[] x2 = p2_;
		// Iterate
		int step = 1;
		for (;;) {
			int i;
			for (i = 0; i < w2; i += 2 * step) {
				int j;
				for (j = 0; j < step; j++) {
					// Columns-sources:
					final int s1 = h * (i + j);// x1 pointer
					final int s2 = h * (i + j + step); // x1 pointer

					// Columns-targets:
					final int t1 = h * (i + 2 * j); // x2 pointer
					final int t2 = h * (i + 2 * j + 1); // x2 pointer
					int m;
					for (m = 0; m < h; m++) {
						x2[t1 + m] = x1[s1 + m];
						x2[t2 + m] = x1[s1 + m];
						if (m + j < h) {
							x2[t1 + m] += x1[s2 + m + j];
						}
						if (m + j + 1 < h) {
							x2[t2 + m] += x1[s2 + m + j + 1];
						}
					}
				}
			}

			// Swap the tables:
			final int[] aux = x1;
			x1 = x2;
			x2 = aux;
			// Increase the step:
			step *= 2;
			if (step >= w2) {
				break;
			}
		}
		// Now, compute the sum of squared finite differences:
		for (column = 0; column < w2; column++) {
			int acc = 0;
			final int col = h * column;
			for (row = 0; row + 1 < h; row++) {
				final int diff = x1[col + row] - x1[col + row + 1];
				acc += diff * diff;
			}
			sharpness[w2 - 1 + sign * column] = acc;
		}
	}
}

class Book
{
	public File sourceFolder;
	public File   destFolder;
	public String       name;
	public List<Page>  pages = new ArrayList<Page>();
}

class Page
{
	public File sourceFile;
	public File   destFile;
	public String     name;
	public boolean  skewed;
}