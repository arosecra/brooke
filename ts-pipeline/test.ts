
import * as dotenv from "dotenv";
import * as fs from "fs";
import { BookRunOcrStep } from "./src/steps/book-run-ocr-step";
import { node } from "./src/util/node";
dotenv.config();

(async () => {
  let step = new BookRunOcrStep();
  const ocrFolder = node.pathJoin(
    "C:/scans/temp/pipeline_temp/Test/2_BookRunOcrStep/.ocr",
  );
  fs.mkdirSync(ocrFolder, { recursive: true });
  fs.mkdirSync(node.pathJoin(ocrFolder, 'content_list'), { recursive: true });
  fs.mkdirSync(node.pathJoin(ocrFolder, 'md'), { recursive: true });
  fs.mkdirSync(node.pathJoin(ocrFolder, 'model'), { recursive: true });
  const files = fs
    .readdirSync("C:/scans/temp/pipeline_temp/Test/2_BookRunOcrStep")
    .filter((file) => file.includes("-1-"));

  await step.ocrPool(
    files,
    ocrFolder,
    "C:/scans/temp/pipeline_temp/Test/1_OcrUntarCbtGzStep",
    "C:/scans/temp/pipeline_temp/Test/2_BookRunOcrStep",
  );
})();
