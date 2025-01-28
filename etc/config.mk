HW1= ${REPOS}/homework1

INSTALLSRC = \
	src/TestCollection.java \
	src/TestEfficiency.java \
	src/TestHexTileCollection.java \
	src/TestInvariant.java \
	src/RandomTest.java \
	src/UnlockTest.java \
	src/edu/uwm/cs351/FormatException.java \
	src/edu/uwm/cs351/HexCoordinate.java \
	test/sample.hex

INSTALLSKEL = \
	src/edu/uwm/cs351/HexTileCollection.java \
	src/edu/uwm/cs351/HexTile.java \
	src/Demo.java

INSTALLGFX = show-logical-structure.png

INSTALLDIR = src src/edu src/edu/uwm src/edu/uwm/cs351 lib test

INSTALLJAR = -C ${HW1}/bin edu/uwm/cs351 \
             ${LOCKEDTESTS} ${RANDOMTESTS}

