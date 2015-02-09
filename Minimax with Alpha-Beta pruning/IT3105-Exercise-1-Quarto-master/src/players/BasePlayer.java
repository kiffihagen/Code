package players;

import board.BoardState;
import board.Move;
import board.Piece;

public interface BasePlayer {
	public Move getNextMove(BoardState b, Piece p);
	public String getName();
}
