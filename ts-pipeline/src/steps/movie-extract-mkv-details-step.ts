import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { node } from "../util/node";

export class MovieExtractMkvDetailsStep implements JobStep {
	name = 'MovieExtractMkvDetailsStep';
	execute(job: JobFolder): void {
		fs.readdirSync(job.sourceFolder).filter((file) => file.endsWith('mkv')).forEach((file) => {

			const json = node.execFileSync(
				'D:\\Software\\MKVToolNix\\mkvmerge.exe',
				[ '-J', //
					node.pathJoin(job.sourceFolder, file)
				]
			);

			fs.writeFileSync(node.pathJoin(job.destFolder, 'mkvDetails.json'), json);
		});
	}
	
}