import cluster from 'cluster';
import * as fs from 'fs';
import * as path from 'path';
import process from 'process';
import { MasterSchedule } from "./src/model/master-schedule";
import { Pipeline } from "./src/model/pipeline";
import { RootFolder } from "./src/model/root-folder";
import { Task } from "./src/model/task";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";
import { BookCreateCoverThumbnailStep } from "./src/steps/book-create-cover-thumbnail-step";
import { BookExtractPDFsStep } from "./src/steps/book-extract-pdfs-step";


function setupMasterSchedule() {

  const lightNovels = new RootFolder(
		"Light Novels", "\\\\syn01\\syn01public\\Scans\\Light_Novels_Repository"
	);
  const fiction = new RootFolder(
    "Fiction", "\\\\syn01\\syn01public\\Scans\\Fiction_Repository"
  );
  const nonfiction = new RootFolder(
    "Non Fiction", "\\\\syn01\\syn01public\\Scans\\NonFiction_Repository"
  );
//	const graphicNovels = new RootFolder("Graphic Novels", getLeafFolders("\\\\syn01\\syn01public\\Scans\\Graphic_Novel_Repository"));
//	const magazines = new RootFolder("Magazines", getLeafFolders("\\\\syn01\\syn01public\\Scans\\Magazine_Repository"));
//	const researchPapers = new RootFolder("Research Papers", getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));

//	const anime = new RootFolder("Research Papers", getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));
//	const movies = new RootFolder("Research Papers", getLeafFolders("\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository"));

	// const bookOcrPropPipeline = new Pipeline() //
	// 	.setName("Book OCR Properties Pipeline") //
	// 	.setUses(".*.pdf")
	// 	.setProduces("ocr.properties")
	// 	.addStep(new ExtractBookPDFsStep())
	// 	.addStep(new CreateOCRPropertiesStep())
	// ;
	
// 	const bookCbtDetailsPipeline = new Pipeline() //
// 		.setName("Book CBT Details") //
// 		.setUses(".*.pdf")
// 		.setProduces("cbtDetails.yaml")
// 		// .addStep(new ReadOCRPropertiesStep()) //
// 		.addStep(new BookExtractPDFsStep())
// 		// .addStep(new DeleteExcludedPagesStep())
// 		.addStep(new BookCreateCbtDetailsStep())
// 	;
	
	const bookCoverThumbnailPipeline = new Pipeline()
		.setName("Cover Thumbnail") //
		.setUses([".*cover[s].pdf"]) //
		.setProduces("thumbnail.webp") //
		.addStep(new BookExtractPDFsStep()) //
		.addStep(new BookCreateCoverThumbnailStep(250, 400))
	;
	
// 	const bookCbtPipeline = new Pipeline() //
// 		.setName("Book CBT") //
// 		.setUses(".*.pdf") //
// 		.setProduces(".*.cbt") //
// 		.addStep(new BookExtractPDFsStep()) //
// 		.addStep(new BookResizeImageStep()) //
// 		.addStep(new BookDeskewImageStep()) //
// 		.addStep(new BookCreateThumbnailsStep()) //
// 		.addStep(new BookConvertPngToWebpStep()) //
// // //		.addStep(new RunOCRStep()) //
// 		.addStep(new BookTarToCbtStep()) //
// 	;

	// const bookOcrPipeline = new Pipeline() //
	// 	.setName("Book OCR") //
	// 	.setUses([".*.cbt.gz", ".*.yaml"]) //
	// 	.setProduces(".*.cbt.gz") //
	// 	.setPropertyCheck((file: string) => {
	// 		const list = node.execFileSync('tar', ['-ztf', file]);
	// 		return list.split('\n').some((line) => line.endsWith('.md'))
	// 	}) //
	// 	.addStep(new OcrUntarCbtGzStep())
	// 	.addStep(new BookRunOcrStep())
	// 	.addStep(new BookTarToCbtGzStep())
	// 	// .addStep(new BookTarToCb7Step())
	// 	;

	
	return new MasterSchedule([
		bookCoverThumbnailPipeline	
	]) //
			// .schedule(bookOcrPropPipeline, lightNovels) //
			// .schedule(bookCbtDetailsPipeline, lightNovels) //
			.schedule(bookCoverThumbnailPipeline.name, lightNovels) //
			//
//			.schedule(bookOcrPropPipeline, nonfiction) //
			// .schedule(bookCbtDetailsPipeline, fiction) //
			.schedule(bookCoverThumbnailPipeline.name, fiction) //
			//
//			.schedule(bookOcrPropPipeline, nonfiction) //
			// .schedule(bookCbtDetailsPipeline, nonfiction) //
			.schedule(bookCoverThumbnailPipeline.name, nonfiction) //
	;

}


const threads = 1; //os.cpus().length;
const maxTasks = 1;

const ms = setupMasterSchedule();

if(threads === 1) {
	ms.determineTasks(maxTasks, undefined);
	ms.printSummary();
	for(let i = 0; i < ms.tasks.length; i++) {
		const task = ms.tasks[i];
		new SinglePipelineExecutor()
				.executeTask({} as any, ms.pipelineByName(task.pipelineName), task.itemFolder);
	}
} else {
	if(cluster.isPrimary) {
		ms.determineTasks(maxTasks, undefined);
		ms.printSummary();

		for(let id = 0; id < threads; id++) {
			const firstTask = ms.firstUnassignedTask();
			if(firstTask) {
				firstTask.assigned = true;
				const worker = cluster.fork();
				worker.on('message', (msg: any) => {
					if(msg?.request) {
						const newTask = ms.firstUnassignedTask();
						if(newTask) {
							newTask.assigned = true;
							worker.send({ task: newTask });
						} else {
							worker.send({ shutdown: true });
						}
					}
				});
				worker.on('online', () => worker.send({ task: firstTask }));
			}
		}

	} else {
		process.on('message', (msg: any) => {
			if(msg.task) {
				const task: Task = msg.task as Task;
				console.log(String(process.pid).padStart(8, ' '), 'executing ' + task.itemFolder);
				new SinglePipelineExecutor()
					.executeTask({} as any, ms.pipelineByName(task.pipelineName), task.itemFolder);
				process.send!({ request: true });
			} else if (msg.shutdown) {
				process.exit();
			}
		})
	}
}


