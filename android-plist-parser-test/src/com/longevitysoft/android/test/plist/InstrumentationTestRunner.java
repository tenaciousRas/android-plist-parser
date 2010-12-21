/**
 * Licensed under Creative Commons Attribution 3.0 Unported license.
 * http://creativecommons.org/licenses/by/3.0/
 * You are free to copy, distribute and transmit the work, and 
 * to adapt the work.  You must attribute android-plist-parser 
 * to Free Beachler (http://www.freebeachler.com).
 * 
 * The Android PList parser (android-plist-parser) is distributed in 
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.
 */
package com.longevitysoft.android.test.plist;

import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * A instrumentation runner that contains all tests for the plist parser.
 */
public class InstrumentationTestRunner extends
		android.test.InstrumentationTestRunner {

	@Override
	public TestSuite getAllTests() {
		return new TestSuiteBuilder(InstrumentationTestRunner.class)
				.includePackages(
						"com.longevitysoft.android.test.plist.xml")
				.build();
	}

	@Override
	public ClassLoader getLoader() {
		return InstrumentationTestRunner.class.getClassLoader();
	}

}
