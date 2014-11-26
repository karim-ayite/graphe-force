package grapheforce;
import java.util.Random;

public class Sommet {
	//positions
	double x;
	double y;
	//velocité
	double dx;
	double dy;
	
	double m;
	double charge;
	
	public Sommet()
	{

		x=0;
		y=0;
		dx=0;
		dy=0;
		m=new Random().nextInt(60)+1;
		charge=new Random().nextInt(60)+1;
	}
	
	public Sommet(int i,int j)
	{
		x=i;
		y=j;
		dx=0;
		dy=0;
		m=new Random().nextInt(60)+1;
		charge=5;
	}
	
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public Sommet(int i,int j,int a,int b,int c)
	{
		x=i;
		y=j;
		dx=a;
		dy=b;
		m=c;
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
	public int getDx() {
		return (int) dx;
	}
	public void setDx(int dx) {
		this.dx = dx;
	}
	public int getDy() {
		return (int) dy;
	}
	public void setDy(int dy) {
		this.dy = dy;
	}
	public int getM() {
		return (int) m;
	}
	public void setM(int m) {
		this.m = m;
	}
	
	
}
