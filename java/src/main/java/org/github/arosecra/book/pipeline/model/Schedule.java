
package org.github.arosecra.book.pipeline.model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	
	public Pipeline pipeline;
	public RootFolder rootFolder;
	
	public List<RemoteFolder> workRequired = new ArrayList<>();

	public Schedule(Pipeline pipeline, RootFolder rootFolder) {
		super();
		this.pipeline = pipeline;
		this.rootFolder = rootFolder;
	}
	
	

}
