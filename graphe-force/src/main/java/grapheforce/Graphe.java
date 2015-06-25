package grapheforce;

import java.util.ArrayList;
import java.util.Random;

public class Graphe implements Runnable {
	protected int encours = -1;
	int nbSommets;
	int[][] matriceAdjacence;
	ArrayList<Sommet> sommets;
	ArrayList<int[]> aretes;
	int[] netForce;
	double timestep;

	public Graphe(int n, double t) {
		int[] arete;
		netForce = new int[2];
		timestep = t;

		Random haz = new Random();
		nbSommets = n;
		matriceAdjacence = new int[nbSommets][nbSommets];
		sommets = new ArrayList<Sommet>();
		aretes = new ArrayList<int[]>();
		for (int i = 0; i < nbSommets; i++) {
			for (int j = 0; j < nbSommets; j++) {
				if (i != j) {
					if (haz.nextInt(10000) > 7500) {
						matriceAdjacence[i][j] = 1;
						matriceAdjacence[j][i] = 1;
						arete = new int[2];
						arete[0] = i;
						arete[1] = j;
						aretes.add(arete);
					}
				} else {
					matriceAdjacence[i][j] = 0;
					matriceAdjacence[j][i] = 0;
				}
			}
		}

		for (int i = 0; i < nbSommets; i++) {
			sommets.add(new Sommet(haz.nextInt(500), haz.nextInt(400)));
			//	System.out.println("Sommet "+i+" initialement Ã  : "+sommets.get(i).x+","+sommets.get(i).y);;
		}

	}

	public int getNbSommets() {
		return nbSommets;
	}

	public void setArete(int i, int j, int r) {
		matriceAdjacence[i][j] = r;
		matriceAdjacence[j][i] = r;
	}

	public void setNbSommets(int nbSommets) {
		this.nbSommets = nbSommets;
	}

	public int[][] getMAdjacence() {
		return matriceAdjacence;
	}

	public void setMAdjacence(int[][] adjacence) {
		matriceAdjacence = adjacence;
	}

	public ArrayList<Sommet> getSommets() {
		return sommets;
	}

	public void setSommets(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}

	public ArrayList<int[]> getAretes() {
		return aretes;
	}

	public void setAretes(ArrayList<int[]> aretes) {
		this.aretes = aretes;
	}

	public double[] calculerAttractionDeHooke(int s, int[] a) {
		double[] nf = new double[2];
		double longueurRessort = 0;
		double vx = 0;
		double vy = 0;

		if (a[0] == s) {
			longueurRessort = Math.sqrt(Math.pow((sommets.get(a[1]).x - sommets.get(a[0]).x), 2)
					+ Math.pow((sommets.get(a[1]).y - sommets.get(a[0]).y), 2));
			vx = sommets.get(a[1]).x - sommets.get(a[0]).x;
			vy = sommets.get(a[1]).y - sommets.get(a[0]).y;
		} else {
			longueurRessort = Math.sqrt(Math.pow((sommets.get(a[0]).x - sommets.get(a[1]).x), 2)
					+ Math.pow((sommets.get(a[0]).y - sommets.get(a[1]).y), 2));

			vx = sommets.get(a[0]).x - sommets.get(a[1]).x;
			vy = sommets.get(a[0]).y - sommets.get(a[1]).y;
		}
		//System.out.println("Longueur ressort :"+lgRessort);
		nf[0] = -ConstantesPhysique.CONSTANTE_RESSORT * (longueurRessort - ConstantesPhysique.LONGUEUR_RESSORT_REPOS);
		//System.out.println("Force :"+nf[0]);
		nf[0] = nf[0] * -vx;
		//nf[0] = - cstRessort * vx;
		nf[1] = -ConstantesPhysique.CONSTANTE_RESSORT * (longueurRessort - ConstantesPhysique.LONGUEUR_RESSORT_REPOS);
		nf[1] = nf[1] * -vy;
		//nf[0] = - cstRessort * vx;
		return nf;
	}

	public double[] calculerRepulsionDecoulomb(Sommet s1, Sommet s2) {
		double[] nf = new double[2];
		double vx = 0;
		double vy = 0;
		double dist = Math.sqrt(Math.pow((s2.x - s1.x), 2) + Math.pow((s2.y - s1.y), 2));
		vx = s2.x - s1.x;
		vy = s2.y - s1.y;
		nf[0] = ConstantesPhysique.CONSTANTE_ENV * ((s1.charge * s2.charge) / Math.pow(dist, 2));
		nf[1] = ConstantesPhysique.CONSTANTE_ENV * ((s1.charge * s2.charge) / Math.pow(dist, 2));
		if (s1.x <= 800 && s1.y <= 600 && s2.x <= 800 && s2.y <= 600) {
			nf[0] = nf[0] * -vx;
			nf[1] = nf[1] * -vy;
		}
		return nf;
	}

	public void forceAlgo() {
		double[] netForce = new double[2];
		for (int i = 0; i < this.getNbSommets(); i++) {
			if (i != encours) {
				sommets.get(i).vitesseX = 0;
				sommets.get(i).vitesseY = 0;
			}
		}

		for (int i = 0; i < this.getNbSommets(); i++) {
			if (i != encours) {
				netForce[0] = 0;
				netForce[1] = 0;

				Sommet s = sommets.get(i);

				for (int j = 0; j < this.getNbSommets(); j++) {
					Sommet t = sommets.get(j);

					if (i != j) {
						double[] rep = calculerRepulsionDecoulomb(s, t);
						netForce[0] = netForce[0] + rep[0];
						netForce[1] = netForce[1] + rep[1];
					}
				}

				for (int k = 0; k < this.getAretes().size(); k++) {
					int[] a = aretes.get(k);
					if (a[0] == i || a[1] == i) {
						double[] att = calculerAttractionDeHooke(i, a);
						netForce[0] = netForce[0] + att[0];
						netForce[1] = netForce[1] + att[1];
					}
				}

				s.vitesseX = (s.vitesseX + timestep * netForce[0]) * ConstantesPhysique.COEFF_AMORTISSEMENT;
				s.vitesseY = (s.vitesseY + timestep * netForce[1]) * ConstantesPhysique.COEFF_AMORTISSEMENT;

				s.x = s.x + timestep * s.vitesseX;
				s.y = s.y + timestep * s.vitesseY;

			}
		}
	}

	public void run() {
		this.forceAlgo();
	}

}//fin classe

