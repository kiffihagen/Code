package players.ai.solving_quarto;

import players.ai.solving_quarto.transformations.HorisontalFlip;
import players.ai.solving_quarto.transformations.Transformation;
import players.ai.solving_quarto.transformations.VerticalFlip;
import board.BoardState;

public class StateTransfomers {

	private Transformation HFLIP = new HorisontalFlip();
	private Transformation VFLIP = new VerticalFlip();
	/*private Transformation DFLIP1 = new VerticalFlip();
	private Transformation DFLIP2 = new VerticalFlip();
	private Transformation R180 = new Rotate180();
	private Transformation R270 = new Rotate270();
	private Transformation R90 = new Rotate90();*/
	
	
	
	
	private Transformation transforms[] = {HFLIP,VFLIP};//,DFLIP1,DFLIP2,R180,R270,R90};
	public BoardState canonicalize(BoardState state){
		BoardState best = null;
		int bestScore = 0;
		
		
		for (Transformation t : transforms){
			BoardState newState = t.transform(state);
			int score = evaluateStateTransform(newState);
			if (best == null || score > bestScore){
				best = newState;
				bestScore = score;
			}
		}
		
		return best;
		
	}
	private int evaluateStateTransform(BoardState state){
		int p = 1;
		int r = 0;
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				if (!state.isEmpty(i, j)){
					r += p;
				}
				p = p * 2;
			}
		}
		return r;
	}
}
