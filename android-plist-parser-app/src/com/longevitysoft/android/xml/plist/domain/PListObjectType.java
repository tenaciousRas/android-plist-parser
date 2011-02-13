package com.longevitysoft.android.xml.plist.domain;

/**
 * Defines valid PList object types. These correspond to the
 * elements defined in the PList XML DTD at {@link http
 * ://www.apple.com/DTDs/PropertyList-1.0.dtd}.
 * 
 * @author fbeachler
 * 
 */
public enum PListObjectType {
	ARRAY(0), DATA(1), DATE(2), DICT(3), REAL(4), INTEGER(5), STRING(
			6), TRUE(7), FALSE(8);

	private int type;

	private PListObjectType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

}