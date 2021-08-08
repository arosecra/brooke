package org.github.arosecra.brooke.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class Try {

	public static void loadProperties(Properties props, File propertiesFile) {
		try {
			props.load(new BufferedInputStream(new FileInputStream(propertiesFile)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> readLines(File childFile) {
		try {
			return  FileUtils.readLines(childFile, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
