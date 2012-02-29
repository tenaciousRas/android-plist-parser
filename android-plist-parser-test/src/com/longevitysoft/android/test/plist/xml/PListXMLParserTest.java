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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import junit.framework.TestCase;

import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Data;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.False;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.Real;
import com.longevitysoft.android.xml.plist.domain.True;

/**
 * Tests {@link PListXMLParser} with various XML string fixtures.
 * 
 * @author fbeachler
 * 
 */
public class PListXMLParserTest extends TestCase {

	public static final String INVALID_PLIST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<foo>"
			+ "<bar>Galaxy Zoo Hubble primary classification workflow</bar>"
			+ "<string>1.0</string>" + "</foo>" + "</plist>";
	public static final String VALID_WORKFLOW_VERSION_PLIST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<dict>"
			+ "<key>Galaxy Zoo Hubble primary classification workflow</key>"
			+ "<string>1.0</string>" + "</dict>" + "</plist>";
	public static final String VALID_WORKFLOW_PLIST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
			+ "<plist version=\"1.0\">\n"
			+ "<dict>\n"
			+ "	<key>workflow_answers</key>\n"
			+ "	<array>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/1_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Smooth</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/2_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>30</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Features or disk</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>3</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/3_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>0</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Star or artifact</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>3</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>16</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/16_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>3</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Completely round</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>4</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>20</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/20_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>0</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Lens or arc</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>10</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>4</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>answer_id</key>\n"
			+ "			<integer>21</integer>\n"
			+ "			<key>image_url</key>\n"
			+ "			<string>http://www.galaxyzoo.org/images/buttons/21_button.gif</string>\n"
			+ "			<key>next_workflow_task_id</key>\n"
			+ "			<integer>0</integer>\n"
			+ "			<key>value</key>\n"
			+ "			<string>Disturbed</string>\n"
			+ "			<key>workflow_answer_id</key>\n"
			+ "			<integer>11</integer>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>4</integer>\n"
			+ "		</dict>\n"
			+ "	</array>\n"
			+ "	<key>workflow_tasks</key>\n"
			+ "	<array>\n"
			+ "		<dict>\n"
			+ "			<key>name</key>\n"
			+ "			<string>Is the galaxy simply smooth and rounded, with no sign of a disk?</string>\n"
			+ "			<key>parent_id</key>\n"
			+ "			<integer>-1</integer>\n"
			+ "			<key>task_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "			<key>workflow_answers</key>\n"
			+ "			<array>\n"
			+ "				<integer>1</integer>\n"
			+ "				<integer>2</integer>\n"
			+ "				<integer>3</integer>\n"
			+ "			</array>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>name</key>\n"
			+ "			<string>How rounded is it?</string>\n"
			+ "			<key>parent_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "			<key>task_id</key>\n"
			+ "			<integer>7</integer>\n"
			+ "			<key>workflow_answers</key>\n"
			+ "			<array>\n"
			+ "				<integer>4</integer>\n"
			+ "				<integer>5</integer>\n"
			+ "				<integer>6</integer>\n"
			+ "			</array>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "		</dict>\n"
			+ "		<dict>\n"
			+ "			<key>name</key>\n"
			+ "			<string>Is there anything odd?</string>\n"
			+ "			<key>parent_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "			<key>task_id</key>\n"
			+ "			<integer>6</integer>\n"
			+ "			<key>workflow_answers</key>\n"
			+ "			<array>\n"
			+ "				<integer>7</integer>\n"
			+ "				<integer>8</integer>\n"
			+ "			</array>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>3</integer>\n"
			+ "		</dict>\n"
			+ "	</array>\n"
			+ "</dict>\n" + "</plist>\n" + "";
	public static final String VALID_PLIST_ARRAY_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<array>"
			+ "<dict>"
			+ "<key>foo</key>"
			+ "<string>1.0</string>"
			+ "</dict>"
			+ "<dict>"
			+ "<key>bar</key>"
			+ "<string>1.1</string>"
			+ "</dict>"
			+ "</array>" + "</plist>";
	public static final String VALID_PLIST_ARRAY_ROOT_NESTED_ARRAY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<array>"
			+ "<array>"
			+ "<string>foo</string>"
			+ "<string>bar</string>"
			+ "</array>"
			+ "<array>"
			+ "<string>baz</string>"
			+ "<string>quux</string>"
			+ "</array>" + "</array>" + "</plist>";
	public static final String VALID_PLIST_ARRAY_ROOT_NESTED_DICT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<array>"
			+ "		<dict>\n"
			+ "			<key>name</key>\n"
			+ "			<string>How rounded is it?</string>\n"
			+ "			<key>parent_id</key>\n"
			+ "			<integer>1</integer>\n"
			+ "			<key>task_id</key>\n"
			+ "			<integer>7</integer>\n"
			+ "			<key>workflow_answers</key>\n"
			+ "			<array>\n"
			+ "				<integer>4</integer>\n"
			+ "				<integer>5</integer>\n"
			+ "				<integer>6</integer>\n"
			+ "			</array>\n"
			+ "			<key>workflow_task_id</key>\n"
			+ "			<integer>2</integer>\n"
			+ "		</dict>\n" + "</array>" + "</plist>";
	public static final String VALID_PLIST_DICT_ROOT_NESTED_DICT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<key>cat</key>"
			+ "<dict>"
			+ "<key>ID</key>"
			+ "<string>901</string>"
			+ "<key>title</key>"
			+ "<string>Title</string>"
			+ "<key>thumb</key>"
			+ "<dict>"
			+ "<key>ID</key>"
			+ "<integer>152</integer>"
			+ "<key>uri</key>"
			+ "<string>http://www.google.com</string>"
			+ "</dict>"
			+ "<key>order</key>"
			+ "<integer>2</integer>"
			+ "<key>type</key>"
			+ "<integer>5</integer>" + "</dict>" + "</plist>";
	public static final String VALID_PLIST_STRING_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<string>"
			+ "foobar"
			+ "</string>"
			+ "</plist>";
	public static final String VALID_PLIST_DATA_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<data>"
			+ "Zm9vYmFy"
			+ "</data>"
			+ "</plist>";
	public static final String VALID_PLIST_DATE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<date>"
			+ "Sun, 13 Feb 2011 12:01:00 GMT-0500" + "</date>" + "</plist>";
	public static final String VALID_PLIST_ISO8601_DATE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<date>"
			+ "2012-02-24T10:10:00Z"
			+ "</date>" + "</plist>";
	public static final String VALID_PLIST_REAL_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<real>"
			+ "3.1417"
			+ "</real>"
			+ "</plist>";
	public static final String VALID_PLIST_INTEGER_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">"
			+ "<integer>"
			+ "1"
			+ "</integer>"
			+ "</plist>";
	public static final String VALID_PLIST_TRUE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">" + "<true />" + "</plist>";
	public static final String VALID_PLIST_FALSE_ROOT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
			+ "<plist version=\"1.0\">" + "<false />" + "</plist>";

