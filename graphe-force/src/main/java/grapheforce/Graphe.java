package grapheforce;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Graphe implements Runnable {
	protected int encours=-1;
	int nbSommets;
	int[][] mAdjacence;
	ArrayList<Sommet> sommets;
	ArrayList<int[]> aretes;
	int[] netForce;
	double cstRessort;
	double lgRessortRepos;
	double damping;
	double cstEnvironnement;
	double timestep;
	
	public Graphe(int n,double t)
	{
		int[] arete;
		netForce = new int[2];
		cstRessort = 3;
		lgRessortRepos = 50;
		damping = 0.2 ; //0.4;
		cstEnvironnement = 100000;// 9*Math.pow(10, 9);
		timestep = t;
		
		Random haz=new Random();
		nbSommets=n;
		mAdjacence = new int[nbSommets][nbSommets];
		sommets = new ArrayList<Sommet>();
		aretes = new ArrayList<int[]>();
		for (int i=0;i<nbSommets;i++)
		{
			for (int j=0;j<nbSommets;j++)
			{
				if (i!=j)
				{
					if (haz.nextInt(10000)>7500)
					{
						mAdjacence[i][j]=1;
						mAdjacence[j][i]=1;
						arete=new int[2];
						arete[0]=i;
						arete[1]=j;
						aretes.add(arete);
					}
				}
				else
				{
					mAdjacence[i][j]=0;
					mAdjacence[j][i]=0;
				}
			}
		}
		
		
			for (int i=0;i<nbSommets;i++)
			{
				sommets.add(new Sommet(haz.nextInt(500),haz.nextInt(400)));
			//	System.out.println("Sommet "+i+" initialement Ã  : "+sommets.get(i).x+","+sommets.get(i).y);;
			}
			
			
		}

	public int getNbSommets() {
		return nbSommets;
	}
	
	public void setArete(int i,int j,int r){
		mAdjacence[i][j]=r;
		mAdjacence[j][i]=r;
	}

	public void setNbSommets(int nbSommets) {
		this.nbSommets = nbSommets;
	}

	public int[][] getMAdjacence() {
		return mAdjacence;
	}

	public void setMAdjacence(int[][] adjacence) {
		mAdjacence = adjacence;
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

	public double[] hookeAttraction(int s,int[] a)
	{
		double[] nf=new double[2];
		double lgRessort = 0;
		double vx = 0;
		double vy = 0;
		
		if (a[0] == s)
		{
			lgRessort = Math.sqrt(Math.pow((sommets.get(a[1]).x - sommets.get(a[0]).x),2)+Math.pow((sommets.get(a[1]).y - sommets.get(a[0]).y),2));
			vx = sommets.get(a[1]).x - sommets.get(a[0]).x;
			vy = sommets.get(a[1]).y - sommets.get(a[0]).y;
		}
		else
		{
			lgRessort = Math.sqrt(Math.pow((sommets.get(a[0]).x - sommets.get(a[1]).x),2)+Math.pow((sommets.get(a[0]).y - sommets.get(a[1]).y),2));
			
			vx = sommets.get(a[0]).x - sommets.get(a[1]).x;
			vy = sommets.get(a[0]).y - sommets.get(a[1]).y;
		}
		//System.out.println("Longueur ressort :"+lgRessort);
		nf[0] = - cstRessort * (lgRessort -lgRessortRepos);
		//System.out.println("Force :"+nf[0]);
		nf[0] = nf[0] *  - vx;
		//nf[0] = - cstRessort * vx;
		nf[1] =  - cstRessort * (lgRessort -lgRessortRepos);
		nf[1] = nf[1] *  - vy;
		//nf[0] = - cstRessort * vx;
		return nf;
	}
	
	public double[] coulombRepulsion(Sommet s1,Sommet s2)
	{
		double[] nf=new double[2];
		double vx = 0;
		double vy = 0;
		double step = 10;
		double dist = Math.sqrt(Math.pow((s2.x - s1.x),2)+Math.pow((s2.y - s1.y),2));
		vx = s2.x - s1.x;
		vy = s2.y - s1.y;
		nf[0] = cstEnvironnement * ((s1.charge * s2.charge)/Math.pow(dist,2));
		nf[1] = cstEnvironnement * ((s1.charge * s2.charge)/Math.pow(dist,2));
		if (s1.x <= 800 && s1.y <= 600 && s2.x <= 800 && s2.y <= 600){
		nf[0] = nf[0] *  -vx;
		nf[1] = nf[1] *  -vy;
		}
		return nf;
	}
	
	public void forceAlgo()
	{
		double[] netForce = new double[2];
		double totalKineticEnergy = 100;
		for (int i=0;i<this.getNbSommets();i++)
		{
			if (i != encours){
			sommets.get(i).dx=0;
			sommets.get(i).dy=0;
			}
		}
		//while (totalKineticEnergy > 3)
		//{//
			totalKineticEnergy = 0;
			
			
			
			for (int i=0;i<this.getNbSommets();i++)
			{
				if (i != encours){
				netForce[0]=0;
				netForce[1]=0;
				
				Sommet s=sommets.get(i);
				
				for (int j = 0;j<this.getNbSommets();j++)
				{
					//if (s.getX()<800 && s.getY()<600 && s.getX()>0 && s.getY()>0)
					//{//
					Sommet t = sommets.get(j);
					
					if (i!=j)
					{
						double[] rep = coulombRepulsion(s,t);
						netForce[0] = netForce[0] + rep[0];
						netForce[1] = netForce[1] + rep[1];
					}
				}
				//}//
				
				for (int k = 0;k<this.getAretes().size();k++)
				{
					int[] a = aretes.get(k);
					if (a[0]==i || a[1]==i)
					{
						double[] att=hookeAttraction(i,a);
						netForce[0] = netForce[0] + att[0];
						netForce[1] = netForce[1] + att[1];
					}	
				}
				
				s.dx = ( s.dx + timestep * netForce[0]) * damping;
				s.dy = ( s.dy + timestep * netForce[1]) * damping;
				
				s.x = s.x + timestep * s.dx;
				s.y = s.y + timestep * s.dy;
				
			}
		}
	}

	public void run() {
		this.forceAlgo();
	}

}//fin classe


