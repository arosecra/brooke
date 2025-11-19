import * as fs from 'fs';
import * as path from 'path';
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { node } from "../util/node";

export class BookExtractPDFsStep implements JobStep {
	name = "BookExtractPDFsStep";
	execute(job: JobFolder): void {
		const workFiles = fs.readdirSync(job.sourceFolder);
		let reOrdered = [];


		let pdfCount = 0;
		for(let i = 0; i < workFiles.length; i++) {
			const workFile = workFiles[i];
			if(workFile.endsWith("pdf")) {
				pdfCount++;
			}
		}

		for(let i = 0; i < workFiles.length; i++) {
			const workFile = workFiles[i];
			if(workFile.endsWith('pdf') && workFile.includes('cover'))
				reOrdered.push(workFile);
		}

		for(let i = 0; i < workFiles.length; i++) {
			const workFile = workFiles[i];
			if(workFile.endsWith('pdf') && !workFile.includes('cover'))
				reOrdered.push(workFile);
		}


		for(let i = 0; i < reOrdered.length; i++) {
			const workFile = reOrdered[i];
			const prefix = String(i).padStart(3, '0');
			let colorSpace = "pngmono";
			let colorPrefix = "1";
			let size = 1200;
			if(workFile.includes('cover') || workFile.includes('color')) {
				colorSpace = "png16m";
				colorPrefix = "8";
				size = 300;
			}

			
			const outputFilename = 
				`${path.basename(job.workFolder)}-${prefix}-${colorPrefix}-%03d.png`
			const outputFile = path.join(job.destFolder, outputFilename) 

//				JobSubStep jss = new JobSubStep("Extract", job.workFolder, i + 2, pdfCount);
//				jss.startAndPrint();
			node.execFileSync(
				'C:\\software\\ghostscript\\bin\\gswin64c.exe',
				[
					'-dBATCH', //
					'-dNOPAUSE', //
					'-dGraphicsAlphaBits=4', //
					'-q', //
					'-r' + size, //
					'-sDEVICE=' + colorSpace, //
					'-dUseCropBox', //
					'-sOutputFile=' + outputFile,
					path.join(job.sourceFolder, workFile)
				]
			)
		}

	}
	


}