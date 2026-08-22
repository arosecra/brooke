# brooke

Custom book reader for personal use

## Merge in changes from v4

- camel case filenames to match solid as much as possible
- actions
  - create an common act function in
    appContext. no need for anything more
    complicated
- implement search
  - get all keys from the items in the search component
    - show nothing until past the settings page, then show loading / busy circle while loading the keys
  - implement disable for the search
  - when typed, search through the items
  - implement a method in appDB to get all of the items via matched keys
  - create a main panel mode for search results, showing the list of items
- implement webmcp
- show which anime are on cr

- correct styling. ex - hover, click, focus, elevation/shadow
- remove styling that's not needed. buttons may not need as much variation right now
- streamline css - i probably don't need tokens for every little thing. this \*is my app, afterall
  - leave tokens for color, other common stuff
  - also for anything changed by device type/size
- streamline css - sizes. at least heights of buttons
- convert complete item into composite item - put the best of item, itemref and thumb into one object
  - create a factory method

- correct heights / alignment between fullscreen and non-fullscreen

## updated todo:

-pipeline: - generate webp thumbnails for movies - try other ocr - perhaps we can compare ocr results?

- ui:
  - add full screen progress overlay for adding collection
    - show top level folder being processed
    - show current leaf being processed
    - on each current leaf, add a couple steps / checkmarks
      - check for series
      - add series if necessary
      - add series thumb if necessary
      - add item
      - add item thumb if necessary
    - show last few leafs being processed (last 10 or so)
      - truncate leaf folder name in the middle if necessary (over 30 chars) - last 3, first 24, ...

  - evaluate popover api and consider replacing angular components with it

  - don't convert image to base64, just use a blob

  - since it seems that we can't share mkv, have buttons to create playlists, download / delete local videos

  - implement BookToC, BookOptions, BookDetails

  - add auto crop based off of ocr output

  - add bounding box overlay based off of ocr output
  - finish and use crud interface for indexeddb
    - make an api that's something like
      tx.addCategory().addItems().commit()
    - and on commit, it also reloads an associated resource (or at least commits changes)

  - keep track of which files are cached & make download button disabled accordingly
    - just get the cached files when the app starts / another resource

  - while app is loading / library is loading, improve the way app displays

  - convert buttons to use action
    - settings
    - etc?
  - disable download button if already downloaded

  - add volume control for orator
    - button, then slider
    - can i hook into volume buttons on mobile?

  - add network interface check to see if we're on wifi / wired before we download
    - maybe also location check

  - add protocol handling for .cbt.gz files

  - add view transitions when we switch between components

  - add voice commands

  - create a dummy page for videos. make a ton of URLs for them so that we can figure out
    how to launch vlc

  - on app load, check url and use it to 'resume' if there are parameters that match

  - consider putting book pages into the indexed db and fetching the necessary page when it's being loaded
    - could, again, save on memory while running
    - this might be a big structure change, not sure
    - or maybe it just has less
  - attempt to standardize image sizes. pad transparency onto images to fill height gap?
    -need to handle series thumbnails in pipeline

  - editor for markdown, when i want to change / update it?
    - keep track of errata'd files / sections in case we re-ocr

  - switch to pnpm

  - add a setting for preferring one page vs two page

  - settings
    - convert settings table to a Record<string, any>,
    - create an interface for what the value is supposed to be (string, etc)
    - add zod & use it for model
      - how to refer to FileSystemFileHandle

## ToDo:

- in the process of a major overhaul, investigate which of the below are still relevant
- rewrite docs
- replace plantuml w/ mermaid

- overall: investigate that literate programming plugin

- angular: Ability to add overlay graphics (bounding box, etc) for image algorithm testing
- angular: add ui to edit the collections
  ? angular: add mp3 collection
  ? angular: add podcast collection
