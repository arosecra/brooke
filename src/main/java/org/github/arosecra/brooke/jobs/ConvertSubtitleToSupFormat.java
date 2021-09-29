package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.util.CommandLine;

public class ConvertSubtitleToSupFormat implements BrookeJobStep {

	@Override
	public boolean required(File folder) throws IOException {
		File sup = new File(folder, folder.getName() + ".sup");
		return !sup.exists();
	}

	@Override
	public File execute(File folder) throws IOException {
		for(File file : folder.listFiles()) {
			if(file.getName().endsWith("sub")) {
				CommandLine.run(new String[] {
					"java.exe",
					"-jar D:\\projects\\bdsup2sub.jar",
					"-o",
					folder.getAbsolutePath() + "\\english.sup "+
					file.getAbsolutePath()
				});
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
			if(file.getName().endsWith("sub"))
				mkvs.add(file);
			if(file.getName().endsWith("xml"))
				mkvs.add(file);
		}
		return mkvs;
	}

}
