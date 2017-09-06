package edu.uwm.cs351;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.time.Instant;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The Class DodgeBall.
 */
public class DodgeBall implements Runnable{
	
	//Constants
	static final int FPS = 60;
	static final Dimension PANEL_OFFSET = new Dimension(5,51);
	static final int CLOCK_HEIGHT = 22;
	static final int COUNTDOWN = 5, LEVEL_DURATION = 12;
	
	////////////////////////////////////////////////////
	//         TWEAK THESE TO TEST SCENARIOS          //
	////////////////////////////////////////////////////
	static final Dimension BOUNDS = new Dimension(600,600);
	static final int PLAYER_RADIUS = 15;
	static final int INITIAL_BALL_COUNT = 5;
	static final double MIN_SPEED = 4.5, MAX_SPEED = 6.5;
	static final boolean MORTAL = true;
	/////////////////////////////////////////////////////
	
	//Fields
	private JFrame frame;
	private DodgeBallPanel panel;
	private JLabel _displayClock;
	private Player _player;
	// #(
	private BallCollection _balls;
	/* #)
	private BallSeq _balls; // TODO: Change to be a BallCollection.
	## */
	private Ball _deathBall;
	private int _level;
	private int _ballCount;
	private boolean _suspended, _alive;
	private Instant _startTime, _stopTime;
	
	/**
	 * Instantiates a new DodgeBall game.
	 */
	public DodgeBall(){
		SwingUtilities.invokeLater( ()->createGUI() );
		newGame();
	}
	
	//Create and set all GUI components
	private void createGUI(){
		//create frame, panel, and clock
		_displayClock = new JLabel("", SwingConstants.CENTER);
		_displayClock.setFont(new Font(_displayClock.getFont().getName(), Font.BOLD, 16));
		frame = new JFrame("DodgeBall v0.92");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new DodgeBallPanel();
		frame.add(panel);
		frame.add(_displayClock, BorderLayout.SOUTH);
		
		frame.setSize(new Dimension(BOUNDS.width+PANEL_OFFSET.width, BOUNDS.height+PANEL_OFFSET.height+CLOCK_HEIGHT));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		//create menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenuItem newGameButton = new JMenuItem("New Game");
		newGameButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		menuBar.add(newGameButton);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
	
	//Reset all game elements
	private void newGame(){
		_player = new Player(new Point(0,0));
		_player.setImg(happyImg);
		_level = 0;
		_ballCount = INITIAL_BALL_COUNT;
		_alive=true;
		nextLevel();
	}
	
	//Suspend game, create next level
	private void nextLevel(){
		_suspended = true;
		_level++;
		// #(
		_balls = new BallCollection();
		/* #)
		_balls = new BallSeq(); // TODO: Change to be a BallCollection.
		## */
		for (int i=0; i<_ballCount; i++)
			createBall(i);
		_startTime = Instant.now().plus(Duration.ofSeconds(COUNTDOWN));
		_stopTime = _startTime.plus(Duration.ofSeconds(LEVEL_DURATION));
	}
	
	//Create a ball, using helper method to make sure no balls overlap
	private void createBall(int index){
		Ball newBall;
		while (!validLocation(newBall = randomBall())) {}
		// #(
		_balls.add(newBall);
		/* #)
		_balls.addAfter(newBall); // TODO: Change to add to BallCollection.
		 ## */
	}
	
	private boolean validLocation(Ball newBall){
		// #(
		for (Ball b: _balls)
			if (b.isColliding(newBall))
				return false;
		/* #)
		// TODO: Change to use a BallCollection iterator.
		for (_balls.start();_balls.isCurrent();_balls.advance()) {
			Ball b = _balls.getCurrent();
			if (b.isColliding(newBall)) return false;
		}
		## */
		return true;
	}
	
	/*
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run(){
		Timer timer = new Timer(1000/FPS, (ae)->update());
		SwingUtilities.invokeLater(()->timer.start());
	}
	
	//Update all game elements
	private void update(){
		if (!_alive) return;
		//update time
		updateTime();
		//update player
		_player.update(getAdjustedMouseLoc());
		//update balls
		// #(
		for (Ball b: _balls)
			b.step();
		/* #)
		// TODO: Change to use a BallCollection iterator.
		for (_balls.start(); _balls.isCurrent(); _balls.advance()) {
			Ball b = _balls.getCurrent();
			b.step();}
        ## */
		if (!_suspended){
			checkCollisions();
			checkGameOver();}
	}
	
	//Update times, queue start of rounds and transitions between rounds
	private void updateTime(){
		if (Instant.now().isBefore(_startTime))
			_displayClock.setText(""+(Duration.between(Instant.now(), _startTime).getSeconds()+1));
		else if (Instant.now().isBefore(_stopTime)){
			if (_suspended){
				_suspended = false;
				// #(
				for (Ball b: _balls)
					b.launch();
				}
				/* #)
				// TODO: Change to use a BallCollection iterator.
				for (_balls.start(); _balls.isCurrent(); _balls.advance()) {
					Ball b = _balls.getCurrent();
					b.launch();}}
			    ## */
			_displayClock.setText(""+(Duration.between(Instant.now(), _stopTime).getSeconds()+1));}
		else{
			makeHarder();
			nextLevel();}
	}
	
	//Make game harder as rounds progress
	//Feel free to add your own!
	private void makeHarder(){
		switch ((int) (Math.random() * 2)){
			case 0: {_ballCount++;System.out.println("Added ball.");break;}
			default: {_player.grow();System.out.println("Increased player size.");break;}
		}
	}

