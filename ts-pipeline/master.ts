import * as fs from "fs";
import * as path from "path";
import cluster from "cluster";
import process from "process";
import express, { Request, Response } from 'express';
import os from "os";
import { SinglePipelineExecutor } from "./src/pipeline/single-pipeline-executor";
import { Task } from "./src/model/task";
import yargs from "yargs";
import { hideBin } from "yargs/helpers";

import * as dotenv from 'dotenv'; // or import 'dotenv/config'
import { setupMasterSchedule } from "./setupMasterSchedule";
dotenv.config();

const argv = yargs(hideBin(process.argv))
	.option('pipelines', {
		type: 'string',
		description: 'Pipeline'
	})
	.help()
  .parseSync();


let ms = setupMasterSchedule();
ms.determineTasks(argv.pipelines);
ms.printSummary();

const app = express();
const port = 3000;

app.get('/assign', (req: Request, res: Response) => {
  const firstTask = ms.firstUnassignedTask();
  if(firstTask) {
    firstTask.assigned = true;
    console.log(`Assigning pipeline ${firstTask.pipelineName} - ${firstTask.itemFolder}`);
    res.send(JSON.stringify({
      pipelineName: firstTask.pipelineName,
      itemFolder: firstTask.itemFolder,
    }))
  } else {
    res.send(JSON.stringify({done: true}));
  }

});

app.get('/summary', (req: Request, res: Response) => {
  res.set('Content-Type', 'text/plain');
  res.send(ms.summary());
});

app.get('/compute', (req: Request, res: Response) => {
  const newMs = setupMasterSchedule();
  newMs.determineTasks(argv.pipelines);
  newMs.tasks.forEach((newT) => {
    const oT = ms.tasks.find((oldTask) => oldTask.itemFolder === newT.itemFolder && oldTask.pipelineName === newT.pipelineName);
    newT.assigned = !!oT?.assigned;
  })
  ms = newMs;
  res.redirect('/summary');
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});


