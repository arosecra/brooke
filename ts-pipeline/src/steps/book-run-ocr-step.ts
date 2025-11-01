import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import * as path from 'path';
import { node } from "../util/node";

export class BookRunOcrStep implements JobStep {
	execute(pipeline: Pipeline, job: JobFolder): void {
		fs.readdirSync(job.destFolder).filter((file) => file.includes('-1-')).forEach((file) => {
			let p = node.pathJoin(job.tempFolder, path.basename(file).replace('.png', ''))
			console.log(p, fs.existsSync(p))
			if(!fs.existsSync(p)) {
				let out = '';
				try {
					out = node.execFileSync(
					"C:\\Software\\Python\\Python313\\Scripts\\uv.exe",
					[ '-q', 
						'--directory', "C:\\Software\\mineru",
						'run', 'mineru',
						'-o', job.tempFolder, //
						'-l', 'en', //
						'-d', 'cpu', //
						'-b', 'pipeline', //
						'-m', 'auto', //
						'--source', 'local', //
						'-p', node.pathJoin(job.destFolder, file),
					], {
						shell: true
					}
					);
					console.log(String(out));
				} catch (err) {
					console.error(String(out));


			//once done, base64 intern figures
			//     will have to re-develop
			//     will need new lib or rebuild myself

			//or will have to use json files to rebuild the
			//   markdown - that's an option
	}
	
}