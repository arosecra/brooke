import { CbtDetails } from "../model/book/cbtDetails";
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import { stringify } from 'yaml'
import { node } from "../util/node";

export class BookCreateCbtDetailsStep implements JobStep {

	//gs -q -dNODISPLAY --permit-file-read="document.pdf" -c "(document.pdf) (r) file runpdfbegin pdfpagecount = quit"

	execute(pipeline: Pipeline, job: JobFolder): void {

		const files = fs.readdirSync(job.tempFolder).filter((file) => file.endsWith('png'));

		const cbtDetails: CbtDetails = {
			numberOfPages: files.length
		};

		const content = stringify(cbtDetails);

		fs.writeFileSync(node.pathJoin(job.destFolder, 'cbtDetails.yaml'), content);

	}
	
}