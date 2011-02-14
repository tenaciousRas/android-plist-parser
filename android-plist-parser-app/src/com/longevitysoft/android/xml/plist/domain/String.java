package com.longevitysoft.android.xml.plist.domain;

import com.longevitysoft.android.util.Stringer;

/**
 * Represents a simple plist string element. Not to be confused with
 * {@link java.lang.String}.
 */
public class String extends PListObject implements
		IPListSimpleObject<java.lang.String> {

	protected Stringer str;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134261357175236382L;

	public String() {
		setType(PListObjectType.STRING);
		str = new Stringer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#getValue()
	 */
	@Override
	public java.lang.String getValue() {
		return this.str.getBuilder().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.Object)
	 */
	@Override
	public void setValue(java.lang.String val) {
		str.newBuilder().append(val);
	}

}