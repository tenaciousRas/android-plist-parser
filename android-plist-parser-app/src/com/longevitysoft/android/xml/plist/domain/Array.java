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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a PList Array object. Essentially a proxy for a
 * {@link java.util.List} implementation that contains a list of
 * {@link PListObject}s.
 * 
 * @author fbeachler
 * 
 */
public class Array extends PListObject implements java.util.List<PListObject> {

	private ArrayList<PListObject> data;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2673110114913406413L;

	/**
	 * 
	 */
	public Array() {
		setType(PListObjectType.ARRAY);
		data = new ArrayList<PListObject>();
	}

	/**
	 * @param collection
	 */
	public Array(Collection<? extends PListObject> collection) {
		setType(PListObjectType.ARRAY);
		data = new ArrayList<PListObject>(collection);
	}

	/**
	 * @param capacity
	 */
	public Array(int capacity) {
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
	@Override
	public boolean addAll(Collection<? extends PListObject> arg0) {
		return data.addAll(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends PListObject> arg1) {
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
	@Override
	public List<PListObject> subList(int arg0, int arg1) {
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
	@Override
	public boolean containsAll(@SuppressWarnings("rawtypes") Collection arg0) {
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