	/**
	 * The class under test.
	 */
	protected PListXMLParser parser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		parser = new PListXMLParser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		parser = null;
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#getHandler()}
	 * and
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#setHandler(com.longevitysoft.android.test.plist.xml.PListXMLHandler)}
	 * .
	 */
	public void testHandlerGetterSetter() {
		assertNull(parser.getHandler());
		PListXMLHandler expected = new PListXMLHandler();
		parser.setHandler(expected);
		assertEquals(expected, parser.getHandler());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseWithNoHandler() {
		try {
			parser.parse("");
		} catch (Exception e) {
			assertEquals(IllegalStateException.class.getName(), e.getClass()
					.getName());
			return;
		}
		fail("expected exception not thrown");
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseNull() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse("");
		assertNull(((PListXMLHandler) parser.getHandler()).getPlist());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidXMLInvalidPList() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(INVALID_PLIST);
		assertNotNull(((PListXMLHandler) parser.getHandler()).getPlist());
		assertNull(((PListXMLHandler) parser.getHandler()).getPlist()
				.getRootElement());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidXMLWorkflowVersion() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_WORKFLOW_VERSION_PLIST);
		assertNotNull(((PListXMLHandler) parser.getHandler()).getPlist());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidXMLWorkflow() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_WORKFLOW_PLIST);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(6, ((Dict) actualPList.getRootElement())
				.getConfigurationArray("workflow_answers").size());
		assertEquals(3, ((Dict) actualPList.getRootElement())
				.getConfigurationArray("workflow_tasks").size());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListDictRootNestedDict() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_DICT_ROOT_NESTED_DICT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		Dict cat = (Dict) actualPList.getRootElement();
		assertEquals("901", cat.getConfiguration("ID").getValue());
		assertEquals("Title", cat.getConfiguration("title").getValue());
		assertEquals(new Integer(152), cat.getConfigurationInteger("thumb.ID")
				.getValue());
		assertEquals("http://www.google.com", cat.getConfiguration("thumb.uri")
				.getValue());
		assertEquals(new Integer(2), cat.getConfigurationInteger("order")
				.getValue());
		assertEquals(new Integer(5), cat.getConfigurationInteger("type")
				.getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListArrayRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_ARRAY_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(2, ((Array) actualPList.getRootElement()).size());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListArrayRootNestedArray() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_ARRAY_ROOT_NESTED_ARRAY);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(2, ((Array) actualPList.getRootElement()).size());
		assertEquals(2,
				((Array) ((Array) actualPList.getRootElement()).get(0)).size());
		assertEquals(2,
				((Array) ((Array) actualPList.getRootElement()).get(1)).size());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListStringRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_STRING_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertNotNull(actualPList.getRootElement());
		assertEquals(
				"foobar",
				((com.longevitysoft.android.xml.plist.domain.String) actualPList
						.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListDataRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_DATA_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals("foobar", ((Data) actualPList.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListISO8601DateRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_ISO8601_DATE_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		// 2012-02-24T10:10:00Z
		assertEquals(new Date("Fri, 24 Feb 2012 10:10:00 GMT-0700"),
				((com.longevitysoft.android.xml.plist.domain.Date) actualPList
						.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListDateRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_DATE_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(new Date("Sun, 13 Feb 2011 12:01:00 GMT-0500"),
				((com.longevitysoft.android.xml.plist.domain.Date) actualPList
						.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListRealRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_REAL_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(new Float(3.1417),
				((Real) actualPList.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListIntegerRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_INTEGER_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(
				new Integer(1).intValue(),
				((com.longevitysoft.android.xml.plist.domain.Integer) actualPList
						.getRootElement()).getValue().intValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListTrueRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_TRUE_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertTrue(((True) actualPList.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidPListFalseRoot() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_PLIST_FALSE_ROOT);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertFalse(((False) actualPList.getRootElement()).getValue());
	}

	/**
	 * Test method for
	 * {@link com.longevitysoft.android.plist.xml.PListXMLParser#parse(java.io.InputStream)}
	 * .
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void testParseValidPListArrayRootAsInputStream()
			throws IllegalStateException, IOException {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		InputStream bas = new ByteArrayInputStream(
				VALID_PLIST_ARRAY_ROOT.getBytes());
		parser.parse(bas);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(2, ((Array) actualPList.getRootElement()).size());
	}

}
