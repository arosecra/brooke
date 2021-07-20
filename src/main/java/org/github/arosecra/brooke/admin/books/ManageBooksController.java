package org.github.arosecra.brooke.admin.books;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.github.arosecra.brooke.admin.AdminService;
import org.github.arosecra.brooke.book.Book;
import org.github.arosecra.brooke.book.BookService;
import org.github.arosecra.brooke.catalog.BookListing;
import org.github.arosecra.brooke.library.Library;
import org.github.arosecra.brooke.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageBooksController {
	
	@Autowired
	private LibraryService libraryService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AdminService adminService;

	@GetMapping("/manage/books")
	public String getManageBooks(Model model) throws IOException {
		
		BooksOverview books = new BooksOverview();
		Library library = libraryService.getLibrary();
		Map<String, List<BookListing>> booksToListings = adminService.getBookToListingsMap(library);
		
		for(File file : new File("D:/scans/books").listFiles()) {
			Book book = bookService.getBook(file.getName());
			BookOverview overview = new BookOverview();
			overview.setName(book.getName());
			overview.setThumbnailGenerated(new File(file, "thumbnail.png").exists());
			if(booksToListings.containsKey(book.getName()))
				overview.setAssigned(booksToListings.get(book.getName()).size());
			overview.setCbtExists(new File(file, file.getName() + ".cbt").exists());
			overview.setTocGenerated(new File(file, "toc.txt").exists());
			
			if(overview.getAssigned() == 0)
				books.getBooks().add(overview);	
			
		}
		
		
		model.addAttribute("books", books);
		
		return "managebooks";
	}
}
