package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.uwm.cs351.Note;
import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM

/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * You should override clear() for efficiency, and add(Note)
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
public class Song extends AbstractCollection<Note> implements Collection<Note>, Iterable<Note>, Cloneable{

	/** Static Constants */
	private static final String DEFAULT_NAME = "Untitled";
	private static final int MIN_BPM = 20, MAX_BPM = 1000;
	private static final int DEFAULT_BPM = 60;
	private static final int INITIAL_CAPACITY = 1;

	/** Song Fields */
	private String _name;
	private int _bpm;
	
	/** Collection Fields */
	private int _count;
	private int _version;
	Note[] _data;
	
	private Song(boolean ignored) {} // DO NOT CHANGE THIS

	private boolean _report(String s) {
		System.out.println(s);
		return false;
	}
	
	// The invariant:
	private boolean _wellFormed() {
		// 0. _data is not null
		if(_data == null) return _report("_data is null");
		// 1. _count is a valid index of _data
		if(_data.length < _count) return _report("count is incorrect");
		// 2. elements in _data indices [0, _count-1] are non-null
		for(int i=0; i<_count; i++)
			if(_data[i] == null) return _report("index "+i+" contains null");
		return true;
	}
	
	/**
	 * Initialize an empty Song using default values for name and BPM.
	 */
	public Song() {
		this(DEFAULT_NAME, DEFAULT_BPM);
	}

	/**
	 * Initialize an empty song with a specified name and BPM and an initial
	 * capacity of INITIAL_CAPACITY. The {@link #insert(Note)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param name
	 *   the name of this song, must not be null
	 * @param bpm
	 *   the beats per minute of this song, must be in the range [MIN_BPM, MAX_BPM]
	 * @postcondition
	 *   This song is empty, has specified name and bpm, and has an initial
	 *   capacity of INITIAL_CAPACITY.
	 * @throws IllegalArgumentException
	 *    If the name is null, or the BPM is outside of the legal range.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Note[initialCapacity].
	 **/   
	public Song(String name, int bpm)
	{
		// NB: We don't have to check invariant at beginning of constructor. Why?
		if (name == null)
			throw new IllegalArgumentException("Name shoud not be null");
		if (bpm < MIN_BPM || bpm > MAX_BPM)
			throw new IllegalArgumentException("BPM out of legal range [" + MIN_BPM + "," + MAX_BPM + "]: " + bpm);
		
		this._name = name;
		this._bpm = bpm;
		
		// TODO: implement rest of constructor
		// TODO: assert _wellFormed() after body
		//#(
		_data = new Note[INITIAL_CAPACITY];
		_count = 0;
		_version++;
		assert _wellFormed() : "invariant failed at end of constructor";
		//#)
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		if (_data.length >= minimumCapacity) return;
		int newCapacity = Math.max(_data.length*2+1, minimumCapacity);
		Note[] newData = new Note[newCapacity];
		for (int i=0; i < _count; i++) 
			newData[i] = _data[i];
		
		_data = newData;
	}
	

