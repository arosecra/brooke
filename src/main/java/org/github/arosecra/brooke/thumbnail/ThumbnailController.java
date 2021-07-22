package org.github.arosecra.brooke.thumbnail;

import java.io.IOException;

import org.github.arosecra.brooke.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ThumbnailController {
	
	@Autowired
	private BookService bookService;

	@GetMapping(value="/thumbnail/{bookname}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] greeting(@PathVariable(name="bookname") String bookname, Model model) throws IOException {
		return bookService.getBookMetaData(bookname).getThumbnail();
	}

}