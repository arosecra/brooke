package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.github.arosecra.brooke.services.ImageService;
import org.github.arosecra.brooke.util.CommandLine;

public class CopyForBooxTabletTask implements IRunnableTask {

	private AtomicLong steps = new AtomicLong(0L);
	private AtomicLong current = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private String status;
	private File remoteFile;
	private String itemName;
	private ImageService imageService;
	private boolean runCalibre;
	private int desiredWidth;
	private String folderName;

	public CopyForBooxTabletTask(ImageService imageService, File remoteFile, String itemName, String folderName, int desiredWidth, boolean runCalibre) {
		this.imageService = imageService;
		this.remoteFile = remoteFile;
		this.itemName = itemName;
		this.folderName = folderName;
		this.desiredWidth = desiredWidth;
		this.runCalibre = runCalibre;
	}

	@Override
	public void run() {
		steps.set(7);
			
		started.set(true);
		File tempSsdFolder = new File("C:\\scans\\temp");
		File unzippedFolder = new File(tempSsdFolder, itemName);
		File coverFile = new File(tempSsdFolder.getAbsolutePath(), "cover.png");
		File localSourceFile = new File(tempSsdFolder, remoteFile.getName());
		File localCbzFile = new File(tempSsdFolder.getAbsolutePath(), itemName + ".cbz");
		File azw3OutputFile = new File(localCbzFile.getAbsolutePath().replace(".cbz", ".azw3"));
		unzippedFolder.mkdirs();
		try {
			FileUtils.copyFileToDirectory(remoteFile, tempSsdFolder);
			current.set(1);
			CommandLine.run(new String[] {
					"D:\\software\\7za\\7za.exe",
					"e",
					"-o" + unzippedFolder.getAbsolutePath(),
					localSourceFile.getAbsolutePath()
			});
			current.set(2);
			
			boolean coverGenerated = false;
			
			if(this.desiredWidth != 0) {
				
				for(File file : unzippedFolder.getAbsoluteFile().listFiles()) {
					if(file.getName().endsWith("png")) {
						
						byte[] img = FileUtils.readFileToByteArray(file);
						byte[] resizedImg = this.imageService.resizeImageToWidth(img, this.desiredWidth);
						FileUtils.writeByteArrayToFile(file, resizedImg);
						
						if(!coverGenerated) {
							byte[] cover = this.imageService.resizeImageToWidth(img, 600);
							FileUtils.writeByteArrayToFile(coverFile, cover);
							coverGenerated = true;
						}
					}
				}
			}
			current.set(3);

			CommandLine.run(new String[] {
					"D:\\software\\7za\\7za.exe",
					"a",
					"-tzip",
					"-o" + unzippedFolder.getAbsolutePath(),
					localCbzFile.getAbsolutePath(),
					unzippedFolder.getAbsolutePath() + "\\*.png"
			});
			
			current.set(4);
			File outputFile = localCbzFile;
			if(this.runCalibre) {
				outputFile = new File(localCbzFile.getAbsolutePath().replace(".cbz", ".azw3"));
				
				//ebook-convert Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon_04.cbz Is_it_Wrong_to_Pick_Up_Girls_in_a_Dungeon_04.azw3 --no-process
				CommandLine.run(new String[] {
						"ebook-convert",
						localCbzFile.getAbsolutePath(),
						outputFile.getAbsolutePath(),
						"--no-process",
						"--cover",
						new File(tempSsdFolder.getAbsolutePath(), "cover.png").getAbsolutePath()
				});
			}
			
			current.set(5);
			FileUtils.copyFileToDirectory(outputFile, new File("\\\\drobo5n\\Public\\Scans\\" + this.folderName));
			
			current.set(6);
			FileUtils.deleteDirectory(unzippedFolder);
			FileUtils.delete(localSourceFile);
			FileUtils.delete(localCbzFile);
			FileUtils.delete(coverFile);
			if(this.runCalibre) {
				FileUtils.delete(azw3OutputFile);
			}
			
			
			current.set(7);
			status = "Complete";
		} catch (IOException e) {
			status = "Failed";
		}
	}

	@Override
	public String getJobDescription() {
		return "Copy for Tablet";
	}

	@Override
	public String getJobType() {
		return "Copy for Tablet";
	}

	@Override
	public String getCurrentProgressDescription() {
		return "Step";
	}

	@Override
	public String getTotalProgressDescription() {
		return "Step";
	}

	@Override
	public AtomicBoolean started() {
		return this.started;
	}

	@Override
	public AtomicLong getTotalProgress() {
		return this.steps;
	}

	@Override
	public AtomicLong getCurrentProgress() {
		return this.current;
	}

}
