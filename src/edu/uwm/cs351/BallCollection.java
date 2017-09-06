package edu.uwm.cs351;

import java.awt.Color;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM

/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * You should override clear() for efficiency, and add(Ball)
 * for functionality.  You will also be required to override the abstract methods
 * count() and iterator().  All these methods should be declared "@Override".
 * 
 * The data structure is a dynamic sized array.
 * The fields should be:
 * <dl>
 * <dt>_data</dt> The data array.
 * <dt>_count</dt> Number of true elements in the collection.
 * <dt>_version</dt> Version number (used for fail-fast semantics)
 * </dl>
 * The class should define a private _wellFormed() method
 * and perform assertion checks in each method.
 * You should use a version stamp to implement <i>fail-fast</i> semantics
 * for the iterator.
 */
public class BallCollection extends AbstractCollection<Ball> implements Collection<Ball>, Iterable<Ball>{

	int _count, _version;
	Ball[] _data;

	private static final int INITIAL_CAPACITY = 1;
	
	private BallCollection(boolean ignored) {} // DO NOT CHANGE THIS

	private boolean _report(String s) {
		System.out.println(s);
		return false;
	}
	
	// The invariant:
	private boolean _wellFormed() {
		// 0. _data is not null
		if(_data == null) return _report("_data is null");
		// 1. count is a valid index of _data
		if(_data.length < _count) return _report("count is incorrect");
		// 2. elements in _data indices [0, _count-1] are non-null
		for(int i=0; i<_count; i++)
			if(_data[i] == null) return _report("index "+i+" contains null");
		return true;
	}
	
	/**
	 * Instantiates a new ball collection.
	 */
	public BallCollection() {
		// #(
		_data = new Ball[INITIAL_CAPACITY];
		_count = 0;
		_version++;
		assert _wellFormed() : "invariant broken at constructor";
		// #)
		// NB: We don't have to check invariant at beginning of constructor. Why?
		
		// TODO: implement constructor
		
		// TODO: assert _wellFormed() after body
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		if (_data.length >= minimumCapacity) return;
		int newCapacity = Math.max(_data.length*2+1, minimumCapacity);
		Ball[] newData = new Ball[newCapacity];
		for (int i=0; i < _count; i++) 
			newData[i] = _data[i];
		
		_data = newData;
	}
	

	/*
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 * NB: We are able to parameterize this method with Ball instead of Object
	 * 	   because we have extended AbstractCollection with type parameter <Ball>.
	 */
	@Override
	public boolean add(Ball b){
		// #(
		assert _wellFormed() : "invariant broken at beginning of add()";
		ensureCapacity(_count+1);
		_data[_count++] = b;
		_version++;
		assert _wellFormed() : "invariant broken at end of add()";
		return true;
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement add(Ball b)
		
		// TODO: assert _wellFormed() after body
	}
	
	/*
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear(){
		// #(
		assert _wellFormed() : "invariant broken at beginning of clear()";
		_count=0;
		_version++;
		_data = new Ball[INITIAL_CAPACITY];
		assert _wellFormed() : "invariant broken at end of clear()";
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement clear()
		
		// TODO: assert _wellFormed() after body
	}
	
	/*
	 * @see java.util.AbstractCollection#size()
	 */
	public int size(){
		// #(
		assert _wellFormed() : "invariant broken at beginning of size()";
		return _count;
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement size()
		
		// NB: We don't have to check invariant at end of size(). Why?
	}
	
	
	/*
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<Ball> iterator() {
		// #(
		assert _wellFormed() : "invariant broken at beginning of iterator()";
		return new MyIterator();
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO return new iterator
		
		// NB: We don't have to check invariant at end of iterator(). Why?
	}
	
	private class MyIterator implements Iterator<Ball> {
		
		int _myVersion, _currentIndex;
		boolean _calledNext;

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS
		
		private boolean _wellFormed() {
			
			// Invariant for recommended fields:
			// NB: Don't check 1,2 unless the version matches.

			// 0. The outer invariant holds
			//		NB: 
			// TODO
			// 1. _currentIndex is between -1 (inclusive) and _count (exclusive)
			// TODO
			// 2. _calledNext is true only if _currentIndex is not -1
			// TODO
			
			// #(
			// 0.
			if (!BallCollection.this._wellFormed()) return _report("outer invariant broken while iterating");
			if (_myVersion == _version) {
				// 1.
				if (_currentIndex<-1 || _currentIndex>=_count) return _report("_currentIndex holds illegal value.");
				// 2.
				if (_calledNext && _currentIndex==-1) return _report("_calledNext is true but _currentIndex is -1");
			}
			// #)
			return true;
		}	
		
		/**
		 * Instantiates a new MyIterator.
		 */
		public MyIterator() {
			// #(
			_myVersion = _version;
			_currentIndex=-1;
			_calledNext = false;
			// #)
			// TODO
			assert _wellFormed() : "invariant fails in iterator constructor";
		}

		/**
		 * Returns true if the iteration has more elements. (In other words, returns true
		 * if next() would return an element rather than throwing an exception.) 
		 * 
		 * @return true if the iteration has more elements
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 */
		@Override
		public boolean hasNext() {
			assert _wellFormed() : "invariant fails at beginning of iterator hasNext()";
			// #(
			if (_myVersion!=_version)	throw new ConcurrentModificationException();
			return (_currentIndex<_count-1);
			// #)
			//TODO
		}

