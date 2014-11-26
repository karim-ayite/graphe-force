package grapheforce;
import java.awt.*;

import javax.swing.*;


public class FenetrePrincipale extends JFrame {

	Affichage a;
	
	public FenetrePrincipale()
	{
		a=new Affichage();
		this.setSize(800,600);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		this.getContentPane().add(a);
		this.setTitle("Algorithme de force");
		
	}
	
	public static void main(String[] args) {
		System.out.println("execution");
		FenetrePrincipale f=new FenetrePrincipale();
		f.setVisible(true);
		f.a.timer.start();
	}
}
