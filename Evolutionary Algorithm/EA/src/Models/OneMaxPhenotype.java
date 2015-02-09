package Models;

import java.util.ArrayList;



public class OneMaxPhenotype extends Phenotype{
	
	private boolean[] bitString;

	public OneMaxPhenotype(Genotype g) {
		
		ArrayList<Allele> a = g.getAlleles();
		int size = a.size();
		//System.out.println("size "+size);
		bitString = new boolean[size];
		for (int i = 0; i < size; i++) {
			OneMaxAllele omg = (OneMaxAllele) a.get(i);
			bitString[i] = omg.getValue();	
		}
		setFitness(calculateFitness());
		setGenotype(g);
	}

	@Override
	public double calculateFitness() {
		int sum = 0;
		for (int i = 0; i < bitString.length; i++) {
			if(bitString[i])
				sum++;
		}
		return sum;
	}

	@Override
	public Genotype mate(Phenotype g, double mutationRate, int type) {
		Genotype p1 = this.getGenotype();
		Genotype p2 = g.getGenotype();
		return p1.mate(p2,mutationRate,type);
	}
	
	
	

}
