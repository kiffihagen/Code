package Models;

import java.util.ArrayList;
import java.util.List;

public abstract class Genotype{
	
	private double fitness;
	private double mutationRate;
	private ArrayList<Allele> alleles;
	
	
	public abstract Phenotype develop();
	public abstract void mutate();
	public abstract Genotype mate(Genotype g,double mutationRate,int type);
	public abstract void generateRandom(int s);
	public abstract Genotype copy();
	public abstract ArrayList<Allele> getAlleles();
	


}
