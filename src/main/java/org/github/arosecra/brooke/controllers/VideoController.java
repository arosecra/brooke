package org.github.arosecra.brooke.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoController 
{

	@GetMapping(value="/v2")
	public String v() {
	    return "v2";
	}

	@GetMapping(value="/video", produces="video/mp4")
	public void greeting(HttpServletResponse response) {
	    try {
	        // get your file as InputStream
	        InputStream is = new BufferedInputStream(new FileInputStream(new File("D:/video/Angel Beats 01.mp4")));
	        // copy it to response's OutputStream
	        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	        response.flushBuffer();
	      } catch (IOException ex) {
//	        log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
	        throw new RuntimeException("IOError writing file to output stream", ex);
	      }
	}

	@GetMapping(value="/vtt")
	public void vtt(HttpServletResponse response) {
	    try {
	        // get your file as InputStream
	        InputStream is = new BufferedInputStream(new FileInputStream(new File("D:/video/Angel Beats 01.vtt")));
	        // copy it to response's OutputStream
	        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	        response.flushBuffer();
	      } catch (IOException ex) {
//	        log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
	        throw new RuntimeException("IOError writing file to output stream", ex);
	      }
	}
	
}