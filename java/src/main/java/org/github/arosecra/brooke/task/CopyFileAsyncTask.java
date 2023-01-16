package org.github.arosecra.brooke.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.scheduling.annotation.Async;

public class CopyFileAsyncTask implements ProgressableRunnable  {
	
	private AtomicLong filesize = new AtomicLong(0L);
	private AtomicLong copied = new AtomicLong(0L);
	private AtomicBoolean started = new AtomicBoolean(false);
	private File remoteFile;
	private File cacheFile;
	
	public CopyFileAsyncTask(File remoteFile, File cacheFile) {
		this.remoteFile = remoteFile;
		this.cacheFile = cacheFile;
	}
	
	@Async
	public void copyFilesWithProgress(File remoteFile, File cacheFile) {
        FileInputStream  fin  = null;
        FileOutputStream fout = null;
        
        this.filesize.set(remoteFile.length());
        
        long counter = 0;
        int r = 0;
        byte[] b = new byte[1024];
        try {
                fin  = new FileInputStream(remoteFile);
                fout = new FileOutputStream(cacheFile);
                while( (r = fin.read(b)) != -1) {
                        counter += r;
                        this.copied.set(counter);
                        fout.write(b, 0, r);
                }
        }
        catch(Exception e){
                System.out.println("foo");
        }
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
	public void run() {
        FileInputStream  fin  = null;
        FileOutputStream fout = null;
        
        this.filesize.set(remoteFile.length());
        
        long counter = 0;
        int r = 0;
        byte[] b = new byte[1024*1024];
        try {
                fin  = new FileInputStream(remoteFile);
                fout = new FileOutputStream(cacheFile);
                while( (r = fin.read(b)) != -1) {
                        counter += r;
                        this.copied.set(counter);
                        fout.write(b, 0, r);
                        System.out.println(counter + " of " + remoteFile.length());
                }
                System.out.println("Copied " + remoteFile + " to " + cacheFile);
                fout.flush();
                fout.close();
        }
        catch(Exception e){
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
