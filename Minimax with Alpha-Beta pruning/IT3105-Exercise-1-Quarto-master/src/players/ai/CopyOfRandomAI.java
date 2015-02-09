package players.ai;

import java.util.ArrayList;
import java.util.Collections;

import board.BoardState;
import board.Move;
import board.Piece;

public class CopyOfRandomAI extends BaseAI {
	
	final String name = CopyOfRandomAI.class.getName();

	public CopyOfRandomAI(boolean verboseOutput) {
		super(verboseOutput);
	}

	@Override
	public Move getNextMove(BoardState b, Piece place) {
		b.forceRemovePiece(place);
		
		ArrayList<Move> myMoves = BoardState.getAllMoves(b, place);
		
		Collections.shuffle(myMoves);
		
		return myMoves.get(0);
	}
	@Override
	public String getName() {
		return this.name;
	}

}
