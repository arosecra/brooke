package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExtractMkvDetails implements BrookeJobStep {
	
	private static final String GS_EXE = "D:\\Software\\MKVToolNix\\mkvmerge.exe";
	
	public ExtractMkvDetails() {}

	private static void extractDetails(File file, File outputFile) {
		String[] args = new String[] {
			"cmd",
			"/C",
			
			GS_EXE,
			
			"-J",
			file.getAbsolutePath(),
			
			">",
			outputFile.getAbsolutePath()
		};
		
		org.github.arosecra.brooke.util.CommandLine.run(args);
	}

	@Override
	public boolean required(JobFolder folder) {
		boolean mkvDetailsExists = false;
		boolean mkvExists = false;
		for(File file : folder.remoteFiles) {
			if(file.getName().equals("mkvDetails.json"))
				mkvDetailsExists = true;
			if(file.getName().endsWith("mkv"))
				mkvExists = true;
		}		
		return mkvExists && !mkvDetailsExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {
		if(required(folder)) {
			
			File mkvFile = null;
			for(int i = 0; i < folder.workFiles.size(); i++) {
				File file = folder.workFiles.get(i);
				if(file.getName().endsWith("mkv")) {
					mkvFile = file;
				}
			}

			JobSubStep jss = new JobSubStep("Extract MKV Details", folder.workFolder, 0, 0);
			jss.startAndPrint();
			
			extractDetails(mkvFile, new File(folder.workFolder, "mkvDetails.json"));
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
			if(child.getName().endsWith("mkv"))
				result.add(child);
		}		
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}
}
