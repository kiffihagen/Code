package adultSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Models.Phenotype;
import Models.Population;

public class FullReplacement implements AdultSelector {
	
	private double survivalRate;
	private double harshness;
	private boolean elitism;
	
	public FullReplacement(double rate,double harshness){
		this.survivalRate = rate;
		this.harshness = harshness;
		this.elitism = false;
	}
	
	public FullReplacement(double rate, boolean elitism){
		this.survivalRate = rate;
		this.harshness = .3;
		this.elitism = elitism;
	}


	@Override
	public Population generateNextPopulation(Population lastPop, Population newPop) {
		//Population newP = newPop;
		ArrayList<Phenotype> nextPop = new ArrayList<Phenotype>();
		ArrayList<Phenotype> members = new ArrayList<Phenotype>();
		
		/*
		if(survivalRate>=1){
			if(!elitism)
				return newPop;
			else{
				newP.sortPopulation();
				nextPop = newP.getMembers();
				nextPop.remove(nextPop.size()-1);
				nextPop.add(getBestFromPop(lastPop));
				new Population(nextPop);
				
			}
		}
		*/
		
		newPop.sortPopulation();
		if(elitism){
			members = newPop.getMembers();
			members.remove(Math.max(0, members.size()-1));
			members.add(getBestFromPop(lastPop));
			Collections.sort(members);
			Collections.reverse(members);
		} else
			members = newPop.getMembers();
	
		//System.out.println("Should maybe happen");
		
		int popSize = newPop.getPopulationSize();
		int keepers = (int) Math.floor(survivalRate*(1.0*popSize));
		
		
		Random rand = new Random();
		//get enough members
		for (int i = 0; i < keepers; i++) {
			//get random good members
			int j = 0;
			while(rand.nextDouble()>harshness){
				j++;
			}
			//adds members to next gen, not out of bounds.
			nextPop.add(members.get(Math.min(j, members.size()-1)));
			members.remove(Math.min(j, members.size()-1));
			
		}
		
		
		return new Population(nextPop);
	}
	
	private Phenotype getBestFromPop(Population p){
		p.sortPopulation();
		ArrayList<Phenotype> genes = p.getMembers();
		return genes.get(0);
	}


}
