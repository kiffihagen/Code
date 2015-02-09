import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

import Models.Swarm;
import UI.GUI;

public class CircleProblem extends JFrame{
	Swarm swarm;
	double targetValue;
	int maxRuns;
	
    public CircleProblem(double targetValue, int amount, double range, int dims, int runs, int neighbours, boolean inertia, double LW, double GW) {
    	maxRuns = runs;
    	this.targetValue = targetValue;
    	swarm = new Swarm(amount, range, dims, runs, neighbours, inertia, LW, GW);

        add(new GUI(swarm));
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setTitle("PSO");
        setResizable(true);
        setVisible(true);
        
        runSwarm();
        
    }


    public static void main(String[] args) {
        new CircleProblem(0.001 , 50 , 1.0 , 2, 1000, 3, false, 2.0, 1.0);
    }
    
    private void runSwarm(){
    	int counter = 0;
    	
    	while(counter < maxRuns && swarm.getBestValue()>this.targetValue){
    		swarm.updateParticles(counter);
    		
    		counter++;
    		//Animation sleep
    		sleep(50);
    	}
    	
    	System.out.println("Best value found: " +swarm.getBestValue());
    	System.out.println("Best location found:");
    	swarm.getBestPos().printString();
    	//System.exit(0);
    }
    
	private static void sleep(long time){

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
}



