import { Pipeline } from "./pipeline";
import { RootFolder } from "./root-folder";
import * as fs from 'fs';
import * as path from 'path';

export class Schedule {

	public workRequired: string[] = [];

	constructor(
		public pipeline: Pipeline, 
		public rootFolder: RootFolder
	) {}


	doesRemoteUsesExists(pipeline: Pipeline, rf: string): boolean {
		return fs.readdirSync(rf).some((file) => {
			const basename = path.basename(file);
			return basename.match(pipeline.uses);
		});
	}

	doesRemoteProducesExist(pipeline: Pipeline, rf: string): boolean {
		return fs.readdirSync(rf).some((file) => {
			const basename = path.basename(file);
			return basename.match(pipeline.produces);
		});
	}
}