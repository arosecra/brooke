package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequireOneSubtitleFile implements BrookeJobStep {

	@Override
	public boolean required(JobFolder folder) throws IOException {
		int nonEnglishSubtitleCount = 0;
		for(File file : folder.remoteFiles) {
			if((file.getName().endsWith("vtt") ||
				file.getName().endsWith("sup") ||
				file.getName().endsWith("sub")
				) && !file.getName().startsWith("english")) {
				nonEnglishSubtitleCount++;
			}
		}
		return nonEnglishSubtitleCount > 0;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		
	}

	@Override
	public boolean isManual() {
		return true;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}

	@Override
	public List<File> filesRequiredForExecution(JobFolder folder) {
		return new ArrayList<>();
	}

}
