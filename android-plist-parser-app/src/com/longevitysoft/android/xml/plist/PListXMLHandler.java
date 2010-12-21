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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import android.util.Log;

import com.longevitysoft.android.util.Stringer;

/**
 * <p>
 * Parse the Dict workflow at http://www.galaxyzoo.org/workflows/default.plist.
 * Documentation on PLists can be found at: <a href="http://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/PropertyLists/AboutPropertyLists/AboutPropertyLists.html#//apple_ref/doc/uid/10000048i-CH3-SW2"
 * >The Mac OS X Reference</a>
 * </p>
 * 
 * @author fbeachler
 * 
 */
public class PListXMLHandler extends DefaultHandler2 {

	public static final String PIPE = "|";

	public static final String TAG = "PListXMLHandler";

	public static final String TAG_PLIST = "plist";
	public static final String TAG_DICT = "dict";
	public static final String TAG_PLIST_ARRAY = "array";
	public static final String TAG_KEY = "key";
	public static final String TAG_INTEGER = "integer";
	public static final String TAG_STRING = "string";
	public static final String TAG_REAL = "real";
	public static final String TAG_BOOL_TRUE = "true";
	public static final String TAG_BOOL_FALSE = "false";
	public static final String TAG_DATA = "data";

	/**
	 * @author fbeachler
	 * 
	 */
	public static class PList {

		/**
		 * @author fbeachler
		 * 
		 */
		public static class Dict {

			public static final String DOT = ".";
			protected Map<String, Object> configMap;

			public Dict() {
				configMap = new TreeMap<String, Object>();
			}

			/**
			 * Put the config value with the given key.
			 * 
			 * @param key
			 * @param value
			 */
			public void putConfig(String key, Object value) {
				configMap.put(key, value);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			/**
			 * @return the configMap
			 */
			public Map<String, Object> getConfigMap() {
				return configMap;
			}

			/**
			 * @param configMap
			 *            the configMap to set
			 */
			public void setConfigMap(Map<String, Object> configMap) {
				this.configMap = configMap;
			}

			/**
			 * Utility method which tokenizes the given keyName using the "."
			 * delimiter and then looks up each token in the configuration
			 * dictionary. If the token key points to a dictionary then it
			 * proceeds to the next token key and looks up value of the token
			 * key in the dictionary it found from the previous token key.
			 * 
			 * @param key
			 *            The fully qualified key text.
			 * @return The Object value associated with the given key, or null
			 *         if the key does not exist.
			 */
			@SuppressWarnings("unchecked")
			private Object getConfigurationObject(String key) {
				StringTokenizer st = new StringTokenizer(key, DOT);

				if (st.hasMoreTokens()) {
					Map<String, Object> dict = configMap;
					Object obj;
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						obj = dict.get(token);
						if (obj instanceof TreeMap<?, ?>) {
							dict = (Map<String, Object>) obj;
							continue;
						}
						return obj;
					}
				}
				return configMap.get(key);
			}

			/**
			 * Get an String configuration value for the given key.
			 * 
			 * @param key
			 *            The text of the key to look up in the configuration
			 *            dictionary.
			 * @return The String value of the specified key.
			 */
			public String getConfiguration(String key) {
				return (String) getConfigurationObject(key);
			}

			/**
			 * Get a String configuration value for the given key. If there is
			 * no value for the given key, then return the default value.
			 * 
			 * @param key
			 *            The text of the key to look up in the configuration
			 *            dictionary.
			 * @param defaultValue
			 *            The default value to return if they key has no
			 *            associated value.
			 * @return The String value of the specified key, or defaultValue if
			 *         the value for keyName is null.
			 */
			public String getConfigurationWithDefault(String key,
					String defaultValue) {
				String value = getConfiguration(key);
				if (value == null) {
					return defaultValue;
				}

				return value;
			}

			/**
			 * Get an Integer configuration value for the given key.
			 * 
			 * @param key
			 *            The text of the key to look up in the configuration
			 *            dictionary.
			 * @return The Integer value of the specified key.
			 */
			public Integer getConfigurationInteger(String key) {
				return (Integer) getConfigurationObject(key);
			}

			/**
			 * Get an Integer configuration value for the given key. If there is
			 * no value for the given key, then return the default value.
			 * 
			 * @param key
			 *            The text of the key to look up in the configuration
			 *            dictionary.
			 * @param defaultValue
			 *            The default value to return if they key has no
			 *            associated value.
			 * @return The Integer value of the specified key, or defaultValue
			 *         if the value for keyName is null.
			 */
			public Integer getConfigurationIntegerWithDefault(String key,
					Integer defaultValue) {
				Integer value = getConfigurationInteger(key);
				if (value == null) {
					return defaultValue;
				}

				return value;
			}

			/**
			 * Get an Integer configuration value for the given key.
			 * 
			 * @param key
			 *            The text of the key to look up in the configuration
			 *            dictionary.
			 * @return The Integer value of the specified key.
			 */
			@SuppressWarnings("unchecked")
			public ArrayList<Object> getConfigurationArray(String key) {
				return (ArrayList<Object>) getConfigurationObject(key);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public String toString() {
				StringBuilder retVal = new StringBuilder();
				Set<String> keys = configMap.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					retVal.append("key=").append(key).append(
							configMap.get(key).toString());
				}
				return retVal.toString();
			}

		}

