package grapheforce;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;


public class Affichage extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	
	Graphe gForce;
	Timer timer;
	Graphics2D g2d;
	int d=1;
	Thread tCalcul;
	private boolean deplacement;
	Point pressedPoint = null;
	Point releasePoint = null;
	
	public Affichage()
	{
		addMouseListener(this);
		addMouseMotionListener(this);
		gForce=new Graphe(new Random().nextInt(11)+2,0.02);
		timer = new Timer(20,this);
		this.setSize(800,600);
	}


	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
		
		g2d = (Graphics2D)g ;
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		//g2d.translate(getWidth() / 2, getHeight() / 2);
		//g2d.drawString("(0,0)",0,0);
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
		for(int i=0;i<gForce.getNbSommets();i++)
		{
			Sommet s = gForce.sommets.get(i);
			
			g2d.setColor(Color.RED);	
			g2d.drawString(Integer.toString(i),s.getX(),s.getY()+25);
			g2d.setColor(Color.black);
			g2d.fillOval(s.getX()-1,s.getY()-1,12,12);
			g2d.setColor(Color.RED);
			g2d.fillOval(s.getX(),s.getY(),10,10);
			

	//		System.out.println("Sommet "+i+" : "+s.x+","+s.y);
		}
		
		for(int[] a:gForce.getAretes())
		{
			g2d.setColor(new Color(45,30,40));
			g2d.drawLine(gForce.getSommets().get(a[0]).getX()+5, gForce.getSommets().get(a[0]).getY()+5, gForce.getSommets().get(a[1]).getX()+5, gForce.getSommets().get(a[1]).getY()+5);
		}
		tCalcul = new Thread(gForce);
		tCalcul.start();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==timer)
		{
			repaint();
	//		d=d+1;
		}
	}


	public void mouseClicked(MouseEvent e) {
		if ( SwingUtilities.isRightMouseButton(e)  ) {
			System.out.println("click de souris");
			//on recupere les coordonnées de la souris
			int mouseX = e.getX();
			int mouseY = e.getY();
			//on cree un nouveau sommet
			Sommet n = new Sommet(mouseX,mouseY);
			//g2d.translate(getWidth()/2,getHeight()/2);
			
			gForce.setNbSommets(gForce.nbSommets + 1);
			
			ArrayList<Sommet> sommetsTMP = new ArrayList<Sommet>();
			for(int l=0; l<gForce.sommets.size(); l++) {
					sommetsTMP.add(gForce.sommets.get(l));
			}
			sommetsTMP.add(n);
			gForce.setSommets(sommetsTMP);
			
			int matriceTMP[][]= new int [gForce.nbSommets][gForce.nbSommets];
			for (int j=0; j<gForce.nbSommets-1; j++) {
				for(int k=0; k<gForce.nbSommets-1; k++) {
					matriceTMP[j][k] = gForce.matriceAdjacence[j][k];
				}
			}
			gForce.setMAdjacence(matriceTMP);
    } else if (SwingUtilities.isLeftMouseButton(e)) {
    	gForce= new Graphe(new Random().nextInt(11)+2,0.02);
		this.getGraphics().dispose();
    }	
	}


	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent e) {
		pressedPoint = e.getPoint();
		System.out.println("souris presser");
	}


	public void mouseReleased(MouseEvent e) {
		releasePoint = e.getPoint();
		gForce.encours = -1;
		double distance3 = -1;
		int j=-1,k=-1;
		//Vector<Integer> sommetCandidats = new Vector<Integer>();
		
		System.out.println("souris relacher");
		if (pressedPoint != null && releasePoint != null){
		for(int i=0; i<gForce.nbSommets; i++){
			double distance1 = Math.sqrt (Math.pow((gForce.sommets.get(i).x - pressedPoint.x),2)+ Math.pow((gForce.sommets.get(i).y - pressedPoint.y),2));
			double distance2 = Math.sqrt (Math.pow((gForce.sommets.get(i).x - releasePoint.x),2)+ Math.pow((gForce.sommets.get(i).y - releasePoint.y),2));
			distance3 = Math.sqrt (Math.pow((pressedPoint.x - releasePoint.x),2)+ Math.pow((pressedPoint.y - releasePoint.y),2));
			
			if (distance1 <= 60 && j == -1){
			//	sommetCandidats.add(i);
				j = i;
				System.out.println(" j = "+j);
			}
			
			if (distance2 <= 60 && k == -1){
				//sommetCandidats.add(i);
				k = i;
				System.out.println(" k = "+k);
			}
		}
		
		if (j != -1 && k != -1 ){
			System.out.print("ok1");
			if (gForce.getMAdjacence()[j][k] == 0){
				System.out.print("ok");
				int[] a = new int[2];
				a[0]=j;
				a[1]=k;
				gForce.aretes.add(a);
				pressedPoint = null;
				releasePoint = null;
				k = -1;
				j = -1;
			}
		}
		}
	}


	public void mouseDragged(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if (gForce.encours == -1){
		for(int i=0; i<gForce.nbSommets; i++){
			double distance = Math.sqrt (Math.pow((gForce.sommets.get(i).x - mouseX),2)+ Math.pow((gForce.sommets.get(i).y - mouseY),2));
			if (distance <= 20){
				//System.out.println("mode deplacement");
				gForce.sommets.get(i).setX(mouseX);
				gForce.sommets.get(i).setY(mouseY);
				gForce.encours = i;
				break;
			}
		}
		}
		else
		{
			gForce.sommets.get(gForce.encours).setX(mouseX);
			gForce.sommets.get(gForce.encours).setY(mouseY);
		}
	}
	

	public void mouseMoved(MouseEvent e) {
//		affichage des coordonéesde la souris en console
		//System.out.println("X: "+e.getX()+" Y: "+e.getY());
	}
	
}
