package players.ai.solving_quarto.transformations;

import board.BoardState;
import board.Piece;

public class VerticalFlip  extends Transformation{

	@Override
	public BoardState transform(BoardState s) {
		// TODO Auto-generated method stub
		BoardState newState = s.deepCopy();
		Piece [][] board = newState.getRawBoard();
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 2; j++){
				Piece t = board[i][j];
				board[i][j] = board[i][3-j];
				board[i][3-j] = t;
			}
		}
		return newState;
	}

}
