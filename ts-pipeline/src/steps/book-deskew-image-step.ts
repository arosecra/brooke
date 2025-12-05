import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { node } from "../util/node";

export class BookDeskewImageStep implements JobStep {
	name = "BookDeskewImageStep";
	execute(job: JobFolder): void {
		const imagemagick = process.env.imagemagick ?? 'D:\\Software\\ImageMagick\\'
		const files = fs.readdirSync(job.sourceFolder);
		files.forEach((file) => {
			if(file.includes('-8-')) {
				node.fsMove(node.pathJoin(job.sourceFolder, file), node.pathJoin(job.destFolder, file));
			} else {
				let skewDegrees = node.execFileSync(
					`${imagemagick}\\magick.exe`, //
					[ node.pathJoin(job.sourceFolder, file), //
						'-deskew', '40%', //
						'-format', '%[deskew:angle]', //
						'info:'
					]
				);

				skewDegrees = String(skewDegrees);
				
				if(skewDegrees !== '0' && skewDegrees !== '-0') {
					const skewOut = node.execFileSync(
					`${imagemagick}\\magick.exe`, //
						[ 'convert', node.pathJoin(job.sourceFolder, file),
							'-virtual-pixel', 'white', //
							'-distort', 'SRT', skewDegrees, //
							'-depth', '1', //
							'-monochrome', //
							node.pathJoin(job.destFolder, file)
						]
					);
					fs.rmSync(node.pathJoin(job.sourceFolder, file));
					console.log(String(skewOut));
				} else {
					node.fsMove(node.pathJoin(job.sourceFolder, file), node.pathJoin(job.destFolder, file));
				}
			}
		})


	}
	
}