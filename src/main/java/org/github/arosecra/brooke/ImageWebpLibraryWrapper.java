package org.github.arosecra.brooke;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class ImageWebpLibraryWrapper {

  private static final String CWEBP_BIN_PATH = "C:\\Software\\libwebp\\bin\\cwebp";

  public static boolean isWebPAvailable() {
    if ( CWEBP_BIN_PATH == null ) {
      return false;
    }
    return new File( CWEBP_BIN_PATH ).exists();
  }

  public static boolean convertToWebP( File imageFile, File targetFile) {
    Process process;
    try {
      process = new ProcessBuilder( CWEBP_BIN_PATH, "-lossless", imageFile.getAbsolutePath(), "-o",
          targetFile.getAbsolutePath() ).start();
      process.waitFor( 30, TimeUnit.MINUTES );
      if ( process.exitValue() == 0 ) {
        // Success
        printProcessOutput( process.getInputStream(), System.out );
        return true;
      } else {
        printProcessOutput( process.getErrorStream(), System.err );
        return false;
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      return false;
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