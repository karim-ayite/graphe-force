package grapheforce;

import java.util.Random;

public class Sommet {
	//positions
	double x;
	double y;
	//velocité
	double vitesseX;
	double vitesseY;

	double m;
	double charge;

	public Sommet() {

		x = 0;
		y = 0;
		vitesseX = 0;
		vitesseY = 0;
		m = new Random().nextInt(60) + 1;
		charge = new Random().nextInt(60) + 1;
	}

	public Sommet(int i, int j) {
		x = i;
		y = j;
		vitesseX = 0;
		vitesseY = 0;
		m = new Random().nextInt(60) + 1;
		charge = 5;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public Sommet(int i, int j, int a, int b, int c) {
		x = i;
		y = j;
		vitesseX = a;
		vitesseY = b;
		m = c;
	}

	public int getX() {
		return (int) x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getVitesseX() {
		return (int) vitesseX;
	}

	public void setVitesseX(int dx) {
		this.vitesseX = dx;
	}

	public int getVitesseY() {
		return (int) vitesseY;
	}

	public void setVitesseY(int dy) {
		this.vitesseY = dy;
	}

}
