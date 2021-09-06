package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.IOException;

public interface BrookeJobStep {
	public boolean required(File folder) throws IOException;
	public File execute(File folder) throws IOException;
	public boolean isManual();
}
