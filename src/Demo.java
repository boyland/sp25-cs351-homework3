import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uwm.cs351.FormatException;
import edu.uwm.cs351.HexTileCollection;
import edu.uwm.cs351.HexTile;

/**
 * Render files of hex tiles on the screen.
 * The main program should be executed with a series of
 * files.  Each file is opened in turn and the hex tiles are read from it.
 * If no files are given to the main program, it reads hex tiles
 * from standard input.
 */
public class Demo extends JFrame {
	/**
	 * Eclipse wants this
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		final HexTileCollection col = new HexTileCollection();
		try {
			if (args.length == 0) {
				System.out.println("Enter hex tiles (one on each line) and then type 'quit'.");
				readCol(col,new BufferedReader(new InputStreamReader(System.in)));
			} else {
				for (String arg : args) {
					readCol(col,new BufferedReader(new FileReader(arg)));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Demo x = new Demo(col);
				x.setSize(500, 300);
				x.setVisible(true);
				x.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		});
	}
	
	private static void readCol(HexTileCollection col, BufferedReader r) throws IOException {
		String input;
		while ((input = r.readLine()) != null && !input.equalsIgnoreCase("quit")) {
			try {
				// #(
				col.add(HexTile.fromString(input));
				// #)
				// TODO: use HexTiles static fromString method to get a hex tile and then add it to the collection
			} catch (FormatException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	@SuppressWarnings("serial")
	public Demo(final HexTileCollection col) {
		this.setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// #(
				for (HexTile ht : col) {
					ht.draw(g);
				}
				// #)
				// TODO: draw all the hex tiles in the collection (using HexTile#draw)
				// You should use the collection's iterator to do this
			}
		});
	}
}
