package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BrookeJobStep {
	public boolean required(File folder) throws IOException;
	public File execute(File folder) throws IOException;
	public boolean isManual();
	public boolean isRemoteStep();
	public List<File> filesRequiredForExecution(File folder);
}
