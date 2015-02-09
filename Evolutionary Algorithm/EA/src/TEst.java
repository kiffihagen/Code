import java.util.ArrayList;

import parentSelection.BoltzmanPieSelector;
import parentSelection.ParentSelector;
import parentSelection.PieSelector;
import parentSelection.SigmaPieSelector;
import parentSelection.TournamentSelector;
import parentSelection.createNextGen;
import adultSelection.AdultSelector;
import adultSelection.FullReplacement;
import adultSelection.Generations;
import Models.Genotype;
import Models.OneMaxGenotype;
import Models.Population;
import UI.XYChart;


public class TEst {
	public static void main(String[] args){
		System.out.println("Testing");
		
		
		//EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(10, 40, new Generations(10,.9), new TournamentSelector(3, .9), 100, .3);
		
		
		//first task
		//EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(100, 40, new FullReplacement(1, true), new PieSelector(), 100 , new createNextGen(1 , .1 , 2 ));
		
		EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(500, 40, new FullReplacement(1, false), new PieSelector(), 100 , new createNextGen(1 , .25 , 3 ));
		
		

		
		
		
		/*
		Population lastGen = new Population();
		Population nextGen = new Population();
		XYChart chart = new XYChart("Test Chart");
		
		for (int i = 0; i < 10; i++) {
			OneMaxGenotype g = new OneMaxGenotype(40);
			lastGen.addGenotype(g);
			OneMaxGenotype g2 = new OneMaxGenotype(40);
			nextGen.addGenotype(g2);
			//System.out.println("Fitness: "+g.getFitness());
		}
		
		System.out.println("1st Gen");
		lastGen.sortPopulation();
		lastGen.printList();
		
		System.out.println("2nd Gen");
		nextGen.sortPopulation();
		nextGen.printList();
		
		chart.getPopData(lastGen, 0);
		
		ArrayList<Genotype> parents = new ArrayList<Genotype>();
		ParentSelector ps = new TournamentSelector(4, .25);
		AdultSelector as = new Generations(10, .2);
		createNextGen cg = new createNextGen(1, .1);
		
		lastGen = as.generateNextPopulation(lastGen, nextGen);
		chart.getPopData(lastGen, 1);
		parents = ps.generateNextGeneration(lastGen);
		nextGen = cg.createChrildren(parents);
		
		lastGen = as.generateNextPopulation(lastGen, nextGen);
		chart.getPopData(lastGen, 2);
		parents = ps.generateNextGeneration(lastGen);
		nextGen = cg.createChrildren(parents);
		
		lastGen = as.generateNextPopulation(lastGen, nextGen);
		chart.getPopData(lastGen, 3);
		parents = ps.generateNextGeneration(lastGen);
		nextGen = cg.createChrildren(parents);
		
		lastGen = as.generateNextPopulation(lastGen, nextGen);
		chart.getPopData(lastGen, 4);
		parents = ps.generateNextGeneration(lastGen);
		nextGen = cg.createChrildren(parents);

		
		System.out.println("12th gen:");
		lastGen.sortPopulation();
		lastGen.printList();
		
		
		chart.createGraph();
		chart.pack();
		chart.setVisible(true);
		*/
		
		
	}

}
