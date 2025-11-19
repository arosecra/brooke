import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";

import * as child_process from "child_process";
import { node } from "../util/node";

export class BookResizeImageStep implements JobStep {
	name = "BookResizeImageStep";
  execute(job: JobFolder): void {
    node.execFileSync( //
			"D:\\Software\\ImageMagick\\magick.exe", //
			[
				"mogrify", //
				"-path", job.destFolder, //
				"-format", "png", //
				"-resize", "1920x>", //
				"*-8-*.png"
			], {
				cwd: job.sourceFolder
			}
		);
		
    node.execFileSync( //
			"D:\\Software\\ImageMagick\\magick.exe", //
			[
				"mogrify", //
				"-path", job.destFolder, //
				"-format", "png", //
				"-depth", "1", //
				"-adaptive-resize", "1920x>", //
				"*-1-*.png"
			], {
				cwd: job.sourceFolder
			}
		);
  }
}
