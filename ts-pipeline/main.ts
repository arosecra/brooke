import os from "os";
import process from "process";
import yargs from "yargs";
import { hideBin } from "yargs/helpers";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";
import { setupMasterSchedule } from "./setupMasterSchedule";
import * as dotenv from "dotenv";
dotenv.config();

const argv = yargs(hideBin(process.argv))
  .option("tasks", {
    type: "number",
    description: "Maximum number of tasks",
    default: 0,
  })
  .option("delete", {
    type: "boolean",
    description: "Delete work files when complete",
    default: true,
  })
  .option("threads", {
    type: "number",
    description: "Number of threads",
    default: 1,
  })
  .option("vc", {
    type: "boolean",
    description: "print commands",
    default: false,
  })
  .option("pipelines", {
    type: "string",
    description: "Pipeline",
  })
  .option("summary", {
    type: "boolean",
    description: "summarize work to complete and exit",
    default: false,
  })
  .help()
  .parseSync();

let threads = os.cpus().length;
if (argv.threads > 0) {
  threads = argv.threads;
}
if (argv.summary) {
  threads = 1;
}

const ms = setupMasterSchedule();

ms.determineTasks(argv.pipelines);
ms.printSummary();

if (argv.summary) {
  ms.tasks.forEach((task) => {
    console.log(task.pipelineName, task.itemFolder);
  });
  process.exit();
}

(async () => {
    for (let i = 0; i < ms.tasks.length; i++) {
        const task = ms.tasks[i];
        task.assigned = true;
        await new SinglePipelineExecutor().executeTask(
            argv,
            ms.pipelineByName(task.pipelineName),
            task.itemFolder,
        );
        ms.printSummary();
    }
})();