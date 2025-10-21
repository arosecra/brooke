package org.github.arosecra.book.pipeline.model;

import java.io.IOException;

public interface JobStep {
	public void execute(Pipeline pipeline, JobFolder job) throws IOException;
}
