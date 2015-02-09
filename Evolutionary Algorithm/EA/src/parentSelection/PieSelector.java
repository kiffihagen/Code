package parentSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Models.Genotype;
import Models.Phenotype;
import Models.Population;

public class PieSelector implements ParentSelector {

	@Override
	public ArrayList<Phenotype> generateNextGeneration(Population p,double t) {
		
		Random rand = new Random();

		p.sortPopulation();
		ArrayList<Phenotype> allParents = p.getMembers();
		Population p1 = new Population(allParents);
		
		ArrayList<Double> pieceOfPie = new ArrayList<Double>();
		ArrayList<Phenotype> selectedParents = new ArrayList<Phenotype>();
		
		double totalFitness = p.getFitnessSum();
		
		double temp = 0;
		for (int i = 0; i < allParents.size(); i++) {
			double value = allParents.get(i).getFitness()/totalFitness;
			pieceOfPie.add(value+temp);
			//System.out.println("Pievalue "+(value+temp));
			temp = temp+value;
		}
		
		//adding N parents:
		
		for (int i = 0; i < allParents.size(); i++) {
			int j = 0;
			double pointer = rand.nextDouble();
			while(pointer>pieceOfPie.get(j)){
				j++;
			}
			selectedParents.add(allParents.get(j));
		}
		
		
		
		return selectedParents;
	}

}
