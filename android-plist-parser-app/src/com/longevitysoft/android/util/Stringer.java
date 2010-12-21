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
package com.longevitysoft.android.util;

/**
 * Wrapper for {@link StringBuilder}.
 * 
 * @author fbeachler
 * 
 */
public class Stringer {

	private StringBuilder builder;

	/**
	 * 
	 */
	public Stringer() {
		builder = new StringBuilder();
	}

	/**
	 * 
	 */
	public Stringer(String val) {
		builder = new StringBuilder(val);
	}

	/**
	 * Clear the class-global stringbuilder.
	 * 
	 * @return fluent interface to {@link builder}
	 */
	public StringBuilder newBuilder() {
		builder.setLength(0);
		return builder;
	}

	/**
	 * Clear the class-global stringbuilder.
	 * 
	 * @return fluent interface to {@link builder}
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

}
