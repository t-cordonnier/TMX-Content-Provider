This plugin is a comments provider which adds in Comments pane notes extracted from a TMX file.

The file must be in directory /notes from the project, have extension .tmx and be in TMX format.
If there are more than one file in the directory, only one will be kept (no rule is implemented to decide which one).
It is supposed to be the project_save.tmx file of another OmegaT project:
other files will load correctly but notes will be displayed only
if the source corresponds, character per character
and the key values (file, prev, next, id, path) are strictly identical to current entry (or totally absent).

The log will tell you whenever the file was found and correctly loaded or not.

Installation
============

Users of the source version must have JDK 8 or later and Apache Ant: then the command 'ant' builds the JAR file.
Users of binary version directly find the JAR file in the archive.
The file must simply be copied in "plugins" directory of OmegaT.
