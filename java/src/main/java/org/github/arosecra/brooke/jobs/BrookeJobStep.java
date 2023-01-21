package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BrookeJobStep {
	public static class JobFolder {
		public File remoteFolder;
		public File workFolder;
		public List<File> remoteFiles;
		public List<File> workFiles;
	}
	
	public boolean required(JobFolder job) throws IOException;
	public void execute(JobFolder job) throws IOException;
	public boolean isManual();
	public boolean isRemoteStep();
	public List<File> filesRequiredForExecution(JobFolder job);
}
