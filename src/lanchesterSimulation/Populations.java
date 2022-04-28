package lanchesterSimulation;


public class Populations {

	public double gStart;
	public double hStart;
	public double s;
	public double r;
	public double k;
	public String winner = "";
	public String result = "";
	public int endPopulation;
	public double fightTime;
	
	
	public Populations(double gStart, double hStart, double s, double r) {
		this.gStart = gStart;
		this.hStart = hStart;
		this.s = s;
		this.r = r;
		calcK();
		prognosis();
	}
	
	public void calcK () {
		this.k = Math.sqrt(r*s);
	}
	
	
	
	public double gStatus (double t) {
		double value = (gStart * Math.cosh(k * t) - (r/k) * hStart * Math.sinh(k * t));
		if (value<0) {
			return 0;
		}
		else {
			return value;
		}
	}
	
	public double hStatus (double t) {
		double value = (hStart * Math.cosh(k * t) - (s/k) * gStart * Math.sinh(k * t));
		if (value<0) {
			return 0;
		}
		else {
			return value;
		}
	}
	
	public double l () {
		return (s * Math.pow(gStart, 2) - r * Math.pow(hStart, 2));
	}

	
	public void prognosis () {
		double l = l();
		calcK();
		if (l>0) {
			result = "G gewinnt (rot)";
			winner = "G";
			fightTime = ((1/k) * arctanh((hStart/gStart) * (k/s)));
			fightTime = fightTime * 1000;
			fightTime = Math.round(fightTime);
			fightTime = fightTime/1000;
		}
		else if (l<0) {
			result = "H gewinnt (blau)";
			winner = "H";
			fightTime = ((1/k) * arctanh((gStart/hStart) * (k/r)));
			fightTime = fightTime * 1000;
			fightTime = Math.round(fightTime);
			fightTime = fightTime/1000;
		}
		else if (l==0) {
			if (gStart==hStart && r==s) {
				result = "tragisches Unentschieden";
				winner = "X";
				fightTime = estimateEndOfFight(1, 0);
				fightTime = fightTime * 1000;
				fightTime = Math.round(fightTime);
				fightTime = fightTime/1000;
			}
			else if (gStart>hStart) {
				result = "Pyrrhussieg für G";
				winner = "G";
				fightTime = estimateEndOfFight(1, 0);
				fightTime = fightTime * 1000;
				fightTime = Math.round(fightTime);
				fightTime = fightTime/1000;
			}
			else if (gStart<hStart) {
				result = "Pyrrhussieg für H";
				winner = "H";
				fightTime = estimateEndOfFight(1, 0);
				fightTime = fightTime * 1000;
				fightTime = Math.round(fightTime);
				fightTime = fightTime/1000;
			}
			
		}
		if (winner == "G") {
			endPopulation = (int) Math.floor(Math.sqrt(l()/s));
		}
		else if (winner == "H") {
			endPopulation = (int) Math.floor(Math.sqrt((-l())/r));
		}
		else {
			endPopulation = 0;
		}
	}
	
	
	//estimates the FightTime until the manpower is smaller than 0.5
	public double estimateEndOfFight(double i, double t) {
		double p = gStart;
		while (p>0.5) {
			t+=i;
			if (winner == "G") {
				p=gStatus(t);
			}
			else {
				p=hStatus(t);
			}
		}
		if(i<1) {
			return t-i;
		}
		else {
			t=t-i;
			i= i/10;
			return estimateEndOfFight(i,t);
		}
		
	}
	
	public int unitsPerCircle () {
		if (gStart>hStart) {
			if (gStart>200) {
			return ((int) Math.round(gStart / 200));
			}
			else {
				return 1;
			}
		}
		else {
			if (hStart>200) {
			return ((int) Math.round(hStart / 200));
			}
			else {
				return 1;
			}
		}
	}
	
	private double arctanh (double x) {
		 return (0.5*Math.log((1+x)/(1-x)));
	}
	
}
