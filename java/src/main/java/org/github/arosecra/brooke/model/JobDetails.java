package org.github.arosecra.brooke.model;

public class JobDetails {
	private long jobNumber;
	private String jobType;
	private long total;
	private long current;
	private boolean started;
	
	
	public long getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(long jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	public boolean isStarted() {
		return started;
	}
	public void setStarted(boolean started) {
		this.started = started;
	}
	public long getCurrent() {
		return current;
	}
	public void setCurrent(long current) {
		this.current = current;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
