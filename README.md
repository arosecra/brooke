# brooke
Custom book reader for personal use

updated todo:
----------------
- ui:
	- don't convert image to base64, just use a blob. may need to create a page component (probably good for md too)

	- add Component suffix to all components (too many double ups... book and book, for instance)
		- also to filenames

	- implement BookToC

	- finish and use web-fs and web-path utilities

	- item name pipe can maybe just be a function? it's kinda unwieldy to use. and being the only pipe...
		- maybe function, then create a display name string on items / etc
	
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

	- add text to speech if there's markdown
		- allow it to auto next page

	- add fullscreen

	- add offline or geo check to see if download is available
		- and/or add background sync or fetch support for download

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

	- consider removing library type / resource. just grab what we want from the db when necessary
		- may be less klunky, but need to look at how i'm loading/reloading data

	
	- attempt to standardize image sizes. pad transparency onto images to fill height gap?
		-need to handle series thumbnails in pipeline

	- update for responsive
		- only one page by default when less than 1k width
		- hide the breadcrumb
		- figure out what to do with the menu
	

ToDo:
----------------
- in the process of a major overhaul, investigate which of the below are still relevant
- rewrite docs
- replace plantuml w/ mermaid



- overall: investigate that literate programming plugin

- angular: Ability to add TOC entries
- angular: Ability to add overlay graphics (bounding box, etc) for image algorithm testing
- angular: add ui to edit the collections
? angular: add mp3 collection
? angular: add podcast collection
