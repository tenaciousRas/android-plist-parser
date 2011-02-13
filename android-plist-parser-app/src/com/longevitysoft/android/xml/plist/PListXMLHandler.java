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

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import android.util.Log;

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Data;
import com.longevitysoft.android.xml.plist.domain.Date;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.False;
import com.longevitysoft.android.xml.plist.domain.Integer;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.longevitysoft.android.xml.plist.domain.Real;
import com.longevitysoft.android.xml.plist.domain.String;
import com.longevitysoft.android.xml.plist.domain.True;

/**
 * <p>
 * Parse the Dict workflow at http://www.galaxyzoo.org/workflows/default.plist.
 * Documentation on PLists can be found at: <a href=
 * "http://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/PropertyLists/AboutPropertyLists/AboutPropertyLists.html#//apple_ref/doc/uid/10000048i-CH3-SW2"
 * >The Mac OS X Reference</a>
 * </p>
 * 
 * @author fbeachler
 * 
 */
public class PListXMLHandler extends DefaultHandler2 {

	public static final java.lang.String PIPE = "|";

	public static final java.lang.String TAG = "PListXMLHandler";

	public static final java.lang.String TAG_PLIST = "plist";
	public static final java.lang.String TAG_DICT = "dict";
	public static final java.lang.String TAG_PLIST_ARRAY = "array";
	public static final java.lang.String TAG_KEY = "key";
	public static final java.lang.String TAG_INTEGER = "integer";
	public static final java.lang.String TAG_STRING = "string";
	public static final java.lang.String TAG_REAL = "real";
	public static final java.lang.String TAG_DATE = "date";
	public static final java.lang.String TAG_BOOL_TRUE = "true";
	public static final java.lang.String TAG_BOOL_FALSE = "false";
	public static final java.lang.String TAG_DATA = "data";

	/**
	 * Defines the modes the parser reports to registered listeners.
	 * 
	 * @author fbeachler
	 * 
	 */
	public enum ParseMode {
		START_TAG, END_TAG
	};

	/**
	 * Implementors can listen for events defined by {@link ParseMode}.
	 * 
	 * @author fbeachler
	 * 
	 */
	public static interface PListParserListener {
		public void onPListParseDone(PList pList, ParseMode mode);
	}

	/**
	 * Stores the state of the dictionary item during parsing - used for
	 * associating array parents to items being parsed.
	 * 
	 * @author fbeachler
	 */
	public class StatefulDictionaryItem {
		public Dict dict;
		public Array arr;
	}

	/**
	 * {@link Stringer} for this class.
	 */
	private Stringer stringer;

	/**
	 * Listener for this parser.
	 */
	private PListParserListener parseListener;

	/**
	 * The value of parsed characters from elements and attributes.
	 */
	private Stringer tempVal;

	/**
	 * The parsed {@link PList}.
	 */
	private PList pList;

	// Registers to hold state of parsing the workflow as Dict
	protected boolean parentIsDict;
	protected boolean parentIsArray;
	protected int arrayDepth;
	protected int elementDepth;
	protected java.lang.String key;
	protected Stack<PListObject> stack;

	/**
	 * 
	 */
	public PListXMLHandler() {
		super();
		stringer = new Stringer();
	}

	/**
	 * @return the pList
	 */
	public PList getPlist() {
		return pList;
	}

	/**
	 * @param pList
	 *            the pList to set
	 */
	public void setPlist(PList plist) {
		this.pList = plist;
	}

	/**
	 * @return the parseListener
	 */
	public PListParserListener getParseListener() {
		return parseListener;
	}

	/**
	 * @param parseListener
	 *            the parseListener to set
	 */
	public void setParseListener(PListParserListener parseListener) {
		this.parseListener = parseListener;
	}

	/**
	 * @return the tempVal
	 */
	public Stringer getTempVal() {
		return tempVal;
	}

	/**
	 * @param tempVal
	 *            the tempVal to set
	 */
	public void setTempVal(Stringer tempVal) {
		this.tempVal = tempVal;
	}

