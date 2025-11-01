import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import { node } from "../util/node";
import * as fs from 'fs';

export class BookCreateCoverThumbnailStep implements JobStep {
	
	constructor(public width: number, public name: string = 'thumbnail.webp') {}
	
	execute(job: JobFolder): void {
		node.execFileSync(
			'D:\\Software\\ImageMagick\\magick.exe',
			[ 'mogrify',
				'-path', job.destFolder,
				'-format', 'webp',
				'-thumbnail', this.width+'x>',
				'*-8-*.png'
			], {
				cwd: job.destFolder
			}
		);

		const thumbs = fs.readdirSync(job.destFolder);
		for(let i = 1; i < thumbs.length; i++) {
			fs.rmSync(thumbs[i]);
		}
		fs.renameSync(node.pathJoin(job.destFolder, thumbs[0]), node.pathJoin(job.destFolder, 'thumbnail.webp'));

	}
	
}