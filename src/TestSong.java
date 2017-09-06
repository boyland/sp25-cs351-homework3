import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Note;
import edu.uwm.cs351.Song;

public class TestSong extends LockedTestCase {
	
	private Song s1, s2;
	private Iterator<Note> it1, it2;
	
	private Note n1 = new Note("c4", 0.5);
	private Note n2 = new Note("e4", 0.25);
	private Note n3 = new Note("g4", 0.25);
	private Note n4 = new Note("c5", 2.0);
	private Note n5 = new Note(128, 1.0);
	
	@Override
	protected void setUp() {
		try {
			assert s1.size() == s2.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		s1 = new Song();
		s2 = new Song();
	}
	
	// Convert a Note result to a string
	Note notes[] = { null, n1, n2, n3, n4, n5 };
	private String getName(Note b) {
		for (int i=1; i<notes.length;i++)
			if (notes[i] == b)
				return "b" + i;
		return "null";}
	
	protected void testCollection(Song col, String name, Note... parts)
	{
		assertEquals(name + ".size()",parts.length,col.size());
		Iterator<Note> it = col.iterator();
		int i=0;
		while (it.hasNext() && i < parts.length) {
			assertEquals(name + "[" + i + "]",parts[i],it.next());
			++i;}
		assertFalse(name + " too long",it.hasNext());
		assertFalse(name + " too short",(i < parts.length));
	}
	
	public void test08()
	{
		assertEquals(true, s1.isEmpty());
		it1 = s1.iterator();
		assertEquals(Tb(180540892), it1.hasNext());
		s1.add(n1);
		it1 = s1.iterator();
		assertEquals(Tb(1253619686), it1.hasNext());
		assertEquals("Which note should be next?", Ts(851974293), getName(it1.next()));
		assertEquals(Tb(959107088), it1.hasNext());
	
		s1.add(n2);
		testCollection(s1,"{n1,n2}",n1,n2);
		s1.add(n3);
		testCollection(s1,"{n1,n2,n3}",n1,n2,n3);
		s1.add(n4);
		testCollection(s1,"{n1,n2,n3,n4}",n1,n2,n3,n4);
		
		s1.clear();
		testCollection(s1,"after clear");
		it1 = s1.iterator();
		assertEquals(false, it1.hasNext());
	}
	
	public void test07() {
		s1.add(n1);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n1} after remove(n1)");
	}
	
