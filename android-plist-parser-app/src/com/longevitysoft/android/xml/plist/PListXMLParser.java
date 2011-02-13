/**
 * Licensed under Creative Commons Attribution 3.0 Unported license.
 * http://creativecommons.org/licenses/by/3.0/
 * You are free to copy, distribute and transmit the work, and 
 * to adapt the work.  You must attribute android-plist-parser 
 * to Free Beachler (http://www.freebeachler.com).
 * 
 * The Android PList parser (android-plist-parser) is distributed in 
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.
 */
package com.longevitysoft.android.xml.plist;

import java.io.IOException;
import java.io.InputStream;

import com.longevitysoft.android.util.Stringer;

/**
 * @author fbeachler
 * 
 */
public class PListXMLParser extends BaseXMLParser {

	public static final String TAG = "PListXMLParser";

	/**
	 * 
	 */
	public PListXMLParser() {
		super();
	}

	/**
	 * Parse a PList XML document.
	 * 
	 * @param xml
	 */
	public void parse(String xml) throws IllegalStateException {
		PListXMLHandler pListHandler = (PListXMLHandler) getHandler();
		if (null == pListHandler) {
			throw new IllegalStateException(
					"handler is null, must set a document handler before calling parse");
		}
		if (null == xml) {
			pListHandler.setPlist(null);
			return;
		}
		initParser();
		super.parse(xml);
	}

	/**
	 * Parse a PList XML document from an {@link InputStream}.
	 * 
	 * @param xml
	 * @throws IOException
	 */
	public void parse(InputStream is) throws IllegalStateException, IOException {
		PListXMLHandler pListHandler = (PListXMLHandler) getHandler();
		if (null == pListHandler) {
			throw new IllegalStateException(
					"handler is null, must set a document handler before calling parse");
		}
		if (null == is) {
			pListHandler.setPlist(null);
			return;
		}
		Stringer xml = null;
		try {
			xml = Stringer.convert(is);
		} catch (IOException e) {
			throw new IOException(
					"error reading from input string - is it encoded as UTF-8?");
		}
		initParser();
		super.parse(xml.getBuilder().toString());
	}

}
