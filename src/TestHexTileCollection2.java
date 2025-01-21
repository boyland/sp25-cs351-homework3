import java.util.Iterator;
import java.util.function.Supplier;

import edu.uwm.cs351.HexCoordinate;
import edu.uwm.cs351.HexTile;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.Terrain;


public class TestHexTileCollection2 extends TestCollection<HexTile> {

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
		assertEqualsOrException("NullHexCoordinateerException",() -> it.hasNext());
		it = bc.iterator();
		assertEqualsOrException(Ts(12763851),() -> it.hasNext());
		assertEqualsOrException(Ts(775338767),() -> it.next());
		bc.add(e[1]); // added a HexTile
		assertEqualsOrException(Ts(268544907),() -> it.hasNext());
		it = bc.iterator();
		assertEqualsOrException(Ts(1125408703),() -> it.hasNext());
	}
	
	
}
