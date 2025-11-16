# brooke
Custom book reader for personal use

updated todo:
----------------
-pipeline: 
	- pipeline isn't picking up series folders for movies. fix
	- generate webp thumbnails for movies

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

	- convert settings to a Record<string, any>, create an interface for what the value is supposed to be (string, etc)
	
	- make orator global / injectable. just a part of the state?  separate component?

	- add wakelock while orating

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
		- breadcrumb in separate toolbar row
		- make the newstand icon show other topbar rows
		- no buttons on main topbar, move them to other topbars on mobile
		- some buttons do not show at all - 
			- do not show the 1/2 page button
			- do not show the side by side button
			- do not show the thumbs button
			- do not show the book options icon
		- main topbar is newstand button, fullscreen, paginator
		- don't show paginator
		- clicking the page on the right goes to next page
		- clicking on the page on the left goes back
		- change how paginator displays in mobile,
			- don't show 1-1 of 283, just show 1 of 283
			- don't show next / previous, just start / end
			- hide start / end on mobile
		- less of a margin on the left / right

		- add gestures (swipe specifically)
	

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
