package adultSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Models.Phenotype;
import Models.Population;

public class Generations implements AdultSelector{
	
	private int survivors;
	private double harshness;
	
	public Generations(int number){
		this.survivors = number;
		this.harshness = 0.2;
	}
	
	public Generations(int number, double harshness){
		this.survivors = number;
		this.harshness = harshness;
	}

	@Override
	public Population generateNextPopulation(Population lastPop, Population newPop) {
		ArrayList<Phenotype> nextList = new ArrayList<Phenotype>();
		ArrayList<Phenotype> prevList = new ArrayList<Phenotype>();
		
		prevList.addAll(lastPop.getMembers());
		prevList.addAll(newPop.getMembers());
		
		Collections.sort(prevList);
		Collections.reverse(prevList);
		
		Random rand = new Random();
		//get enough members
		for (int i = 0; i < survivors; i++) {
			//get random good members
			int j = 0;
			while(rand.nextDouble()>harshness){
				j++;
			}
			//adds members to next gen, not out of bounds.
			nextList.add(prevList.get(Math.min(j, prevList.size()-1)));
			prevList.remove(Math.min(j, prevList.size()-1));
		}
		
		
		
		// TODO Auto-generated method stub
		return new Population(nextList);
	}

}
