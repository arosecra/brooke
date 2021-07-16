package org.github.arosecra.brooke.book;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	private static final String BOOK_EXTENSION = ".cbt";
	private static byte[] DEFAULT_THUMBNAIL;
	static {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(BookService.class.getResourceAsStream("/static/images/default_thumbnail.png"), baos);
			DEFAULT_THUMBNAIL = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	private Settings settings;
	
	public File getBookFolder(String bookname) {
		return new File(settings.getBooksHome(), bookname);
	}
	
	public Book getBook(String bookname) throws IOException {
		return getBook(bookname, -1);
	}

	public Book getBook(String bookname, int page) throws IOException {
		File bookFolder = getBookFolder(bookname);
		File thumbnailFile = new File(bookFolder, "thumbnail.png");

		Book book = new Book();
		book.setDisplayName(FilenameUtils.getBaseName(bookFolder.getName()));
		book.setName(getBookname(bookname));

		book.setLeftPage(page);
		book.setRightPage(page + 1);
		
		if(!thumbnailFile.exists()) {
			book.getBookMetaData().setThumbnail(DEFAULT_THUMBNAIL);
		} else {
			book.getBookMetaData().setThumbnail(FileUtils.readFileToByteArray(thumbnailFile));
		}
		return book;
	}

	public String getBookFileName(String bookname) {
		return bookname.replace('_', ' ');
	}

	public String getBookname(String name) {
		return name.replace(' ', '_');
	}

}
