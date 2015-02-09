package Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class OneMaxGenotype extends Genotype {
	
	private ArrayList<Allele> vector;
	private Random rand;
	
	
	public OneMaxGenotype(int size){
		this.vector = new ArrayList<Allele>();
		rand = new Random();
		generateRandom(size);
		//super.setFitness(findFitness());
		
	}
	
	public OneMaxGenotype(ArrayList<Allele> a){
		this.vector = a;
		rand = new Random();
		//super.setFitness(findFitness());
	}
	
	@Override
	public void generateRandom(int s) {
		for (int i = 0; i < s; i++) {
			this.vector.add(new OneMaxAllele(rand.nextBoolean()));
			//System.out.println("New Allele");
		}
	}

	@Override
	public Phenotype develop() {
		return new OneMaxPhenotype(this);
	}


	@Override
	public Genotype mate(Genotype g, double mutationRate, int type) {
		OneMaxGenotype otherParent = (OneMaxGenotype) g;
		int size = vector.size();
		ArrayList<Allele> a1 = this.getAlleles();
		ArrayList<Allele> a2 = otherParent.getAlleles();
		ArrayList<Allele> outGenes = new ArrayList<Allele>();
		

		switch(type){
		case 1:
		//split middle:
			for (int i = 0; i < size; i++) {
				if(i<=(1.0*size)/2){
					outGenes.add(a1.get(i));
				} else {
					outGenes.add(a2.get(i));
				}
			}
			break;
		case 2:
		
		//split random:
			int splitpoint = rand.nextInt(size);
			for (int i = 0; i < size; i++) {
				if(i<splitpoint){
					outGenes.add(a1.get(i));
				} else {
					outGenes.add(a2.get(i));
				}
			}
			break;
		case 3:
		default:
			//50/50 every allele
			for (int i = 0; i < size; i++) {
				if(0.5>rand.nextDouble()){
					outGenes.add(a1.get(i));
				} else {
					outGenes.add(a2.get(i));
				}
			}
			break;
				
				
				
				
		}
		
		
		if(rand.nextDouble()<mutationRate){
			int position = rand.nextInt(outGenes.size());
			outGenes.get(position).mutate();
		}
			
		return new OneMaxGenotype(outGenes);
	}

	@Override
	public Genotype copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mutate() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Allele> getAlleles() {
		// TODO Auto-generated method stub
		return vector;
	}




}
