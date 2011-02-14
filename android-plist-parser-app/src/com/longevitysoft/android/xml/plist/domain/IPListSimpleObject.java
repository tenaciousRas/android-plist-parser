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
 * Interface that simple PList objects implement. This includes all objects
 * besides from {@link Array}s and {@link Dict}s.
 */
public interface IPListSimpleObject<E extends Object> {

	/**
	 * Get the value of the plist object.
	 * 
	 * @return
	 */
	public E getValue();

	/**
	 * Set the value of the PList object.
	 * 
	 * @param val
	 */
	public void setValue(E val);

	/**
	 * Set the value of the PList object from a string.
	 * 
	 * @param val
	 */
	public void setValue(java.lang.String val);
}