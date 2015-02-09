package LocalSearch;

import java.util.ArrayList;
import java.util.Random;

import States.AbstractState;

public class SimulatedAnnealing extends ConstraintBasedLocalSearch{
	
	private double MaxTemprature;
	private double DeltaTemperature;
	private double Temperature;
	private int stepsToSolve = 0;
	private double targetScore;
	private double currentScore;
	private int numberNeighbours;
	private int maxRuns;
	private boolean linear;
	public static final String className = SimulatedAnnealing.class.getName();
	
	@Override
	public String getName(){
		return className;
	}
	//example SimulatedAnnealing(20,100,2,0);
	
	public SimulatedAnnealing(int numberNeighbours,double MaxTemprature, double DeltaTemprature, double targetScore,boolean debug, int maxRuns, boolean linear){
		super(debug);
		this.DeltaTemperature = DeltaTemprature;
		this.numberNeighbours = numberNeighbours;
		this.MaxTemprature = MaxTemprature;
		this.targetScore = targetScore;
		this.maxRuns = maxRuns;
		this.linear = linear;
	}
	
	//sets deltatemp to decrease linearly throughout the run
	public SimulatedAnnealing(int numberNeighbours,double MaxTemprature, double targetScore,boolean debug, int maxRuns, boolean linear){
		super(debug);
		double temp;
		if (linear){
			temp = MaxTemprature/((double)maxRuns);
		} else {
			double MR = 0.1 / ((double)maxRuns);
			double MT = Math.log(1.0/MaxTemprature);
			temp = Math.pow(Math.E,MR*MT);
			System.out.println("deltaTemp is : " +temp);
		}
		this.DeltaTemperature = temp;
		this.numberNeighbours = numberNeighbours;
		this.MaxTemprature = MaxTemprature;
		this.targetScore = targetScore;
		this.maxRuns = maxRuns;
		this.linear = linear;
	}
	 @Override
     public void solve(){
             Temperature = MaxTemprature;
             currentScore = -sm.getState().getNumberOfConflicts();
             print("Current Score = " + (-currentScore));
             
             while (stepsToSolve < maxRuns && currentScore < targetScore ){
                     AbstractState bestState = null;
                     
                     ArrayList<AbstractState> newStates= new ArrayList<AbstractState>();
                     double maxScore = -Double.MAX_VALUE;
                     
                     for (int i = 0 ; i < numberNeighbours ; i++){
                     		 AbstractState state = sm.generateNeighbourState();
                             double score = -state.getNumberOfConflicts();    
                             
                             if (maxScore < score){
                                     maxScore = score;
                                     bestState = state;
                             }
                             newStates.add(state);    
                     }
                     
                     print("Best Neighbour Score = " + (-maxScore));
                     
                     double q = (maxScore-currentScore)/(-currentScore);
                     print("Value for q = " +q);
                     
                     double exponent = (-q/Temperature);
                     print("Value for exponent = " +exponent);
                     
                     double p = Math.min(1, Math.pow(Math.E, exponent));                                
                                                                                     
                     double x = Math.random();            
                     
                     print("Weighted difference: "+q);
                     print("Current Temperature: " + Temperature);
                     print("Exponent: "+exponent);
                     
                     if (maxScore >= currentScore && x > p ){
                             sm.setState(bestState);
                             print("Random value: "+x+" > " +p+ "  -->  Exploit!");
                     } else {
                    	 	 AbstractState state = newStates.get(new Random().nextInt(newStates.size()));
                             sm.setState(state);
                             print("Random value: "+x+" < " +p+ "  -->  Explore (" + state.getNumberOfConflicts() + ")!");
                     }
                     
                     print("");
                     
                     //temperature:
                     if (linear){
                             //linear
                             Temperature = Math.max(Temperature-DeltaTemperature, 0.0000001);
                     }
                     else {
                             //rate of decay
                             Temperature = Math.max(Temperature*(DeltaTemperature), 0.0000001);
                     }
                     
                     stepsToSolve++;
                     currentScore = -sm.getState().getNumberOfConflicts();
                     print("Round number: " +stepsToSolve);
                     print("Current conflicts: " + (-currentScore));
             }
             print("Steps: "+stepsToSolve+" Conflicts: "+(-currentScore));
     } 


	@Override
	public int getStepsToSolve() {
		return stepsToSolve;
	}

	public void clear() {
		stepsToSolve = 0;
	}
}
