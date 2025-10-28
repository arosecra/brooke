import { pipeline } from "stream";
import { JobFolder } from "../model/job-folder";
import { Schedule } from "../model/schedule";
import * as fs from 'fs';
import * as path from 'path';

export class SinglePipelineExecutor {
	executePiplineForFolder(schedule: Schedule, itemFolder: string) {
		let job = new JobFolder();
		job.remoteFolder = itemFolder;

		job.workFolder = path.join('D:/scans/pipeline_temp', path.basename(itemFolder));
		job.sourceFolder = path.join(job.workFolder, 'source');
		job.tempFolder = path.join(job.workFolder, 'temp');
		job.destFolder = path.join(job.workFolder, 'dest');

		[job.workFolder, job.sourceFolder, job.tempFolder, job.destFolder].forEach((file) => {
			fs.mkdirSync(file, { recursive: true })
		})

		
		copySourceFilesLocally(schedule, itemFolder, job);
		executeSteps(schedule, itemFolder, job);
		copyProducedFilesToRemote(schedule, itemFolder, job);

		fs.rmdirSync(job.workFolder, { recursive: true })
	}
}

function copySourceFilesLocally(schedule: Schedule, itemFolder: string, job: JobFolder) {
	fs.readdirSync(itemFolder).forEach((file) => {
		if(file.match(schedule.pipeline.uses)) {
			fs.copyFileSync(path.join(itemFolder, file), path.join(job.sourceFolder, file));
		}
	});

}


function executeSteps(schedule: Schedule, itemFolder: string, job: JobFolder) {
	for(let i = 0; i < schedule.pipeline.steps.length; i++) {
		let jobStep = schedule.pipeline.steps[i];
			// 		JobSubStep jss = new JobSubStep(js.getClass().getSimpleName(), rf.folder, i, schedule.pipeline.steps.size());
			// jss.start();
			// jss.printStartLn();
		jobStep.execute(schedule.pipeline, job);
			// 		jss.printStart();
			// jss.endAndPrint();
	}
}


function copyProducedFilesToRemote(schedule: Schedule, itemFolder: string, job: JobFolder) {
	fs.readdirSync(job.destFolder).forEach((file) => {
		if(file.match(schedule.pipeline.produces)) {
			fs.copyFileSync(path.join(job.destFolder, file), path.join(itemFolder, file));
		}
	});
}
