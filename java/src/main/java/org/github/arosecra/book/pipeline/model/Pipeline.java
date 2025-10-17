package org.github.arosecra.book.pipeline.model;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
	public List<JobStep> steps = new ArrayList<>();
	public String name;
	public String uses;
	public String produces;
	public String remoteDirectory;
	
	public Pipeline addStep(JobStep step) {
		this.steps.add(step);
		return this;
	}
	
	public Pipeline setName(String name) {
		this.name = name;
		return this;
	}
	
	public Pipeline setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
		return this;
	}
	
	public Pipeline setUses(String uses) {
		this.uses = uses;
		return this;
	}
	
	public Pipeline setProduces(String produces) {
		this.produces = produces;
		return this;
	}
	
}