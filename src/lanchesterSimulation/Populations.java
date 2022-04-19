package lanchesterSimulation;

public class Populations {

	public double gStart;
	public double hStart;
	public double s;
	public double r;
	public String winner = "";
	
	
	public Populations(double gStart, double hStart, double s, double r) {
		this.gStart = gStart;
		this.hStart = hStart;
		this.s = s;
		this.r = r;
	}

	public double k () {
		return Math.sqrt(r*s);
	}
	
	public double gStatus (double t) {
		double k = k();
		double value = (gStart * Math.cosh(k * t) - (r/k) * hStart * Math.sinh(k * t));
		if (value<0) {
			return 0;
		}
		else {
			return value;
		}
	}
	
	public double hStatus (double t) {
		double k = k();
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
	
	public String prognosis () {
		double l = l();
		String result = "";
		if (l>0) {
			result = "G gewinnt (rot)";
			winner = "G";
		}
		else if (l<0) {
			result = "H gewinnt (blau)";
			winner = "H";
		}
		else if (l==0) {
			if (gStart==hStart && r==s) {
				result = "tragisches Unentschieden";
				winner = "X";
			}
			else if (gStart>hStart) {
				result = "Pyrrhussieg für G";
				winner = "G";
			}
			else if (gStart<hStart) {
				result = "Pyrrhussieg für H";
				winner = "H";
			}
			
		}
		return result;
	}
	
	public int unitsPerCircle () {
		if (gStart>hStart) {
			return ((int) Math.round(gStart / 200));
		}
		else {
			return ((int) Math.round(hStart / 200));
		}
	}
}
