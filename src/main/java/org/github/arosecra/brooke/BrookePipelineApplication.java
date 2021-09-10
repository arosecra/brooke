package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.github.arosecra.brooke.jobs.BrookeJobStep;
import org.github.arosecra.brooke.jobs.ConvertToWebp;
import org.github.arosecra.brooke.jobs.CreateCbtThumbnail;
import org.github.arosecra.brooke.jobs.Deskew;
import org.github.arosecra.brooke.jobs.Extract;
import org.github.arosecra.brooke.jobs.JobSubStep;
import org.github.arosecra.brooke.jobs.LightNovelRename;
import org.github.arosecra.brooke.util.Try;

public class BrookePipelineApplication {

	public static void main(String[] args) throws IOException {
		
		Map<String, List<BrookeJobStep>> pipelines = new HashMap<>();
		setupBooksPipeline(pipelines);
		setupLightNovelsPipeline(pipelines);
		setupGraphicNovelPipeline(pipelines);
		setupAnimePipeline(pipelines);
		setupMoviesPipeline(pipelines);
//		pipelines.put("Research_Papers", new ArrayList<>());
		
		Map<String, List<JobSubStep>> stepsPerType = new HashMap<>();
		
		//for each collection
		
		//  iterate through the remote items and determine if a step is necessary
		//  if the step is necessary, check if files need to be downloaded locally
		//  if so, add a step to download the files, and steps to remove them locally after
		//  add step to copy file remotely after step is done
		
		
		//  iterate through the steps for the type, and see if they are needed
		//  if it's a manual step, and needed, do not continue in the list for that file
		//  if needed, add to a list
		
		//print out what is necessary per folder
		
		//if in execute mode, get to work
		//add options to only do so much at a time - by collection, type or # of steps
		
		
		//temporary loop until i work through the sync mechanics
		File basedir = new File("D:\\scans\\tobeexported");
		
		boolean done = false;
		while(!done) {
			boolean workDoneInIteration = false;

			for(File folder : Try.listFilesSafe(basedir)) {
				
				if(folder.exists()) { //folder may have been manually moved since the start of the job
					for(BrookeJobStep step : pipelines.get("Books")) {
						if(step.required(folder)) {
							System.out.println("Executing " + step.getClass().getSimpleName() + " against " + folder.getName());
							step.execute(folder);
							workDoneInIteration = true;
						}
					}
				}
			}
			
			
			if(!workDoneInIteration) 
				done = true;
		}
		
		
	}

	private static void setupMoviesPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		pipelines.put("Movies", new ArrayList<>());
	}

	private static void setupAnimePipeline(Map<String, List<BrookeJobStep>> pipelines) {
		pipelines.put("Anime", new ArrayList<>());
		//convert to mp4
		//extract subs
		//ocr
		//rename pdfs
		//create thumbnail
	}

	private static void setupLightNovelsPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		List<BrookeJobStep> steps = new ArrayList<>();
		steps.add(new LightNovelRename());
		steps.add(new Extract());
		steps.add(new CreateCbtThumbnail());
		steps.add(new Deskew());
		steps.add(new ConvertToWebp());
		pipelines.put("Light_Novels", steps);
	}

	private static void setupGraphicNovelPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		List<BrookeJobStep> steps = new ArrayList<>();
		steps.add(new Extract());
		steps.add(new CreateCbtThumbnail());
		steps.add(new ConvertToWebp());
		pipelines.put("Graphic_Novels", steps);
	}

	private static void setupBooksPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		List<BrookeJobStep> steps = new ArrayList<>();
		steps.add(new Extract());
		steps.add(new CreateCbtThumbnail());
		steps.add(new Deskew());
		steps.add(new ConvertToWebp());
		pipelines.put("Books", steps);
	}
	
	
}
