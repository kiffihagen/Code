package players.ai.minmax;

import players.ai.BaseRecursiveAI;
import board.BoardState;
import board.Move;
import board.Piece;

public abstract class BaseMinMax extends BaseRecursiveAI {
	

	public BaseMinMax(boolean verboseOutput, int maxDepth) {
		super(verboseOutput, maxDepth);
		// TODO Auto-generated constructor stub
	}
	int MAX_VALUE = 1000000;
	int MIN_VALUE = -1000000;
	int WIN = 1000;
	int LOSS = -1000;
	int DRAW = 0;

	private int searchAlphaBeta(BoardState state, final Piece place,int alpha, int beta,final boolean max,final int depth){
		counter ++;
		
		if (state.isDraw())
			return DRAW;
		if (depth <= 0)
			return eval.evaluate(state,max);
		
		if (max)
			alpha = MIN_VALUE;
		else
			beta = MAX_VALUE;
		for (Move m : BoardState.getAllMoves(state, place)){
			BoardState newState = state.deepCopy();
			newState.forceMove(m);
			if(newState.isQuarto()){
				if (max) return WIN;
				else return LOSS;
			}
			if (state.isDraw())
				return DRAW;
			int score = searchAlphaBeta(newState,m.getPieceToGiveOpponent(),alpha,beta,!max,depth-1);
			
			if (max){
				if (score >= alpha){
					alpha = score;
					if( alpha >= beta)
						return score;
				}
			}
			else{
				if (score <= beta){
					beta = score;
					if (alpha >= beta)
						return score;
				}
			}
		}
		if (max) return alpha;
		else return beta;
	}
	
	@Override
	public Move getNextMove(BoardState state, Piece place) {
		int t = 12 + maxDepth;
		if (t > 15) t = 15;
		if(state.getRemainingPieces().size()>(12+maxDepth)){
			return randomizer.getNextMove(state, place);
		}
		if (state.getRemainingPieces().size() == 0){
			for (int [] coord : state.getOpenSlots())
			return new Move(place,null,coord[0],coord[1]);
		}
		Move best = null;
		int alpha = MIN_VALUE;
		int beta = MAX_VALUE;
		for (Move m : BoardState.getAllMoves(state, place)){
			BoardState newState = state.deepCopy();
			newState.forceMove(m);
			if (newState.isQuarto()) return m;
			
			int score = searchAlphaBeta(newState,m.getPieceToGiveOpponent(),alpha,beta,false,maxDepth-1);
			if (score > alpha){
				alpha = score;
				best = m;
			}
		}
		counter = 0;
		return best;
	}


}
