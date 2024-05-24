package org.github.arosecra.brooke.services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageService {

	public byte[] resizeImageToWidth(byte[] image, int desiredWidth) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		BufferedImage source = ImageIO.read(bais);
		BufferedImage output = this.resizeImageToWidth(source, desiredWidth);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(output, "png", baos);
		return baos.toByteArray();
	}

	public BufferedImage resizeImageToWidth(BufferedImage image, int desiredWidth) {
		int width = image.getWidth();
		int height = image.getHeight();

		int newWidth = width;
		int newHeight = height;

		if (desiredWidth < width) {
			newWidth = desiredWidth;
			double reduction = width / desiredWidth;
			newHeight = (int) (height / reduction);
		}

		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, image.getType());
		thumbnail.createGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0,
				null);
		return thumbnail;
	}

	public byte[] toJpegBytes(byte[] image) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		BufferedImage source = ImageIO.read(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(source, "jpeg", baos);
		return baos.toByteArray();
	}
}
