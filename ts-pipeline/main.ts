import { RootFolder } from "./src/model/root-folder";
import * as fs from "fs";
import * as path from "path";
import cluster from "cluster";
import process from "process";
import os from "os";
import { Pipeline } from "./src/model/pipeline";
import { MasterSchedule } from "./src/model/master-schedule";
import { BookExtractPDFsStep } from "./src/steps/book-extract-pdfs-step";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";
import { BookResizeImageStep } from "./src/steps/book-resize-image-step";
import { BookDeskewImageStep } from "./src/steps/book-deskew-image-step";
import { BookCreateThumbnailsStep } from "./src/steps/book-create-thumbnails-step";
import { BookConvertPngToWebpStep } from "./src/steps/book-convert-png-to-webp-step";
import { BookTarToCbtStep } from "./src/steps/book-tar-to-cbt-step";
import { BookCreateCbtDetailsStep } from "./src/steps/book-create-cbt-details-step";
import { BookCreateCoverThumbnailStep } from "./src/steps/book-create-cover-thumbnail-step";
import { Task } from "./src/model/task";
import yargs from "yargs";
import { hideBin } from "yargs/helpers";

const argv = yargs(hideBin(process.argv))
  .option("tasks", {
    type: "number",
    description: "Maximum number of tasks",
    default: 0,
  })
  .option("threads", {
    type: "number",
    description: "Number of threads",
    default: 1,
  })
  .option("vc", {
    type: "boolean",
    description: "print commands",
    default: false,
  })
	.option('pipelines', {
		type: 'string',
		description: 'Pipeline'
	})
  .parseSync();

function getLeafFolders(folder: string): string[] {
  let dirs = fs
    .readdirSync(folder, {
      withFileTypes: true,
      recursive: true,
    })
    .filter((file) => file.isDirectory())
    .filter((file) => {
      //check if the directory has directories
      let p = path.join(file.parentPath, file.name);
      let hasChildDirs = fs
        .readdirSync(p, { withFileTypes: true })
        .some((childFile) => childFile.isDirectory());
      return !hasChildDirs;
    })
    .map((file) => path.join(file.parentPath, file.name));

  return dirs;
}

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

  const bookCbtDetailsPipeline = new Pipeline() //
    .setName("Book CBT Details") //
    .setUses([".*.pdf"])
    .setProduces("cbtDetails.yaml")
    // .addStep(new ReadOCRPropertiesStep()) //
    .addStep(new BookExtractPDFsStep())
    // .addStep(new DeleteExcludedPagesStep())
    .addStep(new BookCreateCbtDetailsStep());
  const bookCoverThumbnailPipeline = new Pipeline()
    .setName("Cover Thumbnail") //
    .setUses([".*cover[s].pdf"]) //
    .setProduces("thumbnail.png") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookCreateCoverThumbnailStep(250));
  const bookCbtPipeline = new Pipeline() //
    .setName("Book CBT") //
    .setUses([".*.pdf"]) //
    .setProduces(".*.cbt") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookResizeImageStep()) //
    .addStep(new BookDeskewImageStep()) //
    .addStep(new BookCreateThumbnailsStep()) //
    .addStep(new BookConvertPngToWebpStep()) //
    // //		.addStep(new RunOCRStep()) //
    .addStep(new BookTarToCbtStep()); //
  return (
    new MasterSchedule([bookCoverThumbnailPipeline, bookCbtPipeline]) //
      // .schedule(bookOcrPropPipeline, lightNovels) //
      // .schedule(bookCbtDetailsPipeline, lightNovels) //
      .schedule(bookCoverThumbnailPipeline.name, lightNovels) //
      .schedule(bookCbtPipeline.name, lightNovels) //
      //
      //			.schedule(bookOcrPropPipeline, nonfiction) //
      // .schedule(bookCbtDetailsPipeline, fiction) //
      .schedule(bookCoverThumbnailPipeline.name, fiction) //
      .schedule(bookCbtPipeline.name, fiction) //
      //
      //			.schedule(bookOcrPropPipeline, nonfiction) //
      // .schedule(bookCbtDetailsPipeline, nonfiction) //
      .schedule(bookCoverThumbnailPipeline.name, nonfiction) //
      .schedule(bookCbtPipeline.name, nonfiction) //
  );
}


let threads = os.cpus().length;
if (argv.threads > 0) {
  threads = argv.threads;
}

const ms = setupMasterSchedule();
if (threads === 1) {
  ms.determineTasks(argv.tasks, argv.pipelines);
  ms.printSummary();

  for (let i = 0; i < ms.tasks.length; i++) {
    const task = ms.tasks[i];
    new SinglePipelineExecutor().executeTask(
      ms.pipelineByName(task.pipelineName),
      task.itemFolder
    );
  }
} else {
  if (cluster.isPrimary) {
    ms.determineTasks(argv.tasks, argv.pipelines);
    ms.printSummary();

    for (let id = 0; id < threads; id++) {
      const firstTask = ms.firstUnassignedTask();
      if (firstTask) {
        firstTask.assigned = true;
        const worker = cluster.fork();
        worker.on("message", (msg: any) => {
          if (msg?.request) {
            const newTask = ms.firstUnassignedTask();
            if (newTask) {
              newTask.assigned = true;
              worker.send({ task: newTask });
            } else {
              worker.send({ shutdown: true });
            }
          }
        });
        worker.on("online", () => worker.send({ task: firstTask }));
      }
    }
  } else {
    process.on("message", (msg: any) => {
      if (msg.task) {
        const task: Task = msg.task as Task;
        console.log(
          String(process.pid).padStart(8, " "),
          "executing " + task.itemFolder
        );
        new SinglePipelineExecutor().executeTask(
          ms.pipelineByName(task.pipelineName),
          task.itemFolder
        );
        process.send!({ request: true });
      } else if (msg.shutdown) {
        process.exit();
      }
    });
  }
}
