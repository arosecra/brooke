import process from "process";
import yargs from "yargs";
import { hideBin } from "yargs/helpers";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";

import * as dotenv from 'dotenv';
import { setupMasterSchedule } from "./setupMasterSchedule";
dotenv.config();

const argv = yargs(hideBin(process.argv))
  .option("tasks", {
    type: "number",
    description: "Maximum number of tasks",
    default: 0,
  })
  .option('ip', {
    type: 'string',
    description: 'master ip',
    default: 'localhost'
  })
  .option("delete", {
    type: "boolean",
    description: "Delete work files when complete",
    default: true,
  })
	.help()
  .parseSync();

const ms = setupMasterSchedule();


let done = false;

(async () => {
  while(!done) {
    console.log('Fetching work');
    const response = await fetch(`http://${argv.ip}:3000/assign`);
    if(response.ok) {
      const task = await response.json();
      done = !!task.done;
      if(!done) 
        console.log(`Starting ${task.pipelineName} - ${task.itemFolder}`);
        await new SinglePipelineExecutor().executeTask(
          argv,
          ms.pipelineByName(task.pipelineName),
          task.itemFolder
        );
    } else {
      done = true;
    }
  }

})();
