package Models;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Swarm {
	private List<Particle> swarm;
	private double bestGlobalFit;
	private Position bestGlobalPos;
	int neighbours;
	boolean isCircle;
	private double[] packageWeight;
	private double[] packageVolume;
	private double[] packageValue;
	Random r = new Random();
	private boolean useVolume;
	//
	private double LOCALWEIGHT;
	private double GLOBALWEIGHT;
	private int maxRuns;
	private boolean inertia;
	
	//for knapsack
	public Swarm(int particles, boolean useVolume, int iterations, double LW, double GW){
		LOCALWEIGHT = LW;
		GLOBALWEIGHT = GW;
		maxRuns = iterations;
		this.useVolume = useVolume;
		bestGlobalPos = null;
		isCircle = false;
		inertia = true;
		bestGlobalFit = -Double.MAX_VALUE;
		packageWeight = new double[2000];
		packageVolume = new double[2000];
		packageValue = new double[2000];
		
		loadText("./Resources/pso-packages.txt");
		generateVolume(useVolume);
		
		//System.out.println("Weight==================== "+packageWeight[10]);
		
		swarm = new ArrayList<Particle>();
		for (int i = 0; i < particles; i++) {
			Particle p = new Particle(2000);
			p.setMyBestLoc();
			double bestFit = evaluateKnapsackFitness2(p,useVolume);
			System.out.println("Bestfit during init: "+bestFit);
			p.setMyBestFitness(bestFit);
			swarm.add(p);
			if(bestFit > bestGlobalFit ){
				bestGlobalFit = bestFit;
				bestGlobalPos = p.getPosition().copy();
				
			}
		}
		System.out.println("Swarm initialized:");
		//System.out.println("Value = "+bestGlobalFit);
		//bestGlobalPos.printDiscreteString();
	}
	//for circle
	public Swarm(int particles, double maxValues, int dims, int runs, int neighbours, boolean inertia, double LW, double GW){
		LOCALWEIGHT = LW;
		GLOBALWEIGHT = GW;
		this.inertia = inertia;
		isCircle = true;
		this.neighbours = neighbours;
		maxRuns = runs;
		bestGlobalFit = Double.MAX_VALUE;
		swarm = new ArrayList<Particle>();
		for (int i = 0; i < particles; i++) {
			Particle p = new Particle(dims, maxValues);
			p.setMyBestLoc();
			//double bestFit = evaluateCircleFitness(p,0);
			double bestFit = evaluateCircleFitness(p);
			p.setMyBestFitness(bestFit);
			swarm.add(p);
			if(bestFit < bestGlobalFit ){
				bestGlobalFit = bestFit;
				bestGlobalPos = p.getPosition().copy();
				System.out.println(bestGlobalFit);
			}
		}
		System.out.println("Swarm initialized\n");
	}
	
	//circle evaluator
	public double evaluateCircleFitness(Particle p){
		double val = Math.sqrt(
								Math.pow(p.getX(),2) + Math.pow(p.getY(),2)
								);
		return Math.abs(val);
	}
	
	//double roosts. not used in demo
	public double evaluateCircleFitness2(Particle p, int iteration){
		double x1 = Math.cos(1.0*iteration/50)/2;
		double y1 = Math.sin(1.0*iteration/50)/2;
		double x2 = -Math.cos(1.0*iteration/50)/2;
		double y2 = -Math.sin(1.0*iteration/50)/2;
		double d1 = Math.sqrt(
				Math.pow(x1-p.getX(),2) + Math.pow(y1-p.getY(),2)
				);
		double d2 = Math.sqrt(
				Math.pow(x2-p.getX(),2) + Math.pow(y2-p.getY(),2)
				);
		double val = Math.min(d1, d2);
		return Math.abs(val);
	}
	
	//evaluate position knapsack
	public double evaluateKnapsackFitnessPos(Position pos, boolean print){
		double value = 0;
		double volume = 0;
		double weight = 0;
		int zeroes = 0;
		//System.out.println("Old best fitness: "+p.getMyBestFitness());
		if(pos==null){
			return 0.0;
		}
		
		for (int i = 0; i < packageValue.length; i++) {
			//System.out.println("Weight = "+packageWeight[i]);
			if(pos.getAPosition(i)>=0.5){
				//System.out.println("Over 0.5");
				value += packageValue[i];
				weight += packageWeight[i];
				volume += packageVolume[i];
			} else {
				zeroes++;
			}
		}
		if (weight>1000 || volume>1000){
			System.out.println("Too heavy, Value = "+value+", Weight = "+weight);

			return 0.0;
		}
		if(print){
			System.out.println("Best combination found:");
			System.out.println("Value = "+value);
			System.out.println("Weight = "+weight);
			System.out.println("Volume = "+volume);
			System.out.println("Exiting...");
		}
		//System.out.println(zeroes+ " out of 2000 not taken");
		return value;
	}
	
	//evaluate particle knapsack legacy
	public double evaluateKnapsackFitness(Particle p){
		double value = 0;
		double volume = 0;
		double weight = 0;
		int zeroes = 0;
		//System.out.println("Old best fitness: "+p.getMyBestFitness());
		
		for (int i = 0; i < packageValue.length; i++) {
			//System.out.println("Weight = "+packageWeight[i]);
			if(p.getPosition().getAPosition(i)>=0.5){
				//System.out.println("Over 0.5");
				value += packageValue[i];
				weight += packageWeight[i];
				volume += packageVolume[i];
			} else {
				zeroes++;
			}
		}
		if (weight>1000 || volume>1000){
			System.out.println("Too heavy, Value = "+value+", Weight = "+weight);

			return 0.0;
		}
		System.out.println(2000-zeroes+ " packets taken");
		return value;
		
	}
	
	//2nd gen evaluator, better for values above 1000.
	public double evaluateKnapsackFitness2(Particle p, boolean useVolume){
		double value = 0;
		double volume = 0;
		double weight = 0;
		int zeroes = 0;
		//System.out.println("Old best fitness: "+p.getMyBestFitness());
		
		for (int i = 0; i < packageValue.length; i++) {
			if(p.getPosition().getAPosition(i)>=0.5){
				value += packageValue[i];
				weight += packageWeight[i];
				volume += packageVolume[i];
			} else
				zeroes++;
		}
		//System.out.println(weight+ " kilos");
		weight = weight<=1000 ? 1.0 : 1000/(1.1*weight);
		volume = volume<=1000 ? 1.0 : 1000/(1.1*volume);
		
		//System.out.println(zeroes+ " out of 2000 not taken");
		
		if(useVolume){
			return value * weight * volume;
		}
		return value *  weight;
		
	}
	
	public List<Particle> getParticles(){
		return swarm;
	}
	
	public double getBestValue(){
		return bestGlobalFit;
	}
	
	public Position getBestPos(){
		return bestGlobalPos;
	}
	
	//engine:
	public void updateParticles(int iteration){
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Update number "+iteration);
		//System.out.println("Best fitness = "+bestGlobalFit);
		//System.out.println("Best position:");
		//bestGlobalPos.printString();
		
		for(Particle p : swarm){
			if(neighbours==0){
				if(inertia){
					p.update(bestGlobalPos, intertiaModifier(iteration), LOCALWEIGHT, GLOBALWEIGHT);
				}else {
					p.update(bestGlobalPos, 1.0, LOCALWEIGHT, GLOBALWEIGHT);
				}	
			} else {
				//find neighbours and stuff:
				List<Particle> myNeighbours = new ArrayList<>();
				List<Double> distances = new ArrayList<>();
				List<Particle> myNeighs = new ArrayList<>();
				
				for(Particle o : swarm){
					if(p!=o){
						myNeighbours.add(o);
						distances.add(p.distance(o));
					}
				}
				
				for (int j = 0; j < neighbours; j++) {
					double tempMin = Double.MAX_VALUE;
					int minLoc = -1;
					for (int i = 0; i < distances.size(); i++) {
						if(distances.get(i)<tempMin){
							minLoc=i;
							tempMin=distances.get(i);
						}
					}
					//System.out.println("Size of N-list: "+myNeighbours.size());
					//System.out.println("Size of D-list: "+distances.size());
					//System.out.println("Asking for element: "+minLoc);
					if(minLoc>=0){
						myNeighs.add(myNeighbours.get(minLoc));
						myNeighbours.remove(minLoc);
						distances.remove(minLoc);
					}
				}
				
				//System.out.println("Number of nbours: " +myNeighs.size());
				double bestSocialFit = Double.MAX_VALUE;
				Position socialPos = null;
				for(Particle p3 : myNeighs){
					double thisFit = p3.getMyBestFitness();
					if(thisFit<bestSocialFit)
						bestSocialFit=thisFit;
						socialPos = p3.getMyBestLoc().copy();
				}
				//System.out.println("Best social fit: "+bestSocialFit);
				//socialPos.printString();
				
				if(inertia){
					p.update(new Position(socialPos), intertiaModifier(iteration), LOCALWEIGHT, GLOBALWEIGHT);
				}else {
					p.update(new Position(socialPos), 1.0, LOCALWEIGHT, GLOBALWEIGHT);
				}
			}
		}
		
		for(Particle p : swarm){
			if(isCircle){
				//double fit = evaluateCircleFitness(p,iteration);
				double fit = evaluateCircleFitness(p);
				if(fit<p.getMyBestFitness()){
					p.setMyBestFitness(fit);
					p.setMyBestLoc();
					//System.out.println("New best local: "+fit);
					
					
				}
				
				if(fit<bestGlobalFit){
					bestGlobalFit=fit;
					bestGlobalPos=p.getPosition().copy();
					System.out.println("New best global fit: "+bestGlobalFit);
					System.out.println("New best global position:");
					bestGlobalPos.printString();
				}
			} else {
				double fit = evaluateKnapsackFitness2(p,useVolume);
				//System.out.println("Fitness found: "+fit);
				if(fit>p.getMyBestFitness()){
					p.setMyBestFitness(fit);
					p.setMyBestLoc();
					//System.out.println("New best local: "+fit);	
				}
				if(fit>bestGlobalFit){
					bestGlobalFit=fit;
					bestGlobalPos=p.getPosition().copy();
					System.out.println("New best global fit: "+bestGlobalFit);
					//System.out.println("New best global position");
					//bestGlobalPos.printString();
				}
				
			}
		}

		
		
	}
	
	public void getRealBestValue(){
		double best = 0;
		Position bestPos = null;
		Particle bestPart = null;
		for(Particle p : swarm){
			double temp = evaluateKnapsackFitnessPos(p.getMyBestLoc(),false);
			if(temp>best){
				bestPos=p.getMyBestLoc();
				best=temp;
			}
		}
		for(Particle o : swarm){
			double temp2 = evaluateKnapsackFitness(o);
			if(temp2>best){
				bestPos=o.getPosition();
				best=temp2;
			}
		}
		if(bestPos!=null)
			bestPos.printDiscreteString();
		evaluateKnapsackFitnessPos(bestPos,true);
		
	}
	
	private double intertiaModifier(int i){
		int maxi = maxRuns;
		double maxVal = 1.0;
		double minVal = 0.4;
		double steps = (maxVal-minVal)/maxi;
		return minVal+(maxi-i)*steps;
	}
	
	private void generateVolume(boolean isEnabled){
		for (int i = 0; i < packageVolume.length; i++) {
			packageVolume[i]= (isEnabled) ? (1 + 100 * r.nextDouble()) : 0.0;
		}
	}
	
	private void loadText(String filename){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		}
		String line = null;
		
		try {
			
			int row = 0;
			
			while ((line = reader.readLine()) != null) {
				String substrings[] = line.split(",");
				packageWeight[row]=Double.parseDouble(substrings[1]);
				packageValue[row]=Double.parseDouble(substrings[0]);	
				row++;
				//System.out.println(row);
				//System.out.println(substrings[0]+" , "+substrings[1]);
			}
		} catch (IOException e) {
			System.err.println("Reading from file failed");
		}
	}

}
