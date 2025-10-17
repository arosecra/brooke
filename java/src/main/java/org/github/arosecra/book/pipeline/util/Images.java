package org.github.arosecra.book.pipeline.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Images {

	public static byte[] resizeImageToWidth(byte[] image, int desiredWidth) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		BufferedImage source = Try.imageIORead(bais);
		BufferedImage output = resizeImageToWidth(source, desiredWidth);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Try.imageIOWrite(output, "png", baos);
		return baos.toByteArray();
	}

	public static BufferedImage resizeImageToWidth(BufferedImage image, int desiredWidth) {
		return resizeImageToWidth(image, desiredWidth, Image.SCALE_SMOOTH);
	}

	public static BufferedImage resizeImageToWidth(BufferedImage image, int desiredWidth, int scaleing) {
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
		thumbnail.createGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, scaleing), 0, 0,
				null);
		return thumbnail;
	}
}
