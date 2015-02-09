package players.ai;

import board.BoardState;
import board.Move;
import board.Piece;
import players.BasePlayer;

public abstract class BaseAI implements BasePlayer{
	protected boolean verboseOutput;
	public BaseAI(boolean verboseOutput){
		this.verboseOutput = verboseOutput;
	}

	protected void printMessage(String msg){
		if (verboseOutput) System.out.println(msg);
	}
	abstract public Move getNextMove(BoardState b, Piece p);
}
