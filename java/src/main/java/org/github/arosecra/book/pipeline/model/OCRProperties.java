package org.github.arosecra.book.pipeline.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OCRProperties {
	public List<String> blankPages = new ArrayList<>();
	public List<String> excludedPages = new ArrayList<>();
	public List<String> imagePages = new ArrayList<>();

	public static OCRProperties fromLists(List<String> blankPages, List<String> imagePages,
			List<String> excludedPages) {
		OCRProperties res = new OCRProperties();
		res.blankPages = blankPages;
		res.imagePages = imagePages;
		res.excludedPages = excludedPages;
		return res;
	}

	public static OCRProperties fromProperties(File file) {
		Properties ocrProperties = new Properties();
		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			ocrProperties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		OCRProperties res = new OCRProperties();
		if(ocrProperties.get("excludePages") != null) {
			String[] propsExcluded = ocrProperties.get("excludePages").toString().split(",");
			res.excludedPages = Arrays.asList(propsExcluded);
		}
		if(ocrProperties.get("imagePages") != null) {
			String[] propsImages = ocrProperties.get("imagePages").toString().split(",");
			res.imagePages = Arrays.asList(propsImages);
			
		}
		if(ocrProperties.get("blankPages") != null) {
			String[] propsBlank = ocrProperties.get("blankPages").toString().split(",");
			res.blankPages = Arrays.asList(propsBlank);			
		}

		return res;
	}

	public static void toProperties(OCRProperties properties, File file) {
		Properties props = new Properties();

		props.put("blankPages", serializeArray(properties.blankPages));
		props.put("imagePages", serializeArray(properties.imagePages));
		props.put("excludePages", serializeArray(properties.excludedPages));

		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
			props.store(os, "OCR Configuration");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String serializeArray(List<String> arr) {
		String res = "";
		if (arr != null) {
			for (String value : arr) {
				res += value + ",";
			}
		}
		return res;
	}

}