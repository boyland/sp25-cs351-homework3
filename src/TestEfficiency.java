import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.HexCoordinate;
import edu.uwm.cs351.HexTile;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.Terrain;


public class TestEfficiency extends EfficiencyTestCase {
	HexTile p1 = new HexTile(Terrain.LAND,new HexCoordinate(1,2));
	HexTile p2 = new HexTile(Terrain.WATER,new HexCoordinate(2,4));
	HexTile p3 = new HexTile(Terrain.FOREST,new HexCoordinate(3,6));
	HexTile p4 = new HexTile(Terrain.MOUNTAIN, new HexCoordinate(4,8));
	HexTile p5 = new HexTile(Terrain.DESERT,new HexCoordinate(5,10));
	HexTile p6 = new HexTile(Terrain.CITY,new HexCoordinate(6,12));
	HexTile p7 = new HexTile(Terrain.INACCESSIBLE, new HexCoordinate(7,14));
	HexTile p8 = new HexTile(Terrain.LAND,new HexCoordinate(8,15));

	HexTile p[] = {null, p1, p2, p3, p4, p5, p6, p7, p8};
	
	HexTileCollection s;
	Collection<HexTile> c;
	Iterator<HexTile> it;
	Random r;
	
	@Override
	public void setUp() {
		c = s = new HexTileCollection();
		r = new Random();
		try {
			assert 1/(int)(p5.getLocation().a()-5) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		super.setUp();
	}

	private static final int MAX_LENGTH = 1000000;
	private static final int SAMPLE = 100;	
	private static final int MAX_WIDTH = 100000;
	
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, c.size());
			c.add(p[i%p.length]);
		}
	}

	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i%p.length]);
		}
		assertEquals(true, c.remove(p[0]));
	}

	
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i % (p.length-1)]);
		}
		c.add(p[p.length-1]);
		assertEquals(true, c.remove(p[p.length-1]));
	}
		
	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i%p.length]);
			assertSame(p[0], c.iterator().next());
		}
	}

	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i%p.length]);
		}
		it = c.iterator();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertSame(p[i%p.length], it.next());
		}
	}
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i%p.length]);
		}
		c.clear();
		assertEquals(0, c.size());
	}
	
	public void test6() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(p[i%p.length]);
		}
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.clear();
		}
	}

	public void test7() {
		HexTileCollection[] a = new HexTileCollection[MAX_WIDTH];
		for (int i=0; i < MAX_WIDTH; ++i) {
			a[i] = s = new HexTileCollection();
			int n = r.nextInt(SAMPLE);
			for (int j=0; j < n; ++j) {
				s.add(p[j%6]);
			}
		}
		
		for (int j = 0; j < SAMPLE; ++j) {
			int i = r.nextInt(a.length);
			s = a[i];
			if (s.size() == 0) continue;
			int n = r.nextInt(s.size());
			Iterator<HexTile> it = s.iterator();
			HexTile current = it.next();
			for (int k=0; k < n; ++k) {
				current = it.next();
			}
			assertSame(p[n%6],current);
		}
	}

}
