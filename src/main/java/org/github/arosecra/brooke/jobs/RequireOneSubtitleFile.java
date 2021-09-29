package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequireOneSubtitleFile implements BrookeJobStep {

	@Override
	public boolean required(File folder) throws IOException {
		int nonEnglishSubtitleCount = 0;
		for(File file : folder.listFiles()) {
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
	public File execute(File folder) throws IOException {
		return folder;
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
	public List<File> filesRequiredForExecution(File folder) {
		return new ArrayList<>();
	}

}
