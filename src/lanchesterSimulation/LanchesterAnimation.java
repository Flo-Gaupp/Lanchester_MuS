package lanchesterSimulation;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
//import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import utils.ApplicationTime;


public class LanchesterAnimation extends Animation {
	
	private static JFrame controlFrame;
	static JButton[] button = new JButton[3];
	public Populations szenario;

	@Override
	protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		/**
		 * Create Frame
		 */
		this.szenario = new Populations (90000,100000,0.08,0.06);		//klarer Sieg für G
	//	this.szenario = new Populations (90000,100000,0.06,0.08);		//klarer Sieg für H
	//	this.szenario = new Populations (10000, 5000, 0.2, 0.8);		//Pyrrhussieg für G
	//	this.szenario = new Populations (5000, 10000, 1, 0.25);		//Pyrrhussieg für H
	//	this.szenario = new Populations (10000, 10000, 1, 1);   	//tragisches Unentschieden
		JFrame frame = new JFrame("Mathematik und Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new LanchesterAnimationPanel(applicationTimeThread, szenario);
		frame.add(panel);
		frame.pack(); // adjusts size of the JFrame to fit the size of it's components
		frame.setVisible(true);
		frames.add(frame);
		createControlFrame(applicationTimeThread, szenario);
		return frames;
	}



private static void createControlFrame(ApplicationTime thread, Populations szenario) {
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
	
	
	JPanel inputPanel = new JPanel();
	inputPanel.setLayout(new GridLayout(2,2,5,5));
	controlFrame.add(panel);
	controlFrame.add(scrollPanel);
	controlFrame.add(inputPanel);
	controlFrame.setVisible(true);

	

	// set up first Panel (Buttons)
	for (int b = 0; b < 3; b++) {
		button[b] = new JButton();
		button[b].setBackground(Color.WHITE);
		button[b].addActionListener(new ControlButtons(button[b], controlFrame, thread));
		panel.add(button[b]);
	}

	button[0].setText("Continue");
	button[1].setText("Restart");
	button[2].setText("Pause");

	// set up second panel (Timescale)
	JLabel scrollLabel = new JLabel("Adjust timescaling:");
	JLabel timeScalingLabel = new JLabel("Current Scaling :");
	JLabel currentScaling = new JLabel("1");

	JScrollBar scrollBar = new JScrollBar(0, 1, 5, -20, 70);
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
	
	
	
	
	// set up third panel (InputFields)
	JLabel populationG = new JLabel("Population G:");
	JLabel populationH = new JLabel("Population H:");
	JLabel strengthG = new JLabel("Stärke G:");
	JLabel strengthH = new JLabel("Stärke H:");
	JFormattedTextField inputPopulationG = new JFormattedTextField();
	JFormattedTextField inputPopulationH = new JFormattedTextField(); 
	JFormattedTextField inputStrengthG = new JFormattedTextField(); 
	JFormattedTextField inputStrengthH = new JFormattedTextField(); 
	inputPopulationG.addActionListener(new ControlInputFields(inputPopulationG, "populationG", controlFrame, thread, szenario));
	inputPopulationH.addActionListener(new ControlInputFields(inputPopulationH, "populationH", controlFrame, thread, szenario));
	inputStrengthG.addActionListener(new ControlInputFields(inputStrengthG, "strengthG", controlFrame, thread, szenario));
	inputStrengthH.addActionListener(new ControlInputFields(inputStrengthH, "strengthH", controlFrame, thread, szenario));
	
	inputPanel.add(populationG);
	inputPanel.add(inputPopulationG);
	inputPanel.add(populationH);
	inputPanel.add(inputPopulationH);
	inputPanel.add(strengthG);
	inputPanel.add(inputStrengthG);
	inputPanel.add(strengthH);
	inputPanel.add(inputStrengthH);


	controlFrame.pack();

	
	


}

}

//Action Listener Input Fields

class ControlInputFields implements ActionListener {

	JFormattedTextField input;
	String name;
	JFrame frame;
	Populations szenario;
	ApplicationTime applicationTimeThread;

