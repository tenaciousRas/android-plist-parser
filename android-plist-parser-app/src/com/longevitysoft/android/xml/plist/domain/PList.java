package com.longevitysoft.android.xml.plist.domain;

/**
 * A PList class contains the objects and methods used to build and access a
 * PList.
 */
public class PList {

	/**
	 * The PList root config element.
	 */
	private PListObject root;

	/**
	 * @return the PList root config element
	 */
	public PListObject getRootElement() {
		return root;
	}

	/**
	 * @param root
	 *            the PList root object to set
	 */
	public void setRootElement(PListObject root) {
		this.root = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public java.lang.String toString() {
		if (null == root) {
			return null;
		}
		return root.toString();
	}

}