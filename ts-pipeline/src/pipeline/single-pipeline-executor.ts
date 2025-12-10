import * as fs from 'fs';
import * as path from 'path';
import { JobFolder } from "../model/job-folder";
import { Pipeline } from "../model/pipeline";
import { Args } from '../model/args';
import { JobStep } from '../model/job-step';
import { node } from '../util/node';

export class SinglePipelineExecutor {
	async executeTask(args: Args, pipeline: Pipeline, itemFolder: string) {
		const temp = process.env.scantemp ?? 'D:/scans'
		let job = new JobFolder();
		job.remoteFolder = itemFolder;

		job.workFolder = path.join(`${temp}/pipeline_temp`, path.basename(itemFolder));
		fs.mkdirSync(job.workFolder, { recursive: true });

		let steps = [
			new CopySourceFilesLocally(pipeline.uses),
			...pipeline.steps,
			new CopyProducedFilesToRemote(pipeline.produces)
		]

		await executeSteps(steps, job);

		if(args.delete) fs.rmdirSync(job.workFolder, { recursive: true })
	}
}


async function  executeSteps(steps: JobStep[], job: JobFolder) {
	let stepSourceFolders = ['remote', ...steps.map((step, index) => path.join(job.workFolder, `${index}_${step.name}`)), 'remote'];

	let lastStepIndex = 0;
	for(let i = steps.length - 1; i >= 0; i--) {
		let folder = stepSourceFolders[i];
		if(fs.existsSync(folder)) {
			lastStepIndex = i;
		}
	}

	let lastDest = stepSourceFolders[lastStepIndex];
	fs.mkdirSync(lastDest, { recursive: true })
	for(let i = lastStepIndex; i < steps.length; i++) {
		let jobStep = steps[i];
		job.sourceFolder = lastDest;
		job.destFolder = stepSourceFolders[i+1];
		fs.mkdirSync(job.destFolder, { recursive: true })
		
		await jobStep.execute(job);

		node.rmdir(job.sourceFolder);
		lastDest = job.destFolder;
	}
}

class CopySourceFilesLocally implements JobStep {
	name= 'CopySourceFilesLocally';
	constructor(private uses: string[]) {}

	execute(job: JobFolder): void {
		fs.readdirSync(job.remoteFolder).forEach((file) => {
			const uses = this.uses;
			for(let i = 0; i < uses.length; i++) {
				const use = uses[i];
				if(file.match(use)) {
					fs.copyFileSync(path.join(job.remoteFolder, file), path.join(job.destFolder, file));
				}
			}
		});
	}
}

class CopyProducedFilesToRemote implements JobStep {
	name= "CopyProducedFilesToRemote";
	constructor(private produces: string) {}

	execute(job: JobFolder): void {
		fs.readdirSync(job.sourceFolder).forEach((file) => {
			if(file.match(this.produces)) {
				fs.copyFileSync(path.join(job.sourceFolder, file), path.join(job.remoteFolder, file));
			}
		});
	}
	
}
