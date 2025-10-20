package org.github.arosecra.book.pipeline.book.steps;

import java.io.IOException;

import org.github.arosecra.book.pipeline.model.JobFolder;
import org.github.arosecra.book.pipeline.model.JobStep;
import org.github.arosecra.brooke.util.CommandLine;

public class TarToCbtStep implements JobStep {
	
	
	@Override
	public void execute(JobFolder job) throws IOException {
		// D:\Software\7za>7za a -oD:\scans\temp\3D_Game_Engine_Architecture  -ttar D:\scans\temp\3D_Game_Engine_Architecture\3D_Game_Engine_Architecture.cbt D:\Scans\temp\3D_Game_Engine_Architecture\*.webp
		CommandLine.run(new String[] {
			"D:\\software\\7za\\7za.exe", 
			"a", 
			"-ttar", 
			"-o" + job.destFolder.getAbsolutePath(),
    		job.destFolder.getAbsolutePath() + "\\" + job.workFolder.getName() + ".cbt", 
    		job.destFolder.getAbsolutePath() + "\\*"		
		});
	}
	
}