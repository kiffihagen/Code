package parentSelection;

import java.util.ArrayList;

import Models.Genotype;
import Models.Phenotype;
import Models.Population;

public interface ParentSelector {
	
	abstract ArrayList<Phenotype> generateNextGeneration(Population p, double t);

}
