# brooke
Custom book reader for personal use

updated todo:
----------------
-pipeline: 
	- generate webp thumbnails for movies
	- try other ocr
		- perhaps we can compare ocr results?


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

	- consider create a separate component for each action
		- makes other html simple
		- can implement actionable
		- doing this allows action button component to inject the actionable
		- can have an 'act' function in actionable
		- can also accomodate more customized buttons
		- can inject all actionables
		- can be named
		- should be able to cleanup common action button component a bunch
		- can move 'action' from app to actionable component
			- if so, how do i deal with shared actions (goToNextPage)?

	- convert all services to components (allows for hierarchal injection)

	- consider injecting resource values. actions that use the resource will take the resource as a parameter

	- don't convert image to base64, just use a blob. may need to create a page component (probably good for md too)

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

	- add network interface check to see if we're on wifi / wired before we download
		- maybe also location check

	- add protocol handling for .cbt.gz files

	- add view transitions when we switch between components

	- add voice commands

	- create a dummy page for videos.  make a ton of URLs for them so that we can figure out
	  how to launch vlc

	- on app load, check url and use it to 'resume' if there are parameters that match

	- consider putting book pages into the indexed db and fetching the necessary page when it's being loaded
		- could, again, save on memory while running
		- this might be a big structure change, not sure
		- or maybe it just has less
	
	- attempt to standardize image sizes. pad transparency onto images to fill height gap?
		-need to handle series thumbnails in pipeline

	- update for responsive
		- clicking the page on the right goes to next page
		- clicking on the page on the left goes back
		- add gestures (swipe specifically)

	- editor for markdown, if i want to change / update it?

	- switch to pnpm

	- switch to pnpm mono repo

	- add a setting for preferring one page vs two page

	- in settings, split read/write permission in table
		- only load settings if read is not present, or action requires write

	- settings
		- convert settings table to a Record<string, any>, 
		- create an interface for what the value is supposed to be (string, etc)
		- add zod & use it for model
			- how to refer to FileSystemFileHandle
		- use new angular signal forms

	

ToDo:
----------------
- in the process of a major overhaul, investigate which of the below are still relevant
- rewrite docs
- replace plantuml w/ mermaid



- overall: investigate that literate programming plugin

- angular: Ability to add overlay graphics (bounding box, etc) for image algorithm testing
- angular: add ui to edit the collections
? angular: add mp3 collection
? angular: add podcast collection
