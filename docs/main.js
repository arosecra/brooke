
// This is a small preprocessor that generates plant uml
// images and pulls in content from other files

var plantuml = require('node-plantuml');
var fs = require('fs');
var path = require('path')

//modified from https://stackoverflow.com/a/54387221, modification is for ignores
const getAllFiles = (base, ignores, dir) =>
	fs.readdirSync(dir).reduce((files, file) => {
		const name = path.join(dir, file);
		let fromBase = name.substring(base.length).replaceAll('\\', '/')
		const isDirectory = fs.statSync(name).isDirectory();
		if (isDirectory)
			fromBase = fromBase + '/'
		if (ignores.includes(fromBase))
			return [...files];
		return isDirectory ? [...files, ...getAllFiles(base, ignores, name)] : [...files, name];
	}, []);


function preProcess(sourceFiles, allAnchors) {
	sourceFiles.forEach((file) => {
		onPreProcessEachFile(allAnchors, file)
	})
}

function process(mdFiles, expandedAnchors) {
	mdFiles.forEach((file) => {
		onProcessEachFile(file, expandedAnchors)
	})
}

function expandAnchors(allAnchors) {
	let result = {}
	allAnchors.forEach((anchor) => {
		if(!result[anchor.name]) {
			result[anchor.name] = {
				anchors: []
			}
		}
		result[anchor.name].anchors.push(anchor)
	})



	return result
}

function onPreProcessEachFile(allAnchors, file) {
	let lines = fs.readFileSync(file)
		.toString()
		.replaceAll('\r\n', '\n')
		.replaceAll('\r', '\n')
		.split('\n');

	lines.forEach((line, idx) => {
		if (line.includes('//DOC')) {
			let options = line
				.replaceAll('[', ' ')
				.replaceAll(']', ' ')
				.replaceAll('=', ' ')
				.replaceAll(',', ' ')
				.split(' ')
			let anchor = {
				name: options[2],
				seq: options[4],
				opt: options[6],
				line: idx,
				file: file
			}
			allAnchors.push(anchor)

		}
	})
}

function onProcessEachFile(file, expandedAnchors) {
	let lines = fs.readFileSync(file)
		.toString()
		.replaceAll('\r\n', '\n')
		.replaceAll('\r', '\n')
		.split('\n');

		const outLines = [];

		let pumlOptions = {
			filename: '',
			lines: []
		}
		
		for(let line of lines) {
			if(line.includes('@startuml')) {
				pumlOptions.filename = line.split(' ')[1] + '.svg'
				pumlOptions.lines.push(line)
			} else if(line.includes('@enduml')) {
				pumlOptions.lines.push(line)
		
				let pumlLines = pumlOptions.lines.join('\n')
		
				var gen = plantuml.generate(pumlLines, {
					format: 'svg'
				});
		
				gen.out.pipe(fs.createWriteStream('../wiki/'+pumlOptions.filename));
		
				pumlOptions = {
					filename: '',
					lines: []
				}
			} else if(pumlOptions.lines.length > 0) {
				pumlOptions.lines.push(line)
			} else if(line.includes('@LINK')) {
				//look up the link content from other files
			}
		
			outLines.push(line);
		}
		fs.writeFileSync('../docs/'+file, outLines.join('\n'))
}

const gitIgnores = fs.readFileSync("..\\.gitignore")
	.toString()
	.replaceAll('\r\n', '\n')
	.replaceAll('\r', '\n')
	.split('\n');

let sourceFiles =
	getAllFiles('../', gitIgnores, '../angular/')
		.concat(getAllFiles('../', gitIgnores, '../electron/'))
		.concat(getAllFiles('../', gitIgnores, '../java/'))

let mdFiles =
	getAllFiles('', [], '.')

let allAnchors = []
//lookup DOC anchors from source files
preProcess(sourceFiles, allAnchors)
let expandedAnchors = expandAnchors(allAnchors)
console.log(mdFiles)
process(mdFiles, expandedAnchors)