Android PList parser is a SAX-based implementation of a PList-XML parser
for Android.  PList-XML is a format developed by Apple Inc.  PLists are commonly used in Apple application development on Cocoa and the iPhone SDK, and the XML format is especially used for iPhone applications.  The intent of this parser is to fully support the PList-XML DTD (http://www.apple.com/DTDs/PropertyList-1.0.dtd).

License
-------
See LICENSE.TXT that accompanies this project.

Project
-------
The project has two modules: android-plist-parser-app and android-plist-parser-test.  The Android unit tests can be run from Eclipse (you must have ADT installed) or with the Maven.

If you use maven to run the tests, you'll need to install android-maven-plugin.  Instructions can be found at http://code.google.com/p/maven-android-plugin/wiki/GettingStarted.

Source
------
The main objective of the project is to provide you with consumable source code for your application.  You may also want to consider using a maven build to generate a JAR for use in your application (and comment out the test module for this).

TODOs
------------
- Base64 support still untested.  Somebody clone and contribute a unit test back to the project for this!
- More complete support of various types of PList permutations to more fully verify the DTD.

PList - Copyright 2010 Apple Inc. All Rights Reserved.