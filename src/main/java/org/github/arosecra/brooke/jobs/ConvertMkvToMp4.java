package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.util.CommandLine;

public class ConvertMkvToMp4 implements BrookeJobStep {

	@Override
	public boolean required(JobFolder folder) throws IOException {
		boolean remoteMp4Exists = false;
		boolean remoteMkvExists = false;
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith("mp4")) {
				remoteMp4Exists = true;
			}
			if(file.getName().endsWith("mkv")) {
				remoteMkvExists = true;
			}
		}
		return !remoteMp4Exists && remoteMkvExists;
	}

	@Override
	public void execute(JobFolder job) throws IOException {
		for(File file : job.workFiles) {
			if(file.getName().endsWith("mkv")) {
				File destFile = new File(job.workFolder, job.workFolder.getName() + ".mp4");
				File outfile = new File(job.workFolder, "out.txt");
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
