import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { node } from "../util/node";

export class BookConvertPngToWebpStep implements JobStep {
	name = "BookConvertPngToWebpStep";
	execute(job: JobFolder): void {
		fs.readdirSync(job.sourceFolder).filter((file) => file.endsWith('.png')).forEach((file) => {
			node.execFileSync(
				'C:\\Software\\libwebp\\bin\\cwebp',
				[ '-lossless',
					node.pathJoin(job.sourceFolder, file),
					'-o',
					node.pathJoin(job.destFolder, file).replaceAll('.png', '.webp'),
				]
			);
			fs.rmSync(node.pathJoin(job.sourceFolder, file))
		});
	}
	
}