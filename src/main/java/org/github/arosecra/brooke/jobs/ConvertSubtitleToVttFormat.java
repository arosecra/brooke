package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ConvertSubtitleToVttFormat implements BrookeJobStep {

	@Override
	public boolean required(JobFolder folder) throws IOException {
		boolean vttExists = false;
		boolean srtExists = false;
		for(File file : folder.remoteFiles) {
			if(file.getName().equals("english.vtt"))
				vttExists = true;
			if(file.getName().equals("english.srt"))
				srtExists = true;
		}
		return !vttExists && srtExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		for(File file : folder.workFiles) {
			if(file.getName().endsWith("srt")) {
				
				List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
				List<String> newLines = new ArrayList<>();
				newLines.add("WEBVTT");
				newLines.add("");
				
				for(String line : lines) {
					if(line.contains("-->"))
						newLines.add(line.replace(',', '.'));
					else if(StringUtils.isEmpty(line)) {
						newLines.add(line);
					} else if (!NumberUtils.isCreatable(line)) {
						newLines.add(line);
					}
				}
				FileUtils.writeLines(new File(folder.workFolder, "english.vtt"), newLines);
//				CommandLine.run(new String[] {
//					"java.exe",
//					"-jar D:\\projects\\bdsup2sub.jar",
//					"-o",
//					folder.getAbsolutePath() + "\\english.vtt "+
//					file.getAbsolutePath()
//				});
			}
		}
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
	public List<File> filesRequiredForExecution(JobFolder folder) {
		List<File> mkvs = new ArrayList<>();
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith("srt"))
				mkvs.add(file);
		}
		return mkvs;
	}

}
