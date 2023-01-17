const {
  app, BrowserWindow, dialog
} = require('electron');
const decompress = require('decompress');
const child_process = require('child_process');
const requestPromise = require('minimal-request-promise');
let i18n;
const path = require('path');
const fs = require("fs");
let mainWindow = null;
let loading = null;
let serverProcess = null;
let allowClose = false;
const jreFolder = 'jdk8u265-b01-jre';

function error_log(exception) {
  fs.appendFile('error.log', exception.stack + "\n", (err) => {
      if (err) throw err;
  });
}

try {
  const gotTheLock = app.requestSingleInstanceLock();

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
                      app.quit();
                  }
              });
              e.preventDefault();
          }
      });
      mainWindow.webContents.openDevTools();
  };
  const awaitStartUp = function (appUrl, callback) {
      requestPromise.get(appUrl).then(function (response) {
          callback();
      }, function (response) {
          setTimeout(function () {
              awaitStartUp(appUrl, callback);
          }, 200);
      });
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
  const getJavaFile = function () {
      // var files = fs.readdirSync(app.getAppPath() + "/java/");
      var filename = 'brooke.war';
      // for (var i in files) {
      //     if (path.extname(files[i]) === ".war") {
      //         filename = path.basename(files[i]);
      //         break;
      //     }
      // }
      if (!filename) {
          throw new Error("There is no JAR file in ./java/ !");
      }
      return filename;
  };
  const showStartUpErrorMessage = function () {
      setTimeout(function () {
          dialog.showMessageBox(null, {
              type: 'error'
              , buttons: [i18n.__('ok')]
              , title: i18n.__("java-runtime-not-available")
              , message: i18n.__("java-runtime-not-available-long")
          });
      }, 200);
  }
  const spawnServerProcess = function (port) {
      console.log('spawning process')
      var filename = getJavaFile();
      platform = process.platform;
      console.log(platform);
      // if (platform === 'win32') {
          console.log('Loading win32 java');
          console.log(app.getAppPath() + path.sep + 'java' + path.sep)
          console.log(filename)
          //jreFolder + path.sep + 'bin' + path.sep + 'java'
          let child = child_process.spawn('java', ['-jar', /*'-Dserver.port=' + port,*/ filename], {
              cwd: 'D:/Projects/brooke/java/target/'//app.getAppPath() + path.sep + 'java' + path.sep
          }).on('error', function (code, signal) {
              '+ path.sep +'
              showStartUpErrorMessage();
          });

          // let scriptOutput = ''
          child.stdout.setEncoding('utf8');
          child.stdout.on('data', function(data) {
              //Here is where the output goes
              if(data.toString().includes('Started BrookeApplication'))
                showApplication('http://localhost:8080/');
              
                process.stdout.write('stdout: ' + data.toString());
          
              // data=data.toString();
              // scriptOutput+=data;
          });
          
          child.stderr.setEncoding('utf8');
          child.stderr.on('data', function(data) {
              //Here is where the error output goes
          
              process.stdout.write('stderr: ' + data.toString());
          
              // data=data.toString();
              // scriptOutput+=data;
          });
          
          child.on('close', function(code) {
              //Here you can get the exit code of the script
          
              console.log('closing code: ' + code);
          
              // console.log('Full output of script: ',scriptOutput);
          });

          return child;
      // } else if (platform === 'darwin') {
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
      // } else if (platform === 'linux') {
      //     return child_process.spawn(jreFolder + '/bin/java', ['-jar', '-Dvaadin.productionMode=true', '-Dserver.port=' + port, filename, '--logging.file=application.log'], {
      //         cwd: app.getAppPath() + '/java/'
      //     }).on('error', function (code, signal) {
      //         showStartUpErrorMessage();
      //     });
      // } else {
      //     throw new Error("Platform not supported");
      // }
  };
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
              const port = 8080; //determine port here
              serverProcess = spawnServerProcess(port);
              var appUrl = "http://localhost:" + port;
              console.log('waiting for startup')
              // awaitStartUp(appUrl, function () {
              //     console.log('showing app')
              //     showApplication(appUrl);
              // });
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
              platform = process.platform;
              const jrePath = app.getAppPath() + path.sep + 'java' + path.sep + jreFolder;
              const compressedJreFilePath = app.getAppPath() + path.sep + 'java' + path.sep;
              const extractionTargetPath = app.getAppPath() + path.sep + 'java' + path.sep;
              let zipFileName;
              if (platform === 'win32') {
                  zipFileName = 'jre_windows.zip';
              } else if (platform === 'darwin') {
                  zipFileName = 'jre_mac.tar.gz';
              } else if (platform === 'linux') {
                  zipFileName = 'jre_linux.tar.gz';
              }
              if (zipFileName) {
                  // if (!fs.existsSync(jrePath)) {
                  //     decompress(compressedJreFilePath + zipFileName, extractionTargetPath).then(files => {
                  //         // remove compressed jre once unpacked
                  //         fs.unlinkSync(compressedJreFilePath + zipFileName);
                  //         beginStartUp();
                  //     });
                  // } else {
                      beginStartUp();
                  // }
              } else {
                  throw new Error("Platform not supported");
              }
          } catch (e) {
              error_log(e);
          }
      });
      app.on('will-quit', function () {
          serverProcess.kill('SIGINT');
      });
  }
} catch (e) {
  error_log(e);
}
