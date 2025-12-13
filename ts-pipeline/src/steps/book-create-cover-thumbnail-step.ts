import * as fs from 'fs';
import * as path from 'path';
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { node } from "../util/node";

export class BookCreateCoverThumbnailStep implements JobStep {
	name = 'BookCreateCoverThumbnailStep';
	constructor(public width: number, public height: number) {}
	
	execute(job: JobFolder): void {
		const imagemagick = process.env.imagemagick ?? 'D:\\Software\\ImageMagick\\'

		const thumbs = fs.readdirSync(job.sourceFolder);
		const sourceImg = thumbs[0];

		node.execFileSync(
			`${imagemagick}\\magick.exe`, //
			[ path.join(job.sourceFolder, sourceImg),
				'-thumbnail', this.width+'x' + this.height + '>',
				'-background', 'none',
				'-extent', `${this.width}x${this.height}`,
				'-gravity', 'northwest',
				'-format', 'webp',
				path.join(job.destFolder, 'thumbnail.webp')
			], {
				cwd: job.destFolder
			}
		);


	}
	
}