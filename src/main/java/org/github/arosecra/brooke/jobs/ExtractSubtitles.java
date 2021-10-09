package org.github.arosecra.brooke.jobs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.github.arosecra.brooke.util.CommandLine;
import org.thymeleaf.util.StringUtils;

public class ExtractSubtitles implements BrookeJobStep {

	@Override
	public boolean required(JobFolder folder) throws IOException {
		boolean subTitlesExist = false;
		
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith("sup") || file.getName().endsWith("sub")) 
				subTitlesExist =true;
		}
		return subTitlesExist;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		for(File file : folder.workFiles) {
			if(file.getName().endsWith("mkv")) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				CommandLine.run(new String[] {
					"D:\\software\\MKVToolNix\\mkvinfo.exe", 
					file.getAbsolutePath()	
				}, ps);
				
				String info = os.toString();
				
				boolean inTracks = false;
				String currentTrackId = "";
				Set<String> subtitleTracks = new HashSet<>();
				List<String> lines = IOUtils.readLines(new ByteArrayInputStream(info.getBytes()));
				for(String line : lines) {
					if(line.contains("Tracks"))
						inTracks = true;
					else if(line.contains("Chapters"))
						inTracks = false;
					else if(inTracks) {
						if(line.contains("Track UID:")) {
							currentTrackId = StringUtils.substringAfter(line, "Track UID:").trim();
						}
						if(line.contains("Track type: subtitles")) {
							subtitleTracks.add(currentTrackId);
						}
					}
					
				}
				
				for(String subtitleTrack : subtitleTracks) {
					CommandLine.run(new String[] {
						"D:\\software\\MKVToolNix\\mkvextract.exe", 
						"-q",
						file.getAbsolutePath(),
						subtitleTrack+":"+subtitleTrack+"_sub"
					});
				}
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
			if(file.getName().endsWith("mkv"))
				mkvs.add(file);
		}
		return mkvs;
	}

}
