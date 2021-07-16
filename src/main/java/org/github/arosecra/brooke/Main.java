//package org.github.arosecra.brooke;
//
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//
//import javax.imageio.ImageIO;
//
//import org.apache.commons.compress.archivers.ArchiveEntry;
//import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
//import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
//import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
//import org.apache.commons.io.IOUtils;
//
//public class Main {
//	public static void main(String[] args) throws Exception {
//		//TODO - no need to deal with the tarball mechanics. expand the tarball if necessary, then create the files & re-tar
//		//       although it might take longer to do, this should be a bit easier to deal with. can probably bake it into the deskew
//		extractTarGZ("D:\\Scans\\3d game engine architecture\\3d game engine architecture.cbt");
//		
//		createThumbnail("D:\\Scans\\3d game engine architecture\\3d game engine architecture\\3d game engine architecture_Page_001.png");
//		createThumbnail("D:\\Scans\\without remorse\\without remorse\\without remorse_Page_001.png");
//	}
//	
//	public static void extractTarGZ(String filename) throws IOException {
//		TarballMetaData metadata = getTarballMetaData(filename);
//	    if(!metadata.hasThumbnail) {
//	    	BufferedImage thumbnail = createThumbnailFromTar(filename);
//	    	Map<File, BufferedImage> filesToArchive = new TreeMap<>();
//	    	if(!metadata.hasMetaDataFolder) {
//	    		File m = new File(".metadata/");
//	    		filesToArchive.put(m, null);
//	    	}
//	    	filesToArchive.put(new File(new File(filename).getParentFile().getName()), thumbnail);
//	    	addFiles(filename, filesToArchive);
//	    	//TODO - now that we have the thumbnail, let's add it to the archive
//	    }
//	}
//	
//	private static void addFiles(String filename, Map<File, BufferedImage> filesToArchive) {
//		try (TarArchiveOutputStream o = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filename))))) {
//		    for (Entry<File, BufferedImage> mapEntry : filesToArchive.entrySet()) {
//		        // maybe skip directories for formats like AR that don't store directories
//		    	ArchiveEntry entry = o.createArchiveEntry(mapEntry.getKey(), mapEntry.getKey().getName());
//		        // potentially add more flags to entry
//		        o.putArchiveEntry(entry);
//		        if (mapEntry.getKey().isFile()) {
//		            try (InputStream i = Files.newInputStream(f.toPath())) {
//		                IOUtils.copy(i, o);
//		            }
//		        }
//		        o.closeArchiveEntry();
//		    }
//		    o.finish();
//		}
//	}
//
//	private static class TarballMetaData {
//		boolean hasMetaDataFolder;
//		boolean hasThumbnail;
//	}
//
//	private static BufferedImage createThumbnailFromTar(String filename) throws IOException {
//		BufferedImage cover = null;
//		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(new File(filename))))) {
//	        TarArchiveEntry entry;
//	        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
//	        	System.out.println(entry.getName());
//	        	if(!entry.getName().startsWith(".metadata") && cover == null) {
//	        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        		IOUtils.copy(tarIn, baos);
//	        		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//	        		cover = ImageIO.read(bais);
//	        	}
//	            /** If the entry is a directory, create the directory. **/
////	            if (entry.isDirectory()) {
////	                File f = new File(entry.getName());
////	                boolean created = f.mkdir();
////	                if (!created) {
////	                    System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
////	                            f.getAbsolutePath());
////	                }
////	            } else {
////	                int count;
////	                byte data[] = new byte[BUFFER_SIZE];
////	                FileOutputStream fos = new FileOutputStream(entry.getName(), false);
////	                try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
////	                    while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
////	                        dest.write(data, 0, count);
////	                    }
////	                }
////	            }
//	        }
//	    }
//		return createThumbnail(cover);
//	}
//
//	private static TarballMetaData getTarballMetaData(String filename) throws IOException, FileNotFoundException {
//		TarballMetaData result = new TarballMetaData();
//		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(new File(filename))))) {
//	        TarArchiveEntry entry;
//	        while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
//	        	System.out.println(entry.getName());
//	        	if(entry.getName().equals(".metadata/thumbnail.png"))
//	        		result.hasThumbnail = true;
//	        	if(entry.isDirectory() && entry.getName().equals(".metadata/"))
//	        		result.hasMetaDataFolder = true;
//	        }
//	    }
//		return result;
//	}
//	
//	public static BufferedImage createThumbnail(BufferedImage input) {
//		int width = input.getWidth();
//		int height = input.getHeight();
//		
//		int newWidth = 250;
//		
//		int reduction = width / 250;
//		int newHeight = height / reduction;
//		
//		
//		BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//		thumbnail.createGraphics().drawImage(input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),0,0,null);
//		return thumbnail;
//	}
//	
//	
//	public static void createThumbnail(String filename) throws Exception {
//		File file = new File(filename);
//		BufferedImage input = ImageIO.read(file);
//		BufferedImage thumbnail = createThumbnail(input);
//		ImageIO.write(thumbnail, "png", new File(file.getParentFile().getName() + ".png"));
//	}
//}
