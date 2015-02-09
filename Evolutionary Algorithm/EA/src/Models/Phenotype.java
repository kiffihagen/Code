package Models;


public abstract class Phenotype implements Comparable<Phenotype> {
	
	private Genotype myGenes;
	private double fitness;

	
	public abstract double calculateFitness();
	public abstract Genotype mate(Phenotype g,double mutationRate,int type);
	
	public double getFitness(){
		return fitness;
	}
	
	public void setFitness(double f){
		this.fitness = f;
	}
	
	public Genotype getGenotype(){
		return myGenes;
	}
	
	public void setGenotype(Genotype g){
		this.myGenes = g;
	}
	
	public int compareTo(Phenotype other){
        return Double.compare(this.fitness, other.getFitness());
	}

}
