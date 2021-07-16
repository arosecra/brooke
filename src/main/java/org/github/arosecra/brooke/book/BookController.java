package org.github.arosecra.brooke.book;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;


	@GetMapping("/book/{bookname}")
	public String home(@PathVariable(name="bookname") String bookname, 
			Model model) {
		
		return "book";
	}

	@GetMapping(value="/book/page/{bookname}/{pagenumber}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] greeting(@PathVariable(name="bookname") String bookname, 
			@PathVariable(name="pagenumber") int pagenumber, 
			Model model) throws IOException {
		return bookService.getBook(bookname, pagenumber).getBookMetaData().getThumbnail();
	}

}