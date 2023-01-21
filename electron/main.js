const {
	app, BrowserWindow, dialog
} = require('electron');
const decompress = require('decompress');
const child_process = require('child_process');
const requestPromise = require('minimal-request-promise');
const getPort = require('get-port')

const springBootServer = require('./spring-boot-server');

let i18n;
const path = require('path');
const fs = require("fs");
let mainWindow = null;
let loading = null;
let serverProcess = null;
let allowClose = false;
let port = null;
const jreFolder = 'jdk8u265-b01-jre';

function error_log(exception) {
	fs.appendFile('error.log', exception.stack + "\n", (err) => {
		if (err) throw err;
	});
}

try {
	console.log('getting lock');
	const gotTheLock = app.requestSingleInstanceLock();
	console.log('got lock, continuing');

	const showApplication = function (appUrl) {
		mainWindow = new BrowserWindow({
			title: 'Brooke'
			, show: false
			, width: 1200
			, height: 800
			, frame: true
			,
		});
		mainWindow.setMenu(null);
		mainWindow.loadURL(appUrl);
		mainWindow.once('ready-to-show', () => {
			loading.hide();
			mainWindow.show();
		});
		mainWindow.on('closed', function () {
			mainWindow = null;
			app.quit();
		});

		mainWindow.on('close', function (e) {
			if (serverProcess && !allowClose) {
				dialog.showMessageBox(this, {
					type: 'question'
					, buttons: ['yes', 'no']
					, title: 'confirm'
					, message: "are-you-sure-you-want-to-quit"
				}).then(result => {
					if (result.response === 0) {
						allowClose = true;
					}
				});
				e.preventDefault();
			}
		});
		if(!app.isPackaged)
			mainWindow.webContents.openDevTools();
	};

	const focusSecondInstance = function () {
		app.on('second-instance', (event, commandLine, workingDirectory) => {
			if (mainWindow) {
				if (mainWindow.isMinimized()) {
					mainWindow.restore();
				}
				mainWindow.focus();
			}
		})
	};

	const showStartUpErrorMessage = function () {
		setTimeout(function () {
			dialog.showMessageBox(null, {
				type: 'error'
				, buttons: ['ok']
				, title: "java-runtime-not-available"
				, message: "java-runtime-not-available-long"
			});
		}, 200);
	}

	const showLoadingScreen = function () {
		loading = new BrowserWindow({
			show: true
			, frame: false
			, width: 500
			, height: 280
		});
		loading.loadURL('file://' + app.getAppPath() + '/loading.html');
	};

	const beginStartUp = function () {
		console.log('beginning start up');
		(async () => {
			try {
				console.log('trying to begin startup')
				port = await getPort();
				serverProcess = springBootServer.start(
					{
						url: `http://localhost:${port}/`,
						port: port,
						filename: `brooke.war`,
						platform: process.platform,
						cwd: process.resourcesPath
					},
					showApplication,
					showStartUpErrorMessage
				)
			} catch (e) {
				error_log(e);
			}
		})();
	}

	if (!gotTheLock) {
		app.quit()
	} else {
		focusSecondInstance();
		app.on('window-all-closed', function () {
			app.quit();
		});

		app.on('ready', function () {
			// i18n = new (require('./translations/i18n'));
			try {
				showLoadingScreen();
				beginStartUp();
			} catch (e) {
				error_log(e);
			}
		});
		app.on('will-quit', function () {

			console.log('stopping spring will-quit');
			springBootServer.stop(serverProcess, port, function () { });
			serverProcess.kill('SIGINT');
		});
	}
} catch (e) {
	error_log(e);
}
