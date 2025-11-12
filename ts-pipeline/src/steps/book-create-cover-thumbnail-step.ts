import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import { node } from "../util/node";
import * as fs from 'fs';
import * as path from 'path';

export class BookCreateCoverThumbnailStep implements JobStep {
	
	constructor(public width: number, public height: number, public name: string = 'thumbnail.webp') {}
	
	execute(job: JobFolder): void {

		const thumbs = fs.readdirSync(job.tempFolder);
		const sourceImg = thumbs[0];

		node.execFileSync(
			'D:\\Software\\ImageMagick\\magick.exe',
			[ path.join(job.tempFolder, sourceImg),
				'-thumbnail', this.width+'x' + this.height + '>',
				'-background', 'none',
				'-extent', '250x400',
				'-gravity', 'northwest',
				'-format', 'webp',
				path.join(job.destFolder, this.name)
			], {
				cwd: job.destFolder
			}
		);


	}
	
}