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
package com.longevitysoft.android.test.plist.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import android.test.AndroidTestCase;

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PListParserListener;
import com.longevitysoft.android.xml.plist.PListXMLHandler.ParseMode;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList.Dict;


/**
 * @author fbeachler
 *
 */
public class PListXMLHandlerTest extends AndroidTestCase {

	public static final String TAG_NAMESPACE = "foo";
	/**
	 * The class under test.
	 */
	protected PListXMLHandler handler;
	private boolean mockListenerInvoked;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		handler = new PListXMLHandler();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		handler = null;
		super.tearDown();
	}

	public void testTempValGetterSetter() {
		assertNull(handler.getTempVal());
		Stringer expected = new Stringer();
		handler.setTempVal(expected);
		assertEquals(expected, handler.getTempVal());
	}

	public void testPListGetterSetter() {
		assertNull(handler.getPlist());
		PListXMLHandler.PList expected = new PListXMLHandler.PList();
		handler.setPlist(expected);
		assertEquals(expected, handler.getPlist());
	}

	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#startDocument()}.
	 * @throws SAXException 
	 */
	public void testStartDocument() throws SAXException {
		assertNull(handler.getPlist());
		handler.startDocument();
		assertNull(handler.getPlist());
		assertNull(handler.getParseListener());
		assertNotNull(handler.getTempVal());
	}

	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#characters(char[], int, int)}.
	 * @throws SAXException 
	 */
	public void testCharacters() throws SAXException {
		handler.setTempVal(new Stringer());
		char[] chars = { 'f', 'o', 'o', 'b', 'a', 'r'};
		handler.characters(chars, 1, 3);
		assertEquals("oob", handler.getTempVal().getBuilder().toString());
	}

	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
	 * @throws SAXException 
	 */
	public void testStartElement_StringStringStringAttributes() throws SAXException {
		Attributes attrs = new AttributesImpl();
		assertNull(handler.getPlist());

		handler.setTempVal(new Stringer());
		handler.startElement(TAG_NAMESPACE, "plist", "plist", attrs);
		assertNotNull(handler.getPlist());
	}

	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws SAXException 
	 */
	public void testEndElementString_Unknown_String() throws SAXException {
		handler.startDocument();
		handler.endElement(TAG_NAMESPACE, "foo", "foo");
		assertNull(handler.getPlist());
	}

	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws SAXException 
	 */
	public void testEndElementString_PList_CallsListener() throws SAXException {
		mockListenerInvoked = false;
		PListParserListener mockListener = new PListXMLHandler.PListParserListener() {

			@Override
			public void onPListParseDone(PList list, ParseMode mode) {
				mockListenerInvoked = true;
			}
			
		};
		handler = new PListXMLHandler() {

			/* (non-Javadoc)
			 * @see org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#startDocument()
			 */
			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
				super.setPlist(new PList());
			}
			
		};
		handler.setParseListener(mockListener);
		handler.startDocument();
		handler.endElement(TAG_NAMESPACE, "plist", "plist");
		assertNotNull(handler.getPlist());
		assertTrue(mockListenerInvoked);
	}

	protected static class MockPListXMLHandlerFullValid extends PListXMLHandler {

			/* (non-Javadoc)
			 * @see org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#startDocument()
			 */
			@Override
			public void startDocument() throws SAXException {
				super.startDocument();
				super.setPlist(new PList());
				super.stack = new Stack<StatefulDictionaryItem>();
				Dict rootDict = new PList.Dict();
				super.dict = rootDict;
				super.getPlist().setRootDictionary(rootDict);
			}

			public void setInArray(String key) throws SAXException {
				super.key = key;
				Attributes attrs = new AttributesImpl();
				super.startElement(TAG_NAMESPACE, "array", "array", attrs);
			}

			public void setInDict(String key) throws SAXException {
				super.key = key;
				Attributes attrs = new AttributesImpl();
				super.startElement(TAG_NAMESPACE, "dict", "dict", attrs);
			}

	};
	/**
	 * Test method for {@link org.zooniverse.android.galaxyzoo.xml.PListXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws SAXException 
	 */
	public void testEndElementString_FullValidPList() throws SAXException {
		// setup test
		MockPListXMLHandlerFullValid mockHandler = new MockPListXMLHandlerFullValid();
		PList expected = new PList();
		PList.Dict expectedDict = new PList.Dict();
		Map<String, Object> dictMap = new HashMap<String, Object>();
		dictMap.put("key-1", "string-val-1");
		dictMap.put("key-2", 101);
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("string-val-2");
		arr.add("102");
		dictMap.put("key-3", arr);
		arr = new ArrayList<Object>();
		Map<String, Object> arrDictMap = new HashMap<String, Object>();
		arrDictMap.put("key-3", "string-val-3");
		arrDictMap.put("key-4", 103);
		arr.add(arrDictMap);
		dictMap.put("key-4", arr);
		expectedDict.setConfigMap(dictMap);
		expected.setRootDictionary(expectedDict);
		// exec test
		mockHandler.startDocument();
		mockHandler.setTempVal(new Stringer("key-1"));
		mockHandler.endElement(TAG_NAMESPACE, "key", "key");
		mockHandler.setTempVal(new Stringer("string-val-1"));
		mockHandler.endElement(TAG_NAMESPACE, "string", "string");
		mockHandler.setTempVal(new Stringer("key-2"));
		mockHandler.endElement(TAG_NAMESPACE, "key", "key");
		mockHandler.setTempVal(new Stringer("101"));
		mockHandler.endElement(TAG_NAMESPACE, "integer", "integer");
		// mock array mode
		mockHandler.setInArray("key-3");
		// populate array
		mockHandler.setTempVal(new Stringer("string-val-2"));
		mockHandler.endElement(TAG_NAMESPACE, "string", "string");
		mockHandler.setTempVal(new Stringer("102"));
		mockHandler.endElement(TAG_NAMESPACE, "integer", "integer");
		mockHandler.setTempVal(new Stringer("   \n   "));
		mockHandler.endElement(TAG_NAMESPACE, "array", "array");
		// mock another array mode
		mockHandler.setInArray("key-4");
		// populate array with PList.DICT's
		mockHandler.setInDict("key-3");
		mockHandler.setTempVal(new Stringer("string-val-3"));
		mockHandler.endElement(TAG_NAMESPACE, "string", "string");
		mockHandler.endElement(TAG_NAMESPACE, "dict", "dict");
		mockHandler.setInDict("key-4");
		mockHandler.setTempVal(new Stringer("103"));
		mockHandler.endElement(TAG_NAMESPACE, "integer", "integer");
		mockHandler.endElement(TAG_NAMESPACE, "dict", "dict");
		mockHandler.setTempVal(new Stringer("   \n   "));
		mockHandler.endElement(TAG_NAMESPACE, "array", "array");
		// wrap up
		mockHandler.endElement(TAG_NAMESPACE, "plist", "plist");
		assertNotNull(mockHandler.getPlist().getRootDictionary().getConfigMap());
		assertTrue(expected.getRootDictionary().getConfiguration("key-1").equals(mockHandler.getPlist().getRootDictionary().getConfiguration("key-1")));
		assertNotNull(mockHandler.getPlist().getRootDictionary().getConfigurationArray("key-3"));
		assertEquals(2, mockHandler.getPlist().getRootDictionary().getConfigurationArray("key-3").size());
		assertEquals(new Integer(103), ((PList.Dict) mockHandler.getPlist().getRootDictionary().getConfigurationArray("key-4").get(1)).getConfigurationInteger("key-4"));
	}

}
