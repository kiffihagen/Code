package LocalSearch;

import java.util.LinkedList;
import java.util.Random;

import States.AbstractState;


public class MinConflicts extends ConstraintBasedLocalSearch {
	public static final String className = MinConflicts.class.getName();
	private int steps = 0;
	@Override
	public String getName(){
		return className;
	}
	
	public MinConflicts(boolean debug) {
		super(debug);
	}
	
	@Override
	public void solve() {
			steps = 0;
			while (!sm.done() && steps < 10000){
				int var = sm.getConflictedVariable();
				//sm.optimizeConflictedVariable(var);
				
				int minConflicts = Integer.MAX_VALUE;
				LinkedList<Integer> minConfValues = new LinkedList<Integer>();
				AbstractState state = sm.getState();
				for (Integer value: state.getPossibleValues()){
					int conflicts = state.testNumberOfConflicts(var,value);
					if (minConflicts >= conflicts){
						if (minConflicts > conflicts){
							minConfValues = new LinkedList<Integer>(); 
							minConflicts = conflicts;
						}
						minConfValues.add(value);
					}
				}
				int rindex = new Random().nextInt(minConfValues.size());	
				
				state.setValue(var, minConfValues.get(rindex));
				
				steps++;
				print("Step number: " + steps + " Number of conflicts: " + sm.getState().getNumberOfConflicts());
			}
			print("Completed in " + steps + " steps");
	}
	
	@Override
	public int getStepsToSolve(){
		return steps;
	}

	public void clear() {
		steps = 0;
	}
}
