
const child_process = require('child_process');
var http = require('http');
const fetch = require('node-fetch');

module.exports = {
	start,
	stop
}

function start(options, showApplication, showStartUpErrorMessage) {
	console.log('spawning process')
	var filename = options.filename;
	console.log('Loading win32 java');
	console.log(filename)
	var java_arguments = [
		'-jar', 
		/*'-Dserver.port=' + options.port,*/ 
		filename
	];

	// if (options.platform === 'win32') {
	let child = child_process.spawn('java', java_arguments, {
		cwd: options.cwd
	}).on('error', function (code, signal) {
		'+ path.sep +'
		showStartUpErrorMessage();
	});

	childProcessSetup(child, options, showApplication);


	// } else if (options.platform === 'darwin') {
	//     child_process.exec('chmod +X ' + app.getAppPath() + '/java/' + jreFolder + '/Contents/Home/bin/' + 'java');
	//     if (!app.getAppPath().startsWith("/Applications/")) {
	//         dialog.showMessageBox(null, {
	//             type: 'error'
	//             , buttons: [i18n.__('ok')]
	//             , title: i18n.__('wrong-directory')
	//             , message: i18n.__('wrong-directory-long')
	//         });
	//         app.quit();
	//         return null;
	//     }
	//     return child_process.spawn(jreFolder + '/Contents/Home/bin/java', ['-jar', '-Dvaadin.productionMode=true', '-Dserver.port=' + port, filename, '--logging.file=application.log'], {
	//         cwd: app.getAppPath() + '/java/'
	//     }).on('error', function (code, signal) {
	//         showStartUpErrorMessage();
	//     });
	// } else if (options.platform === 'linux') {
	//     return child_process.spawn(jreFolder + '/bin/java', ['-jar', '-Dvaadin.productionMode=true', '-Dserver.port=' + port, filename, '--logging.file=application.log'], {
	//         cwd: app.getAppPath() + '/java/'
	//     }).on('error', function (code, signal) {
	//         showStartUpErrorMessage();
	//     });
	// } else {
	//     throw new Error("Platform not supported");
	// }
	return child;
}

function childProcessSetup(child, options, showApplication) {
	child.stdout.setEncoding('utf8');
	child.stdout.on('data', function (data) {
		//call the success callback
		if (data.toString().includes('Started BrookeApplication'))
			showApplication(options.url);

		process.stdout.write('' + data.toString());
	});

	child.stderr.setEncoding('utf8');
	child.stderr.on('data', function (data) {
		process.stdout.write('' + data.toString());
	});

	child.on('close', function (code) {
		console.log('closing code: ' + code);
	});
}

function stop(serverProcess, successCallback) {
	// these are suggestions from stack overflow
	// but they don't work! at least they didn't when testing
	// child_process.spawn("taskkill", ["/pid", serverProcess.pid, '/f', '/t']);
	// serverProcess.kill('SIGINT');
	// 	setTimeout(function() {
	//     serverProcess.kill();
	//     }, 5000
	// );

	fetch('http://localhost:8080/actuator/shutdown', {
    method: 'POST',
    body: '{}',
    headers: {
        'Content-Type': 'application/json'
    }
})
    .then(json => {successCallback()})
    .catch (err => {})
}