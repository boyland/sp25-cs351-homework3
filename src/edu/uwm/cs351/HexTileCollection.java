package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

// This is a Homework Assignment for CS 351 at UWM

/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * 
 * The data structure is a dynamic sized array.
 * The fields should be:
 * <dl>
 * <dt>data</dt> The data array.
 * <dt>manyItems</dt> Number of elements in the collection.
 * <dt>version</dt> Version number (used for fail-fast semantics)
 * </dl>
 * The class should define a private wellFormed() method
 * and perform assertion checks in each method.
 */
public class HexTileCollection 
// extends {Something} implements {Something else}
extends AbstractCollection<HexTile> implements Cloneable // ### \subsection{Extension}
{
	private static final int INITIAL_CAPACITY = 1;

	// #(# \subsection{Fields}
	private int manyItems;
	private int version;
	private HexTile[] data;
	// #)
	// TODO: Fields

	private HexTileCollection(boolean ignored) {} // DO NOT CHANGE THIS

	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	// The invariant:
	private boolean wellFormed() {
		//TODO: write the invariant checker
		// 0. data is not null
		// 1. manyItems is a possible count of elements given the capacity of the array
		//#(# \subsection{wellFormed}
		if(data == null) return report("data is null");
		if(manyItems < 0 || data.length < manyItems) return report("manyItems is incorrect");
		//#)
		return true;
	}

	/**
	 * Initialize an empty HexTile collection with an initial
	 * capacity of INITIAL_CAPACITY. The {@link #add(HexTile)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @postcondition
	 *   This HexTile collection is empty, has an initial
	 *   capacity of INITIAL_CAPACITY.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new HexTile[initialCapacity].
	 **/   
	public HexTileCollection()
	{
		// TODO: implement constructor
		// TODO: assert wellFormed() after body
		//#(# \subsection{Constructor}
		data = new HexTile[INITIAL_CAPACITY];
		manyItems = 0;
		version++;
		assert wellFormed() : "invariant failed at end of constructor";
		//#)
	}

	// #(# \subsection{size}
	@Override // required
	public int size(){
		assert wellFormed() : "invariant broken at beginning of size()";
		return manyItems;
	}
	// #)
	// TODO: override size (required!)

	private void ensureCapacity(int minimumCapacity)
	{
		if (data.length >= minimumCapacity) return;
		int newCapacity = Math.max(data.length*2, minimumCapacity);
		HexTile[] newData = new HexTile[newCapacity];
		for (int i=0; i < manyItems; i++) 
			newData[i] = data[i];

		data = newData;
	}

	// #(# \subsection{add}
	@Override // implementation
	public boolean add(HexTile n){
		assert wellFormed() : "invariant broken at beginning of add()";
		ensureCapacity(manyItems+1);
		data[manyItems++] = n;
		version++;
		assert wellFormed() : "invariant broken at end of add()";
		return true;
	}
	// #)
	// TODO: You will need an "implementation" override

	// #(# \subsection{clear}
	@Override // efficiency
	public void clear(){
		assert wellFormed() : "invariant broken at beginning of clear()";
		if (manyItems > 0) {
			manyItems=0;
			version++;
			data = new HexTile[INITIAL_CAPACITY];
		}
		assert wellFormed() : "invariant broken at end of clear()";
	}
	// #)
	// TODO: You will find an efficiency override necessary

	// #(# \subsection{iterator}
	@Override // required
	public Iterator<HexTile> iterator() {
		assert wellFormed() : "invariant broken at beginning of iterator()";
		return new MyIterator();
	}
	// #)
	// TODO: Another "required" override

	private class MyIterator 
	implements Iterator<HexTile> // ### \subsection{MyIterator}
	// TODO: implements {Something}
	{

		int colVersion, currentIndex;
		boolean isCurrent;

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS

		private boolean wellFormed() {

			// Invariant for recommended fields:
			// NB: Don't check 1,2 unless the version matches.

			// 0. The outer invariant holds
			//		NB: To access the parent HexTileCollection of this iterator, use "HexTileCollection.this"
			//			e.g. HexTileCollection.this.getName()
			// TODO
			// 1. currentIndex is between -1 (inclusive) and manyItems (exclusive)
			// TODO
			// 2. currentIndex is equal to -1 only if isCurrent is false
			// TODO

			// #(# \subsection{Iterator wellFormed}
			// 0.
			if (!HexTileCollection.this.wellFormed()) return false;
			if (colVersion == version) {
				// 1.
				if (currentIndex<-1 || currentIndex>=manyItems) return report("currentIndex holds illegal value.");
				// 2.
				if (isCurrent && currentIndex==-1) return report("isCurrent is true but currentIndex is -1");
			}
			// #)
			return true;
		}	

		/**
		 * Instantiates a new MyIterator.
		 */
		public MyIterator() {
			// #(# |subsection{MyIterator constructor}
			colVersion = version;
			currentIndex=-1;
			isCurrent = false;
			// #)
			// TODO
			assert wellFormed() : "invariant fails in iterator constructor";
		}

		// #(# \subsection{Iterator body}
		@Override // required
		public boolean hasNext() {
			assert wellFormed() : "invariant fails at beginning of iterator hasNext()";
			if (colVersion!=version) throw new ConcurrentModificationException();
			return (currentIndex+1 < manyItems);
		}

		@Override // required
		public HexTile next() {
			assert wellFormed() : "invariant fails at beginning of iterator next()";
			if (colVersion!=version)	throw new ConcurrentModificationException();
			if (!hasNext()) throw new NoSuchElementException();
			++currentIndex;
			HexTile cur = data[currentIndex];
			isCurrent=true;
			assert wellFormed() : "invariant fails at end of iterator next()";
			return cur;
		}

		/**
		 * Removes from the underlying collection the last element returned by this iterator.
		 * This method can be called only once per call to next().
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws IllegalStateException if the next() method has not yet been called, or the remove() 
		 * 			method has already been called after the last call to the next() method
		 */
		@Override // implementation
		public void remove() {
			assert wellFormed() : "invariant fails at beginning of iterator remove()";
			if (colVersion!=version)	throw new ConcurrentModificationException();
			if (!isCurrent) throw new IllegalStateException("nothing to remove");
			for (int i = currentIndex; i < manyItems - 1; i++)
				data[i]=data[i+1];
			manyItems--;
			currentIndex--;
			isCurrent = false;
			colVersion++;
			version++;
			assert wellFormed() : "invariant fails at end of iterator remove()";
		}
		// #)
		// TODO: Implement required methods
	}

	/**
	 * Generate a copy of this HexTile collection.
	 * @param - none
	 * @return
	 *   The return value is a copy of this HexTile collection. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@Override // decorate
	public HexTileCollection clone( ) { 
		assert wellFormed() : "invariant failed at start of clone";
		HexTileCollection result;

		try
		{
			result = (HexTileCollection) super.clone( );
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
		result.data = data.clone( );

		assert wellFormed() : "invariant failed at end of clone";
		assert result.wellFormed() : "invariant on result failed at end of clone";
		return result;
	}

	/**
	 * Used for testing the invariant.  Do not change this code.
	 */
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a debugging instance of the main class
		 * with a particular data structure.
		 * @param a static array to use
		 * @param m size to use
		 * @param v current version
		 * @return a new instance with the given data structure
		 */
		public HexTileCollection newInstance(HexTile[] a, int m, int v) {
			HexTileCollection result = new HexTileCollection(false);
			result.data = a;
			result.manyItems = m;
			result.version = v;
			return result;
		}

		/**
		 * Return an iterator for testing purposes.
		 * @param bc main class instance to use
		 * @param c current index of iterator
		 * @param i the value of 'isCurrent'
		 * @param v the value of colVersion
		 * @return iterator with this data structure
		 */
		public Iterator<HexTile> newIterator(HexTileCollection bc, int c, boolean i, int v) {
			MyIterator result = bc.new MyIterator(false);
			result.currentIndex = c;
			result.isCurrent = i;
			result.colVersion = v;
			return result;
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param bs instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(HexTileCollection bs) {
			return bs.wellFormed();
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param i instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(Iterator<HexTile> i) {
			return ((MyIterator)i).wellFormed();
		}
	}
}
