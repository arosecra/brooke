package org.github.arosecra.brooke.book;

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
import org.thymeleaf.util.StringUtils;

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
	
	public Book getBook(String bookname) throws IOException {
		String adjustedBookname = getBookFileName(bookname);
		File bookFile = new File(settings.getBooksHome(), adjustedBookname + BOOK_EXTENSION);		
		return getBook(bookFile);
	}
	
	public Book getBook(String bookname, int page) throws IOException {
		String adjustedBookname = getBookFileName(bookname);
		File bookFile = new File(settings.getBooksHome(), adjustedBookname + BOOK_EXTENSION);		
		return getBook(bookFile, page);
	}
	
	public Book getBook(File bookFile) throws IOException {
		return getBook(bookFile, -1);
	}
	
	public Book getBook(File bookFile, int startPage) throws IOException {
		Book book = new Book();
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(bookFile)))) {
	        TarArchiveEntry entry;
	        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
	        	if(entry.getName().startsWith(".metadata")) {
		        	if(entry.getName().startsWith(".metadata/thumbnail.png")) {
		        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        		IOUtils.copy(tarIn, baos);
		        		book.getBookMetaData().setThumbnail(baos.toByteArray());
		        	}
	        	} else {
	        		//deal with page numbers
	        	}
	        }
	    }
		if(book.getBookMetaData().getThumbnail() == null) {
			book.getBookMetaData().setThumbnail(DEFAULT_THUMBNAIL);
		}
		
		return book;
	}

	public String getBookFileName(String bookname) {
		return bookname.replace('_', ' ');
	}

	public String getBookname(File file) {
		String name = file.getName();
		name = StringUtils.substringBefore(name, ".");
		name = name.replace(' ', '_');
		return name;
	}
	
}
