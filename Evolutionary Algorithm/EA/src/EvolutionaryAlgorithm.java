import java.util.ArrayList;

import parentSelection.ParentSelector;
import parentSelection.createNextGen;
import Models.Genotype;
import Models.OneMaxGenotype;
import Models.OneMaxPhenotype;
import Models.Phenotype;
import Models.Population;
import UI.XYChart;
import adultSelection.AdultSelector;


public class EvolutionaryAlgorithm {
	
	private AdultSelector adultSelector;
	private ParentSelector parentSelector;
	private Population currentPopulation;
	private createNextGen cg;
	private int steps;
	Population lastGen;
	Population nextGen;
	
	public EvolutionaryAlgorithm(int popSize, int geneSize, AdultSelector as, ParentSelector ps, int steps, createNextGen cg){
		this.lastGen = initPopulation(popSize, geneSize);
		this.nextGen = initPopulation(popSize, geneSize);
		ArrayList<Phenotype> parents = new ArrayList<Phenotype>();
		
		XYChart chart = new XYChart("Test Chart");
		
		this.adultSelector = as;
		this.parentSelector = ps;
		this.cg = cg;
		
		//for (int i = 0; i < steps; i++) {
		int i = 0;
		while(i<steps && lastGen.getBest()<geneSize){
			i++;
			System.out.println("Generation "+i);
			System.out.println("Average fitness is "+ lastGen.getAverage());
			chart.getPopData(lastGen, i);
			
			lastGen = as.generateNextPopulation(lastGen, nextGen);
			
			chart.getPopData(lastGen, i);
			parents = ps.generateNextGeneration(lastGen,((1.0*i)/(1.0*steps)));
			nextGen = cg.createChrildren(parents);
		}
		
		
		chart.createGraph();
		chart.pack();
		chart.setVisible(true);
	
		/*
		System.out.println("last Gen");
		lastGen.sortPopulation();
		lastGen.printList();
		*/
		
	}
	
	
	private Population initPopulation(int popSize, int genSize){
		Population pop = new Population();
		
		for (int i = 0; i < popSize; i++) {
			OneMaxGenotype g = new OneMaxGenotype(genSize);
			Phenotype p = new OneMaxPhenotype(g);
			pop.addPhenotype(p);
		}
		return pop;
	}
	
	

}
