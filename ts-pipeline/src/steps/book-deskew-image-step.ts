import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { node } from "../util/node";

export class BookDeskewImageStep implements JobStep {
	execute(job: JobFolder): void {
		const files = fs.readdirSync(job.tempFolder);
		files.forEach((file) => {
			if(file.includes('-8-')) {
				node.fsMove(node.pathJoin(job.tempFolder, file), node.pathJoin(job.destFolder, file));
			} else {
				let skewDegrees = node.execFileSync(
					"D:\\Software\\ImageMagick\\magick.exe",
					[ node.pathJoin(job.tempFolder, file), //
						'-deskew', '40%', //
						'-format', '%[deskew:angle]', //
						'info:'
					]
				);

				skewDegrees = String(skewDegrees);
				
				if(skewDegrees !== '0' && skewDegrees !== '-0') {
					const skewOut = node.execFileSync(
						"D:\\Software\\ImageMagick\\magick.exe",
						[ 'convert', node.pathJoin(job.tempFolder, file),
							'-virtual-pixel', 'white', //
							'-distort', 'SRT', skewDegrees, //
							'-depth', '1', //
							'-monochrome', //
							node.pathJoin(job.destFolder, file)
						]
					);
					fs.rmSync(node.pathJoin(job.tempFolder, file));
					console.log(String(skewOut));
				} else {
					node.fsMove(node.pathJoin(job.tempFolder, file), node.pathJoin(job.destFolder, file));
				}
			}
		})


	}
	
}