
import * as fs from "fs";
import * as path from "path";

export class RootFolder {

	itemFolders: string[] = []

	constructor(
		public rootFolder: string,
		public remoteFolder: string,
		public assigned: boolean = false,
		public complete: boolean = false
	) {
		this.itemFolders = this.getLeafFolders(rootFolder);
	}

	private getLeafFolders(folder: string): string[] {
		let dirs = fs
			.readdirSync(folder, {
				withFileTypes: true,
				recursive: true,
			})
			.filter((file) => file.isDirectory())
			.filter((file) => {
				//check if the directory has directories
				let p = path.join(file.parentPath, file.name);
				let hasChildDirs = fs
					.readdirSync(p, { withFileTypes: true })
					.some((childFile) => childFile.isDirectory());
				return !hasChildDirs;
			})
			.map((file) => path.join(file.parentPath, file.name));
	
		return dirs;
	}
}