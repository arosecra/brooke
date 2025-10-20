package org.github.arosecra.book.pipeline.model;

import java.io.File;

public class JobSubStep {
	
	private String name;
	private File folder;
	private int index;
	private int total;
	private long start;
	private long end;
	private JobStep step;
	
	public JobSubStep(String name, File folder, int index, int total) {
		super();
		this.name = name;
		this.folder = folder;
		this.index = index;
		this.total = total;
	}

	public void startAndPrint() {
		start();
		printStart();
	}
	public void start() {
		start = System.currentTimeMillis();
		
	}
	
	public void printStart() {
		System.out.print(name + " " + folder.getName() + " (" + ((int)index+1) + " of " + total + ") ");
	}
	
	public void printStartLn() {
		System.out.println(name + " " + folder.getName() + " (" + ((int)index+1) + " of " + total + ") ");
	}
	
	public void endAndPrint() {
		end();
		printEnd();
	}
	public void end() {
		end = System.currentTimeMillis();
	}
	
	public void printEnd() {
		
		long diff = end-start;
		
		if(diff > 120000) {
		    System.out.println("... " + (diff / 60000) + " minutes");
		} else if(diff > 5000) {
		    System.out.println("... " + (diff / 1000) + " seconds");
		} else {
		    System.out.println("... " + (diff) + " milliseconds");
		}
	}

	public JobStep getStep() {
		return step;
	}

	public void setStep(JobStep step) {
		this.step = step;
	}
}
