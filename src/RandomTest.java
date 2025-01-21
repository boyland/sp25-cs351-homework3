import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.uwm.cs.random.AbstractRandomTest;
import edu.uwm.cs.random.Command;
import edu.uwm.cs351.HexCoordinate;
import edu.uwm.cs351.HexTile;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.Terrain;
import edu.uwm.cs351.test.ArrayCollection;


public class RandomTest extends AbstractRandomTest<ArrayCollection<HexTile>,HexTileCollection> {

	private static final int MAX_TESTS = 1_000_000;
	private static final int DEFAULT_MAX_TEST_LENGTH = 1_000;
		
	public static final int NUM_HEXTILES = 10;
	private List<HexTile> HexTiles = new ArrayList<>();
		
	@SuppressWarnings("unchecked")
	private static Class<ArrayCollection<HexTile>> refClass = (Class<ArrayCollection<HexTile>>)(Class<?>)ArrayCollection.class;
	@SuppressWarnings("unchecked")
	private static Class<Iterator<HexTile>> itClass = (Class<Iterator<HexTile>>)(Class<?>)Iterator.class;
    private final RegisteredClass<Iterator<HexTile>, Iterator<HexTile>> additMutClass = super.registerMutableClass(itClass, itClass, "Iterator<HexTile>", "i");

    protected RandomTest() {
		super(refClass, HexTileCollection.class, "HexTileCollection", "tc", MAX_TESTS, DEFAULT_MAX_TEST_LENGTH);
		HexTiles.add(null);
		Terrain[] terrains = Terrain.values();
		for (int i=1; i < NUM_HEXTILES; ++i) {
			HexTiles.add(new HexTile(terrains[i % terrains.length], new HexCoordinate(i,i)));
		}
	}
	
	protected HexTile p(Random r) {
		return HexTiles.get(r.nextInt(NUM_HEXTILES));
	}
	
    @Override
    public String toString(Object x) {
            if (x instanceof HexTile) {
                    HexTile ht = (HexTile) x;
                    int i = ht.getLocation().a();
                    return "t[" + i+ "]";
            }
            return super.toString(x);
    }

	private Command<?> newSequenceCommand = newCommand();
    private Function<Integer,Command<?>> sizeCommand = build(lift(ArrayCollection<HexTile>::size), lift(HexTileCollection::size), "size"); 
    private Function<Integer, Command<?>> clearCommand = build(lift(ArrayCollection<HexTile>::clear), lift(HexTileCollection::clear), "clear");
    private Function<Integer, Command<?>> isEmptyCommand = build(lift(ArrayCollection<HexTile>::isEmpty), lift(HexTileCollection::isEmpty), "isEmpty");
    private BiFunction<Integer, HexTile, Command<?>> addCommand = build(lift(ArrayCollection<HexTile>::add), lift(HexTileCollection::add), "add");
    private BiFunction<Integer, HexTile, Command<?>> containsCommand = build(lift(ArrayCollection<HexTile>::contains), lift(HexTileCollection::contains), "contains");
    private BiFunction<Integer, HexTile, Command<?>> removeCommand = build(lift(ArrayCollection<HexTile>::remove), lift(HexTileCollection::remove), "remove");
    private BiFunction<Integer, Integer, Command<?>> addAllCommand = build(lift(ArrayCollection<HexTile>::addAll), lift(HexTileCollection::addAll), mainClass, "addAll");

    private Function<Integer,Command<?>> iteratorCommand = build(lift(additMutClass,ArrayCollection::iterator), lift(additMutClass,HexTileCollection::iterator), "iterator");
    private Function<Integer,Command<?>> hasNextCommand = build(additMutClass,lift(Iterator<HexTile>::hasNext),"hasNext");
    private Function<Integer,Command<?>> nextCommand = build(additMutClass,lift(Iterator<HexTile>::next),"next");
    private Function<Integer,Command<?>> itRemoveCommand = build(additMutClass,lift(Iterator<HexTile>::remove),"remove");

    @Override
    protected Command<?> randomCommand(Random r) {
    	int n = mainClass.size();
    	if (n == 0) return newSequenceCommand;
        int ni = additMutClass.size();
        int index = r.nextInt(n);
    	int w = r.nextInt(n);

    	switch (r.nextInt(29)) {
    	default:
    	case 0:
    		return newSequenceCommand;
    	case 1:
    		return addAllCommand.apply(w, r.nextInt(n+1)-1);
    	case 2:
    		return clearCommand.apply(w);
    	case 3: 		
    	case 4:
    	case 5:
    		return addCommand.apply(w,p(r));
    	case 6:
    	case 7:
    		return isEmptyCommand.apply(w);
    	case 8:
    	case 9:
    		return sizeCommand.apply(w);
        case 10:
        case 11:
        case 12: 
        	return addCommand.apply(index, p(r));
        case 13:
        case 14:
        case 15:
        case 16:
                if (ni == 0) return iteratorCommand.apply(index);
                return hasNextCommand.apply(r.nextInt(ni)); 
        case 17:
        case 18:
        case 19:
                if (ni == 0) return iteratorCommand.apply(index);
                return nextCommand.apply(r.nextInt(ni)); 
        case 20:
                if (ni == 0) return iteratorCommand.apply(index);
                return itRemoveCommand.apply(r.nextInt(ni));
        case 21:
        case 22:
        case 23:
        case 24:
                return containsCommand.apply(index,  p(r));
        case 25:
        case 26:
        case 27:
        case 28:
                return removeCommand.apply(index,  p(r));
    	}
    }


	@Override
	public void printImports() {	
		super.printImports();
		System.out.println("import java.util.Iterator;\n");	
		System.out.println("import edu.uwm.cs351.HexTile;");	
		System.out.println("import edu.uwm.cs351.HexCoordinate;");	
		System.out.println("import edu.uwm.cs351.Terrain;");	
		System.out.println("import edu.uwm.cs351.HexTileCollection;\n");
	}
	
	@Override
	public void printHelperMethods() {
		super.printHelperMethods();
		System.out.println("\tHexTile[] t = new HexTile[]{ null,");
		Terrain[] terrains = Terrain.values();
		for (int i=1; i < NUM_HEXTILES; ++i) {
			System.out.println("\t\tnew HexTile(Terrain." + terrains[i%terrains.length] + ", new HexCoordinate("+i+","+i+")),");
		}
		System.out.println("\t};\n");
	}
	
	public static void main(String[] args) {
		RandomTest rt = new RandomTest();
		rt.run();
	}
}
