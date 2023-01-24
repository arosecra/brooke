
# Build Process

Brooke uses Maven to build each individual module.  Each module also has a Maven POM, which may just wrap and call NPM.  

Alternatives were tried:
- npm as the combined script.  NPM does not allow for comments in the JSON files, so that option was out.
- Using yaml instead of xml for POM files.  This worked very well for the build, but did not interface into Visual Studio Code