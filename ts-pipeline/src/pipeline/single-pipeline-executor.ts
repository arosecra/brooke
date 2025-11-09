import * as fs from 'fs';
import * as path from 'path';
import { JobFolder } from "../model/job-folder";
import { Pipeline } from "../model/pipeline";
import { Args } from '../model/args';

export class SinglePipelineExecutor {
	executeTask(args: Args, pipeline: Pipeline, itemFolder: string) {
		let job = new JobFolder();
		job.remoteFolder = itemFolder;

		job.workFolder = path.join('D:/scans/pipeline_temp', path.basename(itemFolder));
		job.sourceFolder = path.join(job.workFolder, 'source');
		job.tempFolder = path.join(job.workFolder, 'temp');
		job.destFolder = path.join(job.workFolder, 'dest');

		[job.workFolder, job.sourceFolder, job.tempFolder, job.destFolder].forEach((file) => {
			fs.mkdirSync(file, { recursive: true })
		})

		
		copySourceFilesLocally(pipeline, itemFolder, job);
		executeSteps(pipeline, itemFolder, job);
		copyProducedFilesToRemote(pipeline, itemFolder, job);

		fs.rmdirSync(job.workFolder, { recursive: true })
	}
}

function copySourceFilesLocally(pipeline: Pipeline, itemFolder: string, job: JobFolder) {
	fs.readdirSync(itemFolder).forEach((file) => {
		const uses = pipeline.uses;
		for(let i = 0; i < uses.length; i++) {
			const use = uses[i];
			if(file.match(use)) {
				fs.copyFileSync(path.join(itemFolder, file), path.join(job.sourceFolder, file));
			}
		}
	});

}


function executeSteps(pipeline: Pipeline, itemFolder: string, job: JobFolder) {
	for(let i = 0; i < pipeline.steps.length; i++) {
		let jobStep = pipeline.steps[i];
			// 		JobSubStep jss = new JobSubStep(js.getClass().getSimpleName(), rf.folder, i, schedule.pipeline.steps.size());
			// jss.start();
			// jss.printStartLn();
		jobStep.execute(job);
			// 		jss.printStart();
			// jss.endAndPrint();
	}
}


function copyProducedFilesToRemote(pipeline: Pipeline, itemFolder: string, job: JobFolder) {
	fs.readdirSync(job.destFolder).forEach((file) => {
		if(file.match(pipeline.produces)) {
			fs.copyFileSync(path.join(job.destFolder, file), path.join(itemFolder, file));
		}
	});
}
