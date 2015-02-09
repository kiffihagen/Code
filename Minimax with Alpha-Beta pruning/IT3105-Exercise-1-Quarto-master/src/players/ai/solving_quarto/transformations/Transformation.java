package players.ai.solving_quarto.transformations;

import board.BoardState;

public abstract class Transformation {
	public abstract BoardState transform(BoardState s);
}
