import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { Pipeline } from "../model/pipeline";
import * as fs from 'fs';
import * as path from 'path';
import { node } from "../util/node";
import { parse } from 'yaml'

export class BookRunOcrStep implements JobStep {
	execute(job: JobFolder): void {
		const detailsContent = fs.readFileSync(node.pathJoin(job.remoteFolder, 'cbtDetails.yaml'));
		const cbtDetails = parse(String(detailsContent));

		const ocrFolder = node.pathJoin(job.destFolder, '.ocr');
		fs.mkdirSync(ocrFolder, { recursive: true });

		fs.readdirSync(job.destFolder).filter((file) => file.includes('-1-')).forEach((file) => {
			const baseFilename = path.basename(file);
			const baseFilenameNoExt = path.basename(file).replace('.png', '').replace('.webp', '');
			let expectedOcrOutput = node.pathJoin(job.tempFolder, baseFilenameNoExt);

			const isPng = file.endsWith('png');
			const isWebp = file.endsWith('webp');
			const dataPrefix = isPng ? 'data:image/png;base64,' : 'data:image/webp;base64,';

			if(cbtDetails.imagePages.includes(baseFilenameNoExt)) {
				//we just skip. there won't be a markdown file
			} else if (cbtDetails.blankPages.includes(baseFilenameNoExt)) {
				//skip. we will generate a blank page at runtime
				// no need to create a file
			} else if (isWebp || isPng) {
				if(!fs.existsSync(expectedOcrOutput)) {
					this.runOcr(job, baseFilename);
				}

				const ocrOutput = node.pathJoin(expectedOcrOutput, 'auto');
				const ocrFiles = fs.readdirSync(ocrOutput);

				for(let i = 0; i < ocrFiles.length; i++) {
					const ocrFile = ocrFiles[i];
					const inputPath = node.pathJoin(ocrOutput, ocrFiles[i]);
					const outputPath = node.pathJoin(ocrFolder, path.basename(ocrFile))
					if(ocrFile.endsWith('json')) {
						fs.copyFileSync(inputPath, outputPath)
					} else if(ocrFile.endsWith('md')) {
						let modifiedLines = this.internImages(inputPath, ocrOutput, dataPrefix);
						fs.writeFileSync(outputPath, modifiedLines.join('\n'));
					}
				}

			}			
		});
	}

	private internImages(ocrFile: string, ocrOutput: string, dataPrefix: string) {
		const contentBuff = fs.readFileSync(ocrFile);
		const content = String(contentBuff);
		const lines = content.split('\n');
		let modifiedLines = [];
		for (let j = 0; j < lines.length; j++) {
			let line = lines[j];
			if (line.startsWith('![](images/')) {
				line = line.replace('![](', '').replace(')', '');
				const figurePath = node.pathJoin(ocrOutput, line);
				const base64String = fs.readFileSync(figurePath, 'base64');

				const newLine = '![](' + dataPrefix + base64String + ')';
				modifiedLines.push(newLine);
			} else {
				modifiedLines.push(line);
			}
		}
		return modifiedLines;
	}

	private runOcr(job: JobFolder, file: string) {
		node.execFileSync(
			"D:\\Software\\Python\\Python313\\Scripts\\uv.exe",
			['-q',
				'--directory', "D:\\projects\\mineru",
				'run', 'mineru',
				'-o', job.tempFolder, //
				'-l', 'en', //
				'-d', 'cpu', //
				'-b', 'pipeline', //
				'-m', 'auto', //
				'--source', 'local', //
				'-p', node.pathJoin(job.destFolder, file),
			], {
			shell: true
		});
	}
	
}