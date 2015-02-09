package players.ai.minmax;

import java.util.HashMap;

import players.ai.RecursiveAI;
import players.ai.minmax.evaluation.CloseToQuarto;
import players.ai.solving_quarto.StateTransfomers;
import board.BoardState;
import board.Move;
import board.Piece;

public class MemoMinMaxAI extends RecursiveAI {

	final String name = MemoMinMaxAI.class.getName();
	HashMap<String, Integer> memo = new HashMap<String,Integer>();
	public  MemoMinMaxAI(boolean verboseOutput, int maxDepth) {
		super(verboseOutput,maxDepth);
		eval = new CloseToQuarto();
	}

	@Override
	public String getName() {
		return this.name + "(" + maxDepth + ")";
	}
	int MAX_VALUE = 1000000;
	int MIN_VALUE = -1000000;
	int WIN = 1000;
	int LOSS = -1000;
	int DRAW = 0;
	StateTransfomers canon = new StateTransfomers();
	

	private int searchAlphaBeta(BoardState state, final Piece place,int alpha, int beta,final boolean max,final int depth){
		counter ++;
		String key = recallState(state, max);
		if (key != null){
				if ( memo.get(key) == null){
					System.out.println("Memo get was null");
					return DRAW;
				}
				else{
					return memo.get(key);
				}
		}
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
						break;
				}
			}
			else{
				if (score <= beta){
					beta = score;
					if (alpha >= beta)
						break;
				}
			}
		}
		if (max){
			rememberState(state, max, alpha);
			return alpha;
		}
		else{
			rememberState(state, max, beta);
			return beta;
		}
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
		resetMemo();
		return best;
	}

	private void resetMemo(){
		memo = new HashMap<String,Integer>();
	}
	private String recallState(BoardState state, boolean max){
		BoardState canonState = canon.canonicalize(state);
		String key = makeKey(canonState,max);
		String r = null;
		if(memo.containsKey(key)){
			if (memo.get(key) != null)
				r = key;
			//System.out.println("I REMEMBER THIS!");
		}
		return r;
	}
	private String makeKey(BoardState state, boolean max){
		
		return state.toStringHash() + " " + max;
		
	}
	private void rememberState(BoardState state,boolean max, int value){

		BoardState canonState = canon.canonicalize(state);
		String key = makeKey(canonState,max);
		if (memo.containsKey(key)){
			System.out.println("Key already exists");
		}
		Integer i = value;
		memo.put(key, i);
		memo.put(key, i);
		memo.put(key, i);
		memo.put(key, i);
		if (memo.get(key) == null){
			System.out.println("Somewhere something went terribly wrong");
			System.out.println(key + " " + value);
		}
	}
	
		


}
