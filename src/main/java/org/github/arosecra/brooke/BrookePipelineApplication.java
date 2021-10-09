package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.jobs.BrookeJobStep;
import org.github.arosecra.brooke.jobs.BrookeJobStep.JobFolder;
import org.github.arosecra.brooke.jobs.ConvertMkvToMp4;
import org.github.arosecra.brooke.jobs.ConvertSubtitleToVttFormat;
import org.github.arosecra.brooke.jobs.ConvertToWebp;
import org.github.arosecra.brooke.jobs.CreateCbtThumbnail;
import org.github.arosecra.brooke.jobs.Deskew;
import org.github.arosecra.brooke.jobs.ExtractChaptersXml;
import org.github.arosecra.brooke.jobs.ExtractPDFs;
import org.github.arosecra.brooke.jobs.ExtractSubtitles;
import org.github.arosecra.brooke.jobs.LightNovelRename;
import org.github.arosecra.brooke.jobs.RequireOneSubtitleFile;
import org.github.arosecra.brooke.model.Collection;
import org.github.arosecra.brooke.services.BrookeService;

public class BrookePipelineApplication {

	private static final Map<String, BrookeJobStep> JOBS = new HashMap<>();
	static {
		JOBS.put("LIGHT_NOVEL_RENAME", new LightNovelRename());
		JOBS.put("EXTRACT_BOOK_PDF", new ExtractPDFs());
		JOBS.put("EXTRACT_GENERAL_PDF", new ExtractPDFs("general"));
		JOBS.put("EXTRACT_SLEEVE_PDF", new ExtractPDFs("movie"));
		JOBS.put("CREATE_CBT_THUMBNAIL", new CreateCbtThumbnail());
		JOBS.put("CREATE_PDF_THUMBNAIL", new CreateCbtThumbnail(500));
		JOBS.put("CREATE_MOVIE_THUMBNAIL", new CreateCbtThumbnail(700));
		JOBS.put("DESKEW", new Deskew());
		JOBS.put("CONVERT_TO_WEBP", new ConvertToWebp());
		JOBS.put("CONVERT_TO_WEBP_RAW", new ConvertToWebp("_RAW.tar"));

		JOBS.put("EXTRACT_CHAPTERS_XML", new ExtractChaptersXml());
		JOBS.put("EXTRACT_SUBTITLE_TRACKS", new ExtractSubtitles());
		JOBS.put("CONVERT_MKV_TO_MP4", new ConvertMkvToMp4());
		JOBS.put("REQUIRE_ONE_SUBTITLE", new RequireOneSubtitleFile());
		JOBS.put("CONVERT_SUBTITLE_TO_VTT_FORMAT", new ConvertSubtitleToVttFormat());
		JOBS.put("OCR_ENGLISH_SUBTITLES", null);
	}

	public static void main(String[] args) throws IOException {
		BrookeService service = new BrookeService();
		Settings settings = new Settings();
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		service.setLibraryDao(libraryDao);

		File workDirectory = new File("D://scans//tobeexported");

		Map<String, List<JobFolder>> foldersToProcess = new TreeMap<>();
		Map<String, Collection> collections = new HashMap<>();
		
		selectWork(libraryDao, foldersToProcess, collections);
		printWorkStatus(foldersToProcess);
		executePipelines(workDirectory, foldersToProcess, collections);

	}

	private static void executePipelines(File workDirectory, Map<String, List<JobFolder>> foldersToProcess,
			Map<String, Collection> collections) throws IOException {
		for(Entry<String, List<JobFolder>> entry : foldersToProcess.entrySet()) {
			Collection collection = collections.get(entry.getKey());
			for (JobFolder remoteDir : entry.getValue()) {
				executePipeline(workDirectory, collection, remoteDir);
			}
		}
	}

	private static void executePipeline(File workDirectory, Collection collection, JobFolder remoteDir) throws IOException {
		remoteDir.workFolder = new File(workDirectory, remoteDir.remoteFolder.getName());

		File[] filesBeforeSteps = remoteDir.workFolder.listFiles();
		boolean continueSteps = true;
		int i = 0;
		while (continueSteps && i < collection.getPipeline().length) {
			String pipelineStep = collection.getPipeline()[i];
			BrookeJobStep step = JOBS.get(pipelineStep);
			if (step.required(remoteDir)) {
				if (step.isManual()) {
					continueSteps = false;
				} else {
					System.out.println("Executing " + pipelineStep + " on " + remoteDir.remoteFolder.getName());
					executeStep(remoteDir, pipelineStep, step);
				}
			}
			i++;
		}

		File[] filesAfterSteps = remoteDir.workFolder.listFiles();

		Set<File> filesToCopyRemotely = determineNewFiles(filesBeforeSteps, filesAfterSteps);
		for (File fileToCopyRemotely : filesToCopyRemotely) {
			FileUtils.copyFileToDirectory(fileToCopyRemotely, remoteDir.remoteFolder);
		}
		FileUtils.deleteDirectory(remoteDir.workFolder);
	}

