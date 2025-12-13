import * as fs from 'fs';
import * as path from 'path';
import { parse } from 'yaml';
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { node } from "../util/node";

export class BookRunOcrStep implements JobStep {
	name = 'BookRunOcrStep';
	async execute(job: JobFolder): Promise<void> {
		const detailsContent = fs.readFileSync(node.pathJoin(job.remoteFolder, 'cbtDetails.yaml'));
		const cbtDetails = parse(String(detailsContent));

		fs.mkdirSync(job.destFolder, { recursive: true });
		fs.mkdirSync(node.pathJoin(job.destFolder, 'content_list'), { recursive: true });
		fs.mkdirSync(node.pathJoin(job.destFolder, 'md'), { recursive: true });
		fs.mkdirSync(node.pathJoin(job.destFolder, 'model'), { recursive: true });

		let ocrFiles: any[] = [];
		fs.readdirSync(job.sourceFolder).filter((file) => file.includes('-1-')).forEach((file) => {
			const baseFilename = path.basename(file);
			const baseFilenameNoExt = path.basename(file).replace('.png', '').replace('.webp', '');
			let expectedOcrOutput = node.pathJoin(job.sourceFolder, baseFilenameNoExt);

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
					ocrFiles.push(baseFilename);
					// this.runOcr(job, baseFilename);
					// this.afterOcr(ocrFolder, expectedOcrOutput, dataPrefix);
				} else {
					this.afterOcr(job.destFolder, expectedOcrOutput, dataPrefix);
				}
			}			
		});

		console.log('Running OCR for ' + ocrFiles.length + ' files');

		await this.ocrPool(ocrFiles, job.sourceFolder, job.destFolder);

		//D:\Software\7za\7za.exe a -ttar D:\scans\pipeline_temp\Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon_01\dest\dest.tar D:\scans\pipeline_temp\Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon_01\dest\.ocr.zip
	}

	async ocrPool(ocrFiles: string[], sourceFolder: string, destFolder: string) {
		const chunkSize = 8;
		for (let i = 0; i < ocrFiles.length; i += chunkSize) {
			const chunk = ocrFiles.slice(i, i + chunkSize);
			
			const pool = [];

			for(let j = 0; j < chunk.length; j++) {
				const file = chunk[j];
				const baseFilename = path.basename(file);
				const baseFilenameNoExt = path.basename(file).replace('.png', '').replace('.webp', '');
				let expectedOcrOutput = node.pathJoin(sourceFolder, baseFilenameNoExt);

				const isPng = file.endsWith('png');
				const isWebp = file.endsWith('webp');
				const dataPrefix = isPng ? 'data:image/png;base64,' : 'data:image/webp;base64,';
				pool.push(new Promise((resolve) => {
					this.runOcr(sourceFolder, destFolder, file).then(() => {
						this.afterOcr(destFolder, expectedOcrOutput, dataPrefix);
						resolve(true);
					})
				}))
			}

			await Promise.all(pool);

		}
	}

	afterOcr(destFolder: string, expectedOcrOutput: string, dataPrefix: string) {
		const ocrOutput = node.pathJoin(expectedOcrOutput, 'auto');
		const ocrFiles = fs.readdirSync(ocrOutput);

		for(let i = 0; i < ocrFiles.length; i++) {
			const ocrFile = ocrFiles[i];
			const inputPath = node.pathJoin(ocrOutput, ocrFiles[i]);

			let folder = "md";
			if (ocrFile.includes("content_list")) {
				folder = "content_list";
			} else if (ocrFile.includes("model")) {
				folder = "model";
			} 
			const outputPath = node.pathJoin(destFolder, folder, path.basename(ocrFile));
			if(ocrFile.endsWith('json') && !ocrFile.includes('middle')) {
				fs.copyFileSync(inputPath, outputPath)
			} else if(ocrFile.endsWith('md')) {
				let modifiedLines = this.internImages(inputPath, ocrOutput, dataPrefix);
				fs.writeFileSync(outputPath, modifiedLines.join('\n'));
			}
		}
	}


	async compressData(data: any) {
		const textEncoder = new TextEncoder();
		const readableStream = new ReadableStream({
			start(controller) {
				controller.enqueue(textEncoder.encode(data));
				controller.close();
			},
		});

		const compressedStream = readableStream.pipeThrough(new CompressionStream('gzip'));
		const compressedBlob = await new Response(compressedStream).blob();
		return compressedBlob;
	}

	private internImages(ocrFile: string, ocrOutput: string, dataPrefix: string) {
		const contentBuff = fs.readFileSync(ocrFile);
		const content = String(contentBuff);
		const lines = content.split('\n');
		let modifiedLines = [];
		for (let j = 0; j < lines.length; j++) {
			let line = lines[j];
			if (line.startsWith('![](images/')) {
				line = line.replace('![](', '').replace(')', '').trim();
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

	private runOcr(sourceFolder: string, destFolder: string, file: string) {
		const python = process.env.python ?? 'D:\\Software\\Python\\Python313\\'
		const mineru = process.env.mineru ?? 'D:\\projects\\mineru'

		return node.execFile(
			`${python}\\Scripts\\uv.exe`,
			['-q',
				'--directory', mineru,
				'run', 'mineru',
				'-o', sourceFolder, //
				'-l', 'en', //
				'-d', 'cpu', //
				'-b', 'pipeline', //
				'-m', 'auto', //
				'--source', 'local', //
				'-p', node.pathJoin(sourceFolder, file),
			], {
			shell: true
		});
	}
	
}