package org.github.arosecra.brooke;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FilenameUtils;

public class Main5 {

	public static void main(String[] args) throws IOException {
		//for each file in books
		//   check if the images in the cbt are pngs or webps
		//   if pngs, then
		//		extract to temp
		//      convert pngs to webp
		//      delete pngs
		//      re-tar to csv
		//      
		
		File scansFolder = new File("D:\\scans");
		File booksFolder = new File(scansFolder, "Books");
		File tempFolder = new File(scansFolder, "temp");
		
		for(File bookFolder : booksFolder.listFiles()) {
			File cbtFile = new File(bookFolder, bookFolder.getName() + ".cbt");
			File bookTempFolder = new File(tempFolder, bookFolder.getName());
			File tempCbtFile = new File(bookTempFolder, bookFolder.getName() + ".cbt");

			if(bookFolder.getName().toUpperCase().startsWith("A"))
				continue;
			if(bookFolder.getName().toUpperCase().startsWith("C"))
				break;
			
			if(containsPngs(cbtFile) && !tempCbtFile.exists()) {
				System.out.println("Pngs found in " + cbtFile.getName());
				bookTempFolder.mkdirs();
				extractPngs(cbtFile, bookTempFolder);
				convertPngsToWebp(bookTempFolder);
				retarToCbt(bookTempFolder);
				deleteWebPs(bookTempFolder);
			} else {
				System.out.println("No pngs found in " + cbtFile.getName());
			}
		}
		
		
		
	}

	private static void deleteWebPs(File bookTempFolder) {
		for(File inputFile : bookTempFolder.listFiles()) {
			if(inputFile.getName().endsWith("webp")) {
				inputFile.delete();
			}
		}
	}

	private static void extractPngs(File cbtFile, File bookTempFolder) {
		// D:\Software\7za>7za e -oD:\scans\temp\Acquisition_of_Strategic_Knowledge\ D:\scans\books\Acquisition_of_Strategic_Knowledge\Acquisition_of_Strategic_Knowledge.cbt
		
		
		
		Process process;
	    try {
	    	ProcessBuilder pb = new ProcessBuilder( "D:\\software\\7za\\7za.exe", "e", "-o" + bookTempFolder.getAbsolutePath(),
	    			cbtFile.getAbsolutePath() );

	      process = pb.start();
	      
	      process.waitFor( 30, TimeUnit.MINUTES );
	      if ( process.exitValue() == 0 ) {
	        // Success
	        printProcessOutput( process.getInputStream(), System.out );
	      } else {
	        printProcessOutput( process.getErrorStream(), System.err );
	      }
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
		
		
	}

	private static void convertPngsToWebp(File bookFolder) {
		for(File inputFile : bookFolder.listFiles()) {
			if(inputFile.getName().endsWith("png")) {
				System.out.println("Converting " + inputFile.getName());
				File outputFile = new File(bookFolder, FilenameUtils.getBaseName(inputFile.getName()) + ".webp");
			    ImageWebpLibraryWrapper.convertToWebP(inputFile, outputFile);
				inputFile.delete();
			}
		}
	}

	private static void retarToCbt(File bookTempFolder) {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture  -ttar D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt D:\Scans\temp\3D_Game_Engine_Architecture\*.webp

		Process process;
	    try {
	    	ProcessBuilder pb = new ProcessBuilder( "D:\\software\\7za\\7za.exe", "a", "-ttar", "-o" + bookTempFolder.getAbsolutePath(),
	    			bookTempFolder.getAbsolutePath() + "\\" +bookTempFolder.getName() + ".cbt", bookTempFolder.getAbsolutePath() + "\\*.webp" );

	      process = pb.start();
	      
	      process.waitFor( 30, TimeUnit.MINUTES );
	      if ( process.exitValue() == 0 ) {
	        // Success
	        printProcessOutput( process.getInputStream(), System.out );
	      } else {
	        printProcessOutput( process.getErrorStream(), System.err );
	      }
	    } catch ( Exception e ) {
	      e.printStackTrace();
	    }
	}

	private static boolean containsPngs(File bookFile) throws IOException {
		boolean pngsFound = false;

		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(
				new BufferedInputStream(new FileInputStream(bookFile)))) {
			TarArchiveEntry entry;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (entry.getName().endsWith("png")) {
					pngsFound = true;
				}
			}
		}
		
		return pngsFound;
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
