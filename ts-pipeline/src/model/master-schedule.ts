import { Pipeline } from "./pipeline";
import { RootFolder } from "./root-folder";
import { Schedule } from "./schedule";
import { Task } from "./task";
import * as fs from 'fs';
import * as path from 'path';

export class MasterSchedule {

	public schedules: Schedule[] = [];
	private pipelinesByName: Record<string, Pipeline> = {};
	public tasks: Task[] = [];

	constructor(private pipelines: Pipeline[]) {
		pipelines.forEach((pipeline) => this.pipelinesByName[pipeline.name] = pipeline)
	}

	public schedule(
		pipelineName: string,
		rootFolder: RootFolder
	): MasterSchedule {
		this.schedules.push(new Schedule(pipelineName, rootFolder));
		return this;
	}

	public pipelineByName(name: string): Pipeline {
		return this.pipelinesByName[name];
	}
	
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

	firstUnassignedTask(): Task | undefined {
		return this.tasks.find((task) => !task.assigned);
	}

	addTask(t: Task, schedule: Schedule) {
		this.tasks.push(t);
		schedule.workRequired.push(t);
	}
}