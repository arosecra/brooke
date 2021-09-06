package org.github.arosecra.brooke.jobs;

public class JobSubStep {
	
	private String name;
	private String filename;
	private int index;
	private int total;
	private long start;
	private long end;
	
	public JobSubStep(String name, String filename, int index, int total) {
		super();
		this.name = name;
		this.filename = filename;
		this.index = index;
		this.total = total;
	}


	public void printStart() {
		System.out.print(name + " " + filename + " (" + index + " of " + total + ") ");
		start = System.currentTimeMillis();
		
	}
	
	
	public void printEnd() {
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
