package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist true element.
 */
public class True extends PListObject implements
		IPListSimpleObject<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3560354198720649001L;

	public True() {
		setType(PListObjectType.TRUE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public Boolean getValue() {
		return new Boolean(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Boolean val) {
		// noop
	}

	@Override
	public void setValue(java.lang.String val) {
		// noop
	}

}