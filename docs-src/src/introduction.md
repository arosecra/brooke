
# Introduction

## Overview

Brooke is a personal solution for, first, a book reader for digitized books, and second for
launching videos archived from DVDs and Blu-Rays.  Books are in cbt (Comic Book Tar) archives,
and videos are in MKV files.

Brooke is an Angular web application, backed with a Java Spring server, wrapped in Electron.

Electron launches the Java application. The Angular web application is served from the embedded Spring
server.

<!--
@startuml overview

User -[dotted]-> Electron: Start
Electron -> "Spring Server": Start
Electron -> Angular: "Redirect to"
Angular -> "Spring Server": "Call REST APIs"

@enduml
-->
![](overview.svg)


## Purpose

The purpose of this document is to record the design of Brooke for future reference.  Much of the content
is notes about the application rather than a formal design document.

## History

This is not the first implementation of the Brooke solution. A brief synopsis of prior iterations, and why they were abandoned:

- Concatenated PDFs.  The first solution was to concatenate scanned PDFs together into a single file.  This resulted in some extremely large files, and as a consequence had very poor performance in applications when viewing.

- Utilizing open source Comic Book readers (ComicRacks, etc) with cbz files. No suitably performant solution was found.

- Java web application with slide shows.  Books were htlm files with a bunch of loose PNGs.  This lead to many loose files sitting around, elongating any operation against the book. (Moving files, etc)

- Java web application with cbt and Thymeleaf.  The closest to the current solution, this was a Thymeleaf Spring application.  This solution was modified to use Electron and Angular to remove the outdated  server side rendering.

## Document Conventions

This document is created using markdown with a couple preprocessing steps.  

## Document Preprocessing

### Linking to Files

The preprocessing allows for linking to Epics defined in the source using 
Comment Anchors in Visual Studio Code.  The epics in question outline code
that is then copied into the output files.  The following example copies content
for the starting_angular epic into the output below.

	\\@LINK starting_angular

@LINK starting_angular



### PlantUML

This documentation uses PlantUML to generate images for UML diagrams.  The plant UML text
is inside HTML quotes, and then linked to.

<!--
@startuml firstDiagram

Alice -[dotted]-> Bob: Hello
Bob -> Alice: Hi!
	
@enduml
-->

![](firstDiagram.svg)