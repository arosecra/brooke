package org.github.arosecra.brooke;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Main7 {
	
	public static void main(String[] args) throws Exception {
		File baseDir = new File("\\\\drobo5n\\public\\Scans\\books");
		
		Map<String, Long> rawFileSizesToChar = new TreeMap<>();
		Map<String, Long> cbtFileSizesToChar = new TreeMap<>();
		
		
		
		for(File bookfolder : baseDir.listFiles()) {
		
			
			File rawFile = new File(bookfolder, bookfolder.getName() + "_RAW.cbt");
			File cbtFile = new File(bookfolder, bookfolder.getName() + ".cbt");
			
			String c = ""+bookfolder.getName().charAt(0);
			
			if(!rawFileSizesToChar.containsKey(c)) {
				rawFileSizesToChar.put(c, 0L);
				cbtFileSizesToChar.put(c, 0L);
			}
			cbtFileSizesToChar.put(c, cbtFileSizesToChar.get(c) + cbtFile.length());
			rawFileSizesToChar.put(c, rawFileSizesToChar.get(c) + rawFile.length());
		}
		
		long gbInBytes = 1024 * 1024;
		long totalRaw = 0L;
		long totalCbt = 0L;
		for(Map.Entry<String, Long> entry : rawFileSizesToChar.entrySet()) {
			totalRaw+=entry.getValue();
			totalCbt+=cbtFileSizesToChar.get(entry.getKey());
			
			double raw = entry.getValue() / gbInBytes;
			double cbt = cbtFileSizesToChar.get(entry.getKey()) / gbInBytes;
			System.out.println(entry.getKey() + " " + raw + " " + cbt + " " + (int)(((double)cbt / (double)raw) * 100) + "%");
		}
		double totalRawGbt = totalRaw / gbInBytes;
		double totalCbtGbt = totalCbt / gbInBytes;
		System.out.println("total " + totalRawGbt + " " + totalCbtGbt + " " + (int)(((double)totalCbtGbt / (double)totalRawGbt) * 100) + "%");
	}
	
	
}
