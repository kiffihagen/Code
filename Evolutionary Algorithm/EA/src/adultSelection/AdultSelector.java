package adultSelection;

import Models.Population;

public interface AdultSelector {
	
	abstract Population generateNextPopulation(Population lastPop, Population newPop);

}
