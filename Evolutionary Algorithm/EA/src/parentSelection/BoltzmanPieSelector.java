package parentSelection;

import java.util.ArrayList;
import java.util.Random;

import Models.Phenotype;
import Models.Population;

public class BoltzmanPieSelector implements ParentSelector{

	@Override
	public ArrayList<Phenotype> generateNextGeneration(Population p, double t) {
		Random rand = new Random();
		double temperature = 1.0+4.0*(1-t);

		p.sortPopulation();
		ArrayList<Phenotype> allParents = p.getMembers();
		
		ArrayList<Double> pieceOfPie = new ArrayList<Double>();
		ArrayList<Phenotype> selectedParents = new ArrayList<Phenotype>();
		
		double avgFitness = p.getAverage();
		
		double temp = 0;
		//creating values
		for (int i = 0; i < allParents.size(); i++) {
			double value = Math.pow(Math.E,allParents.get(i).getFitness()/temperature) /
							Math.pow(Math.E,avgFitness/temperature);
					
			pieceOfPie.add(value+temp);
			System.out.println();
			double summ = value+temp;
			System.out.println("Average fitness "+avgFitness);
			System.out.println("T = "+temperature);
			System.out.println("My fitness: "+allParents.get(i).getFitness());
			System.out.println("My value "+summ);
			temp = temp+value;
		}
		
		//adding N parents:
		
		for (int i = 0; i < allParents.size(); i++) {
			int j = 0;
			double pointer = temp*rand.nextDouble();
			while(pointer>pieceOfPie.get(j)){
				j++;
			}
			selectedParents.add(allParents.get(j));
		}
		
		
		
		return selectedParents;
	}
	

}
