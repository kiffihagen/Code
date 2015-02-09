package players.ai.minmax;

import players.ai.minmax.evaluation.AdvancedEvaluator;

public class MinMaxAI extends BaseMinMax {

	final String name = MinMaxAI.class.getName();

	public  MinMaxAI(boolean verboseOutput, int maxDepth) {
		super(verboseOutput,maxDepth);
		eval = new AdvancedEvaluator();
	}

	@Override
	public String getName() {
		return this.name + "(" + maxDepth + ")";
	}
	

	
		


}
