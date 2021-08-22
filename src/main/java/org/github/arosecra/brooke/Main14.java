package org.github.arosecra.brooke;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Main14 {
	public static void main(String[] args) throws Exception {
		
		File remoteVideoRepository = new File("\\\\drobo5n2\\Public\\MkvVideos");
		
		File localVideoCollection = new File("D:\\Library\\Collections\\Videos");
		
		
		
		File localAnime = new File(localVideoCollection, "Anime");
		for(File folder : new File(remoteVideoRepository, "Anime_TV").listFiles()) {
			File seriesFile = new File(localAnime, folder.getName() + ".txt");
			FileUtils.write(seriesFile, folder.getName(), StandardCharsets.UTF_8);
			System.out.println(folder.getName());
		}
		
		List<String> animeMovieLines = new ArrayList<>();
		File animeMoviesCatagory = new File(localAnime, "Movies.txt");
		for(File folder : new File(remoteVideoRepository, "Anime_Movies").listFiles()) {
			animeMovieLines.add(folder.getName());
			System.out.println(folder.getName());
		}
		FileUtils.writeLines(animeMoviesCatagory, animeMovieLines);
		
		
	}
}
