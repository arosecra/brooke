import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { node } from "../util/node";

export class BookRunOcrStep implements JobStep {
	execute(job: JobFolder): void {
		fs.readdirSync(job.destFolder).filter((file) => file.endsWith('.png')).forEach((file) => {
			node.execFileSync(
				"D:\Software\Python\Python313\Scripts\uv.exe",
				[ 'run', 'mineru',
					'-p', node.pathJoin(job.destFolder, file),
					'-o', job.tempFolder,
					'-l', 'en',
					'--source', 'local'
				], {
				 cwd: "D:\\Projects\\mineru"
				}
			)

			//once done, base64 intern figures
			//     will have to re-develop
			//     will need new lib or rebuild myself

			//or will have to use json files to rebuild the
			//   markdown - that's an option
		});
	}
	
}