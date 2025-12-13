import * as path from 'path';
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { node } from "../util/node";

export class BookTarToGzStep implements JobStep {
	name="BookTarToCbtGzStep";
	constructor(private extension: string) {}

	execute(job: JobFolder): void {
		const filename = path.basename(job.workFolder) + `.${this.extension}.gz`;
		node.rm(node.pathJoin(job.sourceFolder, filename));
		node.rm(node.pathJoin(job.destFolder, filename));
		node.execFileSync(
			'tar',
			[ '-czf',
				path.basename(job.workFolder) + `.${this.extension}.gz`,
				'--exclude=*.gz',
				'*'
			], {
				cwd: job.sourceFolder
			}
		)
		node.fsMove(node.pathJoin(job.sourceFolder, filename), node.pathJoin(job.destFolder, filename));
	}
	
}