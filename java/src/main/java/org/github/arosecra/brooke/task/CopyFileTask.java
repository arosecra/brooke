package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FilenameUtils;
import org.github.arosecra.brooke.model.CacheManifest;
import org.github.arosecra.brooke.model.CachedFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class CopyFileTask implements IRunnableTask {

	private AtomicLong filesize = new AtomicLong(0L);
	private AtomicLong copied = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private File remoteFile;
	private CachedFile cacheFile;
	private CacheManifest manifest;

	public CopyFileTask(CacheManifest manifest, File remoteFile, CachedFile cacheFile) {
		this.manifest = manifest;
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
			fout = new FileOutputStream(new File("D:/Library/Cache", cacheFile.getFilename()));
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
		
		this.manifest.getFiles().add(this.cacheFile);

		ObjectMapper mapper = new YAMLMapper();
		try {
			mapper.writeValue(new File("D:\\Library\\cache-manifest.yaml"), this.manifest);
		} catch (IOException e) {
			e.printStackTrace();
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
