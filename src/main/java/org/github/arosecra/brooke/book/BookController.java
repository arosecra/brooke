package org.github.arosecra.brooke.book;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;

	@GetMapping("/book/{bookname}/{pageindex}")
	public String getPages(@PathVariable(name="bookname") String bookname, 
			@PathVariable(name="pageindex") int pageindex, 
			Model model) throws IOException {
		OpenBook book = bookService.openBookTo(bookname, pageindex);
		model.addAttribute("book", book);
		model.addAttribute("bookMetaData", bookService.getBookMetaData(bookname));
		return "book";
	}
	
	
	@GetMapping("/book/addtotoc/{bookname}/{pageindex}/{toc}")
	public String addToc(@PathVariable(name="bookname") String bookname, 
			@PathVariable(name="pageindex") int pageindex, 
			@PathVariable(name="toc") String toc, 
			Model model) throws IOException {
		bookService.addToc(bookname, pageindex, toc);
		OpenBook book = bookService.openBookTo(bookname, pageindex);
		model.addAttribute("book", book);
		model.addAttribute("bookMetaData", bookService.getBookMetaData(bookname));
		return "book";
	}

}