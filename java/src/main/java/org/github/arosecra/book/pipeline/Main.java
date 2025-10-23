package org.github.arosecra.book.pipeline;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.github.arosecra.book.pipeline.book.steps.ConvertBookImageToWebpStep;
import org.github.arosecra.book.pipeline.book.steps.CreateCBTDetails;
import org.github.arosecra.book.pipeline.book.steps.CreateCoverThumbnailStep;
import org.github.arosecra.book.pipeline.book.steps.CreateOCRPropertiesStep;
import org.github.arosecra.book.pipeline.book.steps.CreateThumbnailsStep;
import org.github.arosecra.book.pipeline.book.steps.DeleteExcludedPagesStep;
import org.github.arosecra.book.pipeline.book.steps.ExtractBookPDFsStep;
import org.github.arosecra.book.pipeline.book.steps.HandleBlankImagesStep;
import org.github.arosecra.book.pipeline.book.steps.HandleImageFilesStep;
import org.github.arosecra.book.pipeline.book.steps.ModifyBookImageStep;
import org.github.arosecra.book.pipeline.book.steps.ReadOCRPropertiesStep;
import org.github.arosecra.book.pipeline.book.steps.ResizeBookImageStep;
import org.github.arosecra.book.pipeline.book.steps.RunOCRStep;
import org.github.arosecra.book.pipeline.book.steps.TarToCbtStep;
import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.JobSubStep;
import org.github.arosecra.book.pipeline.model.MasterSchedule;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.model.RemoteFolder;
import org.github.arosecra.book.pipeline.model.RootFolder;
import org.github.arosecra.book.pipeline.model.Schedule;
import org.github.arosecra.book.pipeline.util.Try;

public class Main {
	
	public static final String WORK_DIRECTORY = "D:\\scans\\pipeline_temp";
	
	public static void main(String[] args) throws IOException {
		MasterSchedule ms = setupMasterSchedule();		

		summarize(ms);
		printSummary(ms);
		executeMasterSchedule(ms);
	}

	private static void executeMasterSchedule(MasterSchedule ms) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		
		for(Schedule schedule : ms.schedules) {
			System.out.println("Processing " + schedule.workRequired.size() + " folders through pipeline " + schedule.pipeline.name);
			for(int i = 0; i < schedule.workRequired.size(); i++) {
				RemoteFolder rf = schedule.workRequired.get(i);
				JobSubStep jss = new JobSubStep(schedule.pipeline.name, rf.folder, i, schedule.workRequired.size());
				
				//for debugging
//				jss.start();
//				jss.printStartLn();
//                SinglePipelineExecutor single = new SinglePipelineExecutor();
//                single.executePipelineForFolder(schedule, rf);
//				jss.printStart();
//				jss.endAndPrint();
				
				
	            executor.submit(() -> {
					
					jss.start();
					jss.printStartLn();
	                try {
		                SinglePipelineExecutor single = new SinglePipelineExecutor();
		                single.executePipelineForFolder(schedule, rf);
						jss.printStart();
						jss.endAndPrint();
	                } catch (Exception e) {
	                    Thread.currentThread().interrupt();
	                }
	            });
	            
	            
	            System.gc();
				
				
//				executePipelineForFolder(schedule, schedule.workRequired.get(i));
			}
		}
		
		
		executor.shutdown();
	}

