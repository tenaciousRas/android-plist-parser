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
package com.longevitysoft.android.xml.plist.domain;

import java.util.Stack;

import android.util.Log;

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.Constants;

/**
 * A PList class contains the objects and methods used to build and access a
 * PList. TODO: refactor so this meets the contract stated above
 */
public class PList {

	public static final java.lang.String TAG = "PList";

	private Stringer stringer;

	/**
	 * The PList root config element.
	 */
	private PListObject root;

	private boolean stackCtxInDict;
	private boolean stackCtxInArray;
	private int stackCtxNestedDepth;
	// TODO - replace with some type of Map
	private Stack<PListObject> stack;

	public PList() {
		stringer = new Stringer();
		stackCtxInDict = false;
		stackCtxInArray = false;
		stackCtxNestedDepth = 0;
		stack = new Stack<PListObject>();
	}

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

	/**
	 * @param pList
	 * @param stackCtxNestedDepth
	 * @param stackCtxInArray
	 * @param stackCtxInDict
	 * @param stack
	 * @param obj
	 * @param key
	 */
	private void attachPListObjToParent(PListObject obj, java.lang.String key) {
		if (stackCtxInArray) {
			// attach obj to array parent
			attachPListObjToArrayParent(stack, obj);
		} else if (stackCtxInDict) {
			// attach obj to dict parent
			attachPListObjToDictParent(obj, key);
		} else if (stackCtxNestedDepth == 0) {
			// set root DICT elm
			setRootElement(obj);
		}
	}

	/**
	 * @param stack
	 * @param key
	 * @param obj
	 */
	private void attachPListObjToDictParent(PListObject obj,
			java.lang.String key) {
		Log.v(stringer.newBuilder().append(TAG)
				.append("#attachPListObjToDictParent").toString(),
				stringer.newBuilder().append("key|obj-type|obj: ").append(key)
						.append(Constants.PIPE).append(obj.getType())
						.append(Constants.PIPE).append(obj.toString())
						.append(Constants.PIPE).toString());
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
				stringer.newBuilder().append("obj-type|obj: ")
						.append(Constants.PIPE).append(obj.getType())
						.append(Constants.PIPE).append(obj.toString())
						.append(Constants.PIPE).toString());
		Array parent = (Array) stack.pop();
		parent.add(obj);
		stack.push(parent);
	}

	/**
	 * Stack an object onto {@link this}. Stacking means: sequentially adding
	 * {@PListObject}s onto the {@PList}. The previous
	 * object that was stacked affects the context of the current object being
	 * stacked. For example - if the previous element stacked was an
	 * {@link Array} or {@link Dict} - the current object being stacked will be
	 * a child.
	 * 
	 * @param obj
	 * @param key
	 *            If the parent of the element being added is a {@link Dict} -
	 *            this is required and must be non-null. Otherwise it's not
	 *            used.
	 * @throws Exception
	 *             TODO: refactor - move me
	 */
	public void stackObject(PListObject obj, java.lang.String key)
			throws Exception {
		if (null == key && stackCtxInDict) {
			throw new Exception(
					"PList objects with Dict parents require a key.");
		}
		if (stackCtxNestedDepth > 0 && !stackCtxInDict && !stackCtxInArray) {
			// if obj is not at root, its parent should be an Array or
			// Dict
			throw new Exception(
					"PList elements that are not at the root should have an Array or Dict parent.");
		}
		switch (obj.getType()) {
		case DICT:
			attachPListObjToParent(obj, key);
			stack.push(obj);
			stackCtxInArray = false;
			stackCtxInDict = true;
			stackCtxNestedDepth++;
			break;
		case ARRAY:
			attachPListObjToParent(obj, key);
			stack.push(obj);
			stackCtxInArray = true;
			stackCtxInDict = false;
			stackCtxNestedDepth++;
			break;
		default:
			attachPListObjToParent(obj, key);
		}
	}

	/**
	 * @param obj
	 * @param key
	 * 
	 * @todo refactor - move me - generating PLists from a stack of objets is
	 *       not part of being a PList.
	 */
	public PListObject popStack() {
		if (stack.isEmpty()) {
			return null;
		}
		PListObject ret = stack.pop();
		stackCtxNestedDepth--;
		if (!stack.isEmpty()) {
			switch (stack.lastElement().getType()) {
			case DICT:
				stackCtxInArray = false;
				stackCtxInDict = true;
				break;
			case ARRAY:
				stackCtxInArray = true;
				stackCtxInDict = false;
				break;
			}
		} else {
			stackCtxInArray = false;
			stackCtxInDict = false;
		}
		return ret;
	}

	/**
	 * Build a {@PListObject} from a string that matches one of
	 * the tags defined in {@link Constants}.
	 * 
	 * @param tag
	 * @param value
	 *            can be null if tag equals {@link Constants#TAG_BOOL_FALSE} or
	 *            {@link Constants#TAG_BOOL_TRUE}.
	 * @throws Exception
	 * 
	 * @todo replace with factory for PListObject
	 */
	public PListObject buildObject(java.lang.String tag, java.lang.String value)
			throws Exception {
		if (null == tag) {
			throw new Exception(
					"Cannot add a child with a null tag to a PList.");
		}
		PListObject ret = null;
		if (tag.equalsIgnoreCase(Constants.TAG_INTEGER)) {
			ret = new Integer();
			((Integer) ret).setValue(value);
		} else if (tag.equalsIgnoreCase(Constants.TAG_STRING)) {
			ret = new String();
			((String) ret).setValue(value);
		} else if (tag.equalsIgnoreCase(Constants.TAG_REAL)) {
			ret = new Real();
			((Real) ret).setValue(value);
		} else if (tag.equalsIgnoreCase(Constants.TAG_DATE)) {
			ret = new Date();
			((Date) ret).setValue(value);
		} else if (tag.equalsIgnoreCase(Constants.TAG_BOOL_FALSE)) {
			ret = new False();
		} else if (tag.equalsIgnoreCase(Constants.TAG_BOOL_TRUE)) {
			ret = new True();
		} else if (tag.equalsIgnoreCase(Constants.TAG_DATA)) {
			ret = new Data();
			((Data) ret).setValue(value.trim(), true);
		} else if (tag.equalsIgnoreCase(Constants.TAG_DICT)) {
			ret = new Dict();
		} else if (tag.equalsIgnoreCase(Constants.TAG_PLIST_ARRAY)) {
			ret = new Array();
		}
		return ret;
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