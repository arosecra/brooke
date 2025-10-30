import { JobFolder } from "./job-folder";
import { Pipeline } from "./pipeline";

export declare interface JobStep {
  execute(job: JobFolder): void;
}
