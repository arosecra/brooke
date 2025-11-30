import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import { node } from "../util/node";
import * as fs from 'fs';


export class BookCreateThumbnailsStep implements JobStep {
	name="BookCreateThumbnailsStep";
	execute(job: JobFolder): void {
		const thumbnailFolder = node.pathJoin(job.destFolder, '.thumbnails');
		node.mkdirs(thumbnailFolder);

		const hasColor = fs.readdirSync(job.sourceFolder).some((file) => file.includes('-8-'));
		const hasBw = fs.readdirSync(job.sourceFolder).some((file) => file.includes('-1-'));
		if(hasColor) {
			node.execFileSync(
				'D:\\Software\\ImageMagick\\magick.exe',
				[ 'mogrify',
					'-path', thumbnailFolder,
					'-format', 'webp',
					'-thumbnail', '250x>',
					'*-8-*.png'
				], {
					cwd: job.sourceFolder
				}
			);
		}

		if(hasBw) {
			node.execFileSync(
				'D:\\Software\\ImageMagick\\magick.exe',
				[ 'mogrify',
					'-path', thumbnailFolder,
					'-format', 'png',
					'-depth', '1',
					'-adaptive-resize', '250x>',
					'*-1-*.png'
				], {
					cwd: job.sourceFolder
				}
			);
		}

		fs.readdirSync(job.sourceFolder).forEach((file) => {
			node.fsMove(node.pathJoin(job.sourceFolder, file), node.pathJoin(job.destFolder, file));
		})
	}
	
}