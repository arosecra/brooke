package org.github.arosecra.book.pipeline.model;

import java.util.ArrayList;
import java.util.List;

public class MasterSchedule {
	public List<Schedule> schedules = new ArrayList<>();
	
	public MasterSchedule schedule(Pipeline pipeline, RootFolder rootFolder) {
		this.schedules.add(new Schedule(pipeline, rootFolder));
		return this;
	}
	
}


