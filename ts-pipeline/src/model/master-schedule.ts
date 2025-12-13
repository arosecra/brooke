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
		const files = fs.readdirSync(rf)
		return pipeline.uses.every((use) => {
			return files.some((file) => {
				return !!path.basename(file).match(use);
			});
		});
	}

	doesRemoteProducesExist(pipeline: Pipeline, rf: string): boolean {
		return fs.readdirSync(rf).some((file) => {
			const basename = path.basename(file);
			return basename.match(pipeline.produces);
		});
	}

	doesPropertyCheckPass(pipeline: Pipeline, rf: string): boolean {
		if(!pipeline.propertyCheck) return true;
		return fs.readdirSync(rf).some((file) => {
			const basename = path.basename(file);
			const match = basename.match(pipeline.produces);
			if(match)	return pipeline.propertyCheck(path.join(rf, file));
			else false;
		});
	}

	firstUnassignedTask(): Task | undefined {
		return this.tasks.find((task) => !task.assigned);
	}

	addTask(t: Task, schedule: Schedule) {
		this.tasks.push(t);
		schedule.workRequired.push(t);
	}

	determineTasks(pipelines: string | undefined) {
		this.schedules.forEach((schedule) => {
			schedule.rootFolder.itemFolders.forEach((itemFolder) => {
				const pipeline = this.pipelineByName(schedule.pipelineName);
				const pipelineMatches = !pipelines || pipelines.includes(pipeline.name)
				const producesExists = this.doesRemoteProducesExist(pipeline, itemFolder);
				const usesExists = this.doesRemoteUsesExists(pipeline, itemFolder);

				let produceMissing = !producesExists;
				if(!!pipeline.propertyCheck && usesExists && producesExists) {
					produceMissing = !this.doesPropertyCheckPass(pipeline, itemFolder);
				} 
				const taskApplicable = produceMissing && usesExists && pipelineMatches;

				// console.log(producesExists, usesExists, pipelineMatches, produceMissing, taskApplicable, itemFolder);

				if (taskApplicable) {
					this.addTask(new Task(pipeline.name, itemFolder), schedule);
				}
			});
		});
	}

	printSummary() {
		console.log(this.summary());
	}

	summary() {
		const format = (rf: string, pipeline: string, num: string | number, num2: string | number) => {
			return [rf.padEnd(24, ' '), 
					pipeline.padEnd(48, ' '), 
					String(num).padEnd(8, ' '), 
					String(num2).padEnd(8, ' ')
				].join(' ');
		}

		let res = format("-------------", "--------", "------", "------") + '\r\n';

		res += format("Remote Folder", "Pipeline", "# ToDo", "# Assigned") + '\r\n';
		res += format("-------------", "--------", "------", "------") + '\r\n';

		let total = 0;
		let total2 = 0;
		this.schedules.forEach((schedule) => {
			const req = schedule.workRequired.filter((t) => !t.assigned).length;
			const asn = schedule.workRequired.length - req;

			res += format(
				schedule.rootFolder.rootFolder,
				schedule.pipelineName,
				req,
				asn
				) + "\r\n";
			total += req;
			total2 += asn;
		});
		res += format("-------------", "--------", "------", "------") + '\r\n';
		res += format("Total", "--------", total, total2) + '\r\n';
		return res;
	}
}