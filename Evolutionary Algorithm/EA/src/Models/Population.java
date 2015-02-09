package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population {
	
	public ArrayList<Phenotype> phenotypes;
	
	public Population(){
		phenotypes = new ArrayList<Phenotype>();
	}
	
	public Population(ArrayList<Phenotype> members){
		phenotypes = members;
	}
	
	public void addPhenotype(Phenotype p){
		phenotypes.add(p);
	}
	
	public ArrayList<Phenotype> getMembers(){
		return (ArrayList<Phenotype>) phenotypes;
	}
	
	public void sortPopulation(){
		Collections.sort(phenotypes);
		Collections.reverse(phenotypes);
	}
	
	public int getPopulationSize(){
		return phenotypes.size();
	}
	
	public void printList(){
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = (Phenotype) phenotypes.get(i);
			System.out.println("Fitness is "+p.getFitness());
			
		}
	}
	
	public double getAverage(){
		double sum = 0;
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = (Phenotype) phenotypes.get(i);
			sum = sum + p.getFitness();
		}
		return sum/phenotypes.size();
	}
	
	public double getFitnessSum(){
		return getAverage()*phenotypes.size();
	}
	
	public double getBest(){
		sortPopulation();
		Phenotype p = (Phenotype) phenotypes.get(0);
		return p.getFitness();
	}
	
	public double getStdDev(){
		double sum = 0;
		double average = getAverage();
		
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = (Phenotype) phenotypes.get(i);
			sum = sum + Math.pow(p.getFitness()-average,2);
		}
		
		sum = sum/phenotypes.size();
		return 10*Math.sqrt(sum);
	}
	
	public Population copy(){
		return new Population(this.getMembers());
	}

}
