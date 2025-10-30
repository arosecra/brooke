import { RootFolder } from "./root-folder";
import { Task } from "./task";

export class Schedule {

	public workRequired: Task[] = [];

	constructor(
		public pipelineName: string, 
		public rootFolder: RootFolder
	) {}


}