	private static void printWorkStatus(Map<String, List<JobFolder>> foldersToProcess) {
		for(Map.Entry<String, List<JobFolder>> entry : foldersToProcess.entrySet()) {
			System.out.println(entry.getKey() + " requires " + entry.getValue().size() + " folders to be processed");
		}
	}

	private static void selectWork(LibraryDao libraryDao, Map<String, List<JobFolder>> foldersToProcess,
			Map<String, Collection> collections) throws IOException {
		for (Collection collection : libraryDao.getLibrary().getCollections()) {			
			collections.put(collection.getName(), collection);
			File remoteCollectionDir = new File(collection.getRemoteDirectory());
			
//			if(!collection.getName().contains("Movie"))
//				continue;

			foldersToProcess.put(collection.getName(), new ArrayList<>());
			
			java.util.Collection<File> remoteFiles = FileUtils.listFiles(remoteCollectionDir, null, true);
			
			Map<String, List<File>> filesPerPath = new TreeMap<>();
			
			
			for(File file : remoteFiles) {
				String key = file.getParentFile().getAbsolutePath();
				if(!filesPerPath.containsKey(key))
					filesPerPath.put(key, new ArrayList<>());
				filesPerPath.get(key).add(file);
			}
			

			for (File remoteItemFile : remoteFiles) {
				if(remoteItemFile.getName().endsWith(".item")) {
					List<File> remoteDir = filesPerPath.get(remoteItemFile.getParentFile().getAbsolutePath());
					List<File> remoteParentDir = filesPerPath.get(remoteItemFile.getParentFile().getParentFile().getAbsolutePath());
					
					JobFolder folder = new JobFolder();
					folder.remoteFolder = remoteItemFile.getParentFile();
					folder.remoteFiles = remoteDir;
				
					JobFolder parentFolder = new JobFolder();
					parentFolder.remoteFolder = remoteItemFile.getParentFile().getParentFile();
					parentFolder.remoteFiles = remoteParentDir;
					
					for (String pipelineStep : collection.getPipeline()) {
						BrookeJobStep step = JOBS.get(pipelineStep);
						if (step.required(folder)) {
							foldersToProcess.get(collection.getName()).add(folder);
						}
						if (remoteParentDir != null && step.required(parentFolder)) {
							foldersToProcess.get(collection.getName()).add(parentFolder);
						}
					}
				
				}
			}
		}
	}

	private static void executeStep(JobFolder job, String pipelineStep, BrookeJobStep step)
			throws IOException {
		if (step.isRemoteStep()) {
			// renames are carried out remotely to avoid an extra hard sync process
			// renames should be first in the pipeline
			step.execute(job);
		} else {
			// copy the files necessary for the step and execute it
			List<File> requiredFiles = step.filesRequiredForExecution(job);
			for (File file : requiredFiles) {
				String relativeFromRemote = file.getAbsolutePath().substring(job.remoteFolder.getAbsolutePath().length());
				File localFile = new File(job.workFolder, relativeFromRemote);
				FileUtils.copyFile(file, localFile);
			}
			job.workFiles = new ArrayList<>();
			job.workFiles.addAll(FileUtils.listFiles(job.workFolder, null, true));
			step.execute(job);
		}
	}

	private static Set<File> determineNewFiles(File[] filesBeforeSteps, File[] filesAfterSteps) {
		Set<File> result = new HashSet<>();
		Set<File> before = new HashSet<>();
		if (filesBeforeSteps != null)
			before = new HashSet(Arrays.asList(filesBeforeSteps));
		Set<File> after = new HashSet<>();
		if (filesAfterSteps != null)
			after = new HashSet(Arrays.asList(filesAfterSteps));

		for (File a : after) {
			if (!before.contains(a)) {
				result.add(a);
			}
		}

		return result;
	}
}