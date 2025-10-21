package org.github.arosecra.book.pipeline.book.steps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.book.pipeline.model.Pipeline;
import org.github.arosecra.book.pipeline.util.Try;
import org.github.arosecra.brooke.util.CommandLine;

public class RunOCRStep implements JobStep {

	@Override
	public void execute(Pipeline pipeline, JobFolder job) throws IOException {
		List<String> skipOCR = new ArrayList<>();
		skipOCR.addAll(job.bookJob.ocrProperties.blankPages);
		skipOCR.addAll(job.bookJob.ocrProperties.imagePages);

		File ocrTempFolder = new File(job.workFolder, "ocr");
		ocrTempFolder.mkdirs();

		File mdFolder = new File(job.destFolder, ".md");
		mdFolder.mkdirs();

		for (File file : job.destFolder.listFiles()) {
			String pngName = file.getName().replace(".webp", ".png");
			if (!file.isDirectory() && !skipOCR.contains(pngName)) {

				CommandLine.run(new String[] { "uv", "run", //
						"mineru", //
						"-p", file.getAbsolutePath(), //
						"-o", ocrTempFolder.getAbsolutePath(), //
						"-l", "en", //
						"--source", "local" //
				}, new File("D:\\Projects\\mineru"), System.out);

				Path mdPath = Path.of(ocrTempFolder.getAbsolutePath(), file.getName().replace(".webp", ""), "auto",
						file.getName().replace(".webp", ".md"));

				String contents = Files.readString(mdPath);
				ImageVisitor visitor = new ImageVisitor(mdPath.getParent());
				Parser parser = Parser.builder().build();
				Node doc = parser.parse(contents);
				doc.accept(visitor);
				String modifiedContents = MarkdownRenderer.builder().build().render(doc);
				Files.writeString(Path.of(mdFolder.getAbsolutePath(), file.getName().replace("webp", "md")),
						modifiedContents, StandardOpenOption.CREATE_NEW);
			}
		}
	}
}

class ImageVisitor extends AbstractVisitor {

	private Path parentDir = Path.of("");

	public ImageVisitor(Path parentDir) {
		this.parentDir = parentDir;
	}

	@Override
	public void visit(Image image) {
		String imageFilename = image.getDestination();
		byte[] bytes = Try.readAllBytes(this.parentDir.resolve(imageFilename));
		String base64 = "data:image/webp;base64," + Base64.getEncoder().encodeToString(bytes);
		image.setDestination(base64);

		super.visit(image);
	}

}