package players.ai.minmax;

//import players.ai.minmax.evaluation.ConstantEval;
import players.ai.minmax.evaluation.ConstantEval;

public class MinMaxAI2 extends BaseMinMax {

	final String name = MinMaxAI2.class.getName();

	@Override
	public String getName() {
		return this.name + "(" + maxDepth + ")";
	}
	
	public  MinMaxAI2(boolean verboseOutput, int maxDepth) {
		super(verboseOutput,maxDepth);
		eval = new ConstantEval();
	}



	



}
