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
import org.github.arosecra.brooke.JpaService;
import org.github.arosecra.brooke.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService implements JpaService<Book, Long> {
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
	private BookRepository bookRepository;

	@Autowired
	private Settings settings;
	
	public File getBookFolder(String bookname) {
		return new File(settings.getBooksHome(), bookname);
	}
	
	public OpenBook openBookTo(String bookname) throws IOException {
		return openBookTo(bookname, -1);
	}

	public OpenBook openBookTo(String bookname, int page) throws IOException {
		File bookFolder = getBookFolder(bookname);

		OpenBook book = new OpenBook();
		book.setDisplayName(getBookDisplayName(FilenameUtils.getBaseName(bookFolder.getName())));
		book.setName(getBookFilename(bookname));
		book.setLocal(isLocal(bookname));

		book.setLeftPage(page);
		book.setRightPage(page + 1);
		return book;
	}
	
	public BookMetaData getBookMetaData(String bookname) throws IOException {
		BookMetaData bmd = new BookMetaData();
		
		File bookFolder = getBookFolder(bookname);
		File thumbnailFile = new File(bookFolder, "thumbnail.png");
		File tocFile = new File(bookFolder, "toc.txt");
		

		if(!thumbnailFile.exists()) {
			bmd.setThumbnail(DEFAULT_THUMBNAIL);
		} else {
			bmd.setThumbnail(FileUtils.readFileToByteArray(thumbnailFile));
		}

		if(tocFile.exists()) {
			List<String> lines = FileUtils.readLines(tocFile, StandardCharsets.UTF_8);
			for(String line : lines) {
				if(!StringUtils.isEmpty(line.trim())) {
					String[] parts=line.split("=");
					ToCEntry entry = new ToCEntry();
					entry.setPageIndex(Integer.parseInt(parts[0]));
					entry.setName(parts[1]);
					bmd.getTocEntries().add(entry);
				}
			}
		}
		return bmd;
	}

	public String getBookDisplayName(String bookname) {
		return bookname.replace('_', ' ');
	}

	public String getBookFilename(String bookname) {
		return bookname.replace(' ', '_');
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
	
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	public Book findByFilename(String name) {
		return bookRepository.findByFilename(name);
	}

	@Override
	public JpaRepository<Book, Long> getRepository() {
		return bookRepository;
	}

	public boolean isLocal(String bookname) {
		File bookfolder = getBookFolder(bookname);
		File cbtFile = new File(bookfolder, bookname +".cbt");
		return cbtFile.exists();
	}

}
