import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as path from 'path';
import { node } from "../util/node";
import { cwd } from "process";

export class BookTarToCbtGzStep implements JobStep {
	execute(job: JobFolder): void {
		node.execFileSync(
			'tar',
			[ '-czf',
				path.basename(job.workFolder) + '.cbt.gz',
				'--exclude=*.gz',
				'*'
			], {
				cwd: job.destFolder
			}
		)

	}
	
}