package Models;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;


public class Particle {
	private Position pos;
	private Velocity vel;
	
	private Image image;
	private double myBestFitness;
	private Position myBestPos;
	private Random r = new Random();
	private double boardsize;
	private boolean isCircle;
	
	//circle
	public Particle(int dims, double maxValues){
		isCircle = true;
		boardsize=maxValues;
		pos = new Position(dims);
		pos.initialize(-1.0, 1.0);
		myBestPos = pos.copy();
		
		vel = new Velocity(dims);
		vel.initialize(-0.1, 0.1);
		
		ImageIcon ii = new ImageIcon("./Resources/boid2.jpg");
		//ImageIcon ii = new ImageIcon("./Resources/fredrik.png");
        image = ii.getImage();
	}
	
	//knapsack
	public Particle(int dims){
		isCircle = false;
		boardsize=1.0;
		pos = new Position(dims);
		pos.initialize();
		myBestPos = pos.copy();
		
		vel = new Velocity(dims);
		vel.initialize(-.1,.1);
		
	}
	
	public Position getPosition(){
		return pos;
	}
	
	public Velocity getVelocity(){
		return vel;
	}
	
	public void setVelocity(double[] v){
		this.vel.setvelocity(v,0.05);
	}
	
	 public Image getImage() {
	        return image;
	 }

	public double getX() {
		//System.out.println(pos.getAPosition(0));
		return pos.getAPosition(0);
	}
	 
	public double getY() {
		if(pos.getSize()<2){
			return Math.pow(pos.getAPosition(0), 2);
		}
		return pos.getAPosition(1);
	}
	
	//
	
	public void setMyBestFitness(double f){
		myBestFitness = f;
	}
	public void setMyBestLoc(){
		myBestPos = pos.copy();
	}
	
	public double getMyBestFitness(){
		return myBestFitness;
	}
	public Position getMyBestLoc(){
		return pos;
	}
	
	//finds new velocity and sets new position.
	public void update(Position bestGlobal, double INERTIA, double LOCALWEIGHT, double GLOBALWEIGHT){
		double[] vel = getVelocity().getvelocity();
		double[] out = new double[vel.length];
		for (int i = 0; i < vel.length; i++) {
			out[i] = 	INERTIA*vel[i]	+
					
						LOCALWEIGHT*r.nextDouble()* ( 
								myBestPos.getAPosition(i)-pos.getAPosition(i)
								)	+
						
						GLOBALWEIGHT*r.nextDouble()*( 
								bestGlobal.getAPosition(i)-pos.getAPosition(i)
								);
		}
		setVelocity(out);
		if(isCircle)
			pos.setNewPosition(this.vel);
		else
			pos.setNewKSPosition(this.vel);
	}
	
	//used for finding neighbours
	public double distance(Particle p){
		double dx = p.getX()-this.getX();
		double dy = p.getY()-this.getY();
		double val = Math.sqrt(	Math.pow(dx,2) + Math.pow(dy,2) );
		return val;
	}
	

}
