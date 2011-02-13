package com.longevitysoft.android.xml.plist.domain;

/**
 * Interface for that simple PList objects implement. This includes all
 * objects aside from arrays and dicts.
 */
public interface IPListSimpleObject<E extends Object> {
	public E getValue();

	public void setValue(E val);

	public void setValue(java.lang.String val);
}