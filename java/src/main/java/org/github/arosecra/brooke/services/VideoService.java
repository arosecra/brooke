package org.github.arosecra.brooke.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.github.arosecra.brooke.model.ItemLocation;
import org.github.arosecra.brooke.model.Library;
import org.github.arosecra.brooke.model.api.ItemApiModel;
import org.github.arosecra.brooke.model.api.VlcOptionsApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoService {

	@Autowired
	private LibraryLocationService libraryLocationService;

	@Autowired
	private FileCacheService fileCacheService;

	public void openVLC(Library library, String collectionName, String itemName) throws IOException {
		File cacheFile = fileCacheService.getCachedFile(library, collectionName, itemName);
		
		VlcOptionsApiModel vlcOptions = null;
		ItemLocation itemLocation = this.libraryLocationService.getItemLocation(library, collectionName, itemName);
		ItemApiModel item = this.libraryLocationService.getItemByLocation(library, itemLocation);

		if(item.getVlcOptions() != null) {
			vlcOptions = item.getVlcOptions();
		} else if(itemLocation.getSeriesName() != null) {
			ItemApiModel series = this.libraryLocationService.getSeriesByLocation(library, itemLocation);
			if(series.getVlcOptions() != null) {
				vlcOptions = series.getVlcOptions();
			}
		}
		
		String vlcCacheFilename = "file:///" + cacheFile.getAbsolutePath();
		
		List<String> vlcOptionArgs = new ArrayList<>();
		vlcOptionArgs.add("cmd.exe");
		vlcOptionArgs.add("/C");
		vlcOptionArgs.add("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"");
		vlcOptionArgs.add(vlcCacheFilename);

		if(vlcOptions != null) {
			if(vlcOptions.getAudioTrack() > 0)
				vlcOptionArgs.add(":audio-track-id="+vlcOptions.getAudioTrack());
			if(vlcOptions.getSubtitleTrack() > 0)
				vlcOptionArgs.add(":sub-track-id="+vlcOptions.getSubtitleTrack());
		}
		
		ProcessBuilder builder = new ProcessBuilder((String[]) vlcOptionArgs.toArray(new String[vlcOptionArgs.size()]));
		builder.redirectErrorStream(true);
		final Process process = builder.start();

		// Watch the process
		watch(process);
	}
	
	private static void watch(final Process process) {
	    new Thread() {
	        public void run() {
	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null; 
	            try {
	                while ((line = input.readLine()) != null) {
	                    System.out.println(line);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}
}