	/*
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 * NB: We are able to parameterize this method with Note instead of Object
	 * 	   because we have extended AbstractCollection with type parameter <Note>.
	 */
	@Override
	public boolean add(Note n){
		// #(
		assert _wellFormed() : "invariant broken at beginning of add()";
		ensureCapacity(_count+1);
		_data[_count++] = n;
		_version++;
		assert _wellFormed() : "invariant broken at end of add()";
		return true;
		/* #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement add(Note b)
		
		// TODO: assert _wellFormed() after body
		*/
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
		_data = new Note[INITIAL_CAPACITY];
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
	public Iterator<Note> iterator() {
		// #(
		assert _wellFormed() : "invariant broken at beginning of iterator()";
		return new MyIterator();
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO return new iterator
		
		// NB: We don't have to check invariant at end of iterator(). Why?
	}
	
	private class MyIterator implements Iterator<Note> {
		
		int _myVersion, _currentIndex;
		boolean _calledNext;

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS
		
		private boolean _wellFormed() {
			
			// Invariant for recommended fields:
			// NB: Don't check 1,2 unless the version matches.

			// 0. The outer invariant holds
			//		NB: To access the parent Song of this iterator, use "Song.this"
			//			e.g. Song.this.getName()
			// TODO
			// 1. _currentIndex is between -1 (inclusive) and _count (exclusive)
			// TODO
			// 2. _calledNext is true only if _currentIndex is not -1
			// TODO
			
			// #(
			// 0.
			if (!Song.this._wellFormed()) return _report("outer invariant broken during iteration");
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
			/* #)
			//TODO
			## */
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
		public Note next() {
			assert _wellFormed() : "invariant fails at beginning of iterator next()";
			// #(
			if (_myVersion!=_version)	throw new ConcurrentModificationException();
			if (!hasNext()) throw new NoSuchElementException();
			Note cur = _data[++_currentIndex];
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
	
	/**
	 * Gets the name of the song.
	 * @return the name
	 */
	public String getName() {
		assert _wellFormed() : "invariant failed at start of getName";
		return _name;
	}

	/**
	 * Gets the beats per minute of the song.
	 * @return the BPM
	 */
	public int getBPM() {
		assert _wellFormed() : "invariant failed at start of getBPM";
		return _bpm;
	}

	/**
	 * Gets the total duration of the song by adding duration of all its notes.
	 * @return the total duration
	 */
	public double getDuration() {
		assert _wellFormed() : "invariant failed at start of getDuration";
		double result = 0;
		for (int i = 0; i < _count; i++)
			result += _data[i].getDuration();
		return result;
	}
	
	/**
	 * Sets the name of the song.
	 * @param newName the new name, must not be null
	 */
	public void setName(String newName) {
		assert _wellFormed() : "invariant failed at start of setName";
		if (newName == null) throw new IllegalArgumentException("new name cannot be null");
		_name = newName;
		assert _wellFormed() : "invariant failed at end of setName";
	}

	/**
	 * Sets the beats per minute (BPM) of the song.
	 * @param newBPM the new BPM
	 * @throws IllegalArgumentException in the new BPM is not in the range [MIN_BPM,MAX_BPM]
	 */
	public void setBPM(int newBPM) {
		assert _wellFormed() : "invariant failed at start of setBPM";
		if (newBPM < MIN_BPM || newBPM > MAX_BPM) throw new IllegalArgumentException("BPM out of legal range [" + MIN_BPM + "," + MAX_BPM + "]: " +  newBPM);
		_bpm = newBPM;
		assert _wellFormed() : "invariant failed at end of setBPM";
	}
	
	/**
	 * Stretches the song by the given factor, lengthening or shortening its duration.
	 *
	 * @param factor the factor to multiply each note's duration by
	 * @throws IllegalArgumentException if song is transposed where a note's duration
	 * 				is beyond the valid bounds
	 */
	public void stretch(double factor) {
		assert _wellFormed() : "invariant failed at start of stretch";
		for (int i = 0; i < _count; i++)
			_data[i] = _data[i].stretch(factor);
		assert _wellFormed() : "invariant failed at end of stretch";
	}


	/**
	 * Transposes the song by the given interval, raising or lowering its pitch
	 *
	 * @param interval the interval to transpose each note in the song
	 * @throws IllegalArgumentException if song is transposed where a note is beyond the bounds
	 * 									of valid MIDI pitch values [0,127]
	 */
	public void transpose(int interval) {
		assert _wellFormed() : "invariant failed at start of transpose";
		for (int i = 0; i < _count; i++)
			_data[i] = _data[i].transpose(interval);
		assert _wellFormed() : "invariant failed at end of transpose";
	}
	
	/**
	 * Generate a copy of this song.
	 * @param - none
	 * @return
	 *   The return value is a copy of this song. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public Song clone( ) { 
		assert _wellFormed() : "invariant failed at start of clone";
		Song result;

		try
		{
			result = (Song) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		result._data = _data.clone( );

		assert _wellFormed() : "invariant failed at end of clone";
		assert result._wellFormed() : "invariant on result failed at end of clone";
		return result;
	}
	
	public static class TestInvariant extends TestCase {
		
		protected Song self;
		protected Song.MyIterator iterator;
		
		private Note n1 = new Note("c4", 0.5);
		private Note n2 = new Note("e4", 0.25);
		private Note n3 = new Note("g4", 0.25);
		private Note n4 = new Note("c5", 2.0);
		
		@Override
		protected void setUp() {
			self = new Song(false);
			iterator = self.new MyIterator(false);
		}
		
		// outer invariant 0 - null data
		public void test01() {
			assertFalse("null data", self._wellFormed());
		}
		
		// outer invariant 2 - null element in count
		public void test02() {
			self._data = new Note[2];
			assertTrue(self._wellFormed());
			self._count = 2;
			self._data[0] = null;
			self._data[1] = n1;
			assertFalse("null element",self._wellFormed());
			self._count = 0;
			assertTrue("good empty collection of length 2",self._wellFormed());
		}
		
		// outer invariants 1, 2 - count off
		public void test03() {
			self._data = new Note[4];
			self._count = 1;
			assertFalse("count is wrong",self._wellFormed());
			self._count = 0;
			self._data[0] = n1;
			self._data[1] = n2;
			assertTrue("good empty collection",self._wellFormed());
			self._count = 1;
			assertTrue("good one element collection",self._wellFormed());
			self._count = 3;
			self._data[3] = n3;
			assertFalse("count off",self._wellFormed());
			++self._count;
			assertFalse("null element",self._wellFormed());
			self._data[2] = n4;
			assertTrue("good four element collection",self._wellFormed());
			++self._count;
			assertFalse("_count of 5 in _data array of length 4",self._wellFormed());
		}
		
		// inner invariant 0 - outer invariant broken
		public void test04() {
			self._data = new Note[2];
			self._count = 2;
			assertFalse("outer invariant should fail",iterator._wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test05() {
			self._data = new Note[2];
			iterator._currentIndex = -10;
			assertFalse("_currentIndex too small",iterator._wellFormed());
			iterator._currentIndex = 2;
			assertFalse("_currentIndex too big",iterator._wellFormed());
			++self._version;
			assertTrue("versions don't match",iterator._wellFormed());
			iterator._currentIndex = -1;
			++iterator._myVersion;
			assertTrue("current OK",iterator._wellFormed());
			iterator._calledNext = true;
			assertFalse("cannot remove -1",iterator._wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test06() {
			self._data = new Note[10];
			self._version += 456;
			self._data[0] = n1;
			self._data[1] = n2;
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
