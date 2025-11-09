import * as path from "path";
import { JobFolder } from "../model/job-folder";
import { JobStep } from "../model/job-step";
import { node } from "../util/node";

export class OcrUntarCbtGzStep implements JobStep {
  execute(job: JobFolder): void {
    const tarname = node.pathJoin(
      job.sourceFolder,
      path.basename(job.workFolder) + ".cbt.gz"
    );
    node.execFileSync("tar", ["-xvzf", tarname, "-C", job.destFolder]);
  }
}
