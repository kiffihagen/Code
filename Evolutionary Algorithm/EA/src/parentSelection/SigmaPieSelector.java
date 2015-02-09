package parentSelection;

import java.util.ArrayList;
import java.util.Random;

import Models.Phenotype;
import Models.Population;

public class SigmaPieSelector implements ParentSelector{

	@Override
	public ArrayList<Phenotype> generateNextGeneration(Population p, double t) {
		Random rand = new Random();

		p.sortPopulation();
		ArrayList<Phenotype> allParents = p.getMembers();
		Population p1 = new Population(allParents);
		
		ArrayList<Double> pieceOfPie = new ArrayList<Double>();
		ArrayList<Phenotype> selectedParents = new ArrayList<Phenotype>();
		
		double totalFitness = p.getFitnessSum();
		double var = p.getStdDev();
		if(var==0)
			var = 1;
		double avgFitness = p.getAverage();
		
		double temp = 0;
		//creating values
		for (int i = 0; i < allParents.size(); i++) {
			System.out.println("Average fitness "+avgFitness);
			System.out.println("Sigma: "+var);
			System.out.println("My fitness: "+allParents.get(i).getFitness());
			double value = 1 + (
					(allParents.get(i).getFitness()-avgFitness) / (2*var));
					
			pieceOfPie.add(value+temp);
			double summ = value+temp;
			System.out.println("My Pie Value "+(summ));
			temp = temp+value;
		}
		
		//adding N parents:
		
		for (int i = 0; i < allParents.size(); i++) {
			int j = 0;
			double pointer = allParents.size()*rand.nextDouble();
			while(pointer>pieceOfPie.get(j)){
				j++;
			}
			selectedParents.add(allParents.get(j));
		}
		
		
		
		return selectedParents;
	}
	

}
