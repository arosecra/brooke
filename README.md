# brooke
Custom book reader for personal use

Electron piece based off of https://github.com/appreciated/maven-springboot-electron
 and https://github.com/jreznot/electron-java-app and https://medium.com/@amitbhave10/modern-desktop-application-with-electron-js-and-spring-boot-150419db0d49


ToDo:
----------------
- overall: investigate that literate programming plugin
- overall: move image apis into /images/

- angular: Ability to add TOC entries
- angular: Ability to add overlay graphics (bounding box, etc) for image algorithm testing
- angular: NgRx incorporation
- angular: management menu / page
- angular: add ability to set the subtitle/audio tracks per series / video
- angular: UI to change app settings
- angular: button to reload library
- angular: replace the bulma branding
- angular: some styling
- angular: add ui to edit the collections
? angular: add mp3 collection
? angular: add podcast collection

- electron: use a better icon
- electron: sometimes have to close twice (close confirm close)

- java: better approach for finding a shelf item (find shelf item, find location for it, then find collection, cat, etc)
- java: get settings from the application path
- java: if already cached, do not re-cache
- java: make the pipeline use the background task stuff, runnable task
- java: make the bounding box utility into an api
- java: rename brooke rest service to brooke service facade and move the real work of the services to other service files
- java: use the item location for most queries, instead of looping through collection/category/etc