	//Get mouse location relative to upper left corner of panel
	//We chose to use this instead of a MouseMotionListener to enable
	//player movement whether or not the cursor is within the panel.
	private Point getAdjustedMouseLoc(){
		return new Point((int) MouseInfo.getPointerInfo().getLocation().getX()-frame.getX()-PANEL_OFFSET.width,
						 (int) MouseInfo.getPointerInfo().getLocation().getY()-frame.getY()-PANEL_OFFSET.height);}
	
	//Check ball-on-ball and ball-on-wall collisions
	private void checkCollisions(){
		// #(
		for (Ball a: _balls){
			for (Ball b: _balls){
				if (a != b && a.isColliding(b))
					a.bounce(b);}
			a.bounceWalls(DodgeBall.BOUNDS);}
		/* #)
		// TODO: Change to use BallCollection iterators.
		// NB: Calling the bounce methods will not make our iterators fail because these
		//	   methods change the state of the balls themselves and not the BallCollection.
		BallCollection copy = _balls.clone();
		for (_balls.start(); _balls.isCurrent(); _balls.advance()) {
			Ball a = _balls.getCurrent();
			for (copy.start(); copy.isCurrent(); copy.advance()) {
				Ball b = copy.getCurrent();
				if (a != b && a.isColliding(b)) 
					a.bounce(b);}
			a.bounceWalls(DodgeBall.BOUNDS);}
		## */
	}
	
	//Check if the player was hit by any balls
	private void checkGameOver(){
		// #(
		for (Ball b: _balls){
			if (_player.isColliding(b)){
				_deathBall = b;
				gameover();}}
		/* #)
		// TODO: Change to use a BallCollection iterator.
		for (_balls.start(); _balls.isCurrent(); _balls.advance()) {
			Ball b = _balls.getCurrent();
			if (_player.isColliding(b)){
				_deathBall = b;
				gameover();}}
        ## */
	}
	
	private void gameover(){
		_alive=false;
		_suspended=true;
		_player.setImg(sadImg);
		panel.repaint();
	}
	
	@SuppressWarnings("serial")
	private class DodgeBallPanel extends JPanel{
		public DodgeBallPanel(){
			setDoubleBuffered(true);
			addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent arg0) {}
				@Override
				public void mouseEntered(MouseEvent arg0) {}
				@Override
				public void mouseExited(MouseEvent arg0) {}
				@Override
				public void mousePressed(MouseEvent arg0) {if (_alive)_player.setImg(winkImg);}
				@Override
				public void mouseReleased(MouseEvent arg0) {if (_alive)_player.setImg(happyImg);}});
		}
		
		/*
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) 
		{
			//clean panel
			super.repaint();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, BOUNDS.width+PANEL_OFFSET.width, BOUNDS.height+PANEL_OFFSET.height);
			
			//draw game entities
			// #(
			for (Ball b: _balls)
				b.draw(g);
			/* #)
			// TODO: Change to use a BallCollection iterator.
			for (_balls.start(); _balls.isCurrent(); _balls.advance()) {
				Ball b = _balls.getCurrent();
				b.draw(g);}
			## */
			_player.draw(g);
			
			//draw round info/game over if animation suspended
			if (_suspended){
				//draw translucent mask
				g.setColor(new Color(0,0,0,100));
				g.fillRect(0, 0, BOUNDS.width+PANEL_OFFSET.width, BOUNDS.height+PANEL_OFFSET.height);
				g.setFont(new Font("Verdana", Font.BOLD, 24));
				if (_alive){
					//draw round info
					g.setColor(Color.WHITE);
					g.drawString("Round "+_level + "..", BOUNDS.width/2 - 62, BOUNDS.height/2);}
				else{
					//draw game over
					g.setColor(new Color(255,255,255,50));
					g.fillOval(_deathBall.getLoc().getX()-50, _deathBall.getLoc().getY()-50, 100, 100);
					_deathBall.draw(g);
					_player.draw(g);
					g.setColor(Color.WHITE);
					g.drawString("Game Over!", BOUNDS.width/2 - 70, BOUNDS.height/2);
					g.drawString("You made it", BOUNDS.width/2 - 72, BOUNDS.height/2+50);
					g.drawString(_level+" rounds.", BOUNDS.width/2 - 52, BOUNDS.height/2+80);}
			}
			
		}
	}
	
	//Random Generators
	private static Point randomLoc(){
		int locX = (int) ((DodgeBall.BOUNDS.getWidth() - 2*Ball.DEFAULT_RADIUS)* Math.random())+Ball.DEFAULT_RADIUS;
		int locY = (int) ((DodgeBall.BOUNDS.getHeight() -2*Ball.DEFAULT_RADIUS) * Math.random())+Ball.DEFAULT_RADIUS;
		return new Point(locX,locY);}
	
	private static Vector randomVec(){
		double theta = (Math.random() * Math.PI / 2) + Math.PI;
		double magnitude = DodgeBall.MIN_SPEED + Math.random() * (DodgeBall.MAX_SPEED - DodgeBall.MIN_SPEED);
		return new Vector(theta).scale(magnitude);}
	
	private static Color randomColor(){
		return new Color((int)(Math.random() * 200),
						 (int)(Math.random() * 200),
						 (int)(Math.random() * 200));}

	private static Ball randomBall() {
		return new Ball(randomLoc(),randomVec(),randomColor());}
	
	//Images
	private static BufferedImage happyImg, winkImg, sadImg;
	static{
		try {happyImg = ImageIO.read(new File("images/happy_face.png"));
			 winkImg = ImageIO.read(new File("images/wink_face.png"));
			 sadImg = ImageIO.read(new File("images/sad_face.png"));}
		catch (Exception e){System.out.println("Error loading player image.");}
	}
	
	/**
	 * The main method. Creates a new DodgeBall game and runs it.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		new DodgeBall().run();}
}
