package players.ai;

import board.BoardState;
import board.Move;
import board.Piece;


public class RecursiveAI extends BaseRecursiveAI{

	final String name =  RecursiveAI.class.getName();
	
	public RecursiveAI(boolean verboseOutput, int maxDepth) {
		super(verboseOutput,maxDepth);
	}
	
	public String getName(){
		return this.name + "(" + maxDepth + ")";
	}

	private double evaluateState(BoardState state, boolean max){
		
		return 0;	
	}
	private double searchRecursive(BoardState state, final Piece place,final boolean max,final int depth){
		counter ++;
		if(state.isQuarto()){
			if (max) return -1;
			else return 1;
		}
		if (state.isDraw())
			return 0;
		if (depth <= 0 || counter > 200000){
			return evaluateState(state,max);
		}
		Move best = null;
		double bestScore = 0;
		for (Move m : BoardState.getAllMoves(state, place)){
			BoardState newState = state.deepCopy();
			newState.placePiece(m.getPieceToPlace(), m.getX(),m.getY());
			newState.pickPiece(m.getPieceToGiveOpponent());
			if(newState.isQuarto()){
				if (max) return 1;
				else return -1;
			}
			if (state.isDraw())
				return 0;
			double score = searchRecursive(newState,m.getPieceToGiveOpponent(),!max,depth-1);
			
			if (max){
				if (best == null || score > bestScore){
					bestScore = score;
					best = m;
				}
			}
			else{
				if (best == null || score < bestScore){
					bestScore = score;
					best = m;
				}
			}
		}
		return bestScore;
	}
	
	@Override
	public Move getNextMove(BoardState state, Piece place) {
		
		//randomized novice move first x moves.
		//int x = 3;
		if(state.getRemainingPieces().size()>=12){
			return randomizer.getNextMove(state, place);
		}
		//If there is no more pieces this is the last move and there is no choice but one...
		if (state.getRemainingPieces().size() == 0){
			for (int [] coord : state.getOpenSlots())
			return new Move(place,null,coord[0],coord[1]);
		}
		
		Move best = null;
		double bestScore = 0;
		for (Move m : BoardState.getAllMoves(state, place)){
			BoardState newState = state.deepCopy();
			newState.placePiece(place, m.getX(),m.getY());
			newState.pickPiece(m.getPieceToGiveOpponent());
			double score = searchRecursive(newState,m.getPieceToGiveOpponent(),false,maxDepth-1);
			if (best == null || score > bestScore){
				bestScore = score;
				best = m;
			}
			//System.out.println(score);
		}
		//System.out.println("Counter " + counter );
		counter = 0;
		return best;
	}
	

}
