package org.github.arosecra.brooke;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main8 {
	public static void main(String[] args) throws IOException {
		File remoteVideosDir = new File("\\\\drobo5n2\\public\\MkvVideos");
		File categoryDir = new File(remoteVideosDir, "Anime TV");
		
		File remoteVideosToProcess = new File(categoryDir, "Baccano");
		
		File localVideosDir = new File("D:\\video");
		File localCategoryDir = new File(localVideosDir, remoteVideosToProcess.getName());
		
		for(File remoteVideo : remoteVideosToProcess.listFiles()) {
			String basename = FilenameUtils.getBaseName(remoteVideo.getName());
			File outputDir = new File(localCategoryDir, basename);
			if(!outputDir.exists()) {
				outputDir.mkdirs();
				
				File outputFile = new File(outputDir, basename + ".mp4");
				convertVideo(remoteVideo, outputFile);
				
				for(File file : localVideosDir.listFiles()) {
					if(file.getName().startsWith(basename)) {
						FileUtils.moveFileToDirectory(file, outputDir, false);
					}
				}
			}
		}
		
		
		
	}
	
	public static void convertVideo(File srcFile, File destFile) {
		Process process;
	    try {
//	    	System.out.println("Converting " + srcFile + " to " + destFile);
	    	
	    	System.out.println("D:\\software\\handbrake\\handbrakecli.exe --preset MediaLibrary --preset-import-file D:\\video\\handbrake_preset.json -i \""+srcFile.getAbsolutePath()+"\" -o \""+destFile.getAbsolutePath()+"\"");
	    	
	    	
//	    	ProcessBuilder pb = new ProcessBuilder( "D:\\software\\handbrake\\handbrakecli.exe", 
//	    			"--preset",
//	    			"MediaLibrary",
//	    			"--preset-import-file",
//	    			"D:\\video\\handbrake_preset.json",
//	    			
//	    			"-i",
//	    			"\""+srcFile.getAbsolutePath()+"\"",
//	    			"-o", 
//	    			"\""+destFile.getAbsolutePath()+"\""
//	    			);
//
//	      process = pb.start();
//	      
//	      process.waitFor( 60, TimeUnit.MINUTES );
//	      if ( process.exitValue() == 0 ) {
//	        // Success
//	        printProcessOutput( process.getInputStream(), System.out );
//	      } else {
//	        printProcessOutput( process.getErrorStream(), System.err );
//	      }
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
	}
	
	private static void printProcessOutput( InputStream inputStream, PrintStream output ) throws IOException {
	    try ( InputStreamReader isr = new InputStreamReader( inputStream );
	        BufferedReader bufferedReader = new BufferedReader( isr ) ) {
	      String line;
	      while ( ( line = bufferedReader.readLine() ) != null ) {
	        output.println( line );
	      }
	    }
	  }
}