	public ControlInputFields(JFormattedTextField input, String name,JFrame frame, ApplicationTime applicationTimeThread, Populations szenario) {
		this.input = input;
		this.frame = frame;
		this.name = name;
		this.szenario = szenario;
		this.applicationTimeThread = applicationTimeThread;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (name == "populationG") {
			szenario.gStart=Double.parseDouble(input.getText()) ;
			applicationTimeThread.pauseTime();
			applicationTimeThread.timeSinceStart = 0;
			szenario.prognosis();
		} else if (name == "populationH") {
			applicationTimeThread.pauseTime();
			applicationTimeThread.timeSinceStart = 0;
			szenario.hStart=Double.parseDouble(input.getText()) ;
			szenario.prognosis();
		} else if (name == "strengthG") {
			applicationTimeThread.pauseTime();
			applicationTimeThread.timeSinceStart = 0;
			szenario.s=Double.parseDouble(input.getText()) ;
			szenario.prognosis();
		} else if (name == "strengthH") {
			applicationTimeThread.pauseTime();
			applicationTimeThread.timeSinceStart = 0;
			szenario.r=Double.parseDouble(input.getText()) ;
			szenario.prognosis();
		}
	}
	
}

//Action Listener Buttons

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
		} else if (button.getText() == "Restart") {
			applicationTimeThread.timeSinceStart = 0;
			applicationTimeThread.pauseTime();
			System.out.println("Restart pressed");
		} else if (button.getText() == "Continue") {
			applicationTimeThread.continueTime();
			System.out.println("Continue pressed");
		}
	}
}

@SuppressWarnings("serial")
class LanchesterAnimationPanel extends JPanel {

	// panel has a single time tracking thread associated with it
	private ApplicationTime t;
	private double time;
	public Populations szenario;
	int width = _0_Constants.WINDOW_WIDTH;
	int height = _0_Constants.WINDOW_HEIGHT;
	int diameter = 30;
	int startGx = 100;
	int startGy = 200;
	int startHx = 850-diameter;
	int startHy = 200;

	
	public LanchesterAnimationPanel(ApplicationTime thread, Populations szenario) {
		this.t = thread;
		this.szenario = szenario;
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

		
		//Calculate current Population
		double gStatus = szenario.gStatus(time);
		double hStatus = szenario.hStatus(time);
		
		//Calculate how many Circles need to be drawn
		int gCircles =  (int) Math.floor(gStatus / szenario.unitsPerCircle());
		int hCircles = (int) Math.floor(hStatus / szenario.unitsPerCircle());
		//Calculates the diameter of the Last Circle
		int gDiameterLastCircle =(int) (((gStatus%szenario.unitsPerCircle())/(szenario.unitsPerCircle()))*diameter);
		int hDiameterLastCircle =(int) (((hStatus%szenario.unitsPerCircle())/(szenario.unitsPerCircle()))*diameter);
		//Draw RED Circles
		int gColumnPosition = startGx;
		int gRowPosition = startGy;
		
		g.setColor(Color.RED);
		for (int i = 0; i <= gCircles; i++) {
			if (i!=0 && ((i+1)%10) == 1) {
				gRowPosition += diameter+5;
				gColumnPosition = startGx;
			}
			//Draws the last Circle (different diameter)
			if (i<gCircles) {
			g.fillOval(gColumnPosition, gRowPosition, diameter, diameter);
			gColumnPosition += diameter+5;
			}
			else {
				g.fillOval(gColumnPosition, gRowPosition, gDiameterLastCircle, gDiameterLastCircle);
			}
		}
		
		
		//Draw BLUE Circles
		int hColumnPosition = startHx;
		int hRowPosition = startHy;
		
		g.setColor(Color.BLUE);
		for (int i = 0; i <= hCircles; i++) {
			if (i!=0 && ((i+1)%10) == 1) {
				hRowPosition += diameter+5;
				hColumnPosition = startHx;
			}
			//Draws the last Circle (different diameter)
			if (i<hCircles) {
			g.fillOval(hColumnPosition, hRowPosition, diameter, diameter);
			hColumnPosition -= (diameter+5);
			}
			else {
				g.fillOval(hColumnPosition, hRowPosition, hDiameterLastCircle, hDiameterLastCircle);

			}
		}
			
		
		
		//Pause Time if finished or negative time because of scaling
		
		if (gStatus<=0.5 || hStatus<=0.5 || time<0) {
			t.pauseTime();
		} 
		
		//Generate the Graph
		generateGraph (g,gStatus,hStatus);
		//Generate Detailed Graph

		generateGraphDetail (g,gStatus,hStatus);

		//Generate the Label and Status
		label (g,gStatus,hStatus);
		
	}
		
