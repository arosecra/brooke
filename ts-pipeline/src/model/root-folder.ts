

export class RootFolder {

	constructor(
		public rootFolder: string,
		public itemFolders: string[],
		public assigned: boolean = false,
		public complete: boolean = false
	) {}
}