import java.awt.Color;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Ball;
import edu.uwm.cs351.BallCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;



public class TestBallCollection extends LockedTestCase {
	
	private BallCollection c1, c2;
	private Iterator<Ball> it1, it2;
	
	private Ball b1 = new Ball(new Point(1,1), new Vector(1,1), Color.BLACK);
	private Ball b2 = new Ball(new Point(2,2), new Vector(2,2), Color.WHITE);
	private Ball b3 = new Ball(new Point(3,3), new Vector(3,3), Color.RED);
	private Ball b4 = new Ball(new Point(4,4), new Vector(4,4), Color.BLUE);
	private Ball b5 = new Ball(new Point(5,5), new Vector(5,5), Color.GREEN);
	
	@Override
	protected void setUp() {
		try {
			assert c1.size() == c2.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		c1 = new BallCollection();
		c2 = new BallCollection();
	}
	
	// Convert a Ball result to a string
	Ball balls[] = { null, b1, b2, b3, b4, b5 };
	private String getName(Ball b) {
		for (int i=1; i<balls.length;i++)
			if (balls[i] == b)
				return "b" + i;
		return "null";}
	
	protected void testCollection(BallCollection col, String name, Ball... parts)
	{
		assertEquals(name + ".size()",parts.length,col.size());
		Iterator<Ball> it = col.iterator();
		int i=0;
		while (it.hasNext() && i < parts.length) {
			assertEquals(name + "[" + i + "]",parts[i],it.next());
			++i;}
		assertFalse(name + " too long",it.hasNext());
		assertFalse(name + " too short",(i < parts.length));
	}
	
	public void test08()
	{
		assertEquals(true, c1.isEmpty());
		it1 = c1.iterator();
		assertEquals(Tb(180540892), it1.hasNext());
		c1.add(b1);
		it1 = c1.iterator();
		assertEquals(Tb(1253619686), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(851974293), getName(it1.next()));
		assertEquals(Tb(959107088), it1.hasNext());
	
		c1.add(b2);
		testCollection(c1,"{b1,b2}",b1,b2);
		c1.add(b3);
		testCollection(c1,"{b1,b2,b3}",b1,b2,b3);
		c1.add(b4);
		testCollection(c1,"{b1,b2,b3,b4}",b1,b2,b3,b4);
		
		c1.clear();
		testCollection(c1,"after clear");
		it1 = c1.iterator();
		assertEquals(false, it1.hasNext());
	}
	
	public void test07() {
		c1.add(b1);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b1} after remove(b1)");
	}
	
