package com.longevitysoft.android.xml.plist.domain;

/**
 * POJO for simple plist false element.
 */
public class False extends PListObject implements
		IPListSimpleObject<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8533886020773567552L;

	public False() {
		setType(PListObjectType.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
	 * IPListSimpleObject#getValue()
	 */
	@Override
	public Boolean getValue() {
		return new Boolean(false);
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