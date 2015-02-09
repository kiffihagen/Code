package States;

import java.util.LinkedList;
import java.util.Random;

public class EquationState extends AbstractState {
	private int [][] amatrix;
	private int [] bvector;
	private int [] solution;
	private int k;
	
	@Override
	public LinkedList<Integer> getVars() {
		LinkedList<Integer> r = new LinkedList<Integer>();
		for (int i = 0; i < amatrix.length; i++){
			r.add(i);
		}
		return r;
	}

	@Override
	public int getNumberOfConflicts(int var) {
		int conflicts = 0;
		int [] result = multiply();
		for (int i = 0; i < amatrix.length; i++){
			if(amatrix[i][var] != 0){
				if (result[i] != bvector[i]){
					conflicts++;
				}
			}
		}
		return conflicts;
	}
	
	@Override
	public int getNumberOfConflicts() {
		int [] result = multiply();
		return compare(result,bvector);
	}

	@Override
	public LinkedList<Integer> getPossibleValues() {
		LinkedList<Integer> r = new LinkedList<Integer>();
		for (int i = -50; i < 50; i++){
			r.add(i);
		}
		return r;
	}

	@Override
	public int getValue(int var) {
		if (var >= 0 && var < solution.length){
			return solution[var];
		}
		else{
			throw new IndexOutOfBoundsException("getValue index out of bounds, input was: " + var);
		}
	}

	@Override
	public void setValue(int var, int value) {
		if (var >= 0 && var < solution.length){
			solution[var] = value;
		}
		else{
			throw new IndexOutOfBoundsException("setValue index out of bounds, input was " + var);
		}
	}

	@Override
	public void display() {
		System.out.print("Solution vector: ");
		for (int i = 0; i < solution.length; i++){
			System.out.print(solution[i] + " ");
		}
		System.out.println();
		System.out.println("Number of conflicts:" + getNumberOfConflicts());
	}
	private int[] multiply(){
		int [] res = new int[amatrix.length];
		for (int row = 0; row < amatrix.length; row++){
			for (int col = 0; col < amatrix.length; col++){
				res[col] = solution[col] * amatrix[row][col];
			}
		}
		return res;
	}
	private static int compare(int [] vector1, int[] vector2){
		int res = 0;
		for (int i = 0; i < vector1.length; i++){
			int t = vector1[i] - vector2[i];
			if (t * t > 0) 
				res++;
		}
		return res;
	}
	
	public EquationState(EquationState old) {
		bvector = old.bvector;
		amatrix = old.amatrix;
		k = old.k;
		solution = new int[old.solution.length];
		for (int i = 0; i < old.solution.length; i++){
			solution[i] = old.solution[i];
		}
	}
	
	public EquationState(int k) {
		this.k = k;
		solution = new int[k];
		amatrix = new int[k][k];
		Random r = new Random();
		for (int i = 0; i < k; i++){
			solution[i] = r.nextInt(100) -50;
			for (int j = 0; j < k; j++){
				amatrix[i][j] = r.nextInt(100)-50;
			}
		}
		bvector = multiply();
		for (int i = 0; i < k; i++){
			solution[i] = 0;
		}
	}


	@Override
	public AbstractState copy() {
		return new EquationState(this);
	}

	@Override
	public int evaluate() {
		int res = 0;
		int []vector = multiply();
		for (int i = 0; i < vector.length; i++){
			int t = vector[i] - bvector[i];
			if (t * t > 0) 
				res+= t*t;
		}
		return (int)((double)res/vector.length);
	}

}
