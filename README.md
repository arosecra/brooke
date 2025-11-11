# brooke
Custom book reader for personal use

updated todo:
----------------
- ui:
	- convert thumbnails to webp. for anime collection, should save ~100mb. good for indexeddb
	- don't convert image to base64, just use a blob. may need to create a page component (probably good for md too)
	- add Component suffix to all components (too many double ups... book and book, for instance)
	- implement BookToC
	- finish and use web-fs and web-path utilities
	- item name pipe can maybe just be a function? it's kinda unwieldy to use. and being the only pipe...
		- maybe function, then create a display name string on items / etc
	- finish and use crud interface for indexeddb
	- remove cache table in indexeddb
	- keep track of which files are cached & make download button disabled accordingly
		- just get the cached files when the app starts / another resource
	- implement download buttons
	- move settings code to library-fs class (after finishing and using web-fs/web-path)
	

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