		protected void label (Graphics g, double gStatus, double hStatus) {
			
			g.setColor(Color.BLACK);
			g.drawString("Erwartung durch Rechnung: ", 20, 20);
			
			//Draw legend
			g.drawString("Ein Kreis entspricht: " + szenario.unitsPerCircle() + " Einheiten", 650, 950);
			
			//Set correct color for results
			if (szenario.winner == "G") {
				g.setColor(Color.RED);
			}
			else if (szenario.winner == "H") {
				g.setColor(Color.BLUE);
			}
			
			//Draw calculated expected results
			g.drawString(szenario.result + "        Endpopulation: " + szenario.endPopulation + "        Gefechtszeit: " + szenario.fightTime + "s" + "        L = " + szenario.l(), 20, 40);
			
			//Draw current time
			g.setColor(Color.BLACK);
			g.drawString("Time: "+time + "s", 20, 70);
			
			//Draw current Populations
			g.setColor(Color.RED);
			g.drawString("Aktuelle Population G: "+Math.round(szenario.gStatus(time)*10)/10.0, 20, 90);
			g.setColor(Color.BLUE);
			g.drawString("Aktuelle Population H: "+Math.round(szenario.hStatus(time)*10)/10.0, 20, 110);
			
			//Draw starting Populations
			g.setColor(Color.RED);
			g.drawString("Startpopulation: " + Math.round(szenario.gStart) + "    s = " + szenario.s, startGx, startGy-15);
			g.setColor(Color.BLUE);
			g.drawString("Startpopulation: " + Math.round(szenario.hStart) + "    r = " + szenario.r, startHx-(9*diameter+45), startHy-15);


		}
		
		
		protected void generateGraph (Graphics g, double gStatus, double hStatus) {
			int ursprungX = 1010;
			int ursprungY = 550;
			int endX = ursprungX+440;
			int endY = ursprungY-500;
			int xLength = endX-ursprungX;
			int yLength = ursprungY-endY;
			
			/*
			 * Draw the basic structure for the graph (axis)
			 */
			g.setColor(Color.black);
			//y-axis
			g.drawLine(ursprungX, ursprungY, ursprungX, endY);
			//x-axis
			g.drawLine(ursprungX, ursprungY, endX, ursprungY);
			//Arrows at the end of the axis
			g.drawLine(endX, ursprungY, endX-5, ursprungY-3);
			g.drawLine(endX, ursprungY, endX-5, ursprungY+3);
			g.drawLine(ursprungX, endY, ursprungX-3, endY+5);
			g.drawLine(ursprungX, endY, ursprungX+3, endY+5);
			
			//Labeling the axis
			g.drawString("time(s)", endX, ursprungY-10);
			g.drawString("Population", ursprungX-30, endY-20);
			
			
			//Draw scale Lines on the axis
			//x-axis
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX+i*(xLength/20), ursprungY+3, ursprungX+i*(xLength/20), ursprungY-3);
			}
			//y-axis
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX-3, ursprungY-i*(yLength/20), ursprungX+3, ursprungY-i*(yLength/20));
			}
			
			/*
			 * Labeling of the Graph-Scale
			 */
			
			//Calculate the amount of one Step
			double scaleTimeLabel = (szenario.fightTime+2)/20;
			double scalePopulationLabel;
			//calculate the scale of one pixel
			double scaleTimeDrawing = (szenario.fightTime+2)/xLength;
			double scalePopulationDrawing;
			
			if (szenario.gStart>szenario.hStart) {
				scalePopulationLabel = szenario.gStart/20;
				scalePopulationDrawing = szenario.gStart/yLength;
			}
			else {
				scalePopulationLabel = szenario.hStart/20;
				scalePopulationDrawing = szenario.hStart/yLength;
			}
			
			//Draw the calculated labels
			//y-axis
			for (int i = 1; i<21; i++) {
				int label = (int) Math.round(scalePopulationLabel*i);
				g.drawString(""+label, ursprungX-60, ursprungY-i*(yLength/20)+5);
			}
			//x-axis
			for (int i = 1; i<21; i++) {
				
				//If estimated fightTime is smaller than 20
					double label = scaleTimeLabel*i;
					label = Math.round(label*10.0)/10.0;
					if (i%2==0) {
					g.drawString(""+label, (ursprungX+i*(xLength/20))-12, ursprungY+20);
				}
				
			}
			
			
			/*
			 * Draw the functions
			 */
			
			int [] graphGy = new int [xLength];
			int [] graphHy = new int [xLength];
			
			//calculate the values for each x-value of the function
			for (int i = 0; i<xLength; i++) {
				graphGy [i] = (int) Math.floor(szenario.gStatus(i*scaleTimeDrawing));
				graphHy [i] = (int) Math.floor(szenario.hStatus(i*scaleTimeDrawing));
				
				
				//Draw the points of each x-value
				if (i<=time/scaleTimeDrawing) {
					g.setColor(Color.RED);
					g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)-1), 3, 3);
					g.setColor(Color.BLUE);
					g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)-1), 3, 3);
					
					
					//Connect Points with Line
					if (i!=0) {
						g.setColor(Color.red);
						g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphGy[i-1]/scalePopulationDrawing)));
						g.setColor(Color.BLUE);
						g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphHy[i-1]/scalePopulationDrawing)));
					}
				}
			}
			
		}

		
		protected void generateGraphDetail (Graphics g, double gStatus, double hStatus) {
			int ursprungX = 1010;
			int ursprungY = height-50;
			int endX = ursprungX+440;
			int endY = ursprungY-350;
			int xLength = endX-ursprungX;
			int yLength = ursprungY-endY;
			
			/*
			 * Draw the basic structure for the graph (axis)
			 */
			g.setColor(Color.black);
			//y-axis
			g.drawLine(ursprungX, ursprungY, ursprungX, endY);
			//x-axis
			g.drawLine(ursprungX, ursprungY, endX, ursprungY);
			//Arrows at the end of the axis
			g.drawLine(endX, ursprungY, endX-5, ursprungY-3);
			g.drawLine(endX, ursprungY, endX-5, ursprungY+3);
			g.drawLine(ursprungX, endY, ursprungX-3, endY+5);
			g.drawLine(ursprungX, endY, ursprungX+3, endY+5);
			
			//Labeling the axis
			g.drawString("time(s)", endX, ursprungY-10);
			g.drawString("Population", ursprungX-30, endY-7);
			
			
			//Draw scale Lines on the axis
			//x-axis
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX+i*(xLength/20), ursprungY+3, ursprungX+i*(xLength/20), ursprungY-3);
			}
			//y-axis
			for (int i = 1; i<20; i++) {
				g.drawLine(ursprungX-3, ursprungY-i*(yLength/20), ursprungX+3, ursprungY-i*(yLength/20));
			}
			
			/*
			 * Labeling of the Graph-Scale
			 */
			
			//Calculate the amount of one Step
			double shownTime = 3.0;

			double scaleTimeLabel = shownTime/20.0;
			double scalePopulationLabel;
			//calculate the scale of one pixel
			double scaleTimeDrawing = shownTime/xLength;
			double scalePopulationDrawing;
			
			if (time>(2.0/3.0)*shownTime) {
				if ((szenario.gStatus(time-(2.0/3.0)*shownTime))>(szenario.hStatus(time-(2.0/3.0)*shownTime))) {
					scalePopulationLabel = (szenario.gStart+szenario.gStart*0.2)/20;
					scalePopulationDrawing = (szenario.gStart+szenario.gStart*0.2)/yLength;
					while ((szenario.gStatus(time-(2.0/3.0)*shownTime))<(scalePopulationLabel*3)) {
					scalePopulationLabel = scalePopulationLabel*(3.0/20.0);
					scalePopulationDrawing = scalePopulationDrawing*(3.0/20.0);
					}
				}
				else {
					scalePopulationLabel = (szenario.hStart+szenario.hStart*0.2)/20;
					scalePopulationDrawing = (szenario.hStart+szenario.hStart*0.2)/yLength;
					while ((szenario.hStatus(time-(2.0/3.0)*shownTime))<(scalePopulationLabel*3)) {
					scalePopulationLabel = scalePopulationLabel*(3.0/20.0);
					scalePopulationDrawing = scalePopulationDrawing*(3.0/20.0);
					}
				}
			}
			else {
				if (gStatus>hStatus) {
					scalePopulationLabel = (szenario.gStart+szenario.gStart*0.2)/20;
					scalePopulationDrawing = (szenario.gStart+szenario.gStart*0.2)/yLength;
				}
				else {
					scalePopulationLabel = (szenario.hStart+szenario.hStart*0.2)/20;
					scalePopulationDrawing = (szenario.hStart+szenario.hStart*0.2)/yLength;
				}
			}
			
			//Draw the calculated labels
			//y-axis
			if (scalePopulationDrawing*yLength>1000) {
				for (int i = 1; i<21; i++) {
					int label = (int) Math.round(scalePopulationLabel*i);
					g.drawString(""+label, ursprungX-60, ursprungY-i*(yLength/20)+5);
				}
			}
			else {
				for (int i = 1; i<21; i++) {
					double label = Math.round(scalePopulationLabel*i*100.0)/100.0;
					g.drawString(""+label, ursprungX-60, ursprungY-i*(yLength/20)+5);
				}
			}
			//x-axis
			for (int i = 1; i<21; i++) {
				double label;
				//If estimated fightTime is smaller than 20
				if (time>(2.0/3.0)*shownTime) {
					label = time-((2.0/3.0)*shownTime)+scaleTimeLabel*i;
				}
				else {
					label = scaleTimeLabel*i;
				}
				label = Math.round(label*100.0)/100.0;
					if (i%2==0) {
					g.drawString(""+label, (ursprungX+i*(xLength/20))-12, ursprungY+20);
				
					}
				
			}
			
			
			/*
			 * Draw the functions
			 */
			
			double [] graphGy = new double [xLength];
			double [] graphHy = new double [xLength];
			
			//calculate the values for each x-value of the function
			for (int i = 0; i<xLength; i++) {
				if (time<((2.0/3.0)*shownTime)) {
					graphGy [i] = szenario.gStatus(i*scaleTimeDrawing);
					graphHy [i] = szenario.hStatus(i*scaleTimeDrawing);
					if (i<=time/scaleTimeDrawing) {
						g.setColor(Color.RED);
						g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)-1), 3, 3);
						g.setColor(Color.BLUE);
						g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)-1), 3, 3);
						
						
						//Connect Points with Line
						if (i!=0) {
							g.setColor(Color.red);
							g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphGy[i-1]/scalePopulationDrawing)));
							g.setColor(Color.BLUE);
							g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphHy[i-1]/scalePopulationDrawing)));
						}
					}
				}
				else {
					graphGy [i] = szenario.gStatus(time-((2.0/3.0)*shownTime)+i*scaleTimeDrawing);
					graphHy [i] = szenario.hStatus(time-((2.0/3.0)*shownTime)+i*scaleTimeDrawing);
					if (i<=((2.0/3.0)*shownTime)/scaleTimeDrawing) {
						g.setColor(Color.RED);
						g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)-1), 3, 3);
						g.setColor(Color.BLUE);
						g.fillOval(ursprungX+i-1,(int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)-1), 3, 3);
						
						
						//Connect Points with Line
						if (i!=0) {
							g.setColor(Color.red);
							g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphGy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphGy[i-1]/scalePopulationDrawing)));
							g.setColor(Color.BLUE);
							g.drawLine(ursprungX+i, (int) (ursprungY-Math.round(graphHy[i]/scalePopulationDrawing)), ursprungX+i-1, (int) (ursprungY-Math.round(graphHy[i-1]/scalePopulationDrawing)));
						}
					}
				}
			}
			
		} 

} 
		
		
	


	
