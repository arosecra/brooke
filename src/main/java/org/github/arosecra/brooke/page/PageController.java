package org.github.arosecra.brooke.page;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

	@Autowired
	private PageService pageService;

	@GetMapping(value="/page/{bookname}/{pageindex}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getPage(@PathVariable(name="bookname") String bookname, 
			@PathVariable(name="pageindex") int pageindex, 
			Model model) throws IOException {
		return pageService.getPage(bookname, pageindex).getData();
	}

}