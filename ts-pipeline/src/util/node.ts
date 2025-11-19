
import * as child_process from 'child_process';
import * as path from 'path';
import * as fs from 'fs';

export class node {
	static rm(path: string) {
		if(fs.existsSync(path)) fs.rmSync(path)
	}
	static rmdir(path: string) {
		if(fs.existsSync(path)) fs.rmdirSync(path, { recursive: true })
	}

	static execFileSync(file: string, args: string[], opts?: any) {
		const dir = opts?.cwd ? ` in ${opts.cwd}` : '';
		const cmd = `Running> ${file} ${args.join(' ')}${dir}`

		console.log(cmd);
		return String(child_process.execFileSync(file, args, opts));
	} 

	static fsMove(source: string, dest: string) {
		fs.copyFileSync(source, dest);
		fs.rmSync(source);
	}

	static mkdirs(source: string) {
		fs.mkdirSync(source, { recursive: true });
	}

	static pathJoin(...paths: string[]) {
		return path.join(...paths);
	}
}
