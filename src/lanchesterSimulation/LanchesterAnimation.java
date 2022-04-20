package lanchesterSimulation;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.ApplicationTime;
import app.*;

public class LanchesterAnimation extends Animation {

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create Frame
		 */
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new LanchesterAnimationPanel(applicationTimeThread);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);
		
		frames.add(frame);
		return frames;
	}

}

@SuppressWarnings("serial")
class LanchesterAnimationPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;

	public LanchesterAnimationPanel(ApplicationTime thread) {
		this.t = thread;
	}

	// set this panel's preferred size for auto-sizing the container JFrame
	public Dimension getPreferredSize() {
		return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
	}

	int width = _0_Constants.WINDOW_WIDTH;
	int height = _0_Constants.WINDOW_HEIGHT;
	Populations test = new Populations (100000,100000,0.08,0.06);
	int diameter = 30;
	int startGx = 100;
	int startGy = 200;
	int startHx = 850-diameter;
	int startHy = 200;
	int unitsPerCircle = test.unitsPerCircle(); 

	

	// drawing operations should be done in this method
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		time = t.getTimeInSeconds();

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, _0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);

		double gStatus = test.gStatus(time);
		double hStatus = test.hStatus(time);
		int gCircles =  (int) Math.round(gStatus / unitsPerCircle);
		int hCircles = (int) Math.round(hStatus / unitsPerCircle);

		
		int gColumnPosition = startGx;
		int gRowPosition = startGy;
		
		g.setColor(Color.RED);
		
		for (int i = 0; i < gCircles; i++) {
			if (i!=0 && ((i+1)%10) == 1) {
				gRowPosition += diameter+5;
				gColumnPosition = startGx;
			}
			g.fillOval(gColumnPosition, gRowPosition, diameter, diameter);
			gColumnPosition += diameter+5;
		}
		
		int hColumnPosition = startHx;
		int hRowPosition = startHy;
		
		g.setColor(Color.BLUE);
		
		for (int i = 0; i < hCircles; i++) {
			if (i!=0 && ((i+1)%10) == 1) {
				hRowPosition += diameter+5;
				hColumnPosition = startHx;
			}
			g.fillOval(hColumnPosition, hRowPosition, diameter, diameter);
			hColumnPosition -= (diameter+5);
		}
		
		test.prognosis();		
		
		
		if (gStatus<=0.5 || hStatus<=0.5) {
			t.endThread();
			System.out.println(test.result);
		} 
		
		generateGraph (g,gStatus,hStatus);
		label (g,gStatus,hStatus);
		
	}
		
		protected void label (Graphics g, double gStatus, double hStatus) {
			
			g.setColor(Color.BLACK);
			g.drawString("Erwartung durch Rechnung: ", 20, 20);
			g.drawString("Ein Kreis entspricht: " + unitsPerCircle + " Einheiten", 650, 950);
			
			if (test.winner == "G") {
				g.setColor(Color.RED);
			}
			else if (test.winner == "H") {
				g.setColor(Color.BLUE);
			}
			g.drawString(test.result + "        Endpopulation: " + test.endPopulation + "        Gefechtszeit: " + test.fightTime + "s", 20, 40);
			
			g.setColor(Color.BLACK);
			g.drawString("Time: "+time + "s", 20, 70);
			
			g.setColor(Color.RED);
			g.drawString("Aktuelle Population G: "+Math.round(gStatus), 20, 90);
			g.setColor(Color.BLUE);
			g.drawString("Aktuelle Population H: "+Math.round(hStatus), 20, 110);
			
			g.setColor(Color.RED);
			g.drawString("Startpopulation: " + Math.round(test.gStart) + "    s = " + test.s, startGx, startGy-15);
			g.setColor(Color.BLUE);
			g.drawString("Startpopulation: " + Math.round(test.hStart) + "    r = " + test.r, startHx-(9*diameter+45), startHy-15);


		}
		
		
		protected void generateGraph (Graphics g, double gStatus, double hStatus) {
			int ursprungX = 1010;
			int ursprungY = 700;
			int endX = ursprungX+440;
			int endY = ursprungY-500;
			int xLength = endX-ursprungX;
			int yLength = ursprungY-endY;
			
			//Koordinatensystem
			g.setColor(Color.black);
			g.drawLine(ursprungX, ursprungY, ursprungX, endY);
			g.drawLine(ursprungX, ursprungY, endX, ursprungY);
			g.drawLine(endX, ursprungY, endX-5, ursprungY-3);
			g.drawLine(endX, ursprungY, endX-5, ursprungY+3);
			g.drawLine(ursprungX, endY, ursprungX-3, endY+5);
			g.drawLine(ursprungX, endY, ursprungX+3, endY+5);
			g.drawString("time", endX+10, ursprungY);
			g.drawString("Population", ursprungX-30, endY-20);
			
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX+i*(xLength/20), ursprungY+3, ursprungX+i*(xLength/20), ursprungY-3);
			}
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX-3, ursprungY-i*(yLength/20), ursprungX+3, ursprungY-i*(yLength/20));
			}
			
			//Beschriftung Koordinatensystem
			
			double scaleTimeLabel = (test.fightTime+2)/20;
			double scalePopulationLabel;
			double scaleTimeDrawing = (test.fightTime+2)/xLength;
			double scalePopulationDrawing;
			
			if (test.gStart>test.hStart) {
				scalePopulationLabel = test.gStart/20;
				scalePopulationDrawing = test.gStart/yLength;
			}
			else {
				scalePopulationLabel = test.hStart/20;
				scalePopulationDrawing = test.hStart/yLength;
			}
			
			for (int i = 1; i<21; i++) {
				int label = (int) Math.round(scalePopulationLabel*i);
				g.drawString(""+label, ursprungX-60, ursprungY-i*(yLength/20)+5);
			}
			for (int i = 1; i<21; i++) {
				int label = (int)Math.round(scaleTimeLabel*i);
				g.drawString(""+label, (ursprungX+i*(xLength/20))-6, ursprungY+20);
			}
			
			//Graph
			
			int [] graphGy = new int [yLength];
			int [] graphHy = new int [yLength];
			
			for (int i = 0; i<xLength; i++) {
				graphGy [i] = (int) Math.floor(test.gStatus(i*scaleTimeDrawing));
				graphHy [i] = (int) Math.floor(test.hStatus(i*scaleTimeDrawing));
				
				if (i<=time/scaleTimeDrawing) {
					g.setColor(Color.RED);
					g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)-1), 3, 3);
					g.setColor(Color.BLUE);
					g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)-1), 3, 3);
				}
			}
			
			
			
			
			
			
			
			
			
		}


		/**
		 * Exercises:
		 * 
		 * 1) Use the same initial conditions ( startX, startY, vX, vY ) as above. Let
		 * the circle/ball "bounce off the vertical walls", i.e. provide for correct
		 * changes of velocity whenever the circle/ball "collides elastically with" the
		 * right-hand or the left-hand frame borders.
		 * 
		 * Hints: (i) As soon as the condition if( currentX >= width) yields "TRUE", do
		 * the following - reverse vX: vX = -vX; - assign the value "width" to the
		 * variable "startX"; - assign the value "time" to a new variable
		 * "collisionTime", that you have to add to the code. (ii) Replace the code line
		 * "currentX = startX + time * vX;" by "currentX = startX + (time -
		 * collisionTime) * vX;"
		 * 
		 * Improve the application by ensuring that the ball does not penetrate into the
		 * right-hand wall.
		 * 
		 * 2) Choose any initial conditions (startX, startY, vX, vY ). Let the
		 * circle/ball "bounce off the walls", i.e. provide for correct changes of
		 * velocity whenever the circle/ball "collides elastically with" any of the four
		 * frame borders.
		 * 
		 * (i) As an alternative to the techniques employed in Exercise 1), use now what
		 * we may call the "deltaTime paradigm":
		 * 
		 * double deltaTime = 0.0; double lastFrameTime = 0.0;
		 * 
		 * deltaTime = time - lastFrameTime; lastFrameTime = time; currentX = currentX +
		 * (vX * deltaTime); currentY = currentY + (vY * deltaTime);
		 * 
		 * if (currentX >= _0_Constants.WINDOW_WIDTH - diameter) {
		 * System.out.println("Object has hit the right-hand wall."); vX = -vX; currentX
		 * = currentX - 1; // One pixel is subtracted from to the current x-coordinate
		 * if the ball is at // the right-hand boundary. This prevents the ball from
		 * "sticking to the border" after a collision
		 * 
		 * (ii) Note the following ways of formatting numerical output:
		 *
		 * System.out.println("vX = " + (double) Math.round(100 * vX) / 100);
		 * System.out.println("currentX = " + (double) Math.round(100 * currentX) /
		 * 100); System.out.println("currentY = " + (double) Math.round(100 * currentY)
		 * / 100 + '\n');
		 * 
		 * 
		 * 3) Simulate the motion of the ball/circle under the influence of gravity
		 * Place the circle a some height h above the floor (bottom frame border) with
		 * initial velocity vY = 0. Let the circle/ball undergo accelerated motion
		 * toward the bottom. Once the ball hits the floor, its velocity is reversed
		 * (fully elastic collision), the ensuing upward motion is decelerated until the
		 * circle/ball comes to rest a height h, etc. * 4) As in Exercise 3) except that
		 * there shall now be a loss of kinetic energy each time the ball hits the
		 * botton.
		 * 
		 * 
		 */

	
}