		/**
		 * The PList root config element.
		 */
		private Dict root;

		/**
		 * @return the PList root config element
		 */
		public Dict getRootDictionary() {
			return root;
		}

		/**
		 * @param root
		 *            the PList root config to set
		 */
		public void setRootDictionary(Dict dict) {
			this.root = dict;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (null == root) {
				return null;
			}
			return root.toString();
		}

	}

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
	 * Stored the state of the dictionary item during parsing - used for
	 * associating array parents to items being parsed.
	 * 
	 * @author fbeachler
	 */
	public class StatefulDictionaryItem {
		public PList.Dict dict;
		public ArrayList<Object> arr;
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
	protected boolean parentIsArray;
	protected int arrayDepth;
	protected String key;
	protected Stack<StatefulDictionaryItem> stack;
	protected PList.Dict dict;
	protected PList.Dict childDict;
	protected ArrayList<Object> array;

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
		parentIsArray = false;
		arrayDepth = 0;
		key = null;
		stack = new Stack<StatefulDictionaryItem>();
		dict = null;
		childDict = null;
		array = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Log.v(stringer.newBuilder().append(TAG).append("#startElement")
				.toString(), stringer.newBuilder().append("Start Element :")
				.append(localName).append(PIPE).append(uri).append(PIPE)
				.append(Integer.toString(attributes.getLength())).toString());
		tempVal.newBuilder();
		if (localName.equalsIgnoreCase(TAG_PLIST)) {
			pList = new PList();
		}
		if (localName.equalsIgnoreCase(TAG_DICT)) {
			if (null == key) {
				// set root DICT elm
				pList.setRootDictionary(new PList.Dict());
				dict = pList.getRootDictionary();
			} else if (parentIsArray) {
				childDict = new PList.Dict();
				array.add(childDict);
				StatefulDictionaryItem stackItem = new StatefulDictionaryItem();
				stackItem.dict = dict;
				stackItem.arr = array;
				stack.push(stackItem);
				dict = childDict;
			} else {
				childDict = new PList.Dict();
				dict.putConfig(key, childDict);
				StatefulDictionaryItem stackItem = new StatefulDictionaryItem();
				stackItem.dict = dict;
				stackItem.arr = null;
				stack.push(stackItem);
				dict = childDict;
			}
			parentIsArray = false;
		}
		if (localName.equalsIgnoreCase(TAG_PLIST_ARRAY)) {
			parentIsArray = true;
			array = new ArrayList<Object>();
			dict.putConfig(key, array);
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
		tempVal.getBuilder().append(new String(ch, start, length));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		Log.v(stringer.newBuilder().append(TAG).append("#endElement")
				.toString(), stringer.newBuilder().append(
				"localName|qName|uri|tempVal: ").append(localName).append(PIPE)
				.append(qName).append(PIPE).append(uri).append(PIPE).append(
						tempVal).toString());
		if (localName.equalsIgnoreCase(TAG_KEY)) {
			key = tempVal.getBuilder().toString().trim();
		}
		if (localName.equalsIgnoreCase(TAG_INTEGER)) {
			if (parentIsArray) {
				array.add(Integer.parseInt(tempVal.getBuilder().toString()
						.trim()));
			} else if (null != dict) {
				dict.putConfig(key, Integer.parseInt(tempVal.getBuilder()
						.toString().trim()));
			}
		}
		if (localName.equalsIgnoreCase(TAG_STRING)) {
			if (parentIsArray) {
				array.add(tempVal.getBuilder().toString());
			} else if (null != dict) {
				dict.putConfig(key, tempVal.getBuilder().toString());
			}
		}
		if (localName.equalsIgnoreCase(TAG_REAL)) {
			if (parentIsArray) {
				array.add(Float.parseFloat(tempVal.getBuilder().toString()
						.trim()));
			} else if (null != dict) {
				dict.putConfig(key, Float.parseFloat(tempVal.getBuilder()
						.toString().trim()));
			}
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_FALSE)) {
			if (parentIsArray) {
				array.add(false);
			} else if (null != dict) {
				dict.putConfig(key, false);
			}
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_TRUE)) {
			if (parentIsArray) {
				array.add(true);
			} else if (null != dict) {
				dict.putConfig(key, true);
			}
		}
		if (localName.equalsIgnoreCase(TAG_DATA)) {
			// TODO BASE64 decode tempVal
			if (parentIsArray) {
				array.add(tempVal.getBuilder().toString());
			} else if (null != dict) {
				dict.putConfig(key, tempVal.getBuilder().toString().trim());
			}
		}
		if (localName.equalsIgnoreCase(TAG_DICT)) {
			if (!stack.empty()) {
				StatefulDictionaryItem stackItem = stack.pop();
				dict = stackItem.dict;
				array = stackItem.arr;
				if (null != array) {
					parentIsArray = true;
				}
			}
		}
		if (localName.equalsIgnoreCase(TAG_PLIST_ARRAY)) {
			parentIsArray = false;
			array = null;
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
