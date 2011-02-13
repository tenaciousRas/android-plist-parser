package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist date elements.
 */
public class Date extends PListObject implements
		IPListSimpleObject<java.util.Date> {

	protected java.util.Date date;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3846688440069431376L;

	public Date() {
		setType(PListObjectType.DATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public java.util.Date getValue() {
		return (java.util.Date) date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(java.util.Date val) {
		this.date = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(java.lang.String val) {
		this.date = new java.util.Date(java.util.Date.parse(val.trim()));
	}

}