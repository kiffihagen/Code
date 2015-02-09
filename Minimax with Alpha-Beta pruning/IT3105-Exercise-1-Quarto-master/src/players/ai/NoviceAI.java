package players.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import board.BoardState;
import board.Move;
import board.Piece;

public class NoviceAI extends BaseAI {
	
	final String name = NoviceAI.class.getName();
	
	public NoviceAI(boolean verboseOutput) {
		super(verboseOutput);
	}
	
	public String getName(){
		return this.name;
	}

	public Move getNextMove(BoardState b, Piece place) {
		
		ArrayList<Piece> remaining = b.getRemainingPieces();
		remaining.remove(place);//just in case

		if (b.getRemainingPieces().size() == 0){
			for (int [] coord : b.getOpenSlots())
			return new Move(place,null,coord[0],coord[1]);
		}
		
			
		Random r = new Random(System.currentTimeMillis());
		
		ArrayList<Move> myMoves = BoardState.getAllMoves(b, place);
		ArrayList<Move> goodMoves = new ArrayList<Move>();
		
		
		
		//checking for winning moves:
		for (Move move : myMoves ){
			BoardState nextBoard = b.deepCopy();
			nextBoard.placePiece(move.getPieceToPlace(), move.getX(), move.getY());
			
				if (nextBoard.isGameOver()){
					printMessage("Novice AI: Placing piece " +move.getPieceToPlace().getName() +" in slot " +move.getX() + " " + move.getY() );
					printMessage("Novice AI: Giving opponent piece: " +move.getPieceToGiveOpponent().getName());
					
					return move;
				} else {
						if (!nextBoard.isWinnablePiece(move.getPieceToGiveOpponent())){
							goodMoves.add(move);
						}
				}
		}
		
		
		
		Move myMove;
		
		if (goodMoves.isEmpty()){
			myMove = myMoves.get(r.nextInt(myMoves.size()));
		} else {
			Collections.shuffle(goodMoves);
			myMove = goodMoves.get(r.nextInt(goodMoves.size()));
		}
		
		
		if(myMove.getPieceToPlace() == null){
			printMessage("Novice AI: No piece to place");
		} else
			printMessage("Novice AI: Placing piece " +myMove.getPieceToPlace().getName() +" in slot " +myMove.getX() + " " + myMove.getY() );
		if(myMove.getPieceToGiveOpponent() == null){
			printMessage("Novice AI: Last move, no piece for opponent");
		} else
			printMessage("Novice AI: Giving opponent piece: " +myMove.getPieceToGiveOpponent().getName());
		
		return myMove;
		

	}

}
