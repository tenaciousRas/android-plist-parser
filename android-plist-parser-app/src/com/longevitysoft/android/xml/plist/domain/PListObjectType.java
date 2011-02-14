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
package com.longevitysoft.android.xml.plist.domain;

/**
 * Defines valid PList object types. These correspond to the
 * elements defined in the PList XML DTD at {@link http
 * ://www.apple.com/DTDs/PropertyList-1.0.dtd}.
 * 
 * @author fbeachler
 * 
 */
public enum PListObjectType {
	ARRAY(0), DATA(1), DATE(2), DICT(3), REAL(4), INTEGER(5), STRING(
			6), TRUE(7), FALSE(8);

	private int type;

	private PListObjectType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

}