//	private static void executePipelineForFolder(Schedule schedule, RemoteFolder rf) throws IOException {
//		JobFolder jf = createJobFolder(rf);
//		copySourceFilesLocally(schedule, rf, jf);
//		executeSteps(schedule, rf, jf);
//		copyProducedFilesToRemote(schedule, rf, jf);
//		rf.contents = rf.folder.listFiles();
//		Try.deleteFolder(jf.workFolder.toPath());
//	}
//
//	private static void executeSteps(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
//		for(int i = 0; i < schedule.pipeline.steps.size(); i++) {
//			JobStep js = schedule.pipeline.steps.get(i);
//			JobSubStep jss = new JobSubStep(js.getClass().getSimpleName(), rf.folder, i, schedule.pipeline.steps.size());
//			jss.start();
//			jss.printStartLn();
//			js.execute(schedule.pipeline, jf);
//			jss.printStart();
//			jss.endAndPrint();
//		}
//	}
//
//	private static void copyProducedFilesToRemote(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
//		for(File file : jf.destFolder.listFiles()) {
//			if(file.getName().matches(schedule.pipeline.produces)) {
//				Files.copy(file.toPath(), Path.of(rf.folder.getAbsolutePath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
//			}
//		}
//	}
//
//	private static void copySourceFilesLocally(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
//		for(File file : rf.contents) {
//			if(file.getName().matches(schedule.pipeline.uses)) {
//				Files.copy(file.toPath(), Path.of(jf.sourceFolder.getAbsolutePath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
//			}
//		}
//	}
//
//	private static JobFolder createJobFolder(RemoteFolder rf) {
//		JobFolder jf = new JobFolder();
//		jf.remoteFolder = rf;
//		
//		jf.workFolder = createFileAndMkdirs(new File(WORK_DIRECTORY), rf.folder.getName());
//		jf.sourceFolder = createFileAndMkdirs(jf.workFolder, "source");
//		jf.destFolder = createFileAndMkdirs(jf.workFolder, "dest");
//		jf.tempFolder = createFileAndMkdirs(jf.workFolder, "temp");
//		return jf;
//	}

	private static void summarize(MasterSchedule ms) {
		for(Schedule schedule : ms.schedules) {
			for(RemoteFolder rf : schedule.rootFolder.remoteFolders()) {
				boolean producesExists = doesRemoteProducesExist(schedule.pipeline, rf);
				boolean usesExists = doesRemoteUsesExists(schedule.pipeline, rf);
				if(!producesExists && usesExists) {
					schedule.workRequired.add(rf);
				}
			}
		}
	}
	
