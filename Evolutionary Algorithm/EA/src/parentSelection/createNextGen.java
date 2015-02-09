package parentSelection;

import java.util.ArrayList;

import Models.Genotype;
import Models.Phenotype;
import Models.Population;

public class createNextGen {
	private int type;
	private double mutaterate;
	private int crossOverType;
	
	public createNextGen(int type, double mutaterate, int crossovertype){
		this.type = type;
		this.mutaterate = mutaterate;
		this.crossOverType = crossovertype;
	}
	
	public Population createChrildren(ArrayList<Phenotype> parents){
		ArrayList<Phenotype> children = new ArrayList<Phenotype>();
		double parval = 0;
		double cval = 0;
		Population tp = new Population(parents);
		
		switch(type){
		
			default:
				int i = 0;
				while(i < parents.size()){
					Phenotype p1 = parents.get(i);
					Phenotype p2 = parents.get(i+1);
					
					
					//System.out.println("Parents fitness: "+p1.getFitness()+" and "+p2.getFitness() );
					parval = parval + p1.getFitness() + p2.getFitness();
					
					Genotype c1 = p1.mate(p2, mutaterate, crossOverType);
					Genotype c2 = p2.mate(p1, mutaterate, crossOverType);
					
					Phenotype pc1 = c1.develop();
					Phenotype pc2 = c2.develop();
					
					children.add(pc1);
					children.add(pc2);
					
					//System.out.println("Children fitness: "+pc1.getFitness()+" and "+pc2.getFitness() );
					cval = cval + pc1.getFitness() + pc2.getFitness();
					
					i = i+2;
				}
		}
		//System.out.println("Average fitness "+tp.getAverage());
		System.out.println("Parents fitness: "+(parval/500)+" and "+(cval/500) );
		
		return new Population(children);
			
				
	}

}
