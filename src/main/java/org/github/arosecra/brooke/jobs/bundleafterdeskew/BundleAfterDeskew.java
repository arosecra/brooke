package org.github.arosecra.brooke.jobs.bundleafterdeskew;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;


/*
 * if video:

if book:
	extract pngs (pdf to RAW cbt)
	deskew       (RAW cbt to PNG cbt)
	extract thumbnail (RAW cbt first page)
	convert to webp (PNG cbt to cbt)

if light_novel:
	extract pngs (pdf to RAW cbt)
	extract thumbnail (RAW cbt first page)
	deskew       (RAW cbt to PNG cbt)
	convert to webp (PNG cbt to cbt)

if graphic_novel:
	extract pngs (pdf to RAW cbt)
	extract thumbnail (RAW cbt first page)
	convert to webp (RAW cbt to cbt)

 */
public class BundleAfterDeskew {
	public static void main(String[] args) {
		
		File basedir = new File("D:\\scans\\tobeexported");
		
		for(File bookfolder : basedir.listFiles()) {
			String name = bookfolder.getName();
			File rawCbt = new File(bookfolder, name+"_RAW.cbt");
			File cbt = new File(bookfolder, name+".cbt");
			
			File pngFolder = new File(bookfolder, "pngs");
			File deskewFolder = new File(bookfolder, "deskew");
			
			if(!rawCbt.exists() && pngFolder.exists()) {
				tarToCbt(pngFolder, rawCbt);
			}
			if(!cbt.exists() && deskewFolder.exists()) {
				tarToCbt(deskewFolder, cbt);
			}
		}
		
		
	}
	
	


	private static void tarToCbt(File folder, File output) {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture  -ttar D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt D:\Scans\temp\3D_Game_Engine_Architecture\*.webp

		Process process;
	    try {
	    	ProcessBuilder pb = new ProcessBuilder( "D:\\software\\7za\\7za.exe", 
	    			"a", 
	    			"-ttar", 
	    			"-o" + folder.getAbsolutePath(),
	    			output.getAbsolutePath(), 
	    			folder.getAbsolutePath() + "\\*.png" );

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
