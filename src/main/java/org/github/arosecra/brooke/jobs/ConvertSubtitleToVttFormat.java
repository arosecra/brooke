package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.github.arosecra.brooke.util.CommandLine;

public class ConvertSubtitleToVttFormat implements BrookeJobStep {

	@Override
	public boolean required(File folder) throws IOException {
		File vtt = new File(folder, "english.vtt");
		File srt = new File(folder, "english.srt");
		return !vtt.exists() && srt.exists();
	}

	@Override
	public File execute(File folder) throws IOException {
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith("srt")) {
				
				List<String> lines = FileUtils.readLines(file);
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
				FileUtils.writeLines(new File(folder, "english.vtt"), newLines);
//				CommandLine.run(new String[] {
//					"java.exe",
//					"-jar D:\\projects\\bdsup2sub.jar",
//					"-o",
//					folder.getAbsolutePath() + "\\english.vtt "+
//					file.getAbsolutePath()
//				});
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
			if(file.getName().endsWith("srt"))
				mkvs.add(file);
		}
		return mkvs;
	}

}
