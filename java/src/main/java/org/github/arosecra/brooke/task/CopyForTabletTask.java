package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.util.CommandLine;

public class CopyForTabletTask implements IRunnableTask {

	private AtomicLong steps = new AtomicLong(0L);
	private AtomicLong current = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private String status;
	private File remoteFile;
	private String itemName;

	public CopyForTabletTask(File remoteFile, String itemName) {
		this.remoteFile = remoteFile;
		this.itemName = itemName;
	}

	@Override
	public void run() {
		steps.set(5);
		started.set(true);
		File tempSsdFolder = new File("C:\\scans\\temp");
		File unzippedFolder = new File(tempSsdFolder, itemName);
		unzippedFolder.mkdirs();
		try {
			FileUtils.copyFileToDirectory(remoteFile, tempSsdFolder);
			File localSourceFile = new File(tempSsdFolder, remoteFile.getName());
			File localCbzFile = new File(tempSsdFolder.getAbsolutePath(), itemName + ".cbz");
			current.set(1);
			CommandLine.run(new String[] {
					"D:\\software\\7za\\7za.exe",
					"e",
					"-o" + unzippedFolder.getAbsolutePath(),
					localSourceFile.getAbsolutePath()
			});
			current.set(2);

			CommandLine.run(new String[] {
					"D:\\software\\7za\\7za.exe",
					"a",
					"-tzip",
					"-o" + unzippedFolder.getAbsolutePath(),
					localCbzFile.getAbsolutePath(),
					unzippedFolder.getAbsolutePath() + "\\*.png"
			});
			current.set(3);

			FileUtils.copyFileToDirectory(localCbzFile, new File("\\\\drobo5n\\Public\\Scans\\ForTablet"));
			current.set(4);
			FileUtils.deleteDirectory(unzippedFolder);
			FileUtils.delete(localSourceFile);
			FileUtils.delete(localCbzFile);
			current.set(5);
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
