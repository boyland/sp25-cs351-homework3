import java.util.Iterator;
import java.util.function.Supplier;

import edu.uwm.cs351.FormatException;
import edu.uwm.cs351.HexCoordinate;
import edu.uwm.cs351.HexTile;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.Terrain;


public class TestHexTileCollection extends TestCollection<HexTile> {

	private Iterator<HexTile> it;
	
	
	@Override
	protected void initCollections() {
		c = new HexTileCollection();
		e = new HexTile[] {
				new HexTile(Terrain.INACCESSIBLE, new HexCoordinate(0,0)),
				new HexTile(Terrain.LAND, new HexCoordinate(1,1)),
				new HexTile(Terrain.WATER, new HexCoordinate(2,2)),
				new HexTile(Terrain.FOREST, new HexCoordinate(3,3)),
				new HexTile(Terrain.MOUNTAIN, new HexCoordinate(4,4)),
				new HexTile(Terrain.DESERT, new HexCoordinate(5,5)),
				new HexTile(Terrain.CITY, new HexCoordinate(6,6)),
				new HexTile(Terrain.INACCESSIBLE, new HexCoordinate(7,7)),		
				new HexTile(Terrain.LAND, new HexCoordinate(8,8)),
				new HexTile(Terrain.WATER, new HexCoordinate(9,9))
		};
		// permitNulls = true;
		// preserveOrder = true;
		// permitDuplicates = true;
		// failFast = true;
		// hasRemove = true;
	}

	protected void assertExceptionName(String name, Runnable r) {
		try {
			r.run();
			assertEquals(name, "");
		} catch (RuntimeException ex) {
			assertEquals(name, ex.getClass().getSimpleName());
		}
	}
	
	protected void assertEqualsOrException(String expected, Supplier<?> supp) {
		try {
			Object result = supp.get();
			assertEquals(expected, ""+result);
		} catch (RuntimeException ex) {
			assertEquals(expected, ex.getClass().getSimpleName());
		}
		
	}
	
	
	/// Locked tests
	
	public void test66() {
		HexTileCollection bc = new HexTileCollection();
		// Give the string of the result or name of exception
		// "it" was never assigned and so it is null; 
		assertEqualsOrException("NullPointerException",() -> it.hasNext());
		it = bc.iterator();
		assertEqualsOrException(Ts(12763851),() -> it.hasNext());
		assertEqualsOrException(Ts(775338767),() -> it.next());
		bc.add(e[1]); // added a HexTile LAND<1,1,0>
		assertEqualsOrException(Ts(268544907),() -> it.hasNext()); // when was it initialized?
		it = bc.iterator();
		assertEqualsOrException(Ts(1125408703),() -> it.hasNext());
		assertEqualsOrException(Ts(1106512103), () -> it.next());
	}
	
	/// HexTile tests:
	
	public void test90() {
		HexTile h = HexTile.fromString("LAND<1,1,0>");
		assertEquals(Terrain.LAND, h.getTerrain());
		assertEquals(new HexCoordinate(1,1,0), h.getLocation());
	}
	
	public void test91() {
		HexTile h = HexTile.fromString("FOREST<2,1,1>");
		assertEquals(Terrain.FOREST, h.getTerrain());
		assertEquals(new HexCoordinate(2,1,1), h.getLocation());
	}
	
	public void test92() {
		HexTile h = HexTile.fromString("MOUNTAIN<2,2,0>");
		assertEquals(Terrain.MOUNTAIN, h.getTerrain());
		assertEquals(new HexCoordinate(2,2,0), h.getLocation());
	}
	
	public void test93() {
		HexTile h = HexTile.fromString("CITY<1,2,-1>");
		assertEquals(Terrain.CITY, h.getTerrain());
		assertEquals(new HexCoordinate(1,2,-1), h.getLocation());
	}
	
	public void test94() {
		HexTile h = HexTile.fromString("DESERT<3,1,2>");
		assertEquals(Terrain.DESERT, h.getTerrain());
		assertEquals(new HexCoordinate(3,1,2), h.getLocation());
	}
	
	public void test95() {
		HexTile h = HexTile.fromString("WATER<3,2,1>");
		assertEquals(Terrain.WATER, h.getTerrain());
		assertEquals(new HexCoordinate(3,2), h.getLocation());
	}
	
	public void test96() {
		HexTile h = HexTile.fromString("INACCESSIBLE<-3,-3,0>");
		assertEquals(Terrain.INACCESSIBLE, h.getTerrain());
		assertEquals(new HexCoordinate(-3,-3), h.getLocation());
	}
	
	public void test97() {
		for (int a = -10; a < 10; ++a) {
			for (int b = -5; b < 5; ++b) {
				for (Terrain t : Terrain.values()) {
					HexTile h = new HexTile(t, new HexCoordinate(a,b));
					String s = t + "<" + a + "," + b + "," + (a-b) + ">";
					assertEquals("Faield to read " + s, h, HexTile.fromString(s));
				}
			}
		}
	}
	
	public void test98() {
		assertException(IllegalArgumentException.class, () -> HexTile.fromString("LAND <3,2,1>"));
	}
	
	public void test99() {
		assertException(FormatException.class, () -> HexTile.fromString("LAND<3,2,1> "));
		assertException(FormatException.class, () -> HexTile.fromString("LAND<3>"));
	}
}
