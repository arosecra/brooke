import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import { node } from "../util/node";


export class BookCreateThumbnailsStep implements JobStep {
	execute(job: JobFolder): void {
		const thumbnailFolder = node.pathJoin(job.destFolder, '.thumbnails');
		node.mkdirs(thumbnailFolder);

		node.execFileSync(
			'D:\\Software\\ImageMagick\\magick.exe',
			[ 'mogrify',
				'-path', thumbnailFolder,
				'-format', 'webp',
				'-thumbnail', '250x>',
				'*-8-*.png'
			], {
				cwd: job.destFolder
			}
		);

		node.execFileSync(
			'D:\\Software\\ImageMagick\\magick.exe',
			[ 'mogrify',
				'-path', thumbnailFolder,
				'-format', 'png',
				'-depth', '1',
				'-adaptive-resize', '250x>',
				'*-1-*.png'
			], {
				cwd: job.destFolder
			}
		);
	}
	
}