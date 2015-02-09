package players.ai;

import players.ai.minmax.evaluation.BaseEvaluator;


public abstract class BaseRecursiveAI extends BaseAI{

	protected int maxDepth;
	protected NoviceAI randomizer;
	protected BaseEvaluator eval;
	protected int counter = 0;
	
	public BaseRecursiveAI(boolean verboseOutput, int maxDepth) {
		super(verboseOutput);
		this.maxDepth = maxDepth;
		randomizer = new NoviceAI(verboseOutput);
	}

}
