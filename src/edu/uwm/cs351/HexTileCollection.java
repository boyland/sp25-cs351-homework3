package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM

/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * You should override clear() for efficiency, and add(HexTile)
 * for functionality.  You will also be required to override the abstract methods
 * size() and iterator().  All these methods should be declared "@Override".
 * 
 * The data structure is a dynamic sized array.
 * The fields should be:
 * <dl>
 * <dt>data</dt> The data array.
 * <dt>manyItems</dt> Number of true elements in the collection.
 * <dt>version</dt> Version number (used for fail-fast semantics)
 * </dl>
 * The class should define a private wellFormed() method
 * and perform assertion checks in each method.
 * You should use a version stamp to implement <i>fail-fast</i> semantics
 * for the iterator.
 */
public class HexTileCollection extends AbstractCollection<HexTile> implements Collection<HexTile>, Iterable<HexTile>, Cloneable{

	/** Static Constants */
	private static final int INITIAL_CAPACITY = 1;

	/** Collection Fields */
	private int manyItems;
	private int version;
	private HexTile[] data;
	
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
		//#(
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
		//#(
		data = new HexTile[INITIAL_CAPACITY];
		manyItems = 0;
		version++;
		assert wellFormed() : "invariant failed at end of constructor";
		//#)
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		if (data.length >= minimumCapacity) return;
		int newCapacity = Math.max(data.length*2+1, minimumCapacity);
		HexTile[] newData = new HexTile[newCapacity];
		for (int i=0; i < manyItems; i++) 
			newData[i] = data[i];
		
