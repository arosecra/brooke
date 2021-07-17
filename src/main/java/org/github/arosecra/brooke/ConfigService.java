package org.github.arosecra.brooke;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

	public Configuration getConfig(String foldername, String filename, String extension) {
		return getConfig(foldername, filename + "." + extension);
	}

	public Configuration getConfig(String foldername, String filename) {
		return getConfig(foldername + "/" + filename);
	}

	public Configuration getConfig(String filename) {
		File file = new File(filename);
			if(file.exists()) {
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(params.properties().setFile(file)
			    		.setThrowExceptionOnMissing(false));
		    try {
				Configuration config = builder.getConfiguration();
				return config;
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Could not find " + file.getName());
		}
	    return null;
	}
}
