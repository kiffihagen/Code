package players.ai.minmax.evaluation;

import board.BoardState;
import board.Piece;

public class CloseToQuarto extends BaseEvaluator {

	@Override
	public int evaluate(BoardState board, boolean max) {
		Piece[][] checkList = board.getRowsAndColumns();
		int r = 0;
		for (int i = 0 ; i < 10 ; i++){
			int t = super.rowSameFeatureCount(checkList[i]);
			switch (t){
				case 0:
					break;
				case 1:
					r += 0;
					break;
				case 2:
					r += 50;
					break;
				case 3:
					r += 500;
					break;
			}
		}
		
		//r = r + (super.valueOfPositions(board)/160);
		if (r >= 1000) r = 999;
		if (max)
			return r;
		else
			return -r;
	
	}


}
