package edu.uwm.cs351;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ManyParticles extends JPanel {

	/**
	 * Put this in to keep Eclipse happy. 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ParticleCollection all;
	
	public ManyParticles(ParticleCollection ps) {
		all = ps;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// #(
		for (Particle p : all) {
			p.draw(g);
		}
		// #)
		// TODO: draw all particles.  (Don't create a clone.)
	}
	
	public void move() {
		// #(
		for (Particle p : all) {
			Vector force = new Vector();
			// System.out.println("Force on " + p);
			for (Particle q : all) {
				// System.out.println("  from " + q);
				if (p != q) {
					force = force.add(q.gravForceOn(p));
				}
			}
			p.applyForce(force);
		}
		for (Particle p : all) {
			p.move();
		}
		// #)
		// TODO: Apply gravitational force on all particles,
		// and then Move all particles (see Homework #2 for basic idea)
		repaint();
	}
}