	public void test06() {
		s1.add(n1);
		s1.add(n2);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(490251517), it1.hasNext());
		assertEquals("Which note should be next?",Ts(872278581), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n1,n2} after remove(n1)",n2);
	}
	
	public void test05() {
		s1.add(n1);
		s1.add(n2);
		it1 = s1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n1,n2} after remove(n2)",n1);
	}
	
	public void test04() {
		s1.add(n2);
		s1.add(n1);
		s1.add(n3);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(650685995), it1.hasNext());
		assertEquals("Which note should be next?", Ts(1109302213), getName(it1.next()));
		assertEquals(Tb(1356481467), it1.hasNext());
		assertEquals("Which note should be next?", Ts(862509612), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n2,n1,n3} after remove(n2)",n1,n3);
				
	}
	
	public void testRemove103() {
		s1.add(n3);
		s1.add(n1);
		s1.add(n2);
		it1 = s1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(true, it1.hasNext());
		assertEquals("n2", getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n3,n1,n2} after remove(n1)",n3,n2);
	}

	public void test03() {
		s1.add(n3);
		s1.add(n2);
		s1.add(n1);
		it1 = s1.iterator();
		it1.next();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(Tb(725642519), it1.hasNext());
		it1 = s1.iterator();
		assertEquals(Tb(879757027), it1.hasNext());
		assertEquals("Which note should be next?",Ts(740148936), getName(it1.next()));
		assertEquals(Tb(2004839426), it1.hasNext());
		assertEquals("Which note should be next?", Ts(355454756), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n3,n2,n1} after remove(n1)",n3,n2);	
	}

	public void testRemove003() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it1 = s1.iterator();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		assertTrue(it1.hasNext());
		assertEquals("n3",getName(it1.next()));
		testCollection(s1,"{n1,n2,n3} after remove(n1,n2)",n3);
	}

	public void testRemove020() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it1 = s1.iterator();
		it1.next(); it1.remove();
		it1.next();
		it1.next(); it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n1,n2,n3} after remove(n1,n3)",n2);
	}

	public void testRemove000() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it1 = s1.iterator();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		assertEquals(false, it1.hasNext());
		it1 = s1.iterator();
		assertEquals(false, it1.hasNext());
		testCollection(s1,"{n1,n2,n3} after remove(n1,n2,n3)");
	}

	public void testRemove1034() 
	{
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		testCollection(s1,"{n1 n2 n3 n4}",n1,n2,n3,n4);
		
		it1 = s1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertTrue("Two more after n2 removed",it1.hasNext());
		assertEquals("Next after n2 removed","n3",getName(it1.next()));
		assertTrue("One more and next() after n2 removed",it1.hasNext());
		assertEquals("Next after next() after n2 removed","n4",getName(it1.next()));
		assertTrue("Only two more after n2 removed",!it1.hasNext());		
		testCollection(s1,"{n1,n2,n3,n4} after remove(n2)",n1,n3,n4);
		
		s1.clear();
		testCollection(s1,"after clear");
	}
	
	public void testRemove02() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(1387930327), it1.hasNext());
		assertEquals("Which note should be next?",Ts(2067552273),getName(it1.next()));
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1)",n2,n3,n4);
		assertEquals(Tb(630379853),it1.hasNext());
		assertEquals("Which note should be next?",Ts(1902021469),getName(it1.next()));
		it1.remove();
		assertEquals(Tb(1593286384), it1.hasNext());
		assertEquals("Which note should be next?",Ts(544491364),getName(it1.next()));
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n3)",n2,n4);
		it1.remove();
		assertEquals(Tb(1879427740),it1.hasNext());
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n3,n4)",n2);
		
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());//
		testCollection(s1,"all removed");
		
	}
	
	public void testRemoveEvenMore0030() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		
		it1= s1.iterator();
		it1.next();
		it1.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1)",n2,n3,n4);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n2)",n3,n4);
		it1 = s1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n2,n4)",n3);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		testCollection(s1,"all removed again");
	}
	
	public void testEmptyNext()
	{
		it1 = s1.iterator();
		try {
			it1.next();
			assertFalse("next() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.next() threw wrong exception ",ex instanceof NoSuchElementException);
		}
		assertEquals(false, it1.hasNext());
		testCollection(s1,"still empty");
	}
	
	public void testEmptyRemove()
	{
		it1 = s1.iterator();
		try {
			it1.remove();
			assertFalse("remove() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.remove() threw wrong exception ",ex instanceof IllegalStateException);
		}
		assertEquals(false, it1.hasNext());//
		testCollection(s1,"yet still empty");

	}
	
	public void testStaleHasNext()
	{
		it1 = s1.iterator();
		s1.add(n3);
		try {
			it1.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(s1,"{n3}",n3);
	}
	
	public void test01()
	{
		s1.add(n3);
		it1 = s1.iterator();
		try {
			it1.remove();
			assertTrue("remove() at start of iteration should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("just started remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(743707309), it1.hasNext());
		assertEquals("Which note should be next?", Ts(2098552880), getName(it1.next()));
		testCollection(s1,"still {n3}",n3);
	}
	
	public void testAfterRemoveLast()
	{
		s1.add(n4);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		try {
			it1.next();
			assertTrue("next() after removed only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after removal of only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after removal of only element, hasNext() should still be false",(!it1.hasNext()));
		testCollection(s1,"{n4} after remove (n4)");
	}
	
	public void test00()
	{
		s1.add(n2);
		s1.add(n4);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		try {
			it1.remove();
			assertTrue("remove() after remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(1431749887), it1.hasNext());
		assertEquals("Which note should be next?", Ts(1918724512), getName(it1.next()));
		testCollection(s1,"{n2,n4} after remove (n2)",n4);
	}
	
	public void testStaleHasNextAtEnd()
	{
		s1.add(n3);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		s1.add(n4);
		try {
			it1.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(s1,"{n4}",n4);
	}
	
	public void testNextAtEnd()
	{
		s1.add(n2);
		it1 = s1.iterator();
		it1.next();
		try {
			it1.next();
			assertTrue("next() after iterated past only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after iteration past only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after iteration past only element, hasNext() should still be false",(!it1.hasNext()));
		testCollection(s1,"{n2}",n2);
	}
	
	public void testStaleAfterRemoveSame()
	{
		s1.add(n5);
		s1.add(n1);
		it1 = s1.iterator();
		it2 = s1.iterator();
		it1.next();
		it2.next();
		it1.remove();
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}	
		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}		
		try {
			it1.remove();
			assertTrue("remove() after first remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after first remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertEquals(true, it1.hasNext());
		testCollection(s1,"{n5,n1} after remove (n5)",n1);
		
	}
	
	public void testStateAfterRemoveOther() {
		s1.add(n1);
		s1.add(n3);
		it1 = s1.iterator();
		it1.next();
		it1.remove();
		
		it2 = s1.iterator();
		it1.next();
		it1.remove();
		
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it2.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it1.remove();
			assertTrue("remove() after second remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after second remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertTrue("after remove() after second remove(), hasNext() should still be false",(!it1.hasNext()));
		testCollection(s1,"{n1,n3} after remove (n1,n3)");
	}
	
	public void testStaleAfterClear() {
		s1.add(n1);
		it1 = s1.iterator();
		s1.clear();
		
		try {
			it1.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it1.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it1.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
	}

}
