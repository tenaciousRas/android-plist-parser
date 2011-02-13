package com.longevitysoft.android.xml.plist.domain;

import java.io.Serializable;

/**
 * A PListObject is an object which has a valid {@link PListObjectType}.
 */
public class PListObject extends Object implements Cloneable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5258056855425643835L;

	private PListObjectType type;

	/**
	 * @return the type
	 */
	public PListObjectType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(PListObjectType type) {
		this.type = type;
	}

}