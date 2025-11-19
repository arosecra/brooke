import { JobFolder } from "./job-folder";
import { Pipeline } from "./pipeline";

export declare interface JobStep {
	name: string;
  execute(job: JobFolder): void;
}
