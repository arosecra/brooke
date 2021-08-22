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
		File categoryDir = new File(remoteVideosDir, "Anime_TV");
		
		
		File[] remoteSeriesToProcess = new File[] {
			new File(categoryDir, "Gankutsuou_Count_of_Monte_Cristo"),
			new File(categoryDir, "Gargantia"),	
			new File(categoryDir, "Get_Backers"),	
			new File(categoryDir, "Ghost_in_the_Shell_Arise"),	
			new File(categoryDir, "Ghost_in_the_Shell_SAC_01"),	
			new File(categoryDir, "Ghost_in_the_Shell_SAC_02"),	
			new File(categoryDir, "Gintama"),	
			new File(categoryDir, "Glass_Fleet"),	
			new File(categoryDir, "Grimgar"),	
			new File(categoryDir, "Gundam_OO"),	
			new File(categoryDir, "Gundam_SEED"),	
			new File(categoryDir, "Gundam_SEED_02_Destiny"),	
			new File(categoryDir, "Gundam_Wing"),	
		};
		
		File localVideosDir = new File("D:\\video");
		File localCategoryDir = new File(localVideosDir, categoryDir.getName());
		
		for(File remoteVideosToProcess : remoteSeriesToProcess) {
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