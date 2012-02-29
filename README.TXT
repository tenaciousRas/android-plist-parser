Android PList parser is a SAX-based implementation of a PList-XML parser
for Android.  PList-XML is a format developed by Apple Inc.  PLists are commonly used in Apple application development on Cocoa and iOS, and the XML format is often used in iOS applications.  The intent of this parser is to fully support the PList-XML DTD (http://www.apple.com/DTDs/PropertyList-1.0.dtd).

License
-------
See LICENSE.TXT that accompanies this project.

Project
-------
The project has two modules: android-plist-parser-app and android-plist-parser-test.  The Android unit tests can be run from Eclipse (you must have ADT installed) or with the Maven.

If you use maven to run the tests, you'll need to install android-maven-plugin.  Instructions can be found at http://code.google.com/p/maven-android-plugin/wiki/GettingStarted.  The tests should run an pass via maven on the master branch.  If the tests only run from Eclipse, or don't run at all, please report an issue at https://github.com/tenaciousRas/android-plist-parser/issues.

Deployment
----------------
The main objective of the project is to provide consumable source code for your application.  Checkout the source code and add it to your Android project, or consider using a maven build to generate a JAR for use in your application (for this, use the release profile or comment out the test module).

Troubleshooting
---------------------
If this parser isn't working you should consider contributing a fix back to the project.  You should be able to easily follow the pattern in PListXMLParserTest.java to add test cases for your specific PList structure.  You can then set debugging breakpoints and/or add logging to dig into the code and find a fix.  We'd love to have a pull request from you so we can collaborate and get the code back into the mainline.

TODOs
------------
- Test for Dict inside Dict.  Not sure if this gets all basic valid structures per DTD but close to it.

PList - Copyright 2010 Apple Inc. All Rights Reserved.