package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist string element. Not to be confused with
 * {@link java.lang.String}.
 */
public class String extends PListObject implements
		IPListSimpleObject<java.lang.String> {

	protected java.lang.String str;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134261357175236382L;

	public String() {
		setType(PListObjectType.STRING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public java.lang.String getValue() {
		return this.str;
	}

	@Override
	public void setValue(java.lang.String val) {
		this.str = new java.lang.String(val);
	}

}