import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as path from 'path';
import { node } from "../util/node";

export class BookTarToCb7Step implements JobStep {
	execute(job: JobFolder): void {
		node.execFileSync(
			'D:\\software\\7za\\7za.exe',
			[ 'a',
				'-t7z',
				'-o' + job.destFolder,
				node.pathJoin(job.destFolder, path.basename(job.workFolder) + '.cb7'),
				node.pathJoin(job.destFolder, '*')
			]
		)

	}
	
}