package org.github.arosecra.brooke;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Main8 {
	public static void main(String[] args) throws IOException {
		File remoteVideosDir = new File("\\\\drobo5n2\\public\\Videos");
		File categoryDir = new File(remoteVideosDir, "Anime_TV");
		
		//list all mp4 files in anime
		//list all mkv files in anime_tv
		
		//get the list of mkv files without a mp4
		//the list of files to process is built from that list
		//do the same for movies, tv folders
		
		
		List<File> remoteSeriesToProcess = new ArrayList<>();
		for(File srcFolder : categoryDir.listFiles()) {
			remoteSeriesToProcess.add(srcFolder);
		}
		
		File localVideosDir = new File("D:\\video");
		File localCategoryDir = new File(localVideosDir, categoryDir.getName());
		
		for(File remoteVideosToProcess : remoteSeriesToProcess) {
			System.out.println("REM " + remoteVideosToProcess.getName());
			File remoteDir = getRemoteDir(remoteVideosToProcess.getName());
			for(File remoteVideo : remoteVideosToProcess.listFiles()) {
				String basename = FilenameUtils.getBaseName(remoteVideo.getName());
				File remoteFile = new File(remoteDir, basename + "\\" + basename + ".mp4");
				
				
				File outputDir = new File(localCategoryDir, basename);
				if(!remoteFile.exists()) {
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
		
		
	}
	
	private static File getRemoteDir(String name) {
		char c = name.charAt(0);
		File basedir = new File("\\\\drobo5n2\\public\\Anime");
		
		// TODO Auto-generated method stub
		return new File(basedir, c + "\\" + name);
	}

	public static void convertVideo(File srcFile, File destFile) {
		Process process;
	    try {
//	    	System.out.println("Converting " + srcFile + " to " + destFile);
	    	if(!destFile.exists())
	    		System.out.println("D:\\software\\handbrake\\handbrakecli.exe --preset MediaLibrary --preset-import-file D:\\video\\handbrake_preset.json -i \""+srcFile.getAbsolutePath()+"\" -o \""+destFile.getAbsolutePath()+"\"   > "+destFile.getParentFile().getAbsolutePath()+"\\out.txt 2>&1");
	    	
	    	
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