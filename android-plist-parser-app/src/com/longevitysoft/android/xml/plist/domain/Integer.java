package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist int element.
 */
public class Integer extends PListObject implements
		IPListSimpleObject<java.lang.Integer> {

	protected java.lang.Integer intgr;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5952071046933925529L;

	public Integer() {
		setType(PListObjectType.INTEGER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public java.lang.Integer getValue() {
		return intgr;
	}

	@Override
	public void setValue(java.lang.Integer val) {
		this.intgr = val;
	}

	@Override
	public void setValue(java.lang.String val) {
		this.intgr = new java.lang.Integer(
				java.lang.Integer.parseInt(val.trim()));
	}

}