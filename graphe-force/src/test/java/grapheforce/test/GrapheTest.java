package grapheforce.test;

import static org.junit.Assert.*;

import java.util.Random;

import grapheforce.Graphe;

import org.junit.Before;
import org.junit.Test;

public class GrapheTest {

	private Graphe grapheGenere;
	
	@Before
	public void initialiser(){		
		grapheGenere = new Graphe(new Random().nextInt(11)+2, 0.02);
	}
	
	@Test public void doit_verifier_que_le_graphe_est_sans_boucle() {
		for (int i=0;i<grapheGenere.getNbSommets();i++)
		{
			if (grapheGenere.getMAdjacence()[i][i]!=0){
				fail("Présence d'une boucle");
			}
		}
	}

	@Test public void doit_verifier_que_le_graphe_est_non_oriente(){
		for (int i=0;i<grapheGenere.getNbSommets();i++)
		{
			for (int j=0;j<grapheGenere.getNbSommets();j++)
			{
				if (grapheGenere.getMAdjacence()[i][j]!=0 && grapheGenere.getMAdjacence()[j][i]==0){
					fail("Présence d'une arête orientée");
				}
			}
		}
	}
}
