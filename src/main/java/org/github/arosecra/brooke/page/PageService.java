package org.github.arosecra.brooke.page;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageService {

	@Autowired
	private Settings settings;

	public Page getPage(String bookName, int page) throws IOException {

		File tar = new File(settings.getBooksHome() + "/" + bookName, bookName + ".tar");
		if(!tar.exists()) {
			File cbt = new File(settings.getBooksHome() + "/" + bookName, bookName + ".cbt");
			return getPageFromTar(bookName, page, cbt); 
		} else {
			return getPageFromTar(bookName, page, tar);
		}
	}

	private Page getPageFromTar(String bookName, int page, File file) throws IOException {
		int currentPage = 0;
		Page result = new Page();
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(file)))) {
			TarArchiveEntry entry;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (page == currentPage) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(tarIn, baos);
					byte[] content = baos.toByteArray();
					result.setData(content);
					result.setIndex(page);
					result.setBookname(bookName);
					System.out.println("Loading page " + currentPage + " from tarball");
				}
				currentPage++;
			}
		}
		return result;
	}
}
