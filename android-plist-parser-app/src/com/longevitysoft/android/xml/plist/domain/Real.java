package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist real element.
 */
public class Real extends PListObject implements
		IPListSimpleObject<Float> {

	protected Float real;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4204214862534504729L;

	public Real() {
		setType(PListObjectType.REAL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public Float getValue() {
		return real;
	}

	@Override
	public void setValue(Float val) {
		this.real = val;
	}

	@Override
	public void setValue(java.lang.String val) {
		this.real = new Float(Float.parseFloat(val.trim()));
	}
}