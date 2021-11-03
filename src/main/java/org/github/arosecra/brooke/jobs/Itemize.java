package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Itemize implements BrookeJobStep {
	
	private String ext;

	public Itemize(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean required(JobFolder job) throws IOException {
		boolean itemFileExists = false;
		boolean extensionExists = false;
		for(File file : job.remoteFiles) {
			if(file.getName().endsWith(".item"))
				itemFileExists = true;
			if(file.getName().endsWith(ext))
				extensionExists = true;
		}
		return !itemFileExists && extensionExists;
	}

	@Override
	public void execute(JobFolder job) throws IOException {
		new File(job.workFolder, job.remoteFolder.getName()+".item").createNewFile();
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
	public List<File> filesRequiredForExecution(JobFolder job) {
		return new ArrayList<>();
	}

}
