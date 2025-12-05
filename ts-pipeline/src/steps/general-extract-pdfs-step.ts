import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import * as fs from 'fs';
import { Pipeline } from "../model/pipeline";
import * as child_process from 'child_process';
import * as path from 'path';
import { node } from "../util/node";

export class GeneralExtractPDFsStep implements JobStep {
	name='GeneralExtractPDFsStep'
	execute(job: JobFolder): void {
		const ghostscript = process.env.ghostscript ?? 'C:\\software\\ghostscript'
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
			if(workFile.endsWith('pdf'))
				reOrdered.push(workFile);
		}

		for(let i = 0; i < reOrdered.length; i++) {
			const workFile = reOrdered[i];
			const prefix = String(i).padStart(3, '0');
			let colorSpace = "png16m";
			let colorPrefix = "8";
			let size = 300;
			
			const outputFilename = 
				`${path.basename(job.workFolder)}-${prefix}-${colorPrefix}-%03d.png`
			const outputFile = path.join(job.tempFolder, outputFilename) 

//				JobSubStep jss = new JobSubStep("Extract", job.workFolder, i + 2, pdfCount);
//				jss.startAndPrint();
			node.execFileSync(
				`${ghostscript}\\bin\\gswin64c.exe`,
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