	/**
	 * @param pList
	 * @param elementDepth
	 * @param parentIsArray
	 * @param parentIsDict
	 * @param stack
	 * @param obj
	 * @param key
	 */
	protected void attachPListObjToParent(PList pList, int elementDepth,
			boolean parentIsArray, boolean parentIsDict,
			Stack<PListObject> stack, PListObject obj, java.lang.String key) {
		if (parentIsArray) {
			// attach obj to array parent
			attachPListObjToArrayParent(stack, obj);
		} else if (parentIsDict) {
			// attach obj to dict parent
			attachPListObjToDictParent(stack, key, obj);
		} else if (elementDepth == 1) {
			// set root DICT elm
			pList.setRootElement(obj);
		}
	}

	/**
	 * @param stack
	 * @param key
	 * @param obj
	 */
	private void attachPListObjToDictParent(Stack<PListObject> stack,
			java.lang.String key, PListObject obj) {
		Log.v(stringer.newBuilder().append(TAG)
				.append("#attachPListObjToDictParent").toString(),
				stringer.newBuilder().append("key|obj-type|obj: ").append(key)
						.append(PIPE).append(obj.getType()).append(PIPE)
						.append(obj.toString()).append(PIPE).toString());
		Dict parent = (Dict) stack.pop();
		parent.putConfig(key, obj);
		stack.push(parent);
	}

