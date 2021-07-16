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
		Book book = bookService.getBook(bookname, pageindex);
		model.addAttribute("book", book);
		return "book";
	}

}