		/**
		 * Returns the next element in the iteration. 
		 * 
		 * @return the next element in the iteration
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws NoSuchElementException if the iteration has no more elements
		 */
		@Override
		public Ball next() {
			assert _wellFormed() : "invariant fails at beginning of iterator next()";
			// #(
			if (_myVersion!=_version)	throw new ConcurrentModificationException();
			if (!hasNext()) throw new NoSuchElementException();
			Ball cur = _data[++_currentIndex];
			_calledNext=true;
			// #)
			// TODO
			assert _wellFormed() : "invariant fails at end of iterator next()";
			// #(
			return cur;
			// #)
		}

		/**
		 * Removes from the underlying collection the last element returned by this iterator.
		 * This method can be called only once per call to next().
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws IllegalStateException if the next() method has not yet been called, or the remove() 
		 * 			method has already been called after the last call to the next() method
		 */
		@Override
		public void remove() {
			assert _wellFormed() : "invariant fails at beginning of iterator remove()";
			// #(
			if (_myVersion!=_version)	throw new ConcurrentModificationException();
			if (_calledNext && _currentIndex != -1){
				for (int i = _currentIndex; i < _count - 1; i++)
					_data[i]=_data[i+1];
				_count--;
				_currentIndex--;
			}
			else {throw new IllegalStateException();}
			_myVersion++;
			_version++;
			_calledNext=false;
			// #)
			//TODO
			assert _wellFormed() : "invariant fails at end of iterator remove()";
			
		}
	}
	
	public static class TestInvariant extends TestCase {
		
		protected BallCollection self;
		protected BallCollection.MyIterator iterator;
		
		private Ball b1 = new Ball(new Point(1,1), new Vector(1,1), Color.BLACK);
		private Ball b2 = new Ball(new Point(2,2), new Vector(2,2), Color.WHITE);
		private Ball b3 = new Ball(new Point(3,3), new Vector(3,3), Color.RED);
		private Ball b4 = new Ball(new Point(4,4), new Vector(4,4), Color.GREEN);
		
		@Override
		protected void setUp() {
			self = new BallCollection(false);
			iterator = self.new MyIterator(false);
		}
		
		//outer invariant 0
		public void testNullData() {
			assertFalse("null data", self._wellFormed());
		}
		
		//outer invariant 2
		public void testNull() {
			self._data = new Ball[2];
			assertTrue(self._wellFormed());
			self._count = 2;
			self._data[0] = null;
			self._data[1] = b1;
			assertFalse("null element",self._wellFormed());
			self._count = 0;
			assertTrue("good empty list of length 2",self._wellFormed());
		}
		
		//outer invariants 1, 2
		public void testCountOff() {
			self._data = new Ball[4];
			self._count = 1;
			assertFalse("count is wrong",self._wellFormed());
			self._count = 0;
			self._data[0] = b1;
			self._data[1] = b2;
			assertTrue("good empty list",self._wellFormed());
			self._count = 1;
			assertTrue("good one element list",self._wellFormed());
			self._count = 3;
			self._data[3] = b3;
			assertFalse("count off",self._wellFormed());
			++self._count;
			assertFalse("null element",self._wellFormed());
			self._data[2] = b4;
			assertTrue("good four element list",self._wellFormed());
			++self._count;
			assertFalse("_count of 5 in _data array of length 4",self._wellFormed());
		}
		
		//inner invariant 0
		public void testThroughIterator() {
			self._data = new Ball[2];
			self._count = 2;
			assertFalse("outer wrong",iterator._wellFormed());
		}
		
		//iterator invariant 1, 2, invariant only enforced if versions match
		public void testEmptyIterator() {
			self._data = new Ball[2];
			iterator._currentIndex = -10;
			assertFalse("_currentIndex too small",iterator._wellFormed());
			iterator._currentIndex = 2;
			assertFalse("_currentIndex too big",iterator._wellFormed());
			++self._version;
			assertTrue("version bad",iterator._wellFormed());
			iterator._currentIndex = -1;
			++iterator._myVersion;
			assertTrue("current OK",iterator._wellFormed());
			iterator._calledNext = true;
			assertFalse("cannot remove -1",iterator._wellFormed());
		}
		
		//iterator invariant 1, 2, invariant only enforced if versions match
		public void testIterator() {
			self._data = new Ball[10];
			self._version += 456;
			self._data[0] = b1;
			self._data[1] = b2;
			self._count = 2;
			assertTrue(self._wellFormed());
			assertTrue(iterator._wellFormed());
			iterator._myVersion = 456;
			iterator._currentIndex = -1;
			assertTrue(iterator._wellFormed());
			iterator._currentIndex = 0;
			assertTrue(iterator._wellFormed());
			iterator._calledNext = true;
			assertTrue(iterator._wellFormed());
			iterator._currentIndex = 3;
			assertFalse("currentIndex out of bounds",iterator._wellFormed());
			++iterator._myVersion;
			assertTrue(iterator._wellFormed());
		}
	}
}
