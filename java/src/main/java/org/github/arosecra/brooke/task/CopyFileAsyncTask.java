package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FilenameUtils;

public class CopyFileAsyncTask implements IRunnableTask {

	private AtomicLong filesize = new AtomicLong(0L);
	private AtomicLong copied = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private File remoteFile;
	private File cacheFile;

	public CopyFileAsyncTask(File remoteFile, File cacheFile) {
		this.remoteFile = remoteFile;
		this.cacheFile = cacheFile;
	}

	public AtomicLong getFilesize() {
		return filesize;
	}

	public void setFilesize(AtomicLong filesize) {
		this.filesize = filesize;
	}

	public AtomicLong getCopied() {
		return copied;
	}

	public void setCopied(AtomicLong copied) {
		this.copied = copied;
	}

	@Override
	public String getJobDescription() {
		return "Caching " + FilenameUtils.getBaseName(this.remoteFile.getName()).replace("_", "");
	}

	@Override
	public String getJobType() {
		return "Cache";
	}

	@Override
	public String getCurrentProgressDescription() {
		return "Copied";
	}

	@Override
	public String getTotalProgressDescription() {
		return "Filesize";
	}

	@Override
	public void run() {
		FileInputStream fin = null;
		FileOutputStream fout = null;

		this.filesize.set(remoteFile.length());
		this.started.set(true);

		long counter = 0;
		int r = 0;
		byte[] b = new byte[1024 * 1024];
		try {
			fin = new FileInputStream(remoteFile);
			fout = new FileOutputStream(cacheFile);
			while ((r = fin.read(b)) != -1) {
				counter += r;
				this.copied.set(counter);
				fout.write(b, 0, r);
			}
			fout.flush();
			fout.close();
		} catch (Exception e) {
			System.out.println("foo");
		}
	}

	@Override
	public AtomicBoolean started() {
		return this.started;
	}

	@Override
	public AtomicLong getTotalProgress() {
		return this.filesize;
	}

	@Override
	public AtomicLong getCurrentProgress() {
		return this.copied;
	}

}
