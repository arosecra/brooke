package org.github.arosecra.brooke.book;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
		File tocFile = new File(bookFolder, "toc.txt");

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
		
		if(tocFile.exists()) {
			List<String> lines = FileUtils.readLines(tocFile, StandardCharsets.UTF_8);
			for(String line : lines) {
				if(!StringUtils.isEmpty(line.trim())) {
					String[] parts=line.split("=");
					ToCEntry entry = new ToCEntry();
					entry.setPageIndex(Integer.parseInt(parts[0]));
					entry.setName(parts[1]);
					book.getBookMetaData().getTocEntries().add(entry);
				}
			}
		}
		return book;
	}

	public String getBookFileName(String bookname) {
		return bookname.replace('_', ' ');
	}

	public String getBookname(String name) {
		return name.replace(' ', '_');
	}

	public void addToc(String bookname, int pageindex, String toc) throws IOException {
		File bookFolder = getBookFolder(bookname);
		File tocFile = new File(bookFolder, "toc.txt");
		List<String> lines = new ArrayList<>();
		if(tocFile.exists()) lines = FileUtils.readLines(tocFile, StandardCharsets.UTF_8);
		lines.add(String.format("%d=%s", pageindex, toc));
		Collections.sort(lines, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				int i1 = Integer.parseInt(o1.split("=")[0]);
				int i2 = Integer.parseInt(o2.split("=")[0]);
				return Integer.compare(i1, i2);
			}
		});
		FileUtils.writeLines(tocFile, lines);
	}

}
