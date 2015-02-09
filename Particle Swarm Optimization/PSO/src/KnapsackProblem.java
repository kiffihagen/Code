import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import Models.Swarm;
import UI.GUI;


public class KnapsackProblem {
	Swarm swarm;
	double targetValue;
	int maxRuns;

	public KnapsackProblem(double targetValue, int amount, int runs, boolean volume, double LW, double GW){
	maxRuns = runs;
	this.targetValue = targetValue;
	swarm = new Swarm(amount,volume,runs,LW,GW);
    runSwarm();
	}
	
	private void runSwarm(){
    	//System.out.println("test");
    	int counter = 0;
    	
    	while(counter < maxRuns && swarm.getBestValue()<this.targetValue){
    		swarm.updateParticles(counter);
    		
    		counter++;
    		//System.out.println(counter);
    	}
    	swarm.getRealBestValue();
    	//System.out.println("Best value found: " +swarm.getRealBestValue());
    	//System.out.println("Best location found:");
    	//swarm.getBestPos().printDiscreteString();
    	//System.exit(0);
    }
	
	public static void main(String[] args){
		new KnapsackProblem(10000, 200, 500, false,2.0,1.0);
	}
	
	

	

}
