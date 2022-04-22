package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lanchesterSimulation.Animation;
import utils.ApplicationTime;

public class _4_TemporalFunctionDemo extends Animation{

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create Frame
		 */
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new _4_TemporalFunctionDemoPanel(applicationTimeThread);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);
		
		frames.add(frame);
		return frames;
	}
	
}

@SuppressWarnings("serial")
class _4_TemporalFunctionDemoPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;

	public _4_TemporalFunctionDemoPanel(ApplicationTime thread) {
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

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
		double[] posVector = f(time);
		for (int i = 0; i < 100; i++) {
			g.setColor(Color.BLUE);
			g.fillOval((int) f(2 * Math.PI * i / 100)[0] + 25, (int) f(2 * Math.PI * i / 100)[1] + 15, 4, 4);
		}

		g.setColor(Color.RED);
		g.fillOval((int) posVector[0] + 25 - 15, (int) posVector[1] + 15 - 15, 30, 30);
	}

	double[] f(double t) {
		double[] result = new double[2];
		result[0] = 400 + 200 * Math.cos(t);
		result[1] = 200 + 100 * Math.sin(t);
		return result;
	}

}
