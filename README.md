# brooke
Custom book reader for personal use

updated todo:
----------------
-pipeline: 
	- generate webp thumbnails for movies
	- try other ocr
		- perhaps we can compare ocr results?


- ui:
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

	- finish and use web-fs and web-path utilities

	- add auto crop based off of ocr output

	- add bounding box overlay based off of ocr output

	- item name pipe can maybe just be a function? it's kinda unwieldy to use. and being the only pipe...
		- maybe function, then create a display name string on items / etc
		- more difficult to do than you may think, since category isn't known until later
			- we find all the items in a category by the category, not the other way around
			- and sometimes we generate categories?
	
	- finish and use crud interface for indexeddb
		- make an api that's something like
				tx.addCategory().addItems().commit()
		- and on commit, it also reloads an associated resource (or at least commits changes)

	- keep track of which files are cached & make download button disabled accordingly
		- just get the cached files when the app starts / another resource

	- move settings code to library-fs class (after finishing and using web-fs/web-path)

	- while app is loading / library is loading, improve the way app displays

	- convert buttons to use action
		- settings
		- etc?
	- disable download button if already downloaded

	- convert settings to a Record<string, any>, create an interface for what the value is supposed to be (string, etc)

	- add volume control for orator

	- add network interface check to see if we're on wifi / wired before we download
		- maybe also location check

	- add protocol handling for .cbt.gz files

	- add view transitions when we switch between components

	- add voice commands

	- create a dummy page for videos.  make a ton of URLs for them so that we can figure out
	  how to launch vlc

	- make the thumbnail view a separate component, add a showThumbnail() to panel instead of book

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

	- once switched, create a project to create a subset of the material-symbols
	

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
