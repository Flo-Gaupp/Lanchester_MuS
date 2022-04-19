package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import utils.ApplicationTime;

public class _5_ClockAnimation extends Animation {

	private static JFrame controlFrame;
	static JButton[] button = new JButton[3];

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create Frame
		 */
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new _5_ClockAnimationPanel(applicationTimeThread);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);

		frames.add(frame);

		createControlFrame(applicationTimeThread);
		return frames;
	}

	private static void createControlFrame(ApplicationTime thread) {
		// Create a new frame
		controlFrame = new JFrame("Mathematik und Simulation");
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlFrame.setLayout(new GridLayout(1, 2, 10, 0)); // manages the layout of panels in the frame

		// Add a JPanel as the new drawing surface
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 4, 5, 0)); // manages the layout of elements in the panel (buttons, labels,
														// other panels, etc.)
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridLayout(2, 2, 5, 5));
		controlFrame.add(panel);
		controlFrame.add(scrollPanel);
		controlFrame.setVisible(true);

		// set up first Panel
		for (int b = 0; b < 3; b++) {
			button[b] = new JButton();
			button[b].setBackground(Color.WHITE);
			button[b].addActionListener(new ControlButtons(button[b], controlFrame, thread));
			panel.add(button[b]);
		}

		button[0].setText("Continue");
		button[1].setText("Stop");
		button[2].setText("Pause");

		// set up second panel
		JLabel scrollLabel = new JLabel("Adjust timescaling:");
		JLabel timeScalingLabel = new JLabel("Current Scaling :");
		JLabel currentScaling = new JLabel("1");

		JScrollBar scrollBar = new JScrollBar(0, 1, 5, -50, 55);
		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				double newScaling = (double) scrollBar.getValue() / 5;
				thread.changeTimeScaling(newScaling);
				currentScaling.setText(Double.toString(newScaling));
			}
		});

		scrollPanel.add(scrollLabel);
		scrollPanel.add(scrollBar);

		scrollPanel.add(timeScalingLabel);
		scrollPanel.add(currentScaling);
		controlFrame.pack();

	}

}

class ControlButtons implements ActionListener {
	JButton button;
	JFrame frame;
	ApplicationTime applicationTimeThread;

	public ControlButtons(JButton button, JFrame frame, ApplicationTime applicationTimeThread) {
		this.button = button;
		this.frame = frame;
		this.applicationTimeThread = applicationTimeThread;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (button.getText() == "Pause") {
			applicationTimeThread.pauseTime();
			System.out.println("Pause pressed");
		} else if (button.getText() == "Stop") {
			applicationTimeThread.endThread();
			System.out.println("Stop pressed");
		} else if (button.getText() == "Continue") {
			applicationTimeThread.continueTime();
			System.out.println("Continue pressed");
		}
	}
}

@SuppressWarnings("serial")
class _5_ClockAnimationPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;

	public _5_ClockAnimationPanel(ApplicationTime thread) {
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
		int centerX = 400;
		int centerY = 300;

		g.drawString("Time in Seconds: " + time, 330, 50);

		// draw the clock face
		for (int i = 1; i <= 12; i++) {

			g.setColor(Color.RED);
			double[] lineStart = getPosition(2 * Math.PI * i / 12, 100);
			double[] lineEnd = getPosition(2 * Math.PI * i / 12, 200);
			g.drawLine((int) lineStart[0], (int) lineStart[1], (int) lineEnd[0], (int) lineEnd[1]);

			g.setColor(Color.BLACK);
			double[] numerals = getPosition(2 * Math.PI * i / 12, 210);
			g.drawString("" + i, (int) numerals[0], (int) numerals[1]);
		}

		// draw the clock handles
		g.setColor(Color.BLACK);
		// seconds
		double[] handle1Endpoint = getPosition(Math.PI / 30 * time, 150);
		g.drawLine(centerX, centerY, (int) handle1Endpoint[0], (int) handle1Endpoint[1]);

		g.setColor(Color.BLUE);
		// minutes
		double[] handle2Endpoint = getPosition(Math.PI / 360 * time, 200);
		g.drawLine(centerX, centerY, (int) handle2Endpoint[0], (int) handle2Endpoint[1]);

	}

	// helper method to determine position of points on a circle
	public double[] getPosition(double d, double offset) {
		double[] result = new double[2];
		// (- Math.PI/2) to turn the clock by 90deg
		result[0] = 400 + offset * Math.cos(d - Math.PI / 2);
		result[1] = 300 + offset * Math.sin(d - Math.PI / 2);

		return result;
	}
}