	/**
	 * @param stack
	 * @param key
	 * @param obj
	 */
	private void attachPListObjToArrayParent(Stack<PListObject> stack,
			PListObject obj) {
		Log.v(stringer.newBuilder().append(TAG)
				.append("#attachPListObjToArrayParent").toString(),
				stringer.newBuilder().append("obj-type|obj: ").append(PIPE)
						.append(obj.getType()).append(PIPE)
						.append(obj.toString()).append(PIPE).toString());
		Array parent = (Array) stack.pop();
		parent.add(obj);
		stack.push(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		tempVal = new Stringer();
		pList = null;
		parentIsDict = false;
		parentIsArray = false;
		arrayDepth = 0;
		elementDepth = 0;
		key = null;
		stack = new Stack<PListObject>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(java.lang.String uri, java.lang.String localName,
			java.lang.String qName, Attributes attributes) throws SAXException {
		Log.v(stringer.newBuilder().append(TAG).append("#startElement")
				.toString(),
				stringer.newBuilder()
						.append("Start Element lname|uri|attr.length :")
						.append(localName).append(PIPE).append(uri)
						.append(PIPE).append(attributes.getLength()).toString());
		tempVal.newBuilder();
		if (localName.equalsIgnoreCase(TAG_PLIST)) {
			pList = new PList();
			if (elementDepth != 0) {
				// there should only be one PList element in the root
				throw new SAXException(
						"there should only be one PList element in PList XML");
			}
			elementDepth = 1;
		} else {
			if (elementDepth == 0) {
				throw new SAXException(
						"invalid PList - please see http://www.apple.com/DTDs/PropertyList-1.0.dtd");
			}
			if (localName.equalsIgnoreCase(TAG_DICT)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList Dict elements should have a valid parent");
				}
				Dict dict = new Dict();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, dict, key);
				stack.push(dict);
				parentIsArray = false;
				parentIsDict = true;
			}
			if (localName.equalsIgnoreCase(TAG_PLIST_ARRAY)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList Array elements should have a valid parent");
				}
				Array array = new Array();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, array, key);
				stack.push(array);
				parentIsArray = true;
				parentIsDict = false;
			}
			if (localName.equalsIgnoreCase(TAG_DATA)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'data' elements should have a valid parent");
				}
				Data data = new Data();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, data, key);
			}
			if (localName.equalsIgnoreCase(TAG_STRING)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'string' elements should have a valid parent");
				}
				String str = new String();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, str, key);
			}
			if (localName.equalsIgnoreCase(TAG_REAL)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'real' elements should have a valid parent");
				}
				Real real = new Real();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, real, key);
			}
			if (localName.equalsIgnoreCase(TAG_INTEGER)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'integer' elements should have a valid parent");
				}
				Integer integer = new Integer();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, integer, key);
			}
			if (localName.equalsIgnoreCase(TAG_DATE)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'date' elements should have a valid parent");
				}
				Date date = new Date();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, date, key);
			}
			if (localName.equalsIgnoreCase(TAG_BOOL_FALSE)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'false' elements should have a valid parent");
				}
				False f = new False();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, f, key);
			}
			if (localName.equalsIgnoreCase(TAG_BOOL_TRUE)) {
				if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
					// if Dict is not at root, its parent should be an array or
					// Dict
					throw new SAXException(
							"PList 'true' elements should have a valid parent");
				}
				True t = new True();
				attachPListObjToParent(pList, elementDepth, parentIsArray,
						parentIsDict, stack, t, key);
			}
			elementDepth++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		Log.v(stringer.newBuilder().append(TAG).append("#characters")
				.toString(), stringer.newBuilder().append(ch).append(PIPE)
				.append(start).append(PIPE).append(length).append(PIPE)
				.toString());
		tempVal.getBuilder().append(new java.lang.String(ch, start, length));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(java.lang.String uri, java.lang.String localName,
			java.lang.String qName) throws SAXException {
		Log.v(stringer.newBuilder().append(TAG).append("#endElement")
				.toString(),
				stringer.newBuilder().append("localName|qName|uri|tempVal: ")
						.append(localName).append(PIPE).append(qName)
						.append(PIPE).append(uri).append(PIPE)
						.append(tempVal.getBuilder().toString()).toString());
		if (localName.equalsIgnoreCase(TAG_KEY)) {
			key = tempVal.getBuilder().toString().trim();
		}
		if (localName.equalsIgnoreCase(TAG_INTEGER)) {
			Integer intgr = null;
			if (!stack.empty()) {
				PListObject parent = stack.pop();
				if (parentIsArray) {
					Array castedParent = (Array) parent;
					if (!castedParent.isEmpty()) {
						intgr = (Integer) castedParent
								.get(castedParent.size() - 1);
					} else {
						// something awry in denmark
						throw new SAXException(
								"invalid plist - no integer element at end of array parent");
					}
				} else if (parentIsDict) {
					Dict castedParent = (Dict) parent;
					if (castedParent.getConfigMap().containsKey(key)) {
						intgr = (Integer) castedParent
								.getConfigurationInteger(key);
					} else {
						// something awry in denmark
						throw new SAXException(
								stringer.newBuilder()
										.append("invalid plist - no integer element in dict parent with key: ")
										.append(key).toString());
					}
				}
				intgr.setValue(tempVal.getBuilder().toString());
				stack.push(parent);
			} else {
				intgr = (Integer) pList.getRootElement();
				intgr.setValue(tempVal.getBuilder().toString());
			}
		}
		if (localName.equalsIgnoreCase(TAG_STRING)) {
			String str = null;
			if (!stack.empty()) {
				PListObject parent = stack.pop();
				if (parentIsArray) {
					Array castedParent = (Array) parent;
					if (!castedParent.isEmpty()) {
						str = (String) castedParent
								.get(castedParent.size() - 1);
					} else {
						// something awry in denmark
						throw new SAXException(
								"invalid plist - no string element at end of array parent");
					}
				} else if (parentIsDict) {
					Dict castedParent = (Dict) parent;
					if (castedParent.getConfigMap().containsKey(key)) {
						str = (String) castedParent.getConfiguration(key);
					} else {
						// something awry in denmark
						throw new SAXException(
								stringer.newBuilder()
										.append("invalid plist - no string element in dict parent with key: ")
										.append(key).toString());
					}
				}
				str.setValue(tempVal.getBuilder().toString().trim());
				stack.push(parent);
			} else {
				str = (String) pList.getRootElement();
				str.setValue(tempVal.getBuilder().toString().trim());
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_REAL)) {
			Real real = null;
			if (!stack.empty()) {
				PListObject parent = stack.pop();
				if (parentIsArray) {
					Array castedParent = (Array) parent;
					if (!castedParent.isEmpty()) {
						real = (Real) castedParent.get(castedParent.size() - 1);
					} else {
						// something awry in denmark
						throw new SAXException(
								"invalid plist - no real element in array parent");
					}
				} else if (parentIsDict) {
					Dict castedParent = (Dict) parent;
					if (castedParent.getConfigMap().containsKey(key)) {
						real = (Real) castedParent.getConfigurationObject(key);
					} else {
						// something awry in denmark
						throw new SAXException(
								stringer.newBuilder()
										.append("invalid plist - no real element in dict parent with key: ")
										.append(key).toString());
					}
				}
				real.setValue(tempVal.getBuilder().toString());
				stack.push(parent);
			} else {
				real = (Real) pList.getRootElement();
				real.setValue(tempVal.getBuilder().toString());
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_DATE)) {
			Date date = null;
			if (!stack.empty()) {
				PListObject parent = stack.pop();
				if (parentIsArray) {
					Array castedParent = (Array) parent;
					if (!castedParent.isEmpty()) {
						date = (Date) castedParent.get(castedParent.size() - 1);
					} else {
						// something awry in denmark
						throw new SAXException(
								"invalid plist - no real element in array parent");
					}
				} else if (parentIsDict) {
					Dict castedParent = (Dict) parent;
					if (castedParent.getConfigMap().containsKey(key)) {
						date = (Date) castedParent.getConfigurationObject(key);
					} else {
						// something awry in denmark
						throw new SAXException(
								stringer.newBuilder()
										.append("invalid plist - no real element in dict parent with key: ")
										.append(key).toString());
					}
				}
				date.setValue(tempVal.getBuilder().toString().trim());
				stack.push(parent);
			} else {
				date = (Date) pList.getRootElement();
				date.setValue(tempVal.getBuilder().toString().trim());
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_FALSE)) {
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_TRUE)) {
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_DATA)) {
			Data data = null;
			if (!stack.empty()) {
				PListObject parent = stack.pop();
				if (parentIsArray) {
					Array castedParent = (Array) parent;
					if (!castedParent.isEmpty()) {
						data = (Data) castedParent.get(castedParent.size() - 1);
					} else {
						// something awry in denmark
						throw new SAXException(
								"invalid plist - no data element in array parent");
					}
				} else if (parentIsDict) {
					Dict castedParent = (Dict) parent;
					if (castedParent.getConfigMap().containsKey(key)) {
						data = (Data) castedParent.getConfigurationObject(key);
					} else {
						// something awry in denmark
						throw new SAXException(
								stringer.newBuilder()
										.append("invalid plist - no data element in dict parent with key: ")
										.append(key).toString());
					}
				}
				stack.push(parent);
			} else {
				data = (Data) pList.getRootElement();
				// TODO BASE64 decode tempVal
				data.setValue(tempVal.getBuilder().toString().trim());
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_DICT)) {
			if (!stack.empty()) {
				stack.pop();
				if (!stack.empty()) {
					PListObject parent = stack.pop();
					if (parent instanceof Array) {
						parentIsDict = false;
						parentIsArray = true;
					}
					if (parent instanceof Dict) {
						parentIsDict = true;
						parentIsArray = false;
					}
					stack.push(parent);
				}
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_PLIST_ARRAY)) {
			if (!stack.empty()) {
				stack.pop();
				if (!stack.empty()) {
					PListObject parent = stack.pop();
					if (parent instanceof Array) {
						parentIsDict = false;
						parentIsArray = true;
					}
					if (parent instanceof Dict) {
						parentIsDict = true;
						parentIsArray = false;
					}
					stack.push(parent);
				}
			}
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_PLIST)) {
			parentIsArray = false;
			if (null != parseListener) {
				parseListener.onPListParseDone(pList, ParseMode.END_TAG);
			}
		}
		tempVal.newBuilder();

	}

}
