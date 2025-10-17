package org.github.arosecra.book.pipeline.model;

import java.util.List;

public record RootFolder (String rootFolder, List<RemoteFolder> remoteFolders) {
	
}