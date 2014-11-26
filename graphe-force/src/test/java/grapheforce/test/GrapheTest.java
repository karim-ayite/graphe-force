package grapheforce.test;

import static org.junit.Assert.*;

import java.util.Random;

import grapheforce.Graphe;

import org.junit.Test;

public class GrapheTest {

	@Test
	public void doit_verifier_que_le_graphe_ne_contient_pas_de_boucle() {
		Graphe graphe = new Graphe(new Random().nextInt(11)+2, 0.02);
		for (int i=0;i<graphe.getNbSommets();i++)
		{
			if (graphe.getMAdjacence()[i][i]!=0){
				fail("Présence d'une boucle");
			}
		}
	}

}
