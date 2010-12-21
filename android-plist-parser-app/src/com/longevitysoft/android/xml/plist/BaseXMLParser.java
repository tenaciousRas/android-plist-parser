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
import java.io.StringReader;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.longevitysoft.android.util.Stringer;

/**
 * Base class for implementing SAX parsers. Provides base implementation for
 * initializing the parser. Subclasses can expose data retrieved by the handler,
 * and may want to override {@link this#parse(String)} to provide for
 * initializing data in the handler.
 * 
 * @author fbeachler
 */
public abstract class BaseXMLParser {

	public static final String TAG = "BaseXMLParser";

	/**
	 * {@link Stringer} for this class.
	 */
	protected Stringer stringer;

	/**
	 * The handler used to parse the classifications xml.
	 */
	private DefaultHandler handler;

	/**
	 * Re-usable factory for gettings {@link SAXParser}s.
	 */
	protected SAXParserFactory spf;

	/**
	 * Re-usable {@link SAXParser} for parsing XML.
	 */
	protected SAXParser sp;

	/**
	 * Public c'tor.
	 */
	public BaseXMLParser() {
		stringer = new Stringer();
	}

	/**
	 * @return the handler
	 */
	public DefaultHandler getHandler() {
		return (DefaultHandler) handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(DefaultHandler handler) {
		this.handler = handler;
	}

	/**
	 * Creates a new {@link SAXParserFactory} and gets a new {@link SAXParser}.
	 * 
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void initParser() {
		// get a factory
		if (null == spf) {
			spf = SAXParserFactory.newInstance();
		}
		// get a new parser instance
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			Log.e(
					stringer.newBuilder().append(TAG).append("#parse")
							.toString(), "ParserConfigurationException");
			e.printStackTrace();
		} catch (SAXException e) {
			Log.e(
					stringer.newBuilder().append(TAG).append("#parse")
							.toString(), "SAXException");
			e.printStackTrace();
		}
	}

	/**
	 * Parse an XML document.
	 * 
	 * @param xml
	 */
	public void parse(String xml) throws IllegalStateException {
		try {
			// convert xml to inputsource
			InputSource inSrc = new InputSource(new StringReader(xml));
			// register a handler for callbacks and parse the file
			sp.parse(inSrc, getHandler());
		} catch (SAXException e) {
			Log.e(
					stringer.newBuilder().append(TAG).append("#parse")
							.toString(), "SAXException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(
					stringer.newBuilder().append(TAG).append("#parse")
							.toString(), "IOException");
			e.printStackTrace();
		}

		Log.v(stringer.newBuilder().append(TAG).append("#parse").toString(),
				"done parsing xml");
	}

}