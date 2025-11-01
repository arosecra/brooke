import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as path from 'path';
import { node } from "../util/node";

export class BookTarToCbtStep implements JobStep {
	execute(job: JobFolder): void {
		node.execFileSync(
			'D:\\software\\7za\\7za.exe',
			[ 'a',
				'-ttar',
				'-o' + job.destFolder,
				node.pathJoin(job.destFolder, path.basename(job.workFolder) + '.cbt'),
				node.pathJoin(job.destFolder, '*')
			]
		)

	}
	
}