import { RootFolder } from "./src/model/root-folder";
import * as fs from 'fs';
import * as path from 'path';
import * as child_process from 'child_process';

const lightNovels = new RootFolder(
  "Light Novels",
  "\\\\syn01\\syn01public\\Scans\\Light_Novels_Repository"
);
const fiction = new RootFolder(
  "Fiction",
  "\\\\syn01\\syn01public\\Scans\\Fiction_Repository"
);
const nonfiction = new RootFolder(
  "Non Fiction",
  "\\\\syn01\\syn01public\\Scans\\NonFiction_Repository"
);

const folders = [lightNovels, fiction, nonfiction];
const dimensions: any[] = [];

folders.forEach((rootFolder) => {
rootFolder.itemFolders.forEach((itemFolder) => {
	const thumb = path.join(itemFolder, 'thumbnail.png');
	if(fs.existsSync(thumb)) {
		const dimensionsBuffer = child_process.execFileSync(
			'D:\\Software\\ImageMagick\\magick.exe',
			[ thumb, '-auto-orient', '-format', '{"w": %w, "h": %h, "o": "%[orientation]"}', 'info:']
		);
		const dimensionsString = String(dimensionsBuffer);
		console.log(thumb, dimensionsString);

		const d = JSON.parse(dimensionsString);
		d.thumb = thumb;
		dimensions.push(d);
	}
});
});

const sorted = dimensions.sort((a, b) => b.h - a.h);
const buckets: Record<number, number> = {}
sorted.forEach((d) => {
	if(!buckets[d.h]) buckets[d.h] = 0;
	buckets[d.h]++;
})

console.log(buckets)
console.log(sorted[0])