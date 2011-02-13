package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist data elements.
 */
public class Data extends PListObject implements
		IPListSimpleObject<java.lang.String> {

	protected java.lang.String data;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101592260075687323L;

	public Data() {
		setType(PListObjectType.DATA);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public java.lang.String getValue() {
		return (java.lang.String) data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(java.lang.String val) {
		this.data = val;
	}

}