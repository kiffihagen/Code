package StateManagers;

import States.EquationState;

public class EquationManager extends LocalStateManager {
	public static final String className = EquationManager.class.getName();
	private int numVars;
	public EquationManager(int numVars) {
		super(new EquationState(numVars));
		this.numVars = numVars;
	}

	@Override
	public String getName() {
		return className + " K=" + numVars;
	}

	@Override
	public LocalStateManager copy() {
		return new EquationManager(numVars);
	}

}
