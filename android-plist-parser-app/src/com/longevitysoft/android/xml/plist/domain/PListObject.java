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

import java.io.Serializable;

/**
 * A PListObject is an object which has a valid {@link PListObjectType}.
 */
public class PListObject extends Object implements Cloneable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5258056855425643835L;

	private PListObjectType type;

	/**
	 * @return the type
	 */
	public PListObjectType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(PListObjectType type) {
		this.type = type;
	}

}