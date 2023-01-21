package org.github.arosecra.brooke.task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public interface IRunnableTask extends Runnable {

	public String getJobDescription();
	public String getJobType();
	public String getCurrentProgressDescription();
	public String getTotalProgressDescription();

	public AtomicBoolean started();
	
	public AtomicLong getTotalProgress();
	public AtomicLong getCurrentProgress();
	
}
