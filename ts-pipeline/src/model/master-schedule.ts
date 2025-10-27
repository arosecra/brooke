import { Pipeline } from "./pipeline";
import { RootFolder } from "./root-folder";
import { Schedule } from "./schedule";


export class MasterSchedule {

	public schedules: Schedule[] = [];

	public schedule(
		pipeline: Pipeline,
		rootFolder: RootFolder
	): MasterSchedule {
		this.schedules.push(new Schedule(pipeline, rootFolder));
		return this;
	}
}