//	private static File createFileAndMkdirs(File base, String name) {
//		File ret = new File(base, name);
//		ret.mkdirs();
//		return ret;
//	}

	private static void printSummary(MasterSchedule ms) {
		System.out.println(String.format("%24s %48s %8s", //
				"-------------", //
				"--------", //
				"------"));
		System.out.println(String.format("%24s %48s %8s", //
				"Remote Folder", //
				"Pipeline", //
				"# ToDo"));
		System.out.println(String.format("%24s %48s %8s", //
				"-------------", //
				"--------", //
				"------"));
		int total = 0;
		for(Schedule schedule : ms.schedules) {
			System.out.println(String.format("%24s %48s %8d", //
					schedule.rootFolder.rootFolder(), //
					schedule.pipeline.name, //
					schedule.workRequired.size()));
			total += schedule.workRequired.size();
		}
		System.out.println(String.format("%24s %48s %8s", //
				"-------------", //
				"--------", //
				"------"));
		System.out.println(String.format("%24s %48s %8d", //
				"Total", //
				"--------", //
				total));
	}
	
	private static boolean doesRemoteUsesExists(Pipeline pipeline, RemoteFolder rf) {
		boolean ret = false;
		for(File file : rf.contents) {
			if(file.getName().matches(pipeline.uses)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	private static boolean doesRemoteProducesExist(Pipeline pipeline, RemoteFolder rf) {
		boolean ret = false;
		for(File file : rf.contents) {
			if(file.getName().matches(pipeline.produces)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public static void printHelp() {
		
	}

	private static MasterSchedule setupMasterSchedule() {
		RootFolder lightNovels = new RootFolder("Light Novels", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Light_Novels_Repository"));
		RootFolder fiction = new RootFolder("Fiction", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Fiction_Repository"));
		RootFolder nonfiction = new RootFolder("Non Fiction", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\NonFiction_Repository"));
//		RootFolder graphicNovels = new RootFolder("Graphic Novels", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Graphic_Novel_Repository"));
//		RootFolder magazines = new RootFolder("Magazines", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Magazine_Repository"));
//		RootFolder researchPapers = new RootFolder("Research Papers", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));
		

//		RootFolder anime = new RootFolder("Research Papers", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));
//		RootFolder movies = new RootFolder("Research Papers", RemoteFolder.getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));
		
		Pipeline bookOcrPropPipeline = new Pipeline() //
			.setName("Book OCR Properties Pipeline") //
			.setUses(".*.pdf")
			.setProduces("ocr.properties")
			.addStep(new ExtractBookPDFsStep())
			.addStep(new CreateOCRPropertiesStep())
		;
		
		Pipeline bookCbtDetailsPipeline = new Pipeline() //
			.setName("Book CBT Details") //
			.setUses(".*.pdf")
			.setProduces("cbtDetails.yaml")
			.addStep(new ReadOCRPropertiesStep()) //
			.addStep(new ExtractBookPDFsStep())
			.addStep(new DeleteExcludedPagesStep())
			.addStep(new CreateCBTDetails())
		;
		
		Pipeline bookCoverThumbnailPipeline = new Pipeline()
			.setName("Cover Thumbnail") //
			.setUses(".*cover[s].pdf") //
			.setProduces("thumbnail.png") //
			.addStep(new ExtractBookPDFsStep()) //
			.addStep(new CreateCoverThumbnailStep())
		;
		
		Pipeline bookCbtPipeline = new Pipeline() //
			.setName("Book CBT") //
			.setUses(".*.pdf") //
			.setProduces(".*.cbt") //
//			.addStep(new ReadOCRPropertiesStep()) //
			.addStep(new ExtractBookPDFsStep()) //
			.addStep(new DeleteExcludedPagesStep())
			.addStep(new ResizeBookImageStep()) //
			.addStep(new HandleBlankImagesStep()) //
			.addStep(new HandleImageFilesStep()) //
			.addStep(new ModifyBookImageStep()) //
			.addStep(new CreateThumbnailsStep()) //
			.addStep(new ConvertBookImageToWebpStep()) //
//			.addStep(new RunOCRStep()) //
			.addStep(new TarToCbtStep()) //
		;
		
		MasterSchedule ms = new MasterSchedule() //
				.schedule(bookOcrPropPipeline, lightNovels) //
				.schedule(bookCbtDetailsPipeline, lightNovels) //
				.schedule(bookCoverThumbnailPipeline, lightNovels) //
				.schedule(bookCbtPipeline, lightNovels) //
				//
//				.schedule(bookOcrPropPipeline, nonfiction) //
				.schedule(bookCbtDetailsPipeline, fiction) //
				.schedule(bookCoverThumbnailPipeline, fiction) //
				.schedule(bookCbtPipeline, fiction) //
				//
//				.schedule(bookOcrPropPipeline, nonfiction) //
				.schedule(bookCbtDetailsPipeline, nonfiction) //
				.schedule(bookCoverThumbnailPipeline, nonfiction) //
				.schedule(bookCbtPipeline, nonfiction) //
		;
		return ms;
	}

}


class SinglePipelineExecutor extends Thread {

	public void executePipelineForFolder(Schedule schedule, RemoteFolder rf) throws IOException {
		JobFolder jf = createJobFolder(rf);
		copySourceFilesLocally(schedule, rf, jf);
		executeSteps(schedule, rf, jf);
		copyProducedFilesToRemote(schedule, rf, jf);
		rf.contents = rf.folder.listFiles();
		Try.deleteFolder(jf.workFolder.toPath());
	}

	private void executeSteps(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
		for(int i = 0; i < schedule.pipeline.steps.size(); i++) {
			JobStep js = schedule.pipeline.steps.get(i);
			JobSubStep jss = new JobSubStep(js.getClass().getSimpleName(), rf.folder, i, schedule.pipeline.steps.size());
			jss.start();
			jss.printStartLn();
			js.execute(schedule.pipeline, jf);
			jss.printStart();
			jss.endAndPrint();
		}
	}

	private void copyProducedFilesToRemote(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
		for(File file : jf.destFolder.listFiles()) {
			if(file.getName().matches(schedule.pipeline.produces)) {
				Files.copy(file.toPath(), Path.of(rf.folder.getAbsolutePath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	private void copySourceFilesLocally(Schedule schedule, RemoteFolder rf, JobFolder jf) throws IOException {
		for(File file : rf.contents) {
			if(file.getName().matches(schedule.pipeline.uses)) {
				Files.copy(file.toPath(), Path.of(jf.sourceFolder.getAbsolutePath(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	private JobFolder createJobFolder(RemoteFolder rf) {
		JobFolder jf = new JobFolder();
		jf.remoteFolder = rf;
		
		jf.workFolder = createFileAndMkdirs(new File(Main.WORK_DIRECTORY), rf.folder.getName());
		jf.sourceFolder = createFileAndMkdirs(jf.workFolder, "source");
		jf.destFolder = createFileAndMkdirs(jf.workFolder, "dest");
		jf.tempFolder = createFileAndMkdirs(jf.workFolder, "temp");
		return jf;
	}
	
	private File createFileAndMkdirs(File base, String name) {
		File ret = new File(base, name);
		ret.mkdirs();
		return ret;
	}
}