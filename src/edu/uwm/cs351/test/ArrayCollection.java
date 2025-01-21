package edu.uwm.cs351.test;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of collection that is strictly fail fast.
 * @param T element type
 */
public class ArrayCollection<T> extends AbstractCollection<T> {

	private List<T> wrapped = new ArrayList<>();
	private int version;

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return wrapped.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return new MyIterator();
	}

	@Override
	public Object[] toArray() {
		return wrapped.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return wrapped.toArray(a);
	}

	@Override
	public boolean add(T e) {
		if (wrapped.add(e)) {
			++version;
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		if (wrapped.remove(o)) {
			++version;
			return true;
		}
		return false;
	}	
	
	private class MyIterator implements Iterator<T> {
		Iterator<T> lit = wrapped.iterator();
		int outerVersion = version;
		
		@Override
		public boolean hasNext() {
			checkVersion();
			return lit.hasNext();
		}

		private void checkVersion() {
			if (outerVersion != version) throw new ConcurrentModificationException("stale");
		}

		@Override
		public T next() {
			checkVersion();
			T result = lit.next();
			return result;
		}

		@Override
		public void remove() {
			checkVersion();
			lit.remove();
			outerVersion = ++version;
		}
	}
}
