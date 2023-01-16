package org.github.arosecra.brooke.jobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.github.arosecra.brooke.model.api.BookDetailsApiModel;
import org.github.arosecra.brooke.model.api.TocEntryApiModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


public class ExtractCbtDetails implements BrookeJobStep {
		
	public ExtractCbtDetails() {}

	private static void extractDetails(File cbtFile, File rawFile, File tocFile, File outputFile) throws FileNotFoundException, IOException {
		BookDetailsApiModel details = new BookDetailsApiModel();
		
		
		//determine the # of pages
		int currentPage = 0;
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(cbtFile)))) {
			TarArchiveEntry entry;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				currentPage++;
			}
		}

		details.setNumberOfPages(currentPage);
		
		long mbInBytes = 1024;
		
		if(cbtFile != null) {
			details.setCbtSize(cbtFile.length() / mbInBytes);
		}
		
		if(rawFile != null) {
			details.setRawSize(rawFile.length() / mbInBytes);
		}
		
		if(details.getRawSize() > 0 && details.getCbtSize() > 0) {
			double percent = (double) 
					(
							(double)details.getCbtSize() 
						/ 
							(double)details.getRawSize()
						);
			
			percent = percent * 100;
			
			BigDecimal bd = new BigDecimal(percent);
			bd = bd.setScale(2, RoundingMode.DOWN);
			
			details.setCompression(Double.parseDouble(bd.toPlainString()));
		}
		
		if(tocFile != null) {
			List<String> lines = FileUtils.readLines(tocFile);
			
			for(String line : lines) {
				if(!StringUtils.isAllBlank(line)) {
					String prefix = line.split("=")[0];
					String suffix = line.split("=")[1];
					TocEntryApiModel entry = new TocEntryApiModel();
					entry.setName(suffix);
					entry.setPageNumber(Integer.parseInt(prefix));
					details.getTocEntries().add(entry);
				}
			}
		}
		
		



		YAMLFactory yf = new YAMLFactory();
		ObjectMapper mapper = new ObjectMapper(yf);

		BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(outputFile));
		yf.createGenerator(fos).writeObject(details);
		fos.flush();
		fos.close();
	}

	@Override
	public boolean required(JobFolder folder) {
		boolean cbtDetailsExists = false;
		boolean cbtExists = false;
		for(File file : folder.remoteFiles) {
			if(file.getName().equals("cbtDetails.yaml"))
				cbtDetailsExists = true;
			if(file.getName().endsWith("cbt"))
				cbtExists = true;
		}		
		return cbtExists && !cbtDetailsExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		if(required(folder)) {
			
			
			
			File cbtFile = null;
			File rawFile = null;
			File tocFile = null;
			for(int i = 0; i < folder.workFiles.size(); i++) {
				File file = folder.workFiles.get(i);
				if(file.getName().endsWith("cbt")) {
					cbtFile = file;
				} else if(file.getName().contains("_RAW")) {
					rawFile = file;
				} else if(file.getName().equals("toc.txt")) {
					tocFile = file;
				}
			}

			JobSubStep jss = new JobSubStep("Extract CBT Details", folder.workFolder, 0, 0);
			jss.startAndPrint();
			
			extractDetails(cbtFile, rawFile, tocFile, new File(folder.workFolder, "cbtDetails.yaml"));
		}
	}


	@Override
	public boolean isManual() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(JobFolder folder) {
		List<File> result = new ArrayList<>();
		for(File child : folder.remoteFiles) {
			if(child.getName().endsWith("cbt"))
				result.add(child);
			if(child.getName().contains("_RAW")) {
				result.add(child);
			}
			if(child.getName().equals("toc.txt")) {
				result.add(child);
			}
		}
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}
}
