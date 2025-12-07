import * as child_process from "child_process";
import { MasterSchedule } from "./src/model/master-schedule";
import { Pipeline } from "./src/model/pipeline";
import { RootFolder } from "./src/model/root-folder";
import { BookConvertPngToWebpStep } from "./src/steps/book-convert-png-to-webp-step";
import { BookCreateCoverThumbnailStep } from "./src/steps/book-create-cover-thumbnail-step";
import { BookCreateThumbnailsStep } from "./src/steps/book-create-thumbnails-step";
import { BookDeskewImageStep } from "./src/steps/book-deskew-image-step";
import { BookExtractPDFsStep } from "./src/steps/book-extract-pdfs-step";
import { BookResizeImageStep } from "./src/steps/book-resize-image-step";
import { BookRunOcrStep } from "./src/steps/book-run-ocr-step";
import { BookTarToCbtGzStep } from "./src/steps/book-tar-to-cbt-gz-step copy";
import { OcrUntarCbtGzStep } from "./src/steps/ocr-extract-cbts-step";

export function setupMasterSchedule() {
  const lightNovels = new RootFolder(
    "Light Novels",
    "\\\\syn01\\syn01public\\Scans\\Light_Novels_Repository"
  );
  const fiction = new RootFolder(
    "Fiction",
    "\\\\syn01\\syn01public\\Scans\\Fiction_Repository"
  );
  const nonfiction = new RootFolder(
    "Non Fiction",
    "\\\\syn01\\syn01public\\Scans\\NonFiction_Repository"
  );
  // const graphicNovels = new RootFolder("Graphic Novels", "\\\\syn01\\syn01public\\Scans\\Graphic_Novels_Repository");
  // const magazines = new RootFolder("Magazines", "\\\\syn01\\syn01public\\Scans\\Magazine_Repository");
  // const researchPapers = new RootFolder("Research Papers", "\\\\syn01\\syn01public\\Scans\\Research_Papers_Repository");
  // const anime = new RootFolder("Anime", "\\\\syn01\\syn01public\\Anime");
  // const movies = new RootFolder("Movies", "\\\\drobo5n2\\public\\Movies");
  const bookOcrPipeline = new Pipeline() //
    .setName("Book OCR") //
    .setUses([".*.cbt.gz", ".*.yaml"]) //
    .setProduces(".*.cbt.gz") //
    .setPropertyCheck((file: string) => {
      const pout = String(child_process.execFileSync("tar", ["-ztf", file]));
      const list = pout.split("\r\n");
      return list.some((line) => line.trim().endsWith(".md"));
    }) //
    .addStep(new OcrUntarCbtGzStep())
    .addStep(new BookRunOcrStep())
    .addStep(new BookTarToCbtGzStep());

  const bookCoverThumbnailPipeline = new Pipeline()
    .setName("Book Cover Thumbnail") //
    .setUses([".*cover[s]?.pdf"]) //
    .setProduces("thumbnail.webp") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookCreateCoverThumbnailStep(250, 400));

  // const bookGzipCbtsPipeline = new Pipeline() //
  //   .setName("Book Gzip CBTs") //
  //   .setUses([".*.cbt"]) //
  //   .setProduces(".*.cbt.gz")
  //   .addStep(new OcrUntarCbtGzStep())
  //   .addStep(new BookTarToCbtGzStep());
  const bookCbtPipeline = new Pipeline() //
    .setName("Book CBT") //
    .setUses([".*.pdf"]) //
    .setProduces(".*.cbt.gz") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookResizeImageStep()) //
    .addStep(new BookDeskewImageStep()) //
    .addStep(new BookCreateThumbnailsStep()) //
    .addStep(new BookConvertPngToWebpStep()) //
    .addStep(new BookTarToCbtGzStep()); //

  const singlePdfThumbnailPipeline = new Pipeline()
    .setName("Single PDF Cover Thumbnail") //
    .setUses([".*.pdf"]) //
    .setProduces("thumbnail.webp") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookCreateCoverThumbnailStep(250, 400));

  const movieThumbnailPipeline = new Pipeline()
    .setName("Movie Cover Thumbnail") //
    .setUses([".*cover[s]?.pdf"]) //
    .setProduces("thumbnail.webp") //
    .addStep(new BookExtractPDFsStep()) //
    .addStep(new BookCreateCoverThumbnailStep(350, 350));

  return (
    new MasterSchedule([
      bookCoverThumbnailPipeline,
      bookCbtPipeline,
      // bookGzipCbtsPipeline,
      bookOcrPipeline,
      singlePdfThumbnailPipeline,
      movieThumbnailPipeline,
    ]) //
      .schedule(bookCoverThumbnailPipeline.name, lightNovels) //
      .schedule(bookCbtPipeline.name, lightNovels) //
      .schedule(bookOcrPipeline.name, lightNovels) //

      //
      .schedule(bookCoverThumbnailPipeline.name, fiction) //
      .schedule(bookCbtPipeline.name, fiction) //


      // .schedule(bookOcrPipeline.name, fiction) //
      //
      .schedule(bookCoverThumbnailPipeline.name, nonfiction) //
      .schedule(bookCbtPipeline.name, nonfiction) //











    // .schedule(bookOcrPipeline.name, nonfiction) //
    //
    // .schedule(bookCoverThumbnailPipeline.name, graphicNovels) //
    //
    // .schedule(singlePdfThumbnailPipeline.name, magazines) //
    //
    // .schedule(singlePdfThumbnailPipeline.name, researchPapers) //
    //
    // .schedule(movieThumbnailPipeline.name, anime) //
    //
    // .schedule(movieThumbnailPipeline.name, movies) //
  );
}
