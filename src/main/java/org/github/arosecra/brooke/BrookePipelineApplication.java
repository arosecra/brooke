package org.github.arosecra.brooke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.dao.LibraryDao;
import org.github.arosecra.brooke.jobs.BrookeJobStep;
import org.github.arosecra.brooke.jobs.ConvertMkvToMp4;
import org.github.arosecra.brooke.jobs.ConvertSubtitleToSupFormat;
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
		JOBS.put("CONVERT_SUBTITLE_TO_SUP_FORMAT", new ConvertSubtitleToSupFormat());
		JOBS.put("OCR_ENGLISH_SUBTITLES", null);
	}

	public static void main(String[] args) throws IOException {
		BrookeService service = new BrookeService();
		Settings settings = new Settings();
		LibraryDao libraryDao = new LibraryDao();
		libraryDao.setSettings(settings);
		service.setLibraryDao(libraryDao);

		File workDirectory = new File("D://scans//tobeexported");

		Map<String, List<File>> foldersToProcess = new TreeMap<>();
		Map<String, Collection> collections = new HashMap<>();
		
		selectWork(libraryDao, foldersToProcess, collections);
		printWorkStatus(foldersToProcess);
		executePipelines(workDirectory, foldersToProcess, collections);

	}

	private static void executePipelines(File workDirectory, Map<String, List<File>> foldersToProcess,
			Map<String, Collection> collections) throws IOException {
		for(Map.Entry<String, List<File>> entry : foldersToProcess.entrySet()) {
			Collection collection = collections.get(entry.getKey());
			for (File remoteDir : entry.getValue()) {
				remoteDir = executePipeline(workDirectory, collection, remoteDir);
			}
		}
	}

	private static File executePipeline(File workDirectory, Collection collection, File remoteDir) throws IOException {
		File localWorkDir = new File(workDirectory, remoteDir.getName());

		File[] filesBeforeSteps = localWorkDir.listFiles();
		boolean continueSteps = true;
		int i = 0;
		while (continueSteps && i < collection.getPipeline().length) {
			String pipelineStep = collection.getPipeline()[i];
			BrookeJobStep step = JOBS.get(pipelineStep);
			if (step.required(remoteDir)) {
				if (step.isManual()) {
					continueSteps = false;
				} else {
					System.out.println("Executing " + pipelineStep + " on " + remoteDir.getName());
					remoteDir = executeStep(remoteDir, localWorkDir, pipelineStep, step);
				}
			}
			i++;
		}

		File[] filesAfterSteps = localWorkDir.listFiles();

		Set<File> filesToCopyRemotely = determineNewFiles(filesBeforeSteps, filesAfterSteps);
		for (File fileToCopyRemotely : filesToCopyRemotely) {
			FileUtils.copyFileToDirectory(fileToCopyRemotely, remoteDir);
		}
		FileUtils.deleteDirectory(localWorkDir);
		return remoteDir;
	}

	private static void printWorkStatus(Map<String, List<File>> foldersToProcess) {
		for(Map.Entry<String, List<File>> entry : foldersToProcess.entrySet()) {
			System.out.println(entry.getKey() + " requires " + entry.getValue().size() + " folders to be processed");
		}
	}

	private static void selectWork(LibraryDao libraryDao, Map<String, List<File>> foldersToProcess,
			Map<String, Collection> collections) throws IOException {
		for (Collection collection : libraryDao.getLibrary().getCollections()) {			
			collections.put(collection.getName(), collection);
			File remoteCollectionDir = new File(collection.getRemoteDirectory());
			
//			if(!collection.getName().contains("Movie"))
//				continue;

			foldersToProcess.put(collection.getName(), new ArrayList<>());

			for (File remoteItemFile : FileUtils.listFiles(remoteCollectionDir, new String[] { "item" }, true)) {
				File remoteDir = remoteItemFile.getParentFile();

				for (String pipelineStep : collection.getPipeline()) {
					BrookeJobStep step = JOBS.get(pipelineStep);
					if (step.required(remoteDir)) {
						foldersToProcess.get(collection.getName()).add(remoteDir);
					}
					if (step.required(remoteDir.getParentFile())) {
						foldersToProcess.get(collection.getName()).add(remoteDir.getParentFile());
					}
				}
			}
		}
	}

	private static File executeStep(File remoteDir, File localWorkDir, String pipelineStep, BrookeJobStep step)
			throws IOException {
		if (step.isRemoteStep()) {
			// renames are carried out remotely to avoid an extra hard sync process
			// renames should be first in the pipeline
			remoteDir = step.execute(remoteDir);
		} else {
			// copy the files necessary for the step and execute it
			List<File> requiredFiles = step.filesRequiredForExecution(remoteDir);

			for (File file : requiredFiles) {
				String relativeFromRemote = file.getAbsolutePath().substring(remoteDir.getAbsolutePath().length());
				File localFile = new File(localWorkDir, relativeFromRemote);
				FileUtils.copyFile(file, localFile);
			}
			step.execute(localWorkDir);
		}
		return remoteDir;
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
