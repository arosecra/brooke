module.exports = {
  packagerConfig: {
    "extraResource": [
      "../java/target/brooke.war"
     ]
  },
  rebuildConfig: {},
  makers: [
    {
      name: '@electron-forge/maker-zip',
    }
  ],
};
