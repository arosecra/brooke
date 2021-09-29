package org.github.arosecra.brooke.jobs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.github.arosecra.brooke.util.CommandLine;

public class ConvertToWebp implements BrookeJobStep {
	
	private String srcSuffix = "_PNG.tar";
	
	public ConvertToWebp() {}
	public ConvertToWebp(String srcSuffix) {this.srcSuffix = srcSuffix;}

	@Override
	public boolean required(File folder) throws IOException {		
		boolean pngsFound = false;
		File cbtFile = new File(folder, folder.getName() + srcSuffix);

		if(cbtFile.exists()) {
			try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(cbtFile)))) {
				TarArchiveEntry entry;
				while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
					if (entry.getName().endsWith("png")) {
						pngsFound = true;
					}
				}
			}
		}
		
		return pngsFound && !new File(folder, folder.getName() + ".cbt").exists();
	}

	@Override
	public File execute(File folder) throws IOException {

		File tempSsdFolder = new File("C:\\scans\\temp");
		
		if(required(folder)) {
			File srcCbtFile = new File(folder, folder.getName() + srcSuffix);
			File bookTempSsdFolder = new File(tempSsdFolder, folder.getName());
			if(srcCbtFile.exists()) {
				bookTempSsdFolder.mkdirs();
				extractPngs(srcCbtFile, bookTempSsdFolder);
				convertPngsToWebp(bookTempSsdFolder);
				retarToCbt(bookTempSsdFolder, folder);
				deleteWebPs(bookTempSsdFolder);
			}
			
		}
		return folder;
	}
	
	private void deleteWebPs(File bookTempFolder) {
		for(File inputFile : bookTempFolder.listFiles()) {
			if(inputFile.getName().endsWith("webp")) {
				inputFile.delete();
			}
		}
		bookTempFolder.delete();
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
	public List<File> filesRequiredForExecution(File folder) {
		List<File> result = new ArrayList<>();
		result.add(new File(folder, folder.getName() + srcSuffix));	
		return result;
	}

	@Override
	public boolean isRemoteStep() {
		return false;
	}

}
