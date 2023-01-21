package org.github.arosecra.brooke.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class TarService {

	public byte[] getPageFromTar(File file, int page) throws IOException {
		int currentPage = 0;
		byte[] result = new byte[0];
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(file)))) {
			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (page == currentPage) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(tarIn, baos);
					result = baos.toByteArray();
				}
				currentPage++;
			}
		}
		return result;
	}
	
}