	public void test06() {
		c1.add(b1);
		c1.add(b2);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(490251517), it1.hasNext());
		assertEquals("Which ball should be next?",Ts(872278581), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b1,b2} after remove(b1)",b2);
	}
	
	public void test05() {
		c1.add(b1);
		c1.add(b2);
		it1 = c1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b1,b2} after remove(b2)",b1);
	}
	
	public void test04() {
		c1.add(b2);
		c1.add(b1);
		c1.add(b3);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(650685995), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(1109302213), getName(it1.next()));
		assertEquals(Tb(1356481467), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(862509612), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b2,b1,b3} after remove(b2)",b1,b3);
				
	}
	
	public void testRemove103() {
		c1.add(b3);
		c1.add(b1);
		c1.add(b2);
		it1 = c1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(true, it1.hasNext());
		assertEquals("b2", getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b3,b1,b2} after remove(b1)",b3,b2);
	}

	public void test03() {
		c1.add(b3);
		c1.add(b2);
		c1.add(b1);
		it1 = c1.iterator();
		it1.next();
		it1.next();
		it1.next();
		it1.remove();
		assertEquals(Tb(725642519), it1.hasNext());
		it1 = c1.iterator();
		assertEquals(Tb(879757027), it1.hasNext());
		assertEquals("Which ball should be next?",Ts(740148936), getName(it1.next()));
		assertEquals(Tb(2004839426), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(355454756), getName(it1.next()));
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b3,b2,b1} after remove(b1)",b3,b2);	
	}

	public void testRemove003() {
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		it1 = c1.iterator();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		assertTrue(it1.hasNext());
		assertEquals("b3",getName(it1.next()));
		testCollection(c1,"{b1,b2,b3} after remove(b1,b2)",b3);
	}

	public void testRemove020() {
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		it1 = c1.iterator();
		it1.next(); it1.remove();
		it1.next();
		it1.next(); it1.remove();
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b1,b2,b3} after remove(b1,b3)",b2);
	}

	public void testRemove000() {
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		it1 = c1.iterator();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		it1.next(); it1.remove();
		assertEquals(false, it1.hasNext());
		it1 = c1.iterator();
		assertEquals(false, it1.hasNext());
		testCollection(c1,"{b1,b2,b3} after remove(b1,b2,b3)");
	}

	public void testRemove1034() 
	{
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		c1.add(b4);
		testCollection(c1,"{b1 b2 b3 b4}",b1,b2,b3,b4);
		
		it1 = c1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		assertTrue("Two more after b2 removed",it1.hasNext());
		assertEquals("Next after b2 removed","b3",getName(it1.next()));
		assertTrue("One more and next() after b2 removed",it1.hasNext());
		assertEquals("Next after next() after b2 removed","b4",getName(it1.next()));
		assertTrue("Only two more after b2 removed",!it1.hasNext());		
		testCollection(c1,"{b1,b2,b3,b4} after remove(b2)",b1,b3,b4);
		
		c1.clear();
		testCollection(c1,"after clear");
	}
	
	public void testRemove02() {
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		c1.add(b4);
		
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		assertEquals(Tb(1387930327), it1.hasNext());
		assertEquals("Which ball should be next?",Ts(2067552273),getName(it1.next()));
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1)",b2,b3,b4);
		assertEquals(Tb(630379853),it1.hasNext());
		assertEquals("Which ball should be next?",Ts(1902021469),getName(it1.next()));
		it1.remove();
		assertEquals(Tb(1593286384), it1.hasNext());
		assertEquals("Which ball should be next?",Ts(544491364),getName(it1.next()));
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1,b3)",b2,b4);
		it1.remove();
		assertEquals(Tb(1879427740),it1.hasNext());
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1,b3,b4)",b2);
		
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		assertEquals(false, it1.hasNext());//
		testCollection(c1,"all removed");
		
	}
	
	public void testRemoveEvenMore0030() {
		c1.add(b1);
		c1.add(b2);
		c1.add(b3);
		c1.add(b4);
		
		it1= c1.iterator();
		it1.next();
		it1.remove();
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1)",b2,b3,b4);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1,b2)",b3,b4);
		it1 = c1.iterator();
		it1.next();
		it1.next();
		it1.remove();
		testCollection(c1,"{b1,b2,b3,b4} after remove(b1,b2,b4)",b3);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		testCollection(c1,"all removed again");
	}
	
	public void testEmptyNext()
	{
		it1 = c1.iterator();
		try {
			it1.next();
			assertFalse("next() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.next() threw wrong exception ",ex instanceof NoSuchElementException);
		}
		assertEquals(false, it1.hasNext());
		testCollection(c1,"still empty");
	}
	
	public void testEmptyRemove()
	{
		it1 = c1.iterator();
		try {
			it1.remove();
			assertFalse("remove() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.remove() threw wrong exception ",ex instanceof IllegalStateException);
		}
		assertEquals(false, it1.hasNext());//
		testCollection(c1,"yet still empty");

	}
	
	public void testStaleHasNext()
	{
		it1 = c1.iterator();
		c1.add(b3);
		try {
			it1.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(c1,"{b3}",b3);
	}
	
	public void test01()
	{
		c1.add(b3);
		it1 = c1.iterator();
		try {
			it1.remove();
			assertTrue("remove() at start of iteration should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("just started remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(743707309), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(2098552880), getName(it1.next()));
		testCollection(c1,"still {b3}",b3);
	}
	
	public void testAfterRemoveLast()
	{
		c1.add(b4);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		try {
			it1.next();
			assertTrue("next() after removed only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after removal of only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after removal of only element, hasNext() should still be false",(!it1.hasNext()));
		testCollection(c1,"{b4} after remove (b4)");
	}
	
	public void test00()
	{
		c1.add(b2);
		c1.add(b4);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		try {
			it1.remove();
			assertTrue("remove() after remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(1431749887), it1.hasNext());
		assertEquals("Which ball should be next?", Ts(1918724512), getName(it1.next()));
		testCollection(c1,"{b2,b4} after remove (b2)",b4);
	}
	
	public void testStaleHasNextAtEnd()
	{
		c1.add(b3);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		c1.add(b4);
		try {
			it1.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(c1,"{b4}",b4);
	}
	
	public void testNextAtEnd()
	{
		c1.add(b2);
		it1 = c1.iterator();
		it1.next();
		try {
			it1.next();
			assertTrue("next() after iterated past only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after iteration past only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after iteration past only element, hasNext() should still be false",(!it1.hasNext()));
		testCollection(c1,"{b2}",b2);
	}
	
	public void testStaleAfterRemoveSame()
	{
		c1.add(b5);
		c1.add(b1);
		it1 = c1.iterator();
		it2 = c1.iterator();
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
		testCollection(c1,"{b5,b1} after remove (b5)",b1);
		
	}
	
	public void testStateAfterRemoveOther() {
		c1.add(b1);
		c1.add(b3);
		it1 = c1.iterator();
		it1.next();
		it1.remove();
		
		it2 = c1.iterator();
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
		testCollection(c1,"{b1,b3} after remove (b1,b3)");
	}
	
	public void testStaleAfterClear() {
		c1.add(b1);
		it1 = c1.iterator();
		c1.clear();
		
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
