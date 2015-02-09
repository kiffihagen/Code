package States;

import java.util.LinkedList;

public abstract class AbstractState {
	public abstract LinkedList<Integer> getVars();
	public abstract int getNumberOfConflicts(int var);
	public abstract LinkedList<Integer> getPossibleValues();
	public abstract int getValue(int var);
	public abstract void setValue(int var, int value);
	public abstract void display();
	public abstract AbstractState copy();
	public int evaluate(){
		return -getNumberOfConflicts();
	}

	public boolean isInvolvedInConflict(int var){
		if (getNumberOfConflicts(var) > 0)
			return true;
		else
			return false;
	}
	public int getNumberOfConflicts(){
		int total = 0;
		for (Integer var: getVars()){
			total += getNumberOfConflicts(var);
		}
		return total;
	}
	public boolean isOptimal() {
		if (getNumberOfConflicts() > 0)
			return false;
		else
			return true;
	}
	public int testNumberOfConflicts(int var, int value) {		
		int oldvalue = getValue(var);
		setValue(var,value);
		int conflicts = getNumberOfConflicts();
		setValue(var,oldvalue);
		return conflicts;
	}
	
}
