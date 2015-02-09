package parentSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Models.Phenotype;
import Models.Population;

public class TournamentSelector implements ParentSelector{
	
	private int K;
	private double epsilon;
	
	public TournamentSelector(int k, double e){
		this.K = k;
		if (K<2)
			K = 2;
		this.epsilon = e;
		
	}

	@Override
	public ArrayList<Phenotype> generateNextGeneration(Population p, double t) {
		
		ArrayList<Phenotype> allParents = p.getMembers();
		//ArrayList<Phenotype> children = new ArrayList<Phenotype>();
		ArrayList<Phenotype> selectedParents = new ArrayList<Phenotype>();
		Random rand = new Random();
		
		for (int i = 0; i < allParents.size(); i++) {		
			//add K random members from pool:
			Collections.shuffle(allParents);
			List<Phenotype> contestants = allParents.subList(0, K);
			Collections.sort(contestants);
			Collections.reverse(contestants);
			
			if(rand.nextDouble()<epsilon){
				selectedParents.add(contestants.get(0));
				System.out.println("Selected best parent with fitness "+contestants.get(0).getFitness());
			} else {
				int index = 1+rand.nextInt(contestants.size()-1);
				selectedParents.add(contestants.get(index));
			}		
		}
		/*
		//have a list of N parents pair them 2 and 2:
		int i = 0;
		while(i < selectedParents.size()){
			Phenotype c1 = selectedParents.get(i).mate(selectedParents.get(i+1));
			children.add(c1);
			Phenotype c2 = selectedParents.get(i+1).mate(selectedParents.get(i));
			children.add(c2);
			i = i+2;
		}
		*/
		
		return selectedParents;
	}

}
