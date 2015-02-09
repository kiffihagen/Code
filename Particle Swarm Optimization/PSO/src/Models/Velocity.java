package Models;
import java.util.Random;


public class Velocity {
	private double[] velocity;
	Random r = new Random();
	
	public Velocity(int size){
		velocity = new double[size];
	}
	
	public void initialize(double min, double max){
		for (int i = 0; i < velocity.length; i++) {			
			velocity[i] = min + (max - min) * r.nextDouble();
		}
	}
	
	public double[] getvelocity(){
		return velocity;
	}
	
	//limits abosolute velocity
	public void setvelocity(double[] velocityIn, double maxVel){
		for (int i = 0; i < velocity.length; i++) {
			double v = velocityIn[i];
			if(v>maxVel){
				this.velocity[i] = maxVel;
			} else if (v<-maxVel){
				this.velocity[i] = -maxVel;
			} else {
				this.velocity[i] = v;
			}
			
		}
	}
	
	public double getAvelocity(int pos){
		return velocity[pos];
	}
	
	public void setAvelocity(double value, int pos){
		this.velocity[pos] = value;
	}

}

