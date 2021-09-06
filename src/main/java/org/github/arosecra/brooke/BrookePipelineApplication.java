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
		
		List<File> filesToProcess = new ArrayList<>();
		
		//for each collection
		
		//  iterate through the items
		
		//  iterate through the steps for the type, and see if they are needed
		//  if it's a manual step, and needed, do not continue in the list for that file
		//  if needed, add to a list
		
		//print out what is necessary per file
		
		//if in execute mode, get to work
		
		
		//temporary loop until i work through the sync mechanics
		File basedir = new File("D:\\scans\\tobeexported");
		
		for(File folder : Try.listFilesSafe(basedir)) {
			
			for(BrookeJobStep step : pipelines.get("Books")) {
				if(step.required(folder)) {
					System.out.println("Executing " + step.getClass().getSimpleName() + " against " + folder.getName());
					step.execute(folder);
				}
			}
			
			
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
		steps.add(new CreateCbtThumbnail());
		steps.add(new Extract());
		steps.add(new Deskew());
		steps.add(new ConvertToWebp());
		pipelines.put("Light_Novels", steps);
	}

	private static void setupGraphicNovelPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		List<BrookeJobStep> steps = new ArrayList<>();
		steps.add(new CreateCbtThumbnail());
		steps.add(new Extract());
		steps.add(new ConvertToWebp());
		pipelines.put("Graphic_Novels", steps);
	}

	private static void setupBooksPipeline(Map<String, List<BrookeJobStep>> pipelines) {
		List<BrookeJobStep> steps = new ArrayList<>();
		steps.add(new CreateCbtThumbnail());
		steps.add(new Extract());
		steps.add(new Deskew());
		steps.add(new ConvertToWebp());
		pipelines.put("Books", steps);
	}
	
	
}
