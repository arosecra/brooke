
import * as dotenv from "dotenv";
import * as fs from "fs";
import { BookRunOcrStep } from "./src/steps/book-run-ocr-step";
import { node } from "./src/util/node";
import { RootFolder } from "./src/model/root-folder";
import { Pipeline } from "./src/model/pipeline";
import { JobStep } from "./src/model/job-step";
import { JobFolder } from "./src/model/job-folder";
import { UnGzStep } from "./src/steps/ungz-step";
import { MasterSchedule } from "./src/model/master-schedule";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";
import path from "path";
dotenv.config();



class CbtSplitter implements JobStep {
  name: string = 'CbtSplitter';
  execute(job: JobFolder): void {
    const ocr = path.join(job.sourceFolder, '.ocr');
    const thumbs = path.join(job.sourceFolder, '.thumbnails');

    if(fs.existsSync(ocr)) {
      const filename = `${path.basename(job.workFolder)}.ocr.gz`
      node.execFileSync(
        'tar',
        [ '-czf',
          path.basename(job.workFolder) + '.ocr.gz',
          '--exclude=*.gz',
          '*'
        ], {
          cwd: ocr
        }
      )
      node.fsMove(node.pathJoin(ocr, filename), node.pathJoin(job.destFolder, filename));
      node.rmdir(ocr);
    }
    if(fs.existsSync(thumbs)) {
      const filename = `${path.basename(job.workFolder)}.tmb.gz`
      node.execFileSync(
        'tar',
        [ '-czf',
          path.basename(job.workFolder) + '.tmb.gz',
          '--exclude=*.gz',
          '*'
        ], {
          cwd: thumbs
        }
      )
      node.fsMove(node.pathJoin(thumbs, filename), node.pathJoin(job.destFolder, filename));
      node.rmdir(thumbs);
    }

    const filename = path.basename(job.workFolder) + '.cbt.gz';
    node.execFileSync(
      'tar',
      [ '-czf',
        path.basename(job.workFolder) + '.cbt.gz',
        '--exclude=*.gz',
        '*'
      ], {
        cwd: job.sourceFolder
      }
    )
    node.fsMove(node.pathJoin(job.sourceFolder, filename), node.pathJoin(job.destFolder, filename));
    // if .ocr folder exists, tar it as ${name}.ocr
    // if .thumbnails folder exists, tar it
    // tar the remaining
  }
}

class CopyProducedFilesToRemote implements JobStep {
  name= "CopyProducedFilesToRemote";
  constructor() {}

  execute(job: JobFolder): void {
    fs.readdirSync(job.sourceFolder).forEach((file) => {
      if(file.endsWith('.gz')) {
        fs.copyFileSync(path.join(job.sourceFolder, file), path.join(job.remoteFolder, file));
      }
    });
  }
  
}

(async () => {
    const lightNovels = new RootFolder(
      "Light Novels",
      "\\\\syn01\\syn01public\\Scans\\Light_Novels_Repository"
    );
    const fiction = new RootFolder(
      "Fiction",
      "\\\\syn01\\syn01public\\Scans\\Fiction_Repository"
    );
    const nonfiction = new RootFolder(
      "Non Fiction",
      "\\\\syn01\\syn01public\\Scans\\NonFiction_Repository"
    );

    const splitPipeline = new Pipeline() //
        .setName("Book CBT") //
        .setUses([".*.cbt.gz"]) //
        .setProduces(".*.tmb.gz") //
        .addStep(new UnGzStep())
        .addStep(new CbtSplitter()) //
        .addStep(new CopyProducedFilesToRemote())
        ;

    const ms = new MasterSchedule([
      splitPipeline,
    ]) //
      .schedule(splitPipeline.name, lightNovels) //

      //
      .schedule(splitPipeline.name, fiction) //


      // .schedule(bookOcrPipeline.name, fiction) //
      //
      .schedule(splitPipeline.name, nonfiction) //
    ;

    ms.determineTasks(undefined);
    ms.printSummary();

    for (let i = 0; i < ms.tasks.length; i++) {
      const task = ms.tasks[i];
      await new SinglePipelineExecutor().executeTask(
        { delete: true } as any,
        ms.pipelineByName(task.pipelineName),
        task.itemFolder
      );
      ms.printSummary();
    }

})();