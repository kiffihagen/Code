package Models;
import java.util.Random;


public class Position {
	private double[] position;
	Random r = new Random();
	
	public Position(int size){
		position = new double[size];
	}
	
	public Position(Position p){
		this.position = p.getPosition().clone();
	}
	
	//for circle
	public void initialize(double min, double max){
		
		for (int i = 0; i < position.length; i++) {			
			position[i] = min + (max - min) * r.nextDouble();
		}
	}
	
	//for knapsack, 1/50 over 0.5, rest from 0-0.5  
	public void initialize(){
		for (int i = 0; i < position.length; i++) {
			double temp = -49 + 50.0*r.nextDouble();
			position[i] = temp<0 ? .49*r.nextDouble() : temp;
			
			//position[i] = r.nextDouble();
			
			//position[i] = 0.0 + (0.55) * r.nextDouble();
			//position[i] = Math.abs(r.nextGaussian());
		}
	}
	
	public int getSize(){
		return position.length;
	}
	
	public double[] getPosition(){
		return position;
	}
	
	public void setPosition(double[] position){
		this.position = position;
	}
	
	public double getAPosition(int pos){
		return position[pos];
	}
	
	public void setAPosition(double value, int pos){
		this.position[pos] = value;
	}
	
	//sets new pos, not outside board, circle
	public void setNewPosition(Velocity v){
		for (int i = 0; i < position.length; i++) {
			position[i] = position[i] + v.getAvelocity(i);
			if(position[i]>1)
				position[i]=1;
			else if(position[i]<-1)
				position[i]=-1;
		}
	}
	
	//new pos knapsack 0 to 1.
	public void setNewKSPosition(Velocity v){
		for (int i = 0; i < position.length; i++) {
			position[i] = position[i] + v.getAvelocity(i);
			if(position[i]>1)
				position[i]=1;
			else if(position[i]<0)
				position[i]=0;
		}
	}
	public void printString(){
		//System.out.println("Position:");
		for (int i = 0; i < position.length; i++) {
			System.out.println(i+" = "+position[i]);
		}
	}
	
	public void printDiscreteString(){
		for (int i = 0; i < position.length; i++) {
			System.out.println(i+" = "+( position[i]<0.5 ? 0:1));

				
				
		}
	}
	
	public Position copy(){
		return new Position(this);
	}
	


}
