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

import junit.framework.TestCase;

import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList;

/**
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
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#getHandler()}
	 * and
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#setHandler(org.zooniverse.android.galaxyzoo.xml.PListXMLHandler)}
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
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseWithNoHandler() {
		try {
			parser.parse(null);
		} catch (Exception e) {
			assertEquals(IllegalStateException.class.getName(), e.getClass()
					.getName());
			return;
		}
		fail("expected exception not thrown");
	}

	/**
	 * Test method for
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseNull() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(null);
		assertNull(((PListXMLHandler) parser.getHandler()).getPlist());
	}

	/**
	 * Test method for
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidXMLInvalidPList() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(INVALID_PLIST);
		assertNotNull(((PListXMLHandler) parser.getHandler()).getPlist());
		assertNull(((PListXMLHandler) parser.getHandler()).getPlist()
				.getRootDictionary());
	}

	/**
	 * Test method for
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#parse(java.lang.String)}
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
	 * {@link org.zooniverse.android.galaxyzoo.xml.PListXMLParser#parse(java.lang.String)}
	 * .
	 */
	public void testParseValidXMLWorkflow() {
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_WORKFLOW_PLIST);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
		assertNotNull(actualPList);
		assertEquals(6, actualPList.getRootDictionary().getConfigurationArray(
				"workflow_answers").size());
		assertEquals(3, actualPList.getRootDictionary().getConfigurationArray(
				"workflow_tasks").size());
	}

}
