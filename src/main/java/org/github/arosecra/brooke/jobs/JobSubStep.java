package org.github.arosecra.brooke.jobs;

import java.io.File;

public class JobSubStep {
	
	private String name;
	private File folder;
	private int index;
	private int total;
	private long start;
	private long end;
	
	public JobSubStep(String name, File folder, int index, int total) {
		super();
		this.name = name;
		this.folder = folder;
		this.index = index;
		this.total = total;
	}


	public void start() {
		System.out.print(name + " " + folder.getName() + " (" + index + " of " + total + ") ");
		start = System.currentTimeMillis();
		
	}
	
	
	public void end() {
		end = System.currentTimeMillis();
		
		long diff = end-start;
		
		if(diff > 120000) {
		    System.out.println("... " + (diff / 60000) + " minutes");
		} else if(diff > 5000) {
		    System.out.println("... " + (diff / 1000) + " seconds");
		} else {
		    System.out.println("... " + (diff) + " milliseconds");
		}
	}
}
