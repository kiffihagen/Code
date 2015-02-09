package players.ai.solving_quarto.transformations;

import board.BoardState;
import board.Piece;

public class HorisontalFlip extends Transformation{

	@Override
	public BoardState transform(BoardState s) {
		// TODO Auto-generated method stub
		BoardState newState = s.deepCopy();
		Piece [][] board = newState.getRawBoard();
		
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < 4; j++){
				Piece t = board[i][j];
				board[i][j] = board[3-i][j];
				board[3-i][j] = t;
			}
		}
		return newState;
	}

}
