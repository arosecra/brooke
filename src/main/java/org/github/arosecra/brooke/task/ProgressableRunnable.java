package org.github.arosecra.brooke.task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public interface ProgressableRunnable extends Runnable {

	public AtomicBoolean started();
	
	public AtomicLong getTotalProgress();
	public AtomicLong getCurrentProgress();
	
}
