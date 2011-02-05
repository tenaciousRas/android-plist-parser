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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList.Dict;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList.PListArray;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PList.PListObject;

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

	public static final java.lang.String PIPE = "|";

	public static final java.lang.String TAG = "PListXMLHandler";

	public static final java.lang.String TAG_PLIST = "plist";
	public static final java.lang.String TAG_DICT = "dict";
	public static final java.lang.String TAG_PLIST_ARRAY = "array";
	public static final java.lang.String TAG_KEY = "key";
	public static final java.lang.String TAG_INTEGER = "integer";
	public static final java.lang.String TAG_STRING = "java.lang.String";
	public static final java.lang.String TAG_REAL = "real";
	public static final java.lang.String TAG_BOOL_TRUE = "true";
	public static final java.lang.String TAG_BOOL_FALSE = "false";
	public static final java.lang.String TAG_DATA = "data";

	/**
	 * A PList class contains the objects and methods used to build and access a
	 * PList.
	 */
	public static class PList {

		/**
		 * A PListObject is an object which has a valid {@link PListObjectType}.
		 */
		public static class PListObject extends Object implements Cloneable,
				Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5258056855425643835L;

			/**
			 * Defines valid PList object types. These correspond to the
			 * elements defined in the PList XML DTD at {@link http
			 * ://www.apple.com/DTDs/PropertyList-1.0.dtd}.
			 * 
			 * @author fbeachler
			 * 
			 */
			public static enum PListObjectType {
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

		/**
		 * Interface for that simple PList objects implement. This includes all
		 * objects aside from arrays and dicts.
		 */
		public static interface PListSimpleObject<E extends Object> {
			public E getValue();

			public void setValue(E val);
		}

		/**
		 * A PList Array object - essentially a proxy for a
		 * {@link java.util.List} implementation.
		 * 
		 * @author fbeachler
		 * 
		 */
		public static class PListArray extends PListObject implements
				java.util.List<PListObject> {

			private ArrayList<PListObject> data;

			/**
			 * 
			 */
			private static final long serialVersionUID = -2673110114913406413L;

			/**
			 * 
			 */
			public PListArray() {
				setType(PListObjectType.ARRAY);
				data = new ArrayList<PListObject>();
			}

			/**
			 * @param collection
			 */
			public PListArray(Collection<? extends PListObject> collection) {
				setType(PListObjectType.ARRAY);
				data = new ArrayList<PListObject>(collection);
			}

			/**
			 * @param capacity
			 */
			public PListArray(int capacity) {
				setType(PListObjectType.ARRAY);
				data = new ArrayList<PListObject>(capacity);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#add(int, java.lang.Object)
			 */
			@Override
			public void add(int arg0, PListObject arg1) {
				data.add(arg0, (PListObject) arg1);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#add(java.lang.Object)
			 */
			@Override
			public boolean add(PListObject arg0) {
				return data.add((PListObject) arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#addAll(java.util.Collection)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public boolean addAll(Collection arg0) {
				return data.addAll(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#addAll(int, java.util.Collection)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public boolean addAll(int arg0, Collection arg1) {
				return data.addAll(arg0, arg1);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#isEmpty()
			 */
			@Override
			public boolean isEmpty() {
				return data.isEmpty();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#lastIndexOf(java.lang.Object)
			 */
			@Override
			public int lastIndexOf(Object arg0) {
				return data.indexOf(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#listIterator()
			 */
			@Override
			public ListIterator<PListObject> listIterator() {
				return data.listIterator();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#listIterator(int)
			 */
			@Override
			public ListIterator<PListObject> listIterator(int arg0) {
				return data.listIterator(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#remove(int)
			 */
			@Override
			public PListObject remove(int arg0) {
				return data.remove(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#remove(java.lang.Object)
			 */
			@Override
			public boolean remove(Object arg0) {
				return data.remove(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#removeAll(java.util.Collection)
			 */
			@Override
			public boolean removeAll(Collection<?> arg0) {
				return data.remove(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#retainAll(java.util.Collection)
			 */
			@Override
			public boolean retainAll(Collection<?> arg0) {
				return data.retainAll(arg0);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#set(int, java.lang.Object)
			 */
			@Override
			public PListObject set(int arg0, PListObject arg1) {
				return data.set(arg0, arg1);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#subList(int, int)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public List subList(int arg0, int arg1) {
				return data.subList(arg0, arg1);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#toArray()
			 */
			@Override
			public Object[] toArray() {
				return data.toArray();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#toArray(T[])
			 */
			@SuppressWarnings("unchecked")
			@Override
			public Object[] toArray(Object[] array) {
				return data.toArray(array);
			}

			/**
			 * @see {@link java.util.ArrayList#clear()}
			 */
			public void clear() {
				data.clear();
			}

			/**
			 * @see {@link java.util.ArrayList#clone()}
			 */
			public Object clone() {
				return data.clone();
			}

			/**
			 * @see {@link java.util.ArrayList#contains(Object)}
			 */
			public boolean contains(Object obj) {
				return data.contains(obj);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.List#containsAll(java.util.Collection)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public boolean containsAll(Collection arg0) {
				return data.contains(arg0);
			}

			/**
			 * @see {@link java.util.ArrayList#equals(Object)}
			 */
			public boolean equals(Object that) {
				return data.equals(that);
			}

			/**
			 * @see {@link java.util.ArrayList#get(int)}
			 */
			public PListObject get(int index) {
				return data.get(index);
			}

			/**
			 * @see {@link java.util.ArrayList#indexOf(Object)}
			 */
			public int indexOf(Object object) {
				return data.indexOf(object);
			}

			/**
			 * @see {@link java.util.ArrayList#iterator()}
			 */
			public Iterator<PListObject> iterator() {
				return data.iterator();
			}

			/**
			 * @see {@link java.util.ArrayList#size()}
			 */
			public int size() {
				return data.size();
			}

		}

		/**
		 * POJO for simple plist data elements.
		 */
		public static class Data extends PListObject implements
				PListSimpleObject<java.lang.String> {

			protected java.lang.String data;

			/**
			 * 
			 */
			private static final long serialVersionUID = -3101592260075687323L;

			public Data() {
				setType(PListObjectType.DATA);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public java.lang.String getValue() {
				return (java.lang.String) data;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#setValue(java.lang.Object)
			 */
			@Override
			public void setValue(java.lang.String val) {
				this.data = val;
			}

		}

		/**
		 * POJO for simple plist date elements.
		 */
		public static class Date extends PListObject implements
				PListSimpleObject<java.util.Date> {

			protected java.util.Date date;

			/**
			 * 
			 */
			private static final long serialVersionUID = 3846688440069431376L;

			public Date() {
				setType(PListObjectType.DATE);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public java.util.Date getValue() {
				return (java.util.Date) date;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#setValue(java.lang.Object)
			 */
			@Override
			public void setValue(java.util.Date val) {
				this.date = val;
			}

		}

		/**
		 * POJO for simple plist real element.
		 */
		public static class Real extends PListObject implements
				PListSimpleObject<Float> {

			protected Float real;

			/**
			 * 
			 */
			private static final long serialVersionUID = -4204214862534504729L;

			public Real() {
				setType(PListObjectType.REAL);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public Float getValue() {
				return real;
			}

			@Override
			public void setValue(Float val) {
				this.real = val;
			}

		}

		/**
		 * POJO for simple plist int element.
		 */
		public static class Integer extends PListObject implements
				PListSimpleObject<java.lang.Integer> {

			protected java.lang.Integer intgr;

			/**
			 * 
			 */
			private static final long serialVersionUID = -5952071046933925529L;

			public Integer() {
				setType(PListObjectType.INTEGER);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public java.lang.Integer getValue() {
				return intgr;
			}

			@Override
			public void setValue(java.lang.Integer val) {
				this.intgr = val;
			}

		}

		/**
		 * POJO for simple plist string element. Not to be confused with
		 * {@link java.lang.String}.
		 */
		public static class String extends PListObject implements
				PListSimpleObject<java.lang.String> {

			protected java.lang.String str;

			/**
			 * 
			 */
			private static final long serialVersionUID = -8134261357175236382L;

			public String() {
				setType(PListObjectType.STRING);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public java.lang.String getValue() {
				return this.str;
			}

			@Override
			public void setValue(java.lang.String val) {
				this.str = val;
			}

		}

		/**
		 * POJO for simple plist true element.
		 */
		public static class True extends PListObject implements
				PListSimpleObject<Boolean> {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3560354198720649001L;

			public True() {
				setType(PListObjectType.TRUE);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public Boolean getValue() {
				return new Boolean(true);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#setValue(java.lang.Object)
			 */
			@Override
			public void setValue(Boolean val) {
				// noop
			}

		}

		/**
		 * POJO for simple plist false element.
		 */
		public static class False extends PListObject implements
				PListSimpleObject<Boolean> {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8533886020773567552L;

			public False() {
				setType(PListObjectType.FALSE);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#getValue()
			 */
			@Override
			public Boolean getValue() {
				return new Boolean(false);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seecom.longevitysoft.android.xml.plist.PListXMLHandler.PList.
			 * PListSimpleObject#setValue(java.lang.Object)
			 */
			@Override
			public void setValue(Boolean val) {
				// noop
			}

		}

		/**
		 * @author fbeachler
		 * 
		 */
		public static class Dict extends PListObject {

			/**
			 * 
			 */
			private static final long serialVersionUID = -556589348083152733L;

			public static final java.lang.String DOT = ".";
			protected Map<java.lang.String, PListObject> configMap;

			public Dict() {
				configMap = new TreeMap<java.lang.String, PListObject>();
				setType(PListObjectType.DICT);
			}

			/**
			 * Put the config value with the given key.
			 * 
			 * @param key
			 * @param value
			 */
			public void putConfig(java.lang.String key, PListObject value) {
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
			public Map<java.lang.String, PListObject> getConfigMap() {
				return configMap;
			}

			/**
			 * @param configMap
			 *            the configMap to set
			 */
			public void setConfigMap(
					Map<java.lang.String, PListObject> configMap) {
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
			private Object getConfigurationObject(java.lang.String key) {
				StringTokenizer st = new StringTokenizer(key, DOT);

				if (st.hasMoreTokens()) {
					Map<java.lang.String, PListObject> dict = configMap;
					Object obj;
					while (st.hasMoreTokens()) {
						java.lang.String token = st.nextToken();
						obj = dict.get(token);
						if (obj instanceof TreeMap<?, ?>) {
							dict = (Map<java.lang.String, PListObject>) obj;
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
			public PList.String getConfiguration(java.lang.String key) {
				return (PList.String) getConfigurationObject(key);
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
			public PList.String getConfigurationWithDefault(
					java.lang.String key, PList.String defaultValue) {
				PList.String value = getConfiguration(key);
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
			public PList.Integer getConfigurationInteger(java.lang.String key) {
				return (PList.Integer) getConfigurationObject(key);
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
			public PList.Integer getConfigurationIntegerWithDefault(
					java.lang.String key, PList.Integer defaultValue) {
				PList.Integer value = getConfigurationInteger(key);
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
			public PList.PListArray getConfigurationArray(java.lang.String key) {
				return (PList.PListArray) getConfigurationObject(key);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#toString()
			 */
			@Override
			public java.lang.String toString() {
				StringBuilder retVal = new StringBuilder();
				Set<java.lang.String> keys = configMap.keySet();
				Iterator<java.lang.String> it = keys.iterator();
				while (it.hasNext()) {
					java.lang.String key = it.next();
					retVal.append("key=").append(key).append(
							configMap.get(key).toString());
				}
				return retVal.toString();
			}

		}

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
		public PList.Dict dict;
		public PListArray arr;
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
	 * @param stack
	 * @param key
	 * @param obj
	 */
	private void attachPListObjToDictParent(Stack<PListObject> stack,
			java.lang.String key, PListObject obj) {
		Log.v(stringer.newBuilder().append(TAG).append(
				"#attachPListObjToDictParent").toString(), stringer
				.newBuilder().append("key|obj-type|obj: ").append(key).append(
						PIPE).append(obj.getType()).append(PIPE).append(
						obj.toString()).append(PIPE).toString());
		PList.Dict parent = (PList.Dict) stack.pop();
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
		Log.v(stringer.newBuilder().append(TAG).append(
				"#attachPListObjToArrayParent").toString(), stringer
				.newBuilder().append("obj-type|obj: ").append(PIPE).append(
						obj.getType()).append(PIPE).append(obj.toString())
				.append(PIPE).toString());
		PListArray parent = (PListArray) stack.pop();
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
				.toString(), stringer.newBuilder().append(
				"Start Element lname|uri|attr.length :").append(localName)
				.append(PIPE).append(uri).append(PIPE).append(
						Integer.toString(attributes.getLength())).toString());
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
		}
		if (localName.equalsIgnoreCase(TAG_DICT)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList Dict elements should have a valid parent");
			}
			Dict dict = new PList.Dict();
			if (parentIsArray) {
				// attach dict to parent
				attachPListObjToArrayParent(stack, dict);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, dict);
			} else if (elementDepth == 1) {
				// set root DICT elm
				pList.setRootElement(dict);
			}
			stack.push(dict);
			parentIsArray = false;
			parentIsDict = true;
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_PLIST_ARRAY)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList Array elements should have a valid parent");
			}
			PListArray array = new PListArray();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, array);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, array);
			} else if (elementDepth == 1) {
				// set root array elm
				pList.setRootElement(array);
			}
			stack.push(array);
			parentIsArray = true;
			parentIsDict = false;
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_DATA)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'data' elements should have a valid parent");
			}
			PList.Data data = new PList.Data();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, data);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, data);
			} else if (elementDepth == 1) {
				pList.setRootElement(data);
			}
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_STRING)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'string' elements should have a valid parent");
			}
			PList.Date date = new PList.Date();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, date);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, date);
			} else if (elementDepth == 1) {
				pList.setRootElement(date);
			}
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_REAL)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'real' elements should have a valid parent");
			}
			PList.Real real = new PList.Real();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, real);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, real);
			} else if (elementDepth == 1) {
				pList.setRootElement(real);
			}
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_INTEGER)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'integer' elements should have a valid parent");
			}
			PList.Integer integer = new PList.Integer();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, integer);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, integer);
			} else if (elementDepth == 1) {
				pList.setRootElement(integer);
			}
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_FALSE)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'false' elements should have a valid parent");
			}
			PList.False f = new PList.False();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, f);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, f);
			} else if (elementDepth == 1) {
				pList.setRootElement(f);
			}
			elementDepth++;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_TRUE)) {
			if (elementDepth > 1 && !parentIsDict && !parentIsArray) {
				// if Dict is not at root, its parent should be an array or Dict
				throw new SAXException(
						"PList 'true' elements should have a valid parent");
			}
			PList.True t = new PList.True();
			if (parentIsArray) {
				attachPListObjToArrayParent(stack, t);
			} else if (parentIsDict) {
				attachPListObjToDictParent(stack, key, t);
			} else if (elementDepth == 1) {
				pList.setRootElement(t);
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
				.toString(), stringer.newBuilder().append(
				"localName|qName|uri|tempVal: ").append(localName).append(PIPE)
				.append(qName).append(PIPE).append(uri).append(PIPE).append(
						tempVal.getBuilder().toString()).toString());
		if (localName.equalsIgnoreCase(TAG_KEY)) {
			key = tempVal.getBuilder().toString().trim();
		}
		if (localName.equalsIgnoreCase(TAG_INTEGER)) {
			PList.Integer intgr = new PList.Integer();
			PListObject parent = stack.pop();
			if (parentIsArray) {
				PListArray castedParent = (PListArray) parent;
				if (!castedParent.isEmpty()) {
					intgr = (PList.Integer) castedParent.get(castedParent
							.size() - 1);
				} else {
					// something awry in denmark
					throw new SAXException(
							"invalid plist - no integer element at end of array parent");
				}
			} else if (parentIsDict) {
				Dict castedParent = (Dict) parent;
				if (castedParent.getConfigMap().containsKey(key)) {
					intgr = (PList.Integer) castedParent
							.getConfigurationInteger(key);
				} else {
					// something awry in denmark
					throw new SAXException(
							stringer
									.newBuilder()
									.append(
											"invalid plist - no integer element in dict parent with key: ")
									.append(key).toString());
				}
			}
			intgr.setValue(Integer.getInteger(tempVal.getBuilder().toString()
					.trim()));
			stack.push(parent);
		}
		if (localName.equalsIgnoreCase(TAG_STRING)) {
			PList.String str = new PList.String();
			PListObject parent = stack.pop();
			if (parentIsArray) {
				PListArray castedParent = (PListArray) parent;
				if (!castedParent.isEmpty()) {
					str = (PList.String) castedParent
							.get(castedParent.size() - 1);
				} else {
					// something awry in denmark
					throw new SAXException(
							"invalid plist - no string element at end of array parent");
				}
			} else if (parentIsDict) {
				Dict castedParent = (Dict) parent;
				if (castedParent.getConfigMap().containsKey(key)) {
					str = (PList.String) castedParent.getConfiguration(key);
				} else {
					// something awry in denmark
					throw new SAXException(
							stringer
									.newBuilder()
									.append(
											"invalid plist - no string element in dict parent with key: ")
									.append(key).toString());
				}
			}
			str.setValue(tempVal.getBuilder().toString().trim());
			stack.push(parent);
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_REAL)) {
			PList.Real real = new PList.Real();
			PListObject parent = stack.pop();
			if (parentIsArray) {
				PListArray castedParent = (PListArray) parent;
				if (!castedParent.isEmpty()) {
					real = (PList.Real) castedParent
							.get(castedParent.size() - 1);
				} else {
					// something awry in denmark
					throw new SAXException(
							"invalid plist - no real element in array parent");
				}
			} else if (parentIsDict) {
				Dict castedParent = (Dict) parent;
				if (castedParent.getConfigMap().containsKey(key)) {
					real = (PList.Real) castedParent
							.getConfigurationObject(key);
				} else {
					// something awry in denmark
					throw new SAXException(
							stringer
									.newBuilder()
									.append(
											"invalid plist - no real element in dict parent with key: ")
									.append(key).toString());
				}
			}
			real.setValue(Float.parseFloat(tempVal.getBuilder().toString()
					.trim()));
			stack.push(parent);
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_FALSE)) {
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_BOOL_TRUE)) {
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_DATA)) {
			PList.String str = new PList.String();
			PListObject parent = stack.pop();
			if (parentIsArray) {
				PListArray castedParent = (PListArray) parent;
				if (!castedParent.isEmpty()) {
					str = (PList.String) castedParent
							.get(castedParent.size() - 1);
				} else {
					// something awry in denmark
					throw new SAXException(
							"invalid plist - no data element in array parent");
				}
			} else if (parentIsDict) {
				Dict castedParent = (Dict) parent;
				if (castedParent.getConfigMap().containsKey(key)) {
					str = (PList.String) castedParent.getConfiguration(key);
				} else {
					// something awry in denmark
					throw new SAXException(
							stringer
									.newBuilder()
									.append(
											"invalid plist - no data element in dict parent with key: ")
									.append(key).toString());
				}
			}
			// TODO BASE64 decode tempVal
			str.setValue(tempVal.getBuilder().toString().trim());
			stack.push(parent);
			elementDepth--;
		}
		if (localName.equalsIgnoreCase(TAG_DICT)) {
			if (!stack.empty()) {
				stack.pop();
				if (!stack.empty()) {
					PListObject parent = stack.pop();
					if (parent instanceof PList.PListArray) {
						parentIsDict = false;
						parentIsArray = true;
					}
					if (parent instanceof PList.Dict) {
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
					if (parent instanceof PList.PListArray) {
						parentIsDict = false;
						parentIsArray = true;
					}
					if (parent instanceof PList.Dict) {
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
