import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import { node } from "../util/node";
import * as fs from 'fs';


export class BookCreateThumbnailsStep implements JobStep {
	name="BookCreateThumbnailsStep";
	execute(job: JobFolder): void {
		const imagemagick = process.env.imagemagick ?? 'D:\\Software\\ImageMagick\\'

		const hasColor = fs.readdirSync(job.sourceFolder).some((file) => file.includes('-8-'));
		const hasBw = fs.readdirSync(job.sourceFolder).some((file) => file.includes('-1-'));
		if(hasColor) {
			node.execFileSync(
				`${imagemagick}\\magick.exe`, //
				[ 'mogrify',
					'-path', job.destFolder,
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
				`${imagemagick}\\magick.exe`, //
				[ 'mogrify',
					'-path', job.destFolder,
					'-format', 'png',
					'-depth', '1',
					'-adaptive-resize', '250x>',
					'*-1-*.png'
				], {
					cwd: job.sourceFolder
				}
			);
		}
	}
	
}