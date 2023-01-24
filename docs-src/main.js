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
		if (!result[anchor.name]) {
			result[anchor.name] = {
				seq: {},
			}
		}

		if (!result[anchor.name][anchor.file]) {
			result[anchor.name].seq[anchor.file] = {seq: anchor.seq, file: anchor.file}
			result[anchor.name][anchor.file] = {
				anchors: []
			}
		}

		let currentSeq = result[anchor.name].seq[anchor.file]
		if(parseInt(currentSeq) > parseInt(anchor.seq))
			result[anchor.name].seq[anchor.file] = {seq: anchor.seq, file: anchor.file}

		if (anchor.opt === '//start') {
			let smAnchor = {
				start: anchor.line
			}
			result[anchor.name][anchor.file].anchors.push(smAnchor)
		} else if (anchor.opt === '//stop') {
			let len = result[anchor.name][anchor.file].anchors.length
			let smAnchor = result[anchor.name][anchor.file].anchors[len - 1]
			smAnchor.stop = anchor.line
		} else if (anchor.opt === '//...') {
			let smAnchor = {
				elipse: true
			}
			result[anchor.name][anchor.file].anchors.push(smAnchor)
		}

	})

	Object.keys(result).forEach((resultKey) => {
		Object.keys(result[resultKey]).filter(key => key !== 'seq').forEach((anchorFilename) => {
			let lines = fs.readFileSync(anchorFilename)
				.toString()
				.replaceAll('\r\n', '\n')
				.replaceAll('\r', '\n')
				.split('\n');

			result[resultKey][anchorFilename].content = []
			result[resultKey][anchorFilename].anchors.forEach((anchor) => {
				if(anchor.elipse) {
					result[resultKey][anchorFilename].content.push('...')
				} else {
					// console.log(lines.slice(anchor.start+1, anchor.stop))
					result[resultKey][anchorFilename].content = result[resultKey][anchorFilename].content.concat(
						lines.slice(anchor.start+1, anchor.stop)
					)
				}
			})

		})
	})

	Object.keys(result).forEach((resultKey) => {
		let sortedSequenceFiles = Object.values(result[resultKey].seq).sort((left, right) => {
			if(parseInt(left) < parseInt(right))
				return -1
			else if(parseInt(left) > parseInt(right))
				return 1
			else
				return 0
		})
		result[resultKey].content = []
		sortedSequenceFiles.forEach((seqFile) => {
			result[resultKey].content.push('```' + seqFile.file.substring(seqFile.file.lastIndexOf('.')+1))
			result[resultKey].content = result[resultKey].content.concat(
				result[resultKey][seqFile.file].content
			)
			result[resultKey].content.push('```')
		})
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

	let outLines = [];

	let pumlOptions = {
		filename: '',
		lines: []
	}

	for (let line of lines) {
		let appendLine = true
		if (line.includes('@startuml')) {
			pumlOptions.filename = line.split(' ')[1] + '.svg'
			pumlOptions.lines.push(line)
		} else if (line.includes('@enduml')) {
			pumlOptions.lines.push(line)

			let pumlLines = pumlOptions.lines.join('\n')

			var gen = plantuml.generate(pumlLines, {
				format: 'svg'
			});

			gen.out.pipe(fs.createWriteStream('../docs/' + pumlOptions.filename));

			pumlOptions = {
				filename: '',
				lines: []
			}
		} else if (pumlOptions.lines.length > 0) {
			pumlOptions.lines.push(line)
		} else if (line.includes('@LINK')) {
			if(line.includes('\\\\@LINK')) {
				line = line.replaceAll('\\\\@LINK', '@LINK')
			} else {
				//look up the link content from other files
				
				outLines = outLines.concat(
					expandedAnchors[line.split(' ')[1]].content
				)
				appendLine = false

			}
		}

		if(appendLine)
			outLines.push(line);
	}
	fs.writeFileSync('../docs/' + file.substring('src\\'.length), outLines.join('\n'))
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
	getAllFiles('', [], 'src/')

let allAnchors = []
//lookup DOC anchors from source files
preProcess(sourceFiles, allAnchors)
let expandedAnchors = expandAnchors(allAnchors)
// console.log(expandedAnchors)
process(mdFiles, expandedAnchors)