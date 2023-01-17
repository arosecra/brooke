package org.github.arosecra.brooke.services;

public class BrookeService {



	// public void openVLC(String collectionName, String catalogName, String categoryName, String itemName, int index) throws IOException {
		// TODO Auto-generated method stub
		// ShelfItem shelfItem = this.getItemByName(collectionName, catalogName, categoryName, itemName);
		// File cacheFolder = new File("D:\\Library\\Cache");
		// File remoteFile = getRemoteFile(collectionName, catalogName, categoryName, itemName, index);
		// File cacheFile = new File(cacheFolder, remoteFile.getName());
		
		// String vlcCacheFilename = "file:///" + cacheFile.getAbsolutePath();
		// System.out.println(shelfItem.getFolder());
		
		// File vlcOptionsFile = new File(shelfItem.getFolder(), "vlcOptions.txt");
		// if(!shelfItem.getChildItems().isEmpty()) {
		// 	vlcOptionsFile = new File(shelfItem.getChildItems().get(index).getFolder(), "vlcOptions.txt");
		// }
		
		// List<String> vlcOptions = new ArrayList<>();
		// vlcOptions.add("cmd.exe");
		// vlcOptions.add("/C");
		// vlcOptions.add("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"");
		// vlcOptions.add(vlcCacheFilename);
		
		// if(vlcOptionsFile.exists()) {
		// 	List<String> lines = FileUtils.readLines(vlcOptionsFile);
		// 	vlcOptions.addAll(lines);
		// }
		
		// //:sub-track-id=
		// //:audio-track-id=
		// for(String s : vlcOptions)
		// 	System.out.println(s);
		
		// ProcessBuilder builder = new ProcessBuilder((String[]) vlcOptions.toArray(new String[vlcOptions.size()]));
		// builder.redirectErrorStream(true);
		// final Process process = builder.start();

		// // Watch the process
		// watch(process);
	// }
	
	// private static void watch(final Process process) {
	//     new Thread() {
	//         public void run() {
	//             BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	//             String line = null; 
	//             try {
	//                 while ((line = input.readLine()) != null) {
	//                     System.out.println(line);
	//                 }
	//             } catch (IOException e) {
	//                 e.printStackTrace();
	//             }
	//         }
	//     }.start();
	// }

	// public void copyForTablet(String collectionName, String catalogName, String categoryName, String itemName) throws IOException {
	// 	File tempSsdFolder = new File("C:\\scans\\temp");
	// 	File unzippedFolder = new File(tempSsdFolder, itemName);
	// 	unzippedFolder.mkdirs();
	// 	Collection collection = getCollectionByName(collectionName);
		
	// 	File remoteFolder = getRemoteItemFolder(collection, categoryName, itemName, 0);
	// 	File remoteFile = new File(remoteFolder, itemName + "_PNG.tar");
		
	// 	FileUtils.copyFileToDirectory(remoteFile, tempSsdFolder);
	// 	File localSourceFile = new File(tempSsdFolder, remoteFile.getName());
	// 	File localCbzFile = new File(tempSsdFolder.getAbsolutePath(), itemName + ".cbz");
		
	// 	CommandLine.run(new String[] {
	// 			"D:\\software\\7za\\7za.exe", 
	// 			"e", 
	// 			"-o" + unzippedFolder.getAbsolutePath(),
	// 			localSourceFile.getAbsolutePath()	
	// 	});
		
		
	// 	CommandLine.run(new String[] {
	// 			"D:\\software\\7za\\7za.exe", 
	// 			"a", 
	// 			"-tzip", 
	// 			"-o" + unzippedFolder.getAbsolutePath(),
	// 			localCbzFile.getAbsolutePath(), 
	// 			unzippedFolder.getAbsolutePath() + "\\*.png"		
	// 		});
		
	// 	FileUtils.copyFileToDirectory(localCbzFile, new File("\\\\drobo5n\\Public\\Scans\\ForTablet"));
	// 	FileUtils.deleteDirectory(unzippedFolder);
	// 	FileUtils.delete(localSourceFile);
	// 	FileUtils.delete(localCbzFile);
	// 	System.out.println("Done copying new CBZ to tablet sync directory");
	// }
	
	// public void createBoundingBoxPng() {
//		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//		g = image.createGraphics();  // not sure on this line, but this seems more right
//		g.setColor(Color.white);
//		g.fillRect(0, 0, 100, 100); // give the whole image a white background
//		g.setColor(Color.blue);
//		for( ..... ){
//		    g.fillRect(X , Y,  width , height );
//		        ....        
//		}
	// }
}