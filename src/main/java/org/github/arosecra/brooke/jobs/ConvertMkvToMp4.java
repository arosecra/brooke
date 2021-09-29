package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.util.CommandLine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConvertMkvToMp4 implements BrookeJobStep {

	@Override
	public boolean required(File folder) throws IOException {
		File mp4 = new File(folder, folder.getName() + ".mp4");
		return !mp4.exists();
	}

	@Override
	public File execute(File folder) throws IOException {
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith("mkv")) {
				File destFile = new File(folder, folder.getName() + ".mp4");
				File outfile = new File(folder, "out.txt");
				CommandLine.run(new String[] {
					"cmd.exe",
					"/c",
					"start",
					"/wait",
					"cmd",
					"/c",
					"D:\\software\\handbrake\\handbrakecli.exe", 
					"--preset", 
					"MediaLibrary",
	    			"--preset-import-file",
	    			"D:\\video\\handbrake_preset.json",
	    			"-i",
	    			file.getAbsolutePath(),
	    			"-o",
	    			destFile.getAbsolutePath(),
	    			">",
	    			outfile.getAbsolutePath(),
	    			"2>&1"
				});
				
				if(outfile.exists()) outfile.delete();
			}
		}
		return folder;
	}

	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(File folder) {
		List<File> mkvs = new ArrayList<>();
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith("mkv"))
				mkvs.add(file);
		}
		return mkvs;
	}

}
