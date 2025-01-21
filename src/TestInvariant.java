import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.HexCoordinate;
import edu.uwm.cs351.HexTile;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.Terrain;
import junit.framework.TestCase;

/**
 * Tests of the HexTileCOllection invariant and its iterator invariant.
 */
public class TestInvariant extends TestCase {
	protected HexTileCollection.Spy spy;
	protected int reports;
	protected HexTileCollection r;
	protected Iterator<HexTile> i;
	
	HexTile b1 = new HexTile(Terrain.LAND,new HexCoordinate(1,2));
	HexTile b2 = new HexTile(Terrain.WATER,new HexCoordinate(2,4));
	HexTile b3 = new HexTile(Terrain.FOREST,new HexCoordinate(3,6));
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, HexTileCollection r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}
	
	protected void assertWellFormed(boolean expected, Iterator<HexTile> r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}

	@Override // implementation
	protected void setUp() {
		spy = new HexTileCollection.Spy();
	}

	public void testA0() {
		r = spy.newInstance(null, 0, 0);
		assertWellFormed(false, r);
	}
	
	public void testA1() {
		r = spy.newInstance(new HexTile[0], 0, 0);
		assertWellFormed(true, r);
	}
	
	public void testA2() {
		r = spy.newInstance(new HexTile[3], -1, 0);
		assertWellFormed(false, r);
	}
	
	public void testA3() {
		r = spy.newInstance(new HexTile[3],4,0);
		assertWellFormed(false, r);
	}
	
	public void testA4() {
		r = spy.newInstance(new HexTile[4],4,0);
		assertWellFormed(true, r);
	}
	
	public void testA5() {
		r = spy.newInstance(new HexTile[10],0,0);
		assertWellFormed(true, r);
	}
	
	public void testA6() {
		r = spy.newInstance(new HexTile[5],4,-1);
		assertWellFormed(true, r);
	}
	
	public void testA7() {
		r = spy.newInstance(new HexTile[3],3,3);
		assertWellFormed(true, r);
	}
	
	public void testA8() {
		r = spy.newInstance(new HexTile[5],3,4);
		assertWellFormed(true, r);
	}
	
	public void testA9() {
		r = spy.newInstance(new HexTile[5],4,400);
		assertWellFormed(true, r);
	}

	
	public void testB0() {
		r = spy.newInstance(new HexTile[0], 0, 42);
		i = spy.newIterator(r, -1, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testB1() {
		r = spy.newInstance(new HexTile[0], 0, 42);
		i = spy.newIterator(r, -1, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testB5() {
		r = spy.newInstance(new HexTile[0], 0, 42);
		i = spy.newIterator(r, 0, false, 42);
		assertWellFormed(false, i);
	}
	
	public void testB6() {
		r = spy.newInstance(new HexTile[0], 0, 42);
		i = spy.newIterator(r, 0, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testC0() {
		r = spy.newInstance(new HexTile[1], 0, 76);
		i = spy.newIterator(r, -1, true, 76);
		assertWellFormed(false, i);
	}

	public void testC1() {
		r = spy.newInstance(new HexTile[1], 0, 76);
		i = spy.newIterator(r, -1, false, 76);
		assertWellFormed(true, i);
	}

	public void testC2() {
		r = spy.newInstance(new HexTile[1], 0, 76);
		i = spy.newIterator(r, 0, true, 76);
		assertWellFormed(false, i);
	}

	public void testC3() {
		r = spy.newInstance(new HexTile[1], 0, 76);
		i = spy.newIterator(r, 0, false, 76);
		assertWellFormed(false, i);
	}
	
	public void testC4() {
		r = spy.newInstance(new HexTile[1], 1, 76);
		i = spy.newIterator(r, -1, true, 76);
		assertWellFormed(false, i);
	}

	public void testC5() {
		r = spy.newInstance(new HexTile[1], 1, 76);
		i = spy.newIterator(r, -1, false, 76);
		assertWellFormed(true, i);
	}

	public void testC6() {
		r = spy.newInstance(new HexTile[1], 1, 76);
		i = spy.newIterator(r, 0, true, 76);
		assertWellFormed(true, i);
	}

	public void testC7() {
		r = spy.newInstance(new HexTile[1], 1, 76);
		i = spy.newIterator(r, 0, false, 76);
		assertWellFormed(true, i);
	}

	public void testC8() {
		r = spy.newInstance(new HexTile[]{b1}, 0, 76);
		i = spy.newIterator(r, 0, false, 76);
		assertWellFormed(false, i);
	}

	public void testC9() {
		r = spy.newInstance(new HexTile[] {b1}, 1, 42);
		i = spy.newIterator(r, 0, true, 42);
		assertWellFormed(true, i);
	}

	

	public void testD0() {
		r = spy.newInstance(new HexTile[2], 0, 42);
		i = spy.newIterator(r, -1, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testD1() {
		r = spy.newInstance(new HexTile[2], 2, 42);
		i = spy.newIterator(r, -1, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testD2() {
		r = spy.newInstance(new HexTile[2], 1, 42);
		i = spy.newIterator(r, 0, true, 42);
		assertWellFormed(true, i);
	}
	
	public void testD3() {
		r = spy.newInstance(new HexTile[2], 0, 42);
		i = spy.newIterator(r, 0, false, 42);
		assertWellFormed(false, i);
	}
	
	public void testD4() {
		r = spy.newInstance(new HexTile[2], 0, 42);
		i = spy.newIterator(r, 1, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testD5() {
		r = spy.newInstance(new HexTile[2], 2, 42);
		i = spy.newIterator(r, 1, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testD6() {
		r = spy.newInstance(new HexTile[2], 2, 42);
		i = spy.newIterator(r, 2, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testD7() {
		r = spy.newInstance(new HexTile[2], 2, 42);
		i = spy.newIterator(r, 2, false, 42);
		assertWellFormed(false, i);
	}
	
	public void testD8() {
		r = spy.newInstance(new HexTile[] {b1, b2}, 2, 42);
		i = spy.newIterator(r, 1, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testD9() {
		r = spy.newInstance(new HexTile[] {b1, b2}, 2, 42);
		i = spy.newIterator(r, 2, true, 42);
		assertWellFormed(false, i);
	}

	public void testE0() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, -1, true, 55);
		assertWellFormed(false, i);
	}

	public void testE1() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, -1, false, 55);
		assertWellFormed(false, i);
	}

	public void testE2() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 0, true, 55);
		assertWellFormed(false, i);
	}

	public void testE3() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 0, false, 55);
		assertWellFormed(false, i);
	}

	public void testE4() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 1, true, 55);
		assertWellFormed(false, i);
	}

	public void testE5() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 1, false, 55);
		assertWellFormed(false, i);
	}

	public void testE6() {
		r = spy.newInstance(new HexTile[] {b1}, 2, 55);
		i = spy.newIterator(r, 0, true, 55);
		assertWellFormed(false, i);
	}

	public void testE7() {
		r = spy.newInstance(new HexTile[] {b1}, 2, 55);
		i = spy.newIterator(r, 0, false, 55);
		assertWellFormed(false, i);
	}

	public void testE8() {
		r = spy.newInstance(new HexTile[] {b1}, 2, 55);
		i = spy.newIterator(r, 1, true, 55);
		assertWellFormed(false, i);
	}

	public void testE9() {
		r = spy.newInstance(new HexTile[] {b1}, 2, 55);
		i = spy.newIterator(r, 1, false, 55);
		assertWellFormed(false, i);
	}
	
	public void testF0() {
		r = spy.newInstance(new HexTile[3], 2, 55);
		i = spy.newIterator(r, 2, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testF1() {
		r = spy.newInstance(new HexTile[3], 2, 55);
		i = spy.newIterator(r, 2, true, 42);
		assertWellFormed(true, i);
	}
	
	public void testF2() {
		r = spy.newInstance(new HexTile[3], 2, 55);
		i = spy.newIterator(r, -1, true, 42);
		assertWellFormed(true, i);
	}
	
	public void testF3() {
		r = spy.newInstance(new HexTile[3], 2, 55);
		i = spy.newIterator(r, -3, true, 42);
		assertWellFormed(true, i);
	}
	
	public void testF4() {
		r = spy.newInstance(new HexTile[3], 2, 55);
		i = spy.newIterator(r, 1, false, 42);
		assertWellFormed(true, i);
	}
	
	public void testF5() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 1, false, 42);
		assertWellFormed(false, i);
	}
	
	public void testF6() {
		r = spy.newInstance(new HexTile[1], 2, 55);
		i = spy.newIterator(r, 0, false, 42);
		assertWellFormed(false, i);
	}
	
	public void testF7() {
		r = spy.newInstance(new HexTile[] {b1}, 2, 55);
		i = spy.newIterator(r, 0, true, 42);
		assertWellFormed(false, i);
	}
	
	public void testF8() {
		r = spy.newInstance(new HexTile[] {b1}, 1, 55);
		i = spy.newIterator(r, 0, true, 42);
		assertWellFormed(true, i);
	}
	
	public void testF9() {
		r = spy.newInstance(new HexTile[] {b1}, 1, 55);
		i = spy.newIterator(r, 2, true, 42);
		assertWellFormed(true, i);
	}
}