		data = newData;
	}
	

	/*
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 * NB: We are able to parameterize this method with HexTile
	 * 	   because we have extended AbstractCollection with type parameter <HexTile>.
	 */
	@Override
	public boolean add(HexTile n){
		// #(
		assert wellFormed() : "invariant broken at beginning of add()";
		ensureCapacity(manyItems+1);
		data[manyItems++] = n;
		version++;
		assert wellFormed() : "invariant broken at end of add()";
		return true;
		/*
		// #)
		return false;
		// #(
		 */
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement add(HexTile b)
		
		// TODO: assert wellFormed() after body
	}
	
	@Override // efficiency
	public void clear(){
		// #(
		assert wellFormed() : "invariant broken at beginning of clear()";
		if (manyItems > 0) {
			manyItems=0;
		    version++;
		    data = new HexTile[INITIAL_CAPACITY];
		}
		assert wellFormed() : "invariant broken at end of clear()";
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement clear()
		
		// TODO: assert wellFormed() after body
	}
	
	/*
	 * @see java.util.AbstractCollection#size()
	 */
	public int size(){
		// #(
		assert wellFormed() : "invariant broken at beginning of size()";
		return manyItems;
		/*
		// #)
		return 0;
		// #(
		 */
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO: implement size()
		
		// NB: We don't have to check invariant at end of size(). Why?
	}
	
	
	/*
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<HexTile> iterator() {
		// #(
		assert wellFormed() : "invariant broken at beginning of iterator()";
		return new MyIterator();
		/*
		// #)
		return null;
		// #(
		 */
		// #)
		// TODO: assert wellFormed() before body
		
		// TODO return new iterator
		
		// NB: We don't have to check invariant at end of iterator(). Why?
	}
	
	private class MyIterator implements Iterator<HexTile> {
		
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
			
			// #(
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
			// #(
			colVersion = version;
			currentIndex=-1;
			isCurrent = false;
			// #)
			// TODO
			assert wellFormed() : "invariant fails in iterator constructor";
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
			assert wellFormed() : "invariant fails at beginning of iterator hasNext()";
			// #(
			if (colVersion!=version)	throw new ConcurrentModificationException();
			return (currentIndex+1 < manyItems);
			/*
			// #)
			return false;
			// #(
			 */
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
		public HexTile next() {
			assert wellFormed() : "invariant fails at beginning of iterator next()";
			// #(
			if (colVersion!=version)	throw new ConcurrentModificationException();
			if (!hasNext()) throw new NoSuchElementException();
			++currentIndex;
			HexTile cur = data[currentIndex];
			isCurrent=true;
			// #)
			// TODO
			assert wellFormed() : "invariant fails at end of iterator next()";
			// #(
			return cur;
			/*
			// #)
			return null;
			// #(
			 */
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
			assert wellFormed() : "invariant fails at beginning of iterator remove()";
			// #(
			if (colVersion!=version)	throw new ConcurrentModificationException();
			if (!isCurrent) throw new IllegalStateException("nothing to remove");
			for (int i = currentIndex; i < manyItems - 1; i++)
				data[i]=data[i+1];
			manyItems--;
			currentIndex--;
			isCurrent = false;
			colVersion++;
			version++;
			// #)
			//TODO
			assert wellFormed() : "invariant fails at end of iterator remove()";
		}
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

	public static class TestInvariant extends TestCase {
		
		protected HexTileCollection self;
		protected HexTileCollection.MyIterator iterator;
		
		private HexTile n1 = new HexTile(Terrain.FOREST, new HexCoordinate(0,0));
		private HexTile n2 = new HexTile(Terrain.LAND, new HexCoordinate(1,0));
		private HexTile n3 = new HexTile(Terrain.WATER, new HexCoordinate(0,1));
		private HexTile n4 = new HexTile(Terrain.MOUNTAIN, new HexCoordinate(1,1));
		
		@Override
		protected void setUp() {
			self = new HexTileCollection(false);
			iterator = self.new MyIterator(false);
			// doReport = false;
		}
		
		// outer invariant 0 - null data
		public void test01() {
			assertFalse("null data", self.wellFormed());
		}
		
		// outer invariant 2 - null element in count
		public void test02() {
			self.data = new HexTile[2];
			assertTrue(self.wellFormed());
			self.manyItems = -1;
			assertFalse(self.wellFormed());
			self.manyItems = 2;
			self.data[0] = null;
			self.data[1] = n1;
			assertTrue("null element OK",self.wellFormed());
			self.manyItems = 0;
			assertTrue("good empty collection of length 2",self.wellFormed());
		}
		
		// outer invariants 1, 2 - count off
		public void test03() {
			self.data = new HexTile[4];
			self.manyItems = 1;
			assertTrue("manyItems is OK",self.wellFormed());
			self.manyItems = 0;
			self.data[0] = n1;
			self.data[1] = n2;
			assertTrue("good empty collection",self.wellFormed());
			self.manyItems = 1;
			assertTrue("good one element collection",self.wellFormed());
			self.manyItems = 3;
			self.data[3] = n3;
			++self.manyItems;
			self.data[2] = n4;
			assertTrue("good four element collection",self.wellFormed());
			++self.manyItems;
			assertFalse("manyItems of 5 in data array of length 4",self.wellFormed());
		}
		
		// inner invariant 0 - outer invariant broken
		public void test04() {
			self.data = new HexTile[2];
			self.manyItems = -1;
			assertFalse("outer invariant should fail",iterator.wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test05() {
			self.data = new HexTile[2];
			iterator.currentIndex = -10;
			assertFalse("currentIndex too small",iterator.wellFormed());
			iterator.currentIndex = 1;
			assertFalse("currentIndex too big",iterator.wellFormed());
			++self.version;
			assertTrue("versions don't match",iterator.wellFormed());
			iterator.currentIndex = -1;
			++iterator.colVersion;
			assertTrue("current OK",iterator.wellFormed());
			iterator.currentIndex = 0;
			assertFalse("currentIndex must be smaller than manyItems",iterator.wellFormed());
			iterator.currentIndex = -1;
			iterator.isCurrent = true;
			assertFalse("cannot have current when at index -1",iterator.wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test06() {
			self.data = new HexTile[10];
			self.version += 456;
			self.data[0] = n1;
			self.data[1] = n2;
			self.manyItems = 2;
			assertTrue(self.wellFormed());
			assertTrue(iterator.wellFormed());
			iterator.colVersion = 456;
			iterator.currentIndex = -1;
			iterator.isCurrent = true;
			assertFalse("currentIndex can't be -1 when isCurrent is true",iterator.wellFormed());
			iterator.isCurrent = false;
			assertTrue(iterator.wellFormed());
			iterator.currentIndex = 0;
			assertTrue(iterator.wellFormed());
			iterator.currentIndex = 1;
			assertTrue(iterator.wellFormed());
			iterator.isCurrent = true;
			assertTrue(iterator.wellFormed());
			iterator.currentIndex = 2;
			assertFalse("currentIndex out of bounds",iterator.wellFormed());
			iterator.isCurrent = false;
			assertFalse("currentIndex out of bounds",iterator.wellFormed());
			++iterator.colVersion;
			assertTrue(iterator.wellFormed());
		}
	}
}
