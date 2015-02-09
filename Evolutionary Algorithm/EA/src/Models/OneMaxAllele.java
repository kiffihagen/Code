package Models;



public class OneMaxAllele implements Allele{
	
	private boolean value;
	
	public OneMaxAllele(boolean b){
		this.value = b;
	}

	@Override
	public void mutate() {
		value = !value;	
		//System.out.println("Mutation");
	}
	
	public boolean getValue(){
		return value;
	}

}
