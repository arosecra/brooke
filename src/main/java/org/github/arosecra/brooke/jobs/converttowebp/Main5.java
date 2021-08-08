package org.github.arosecra.brooke.jobs.converttowebp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

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
		File tempSsdFolder = new File("C:\\scans\\temp");
		
		for(String arg : args)
			processBooks(booksFolder, tempFolder, tempSsdFolder, arg);
		
		
		
	}

	private static void processBooks(File booksFolder, File tempFolder, File tempSsdFolder, String arg) throws IOException {
		for(File bookFolder : booksFolder.listFiles()) {
			File cbtFile = new File(bookFolder, bookFolder.getName() + ".cbt");
			File bookTempSsdFolder = new File(tempSsdFolder, bookFolder.getName());
			File bookTempFolder = new File(tempFolder, bookFolder.getName());
			File tempCbtFile = new File(bookTempFolder, bookFolder.getName() + ".cbt");
			
			if(!bookFolder.getName().toUpperCase().startsWith(arg.toUpperCase()))
				continue;
			
			if(containsPngs(cbtFile) && !tempCbtFile.exists()) {
				System.out.println("Pngs found in " + cbtFile.getName());
				bookTempSsdFolder.mkdirs();
				extractPngs(cbtFile, bookTempSsdFolder);
				convertPngsToWebp(bookTempSsdFolder);
				retarToCbt(bookTempSsdFolder, bookTempFolder);
				deleteWebPs(bookTempSsdFolder);
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
		bookTempFolder.delete();
	}

	private static void extractPngs(File cbtFile, File bookTempFolder) {
		// D:\Software\7za>7za e -oD:\scans\temp\Acquisition_of_Strategic_Knowledge\ D:\scans\books\Acquisition_of_Strategic_Knowledge\Acquisition_of_Strategic_Knowledge.cbt
		
		if(ArrayUtils.isEmpty(bookTempFolder.listFiles())) {
		
		
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
	}

	private static void convertPngsToWebp(File bookFolder) {
		File[] files = bookFolder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("png");
			}
		});
		if(files != null) {
			for(int i = 0; i < files.length; i++) {
				File inputFile = files[i];
				System.out.print("Converting " + bookFolder.getName() + " (" + i + " of " + files.length + ") ");
				long start = System.currentTimeMillis();
				File outputFile = new File(bookFolder, FilenameUtils.getBaseName(inputFile.getName()) + ".webp");
			    ImageWebpLibraryWrapper.convertToWebP(inputFile, outputFile);
			    long end = System.currentTimeMillis();
			    System.out.println("... " + (end - start) + " milliseconds");
				inputFile.delete();
			}
		}
	}

	private static void retarToCbt(File bookTempFolder, File tempFolder) {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture  -ttar D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt D:\Scans\temp\3D_Game_Engine_Architecture\*.webp

		Process process;
	    try {
	    	ProcessBuilder pb = new ProcessBuilder( "D:\\software\\7za\\7za.exe", "a", "-ttar", "-o" + tempFolder.getAbsolutePath(),
	    			tempFolder.getAbsolutePath() + "\\" + tempFolder.getName() + ".cbt", bookTempFolder.getAbsolutePath() + "\\*.webp" );

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
