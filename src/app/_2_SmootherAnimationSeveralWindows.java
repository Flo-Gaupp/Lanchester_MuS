package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lanchesterSimulation.Animation;
import utils.ApplicationTime;

public class _2_SmootherAnimationSeveralWindows extends Animation{

	
	public static int positionX;
	public static int positionY;

	
	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create MainFrame
		 */
		JFrame mainFrame = new JFrame("Mathematik und Simulation");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new MainGraphicsContent(applicationTimeThread);
		mainFrame.add(mainPanel);
		mainFrame.pack(); // adjusts size of the JFrame to fit the size of it's components
		mainFrame.setVisible(true);

		/**
		 * Create SecondaryFrame
		 */
		JFrame secondaryFrame = new JFrame("Position");
		secondaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel secondaryPanel = new SecondaryGraphicsContent();
		secondaryFrame.add(secondaryPanel);
		secondaryFrame.pack(); // adjusts size of the JFrame to fit the size of it's components
		secondaryFrame.setVisible(true);
		
		/****************************************/ 
		frames.add(mainFrame);
		frames.add(secondaryFrame);
		return frames;
	}

}


@SuppressWarnings("serial")
class MainGraphicsContent extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;

	public MainGraphicsContent(ApplicationTime thread) {
		this.t = thread;
	}

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
	}

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		time = t.getTimeInSeconds();
		System.out.println("Time since start: " + time);

		int vx = 70;
		int vy = 80;

		_2_SmootherAnimationSeveralWindows.positionX = 20 + (int) (time * vx);
		_2_SmootherAnimationSeveralWindows.positionY = 20 + (int) (time * vy);

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);

		g.setColor(Color.GREEN);
		g.fillOval(_2_SmootherAnimationSeveralWindows.positionX, _2_SmootherAnimationSeveralWindows.positionY, 50, 50);
	}
}

@SuppressWarnings("serial")
class SecondaryGraphicsContent extends JPanel {

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(
				"x: " + _2_SmootherAnimationSeveralWindows.positionX + "  y: " + _2_SmootherAnimationSeveralWindows.positionY, 20,
				20);
		g.setColor(Color.LIGHT_GRAY);
	}
}
