package org.github.arosecra.brooke.jobs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.github.arosecra.brooke.util.CommandLine;

public class ConvertToMarkdown implements BrookeJobStep {
	
	public static void main(String[] args) throws Exception {
		ConvertToMarkdown step = new ConvertToMarkdown();
		step.combineMarkdowns(new File("C:\\Scans\\temp\\Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon_01_MK"));
	}
	
	private String srcSuffix = "_PNG.tar";
	
	public ConvertToMarkdown() {}
	public ConvertToMarkdown(String srcSuffix) {this.srcSuffix = srcSuffix;}

	@Override
	public boolean required(JobFolder folder) throws IOException {		
		boolean pngsFound = false;
		boolean mdExists = false;
		
		for(File file : folder.remoteFiles) {
			if(file.getName().endsWith(".md")) {
				mdExists = true;
			}
		}
		
		if(!mdExists) {
			for(File file : folder.remoteFiles) {
				if(file.getName().endsWith(srcSuffix)) {
					pngsFound = true;
				}
			}
		}
		
		return pngsFound && !mdExists;
	}

	@Override
	public void execute(JobFolder folder) throws IOException {

		File tempSsdFolder = new File("C:\\scans\\temp");
		
		if(required(folder)) {
			File srcCbtFile = new File(folder.workFolder, folder.workFolder.getName() + srcSuffix);
			File bookTempSsdFolder = new File(tempSsdFolder, folder.workFolder.getName());
			File bookTempSsdOutputFolder = new File(tempSsdFolder, folder.workFolder.getName() + "_md");
			if(srcCbtFile.exists()) {
				bookTempSsdFolder.mkdirs();
				bookTempSsdOutputFolder.mkdirs();
				extractPngs(srcCbtFile, bookTempSsdFolder);
				
				//move color pngs to another folder / prefilter
				runMinerU(bookTempSsdFolder, bookTempSsdOutputFolder);
				//combine mds, inlining pngs (including filtered)
				//
				
				
//				convertPngsToWebp(bookTempSsdFolder);
//				retarToCbt(bookTempSsdFolder, folder.workFolder);
//				deleteWebPs(bookTempSsdFolder);
			}
			
		}
	}
	
	private void deleteWebPs(File bookTempFolder) {
		for(File inputFile : bookTempFolder.listFiles()) {
			if(inputFile.getName().endsWith("webp")) {
				inputFile.delete();
			}
		}
		bookTempFolder.delete();
	}
	
	private void combineMarkdowns(File bookTempOutputFolder) throws Exception {
		Parser parser = Parser.builder().build();
		List<File> mdFiles = new ArrayList<>();
		for(File childDir : bookTempOutputFolder.listFiles()) {
			for(File pageFile : new File(childDir, "auto").listFiles()) {
				if(pageFile.getName().endsWith("md")) {
					mdFiles.add(pageFile);
				}
			}
		}
		
		
		String outputContents = "";
		
		
		for(int i = 0; i < mdFiles.size(); i++) {
			File mdFile = mdFiles.get(i);
			File parentDir = mdFile.getParentFile();
			
			String content = FileUtils.readFileToString(mdFile);

			ImageVisitor visitor = new ImageVisitor(parentDir);
			
			Node doc = parser.parse(content);
			doc.accept(visitor);
			String modifiedContents = MarkdownRenderer.builder().build().render(doc);
			modifiedContents += "<!-- Page " + i + "-->" + System.lineSeparator();
			
			outputContents += modifiedContents;
		}
		
		
		FileUtils.writeStringToFile(new File(bookTempOutputFolder, "../test.md"), outputContents);
		
	}
	
	private void runMinerU(File bookTempFolder, File bookTempOutputFolder) {
		CommandLine.run(new String[] {
			"D:\\Software\\Python\\Python313\\Scripts\\uv.exe",
			"-p",
			bookTempFolder.getAbsolutePath(),
			"-o",
			bookTempOutputFolder.getAbsolutePath()
		}, new File("D:/projects/mineru"), System.out);
	}

	private void extractPngs(File cbtFile, File bookTempFolder) {
		// D:\Software\7za>7za e -oD:\scans\temp\Acquisition_of_Strategic_Knowledge\ D:\scans\books\Acquisition_of_Strategic_Knowledge\Acquisition_of_Strategic_Knowledge.cbt
		
		if(ArrayUtils.isEmpty(bookTempFolder.listFiles())) {
			CommandLine.run(new String[] {
					"D:\\software\\7za\\7za.exe", 
					"e", 
					"-o" + bookTempFolder.getAbsolutePath(),
	    			cbtFile.getAbsolutePath()	
			});		
		}
	}

	private void convertPngsToWebp(File bookFolder) {
		File[] files = bookFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("png");
			}
		});
		if(files != null) {
			for(int i = 0; i < files.length; i++) {
				File inputFile = files[i];
				JobSubStep jss = new JobSubStep("Webp Conversion", bookFolder, i, files.length);
				jss.startAndPrint();
				File outputFile = new File(bookFolder, FilenameUtils.getBaseName(inputFile.getName()) + ".webp");
				
				CommandLine.run(new String[] {
						"C:\\Software\\libwebp\\bin\\cwebp", 
						"-lossless", 
						inputFile.getAbsolutePath(), 
						"-o",
						outputFile.getAbsolutePath()		
				});
				jss.endAndPrint();
				inputFile.delete();
			}
		}
	}

	private void retarToCbt(File bookTempFolder, File tempFolder) {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture  -ttar D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt D:\Scans\temp\3D_Game_Engine_Architecture\*.webp
		CommandLine.run(new String[] {
			"D:\\software\\7za\\7za.exe", 
			"a", 
			"-ttar", 
			"-o" + tempFolder.getAbsolutePath(),
    		tempFolder.getAbsolutePath() + "\\" + tempFolder.getName() + ".cbt", 
    		bookTempFolder.getAbsolutePath() + "\\*.webp"		
		});
	}

	@Override
	public boolean isManual() {
		return false;
	}
	@Override
	public List<File> filesRequiredForExecution(JobFolder folder) {
		List<File> result = new ArrayList<>();
		result.add(new File(folder.remoteFolder, folder.remoteFolder.getName() + srcSuffix));	
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}

}

class ImageVisitor extends AbstractVisitor {
	
	File parentDir = new File("");

	public ImageVisitor(File parentDir) {
		this.parentDir = parentDir;
	}

	@Override
	public void visit(Image image) {
		String imageFilename = image.getDestination();
		try {
			byte[] bytes = Files.readAllBytes(new File(this.parentDir, imageFilename).toPath());
			String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
			image.setDestination(base64);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.visit(image);
	}
	
	
}
