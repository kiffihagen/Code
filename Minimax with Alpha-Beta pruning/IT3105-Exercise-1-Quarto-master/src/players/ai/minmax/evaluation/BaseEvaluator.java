package players.ai.minmax.evaluation;

import java.util.ArrayList;

import board.BoardState;
import board.Piece;

public abstract class BaseEvaluator {
	
	int[][] valueOfPositions = { { 3, 2, 2, 3 }, { 2, 3, 3, 2 }, { 2, 3, 3, 2 }, { 3, 2, 2, 3 } };
	
	public BaseEvaluator(){
		
	}
	public abstract int evaluate(BoardState board, boolean max);
	
	protected int rowSameFeatureCount(Piece[] row){
		int features[] = new int[4];
		for (int k = 0; k < 4; k++)
			features[k] = 0;
        int empty = 0;
		
		for (int j = 0 ; j < 4 ; j++){
			if (row[j] == null) empty++;
			else{
				for (int k = 0; k < 4; k++){
					if (row[j].getFeatures()[k]){
						features[k]++;
					}
				}
			}
		}
		int max = 0;
		for (int k = 0; k < 4; k++){
			if (features[k]> max){
				max = features[k];
			}
			else if(4-features[k]-empty > max){
				max = 4-features[k]-empty;
			}
		}
		return max;
	}
	
	protected ArrayList<Piece> findRowCompleters(BoardState board){
		ArrayList<Piece> pieceList = board.getRemainingPieces();
		ArrayList<Piece> outList = new ArrayList<Piece>();
		ArrayList<Piece> testList = new ArrayList<Piece>();
		
		Piece[][] checkList = board.getRowsAndColumns();
		
		for (int i = 0 ; i < checkList.length ; i++){
			int t = rowSameFeatureCount(checkList[i]);
			if (t==3){
				for (int j = 1 ; j < 4 ; j++){
					if (checkList[i][j]!=null)
						testList.add(checkList[i][j]);
				}
			}
		}
		return outList;
	}
	
	protected int getSameFeatureNumber(Piece p1, Piece p2){
		for (int i = 0 ; i < 4 ; i++){
			if (true==p1.getFeatures()[i]==p1.getFeatures()[i])
				return i;
			
		}
		return 4;
	}
	
	protected ArrayList<Piece> findWinners(BoardState board){
		ArrayList<Piece> pieceList = board.getRemainingPieces();
		ArrayList<Piece> outList = new ArrayList<Piece>();
		
		for (Piece piece : pieceList){
			if (board.isWinnablePiece(piece)){
				outList.add(piece);
			}
		}
		
		return outList;
		
	}
	
	//Adds points if positions that are a part of many winning rows are open. (Should it be taken?)
	protected int valueOfPositions(BoardState b){
		int points = 0;
        for(int x = 0; x <4; x++){
            for(int y =0; y < 4; y++){
                if(b.isEmpty(x, y)){
                	points += valueOfPositions[x][y];
                }
            }
        }
        return points;
	}
	
	
	
	//looks through a row of 4 pieces and compares it with the remaining ones.
	private int rateRow(Piece[] row, ArrayList<Piece> remaining) {
		int rowSize = 0;
		int numberOfMatches = 0;
		int matchingPieces = 0;
		int[] featuresRows = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] featuresRemPieces = { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < row.length; i++) {
			if (row[i] != null) {
				rowSize++;
				//
				if (row[i].getFeatures()[0])
					featuresRows[0]++;
				else
					featuresRows[1]++;
				//
				if (row[i].getFeatures()[1])
					featuresRows[2]++;
				else
					featuresRows[3]++;
				//
				if (row[i].getFeatures()[2])
					featuresRows[4]++;
				else
					featuresRows[5]++;
				//
				if (row[i].getFeatures()[3])
					featuresRows[6]++;
				else
					featuresRows[7]++;
				//
			}
		}

		for (Piece piece : remaining) {
				if (piece.getFeatures()[0])
					featuresRemPieces[0]++;
				else
					featuresRemPieces[1]++;
				
				if (piece.getFeatures()[1])
					featuresRemPieces[2]++;
				else
					featuresRemPieces[3]++;
				
				if (piece.getFeatures()[2])
					featuresRemPieces[4]++;
				else
					featuresRemPieces[5]++;
				
				if (piece.getFeatures()[3])
					featuresRemPieces[6]++;
				else
					featuresRemPieces[7]++;	
		}

		for (int i = 0; i < featuresRemPieces.length; i++) {
			
				if (rowSize == 3 && featuresRows[i] == 3 && featuresRemPieces[i] == remaining.size()){
					return 0;
				} else if (rowSize == featuresRows[i]){
				numberOfMatches++;
				matchingPieces += featuresRemPieces[i];
				}
		}
		
		if (rowSize==4 && numberOfMatches == 1 )
			return 1000;

		if (rowSize == 0)
			return 0;
		//
		if (rowSize == 1){
			return 1;

		} else if (rowSize == 2) {
			if (numberOfMatches == 1)
				//return 10;
				return 10+matchingPieces;
				//return matchingPieces;
			else if (numberOfMatches == 2)
				//return 20;
				return 20+2*matchingPieces;
				//return 4*matchingPieces;
			else if (numberOfMatches == 3)
				//return 30;
				return 30+4*matchingPieces;
				//return 8*matchingPieces;
			else
				return -1;
		} else if (rowSize == 3) {
			//here we can/much tweak values
			if (numberOfMatches == 1)
				//return 50;
				return 100+2*matchingPieces;
				//return 10*matchingPieces;
			else if (numberOfMatches == 2)
				//return 100;
				return 200+4*matchingPieces;
				//return 20*matchingPieces;
			else
				return 0;
		} else
			return 0;
	}
	
	protected int getBoardValue(BoardState bs) {
		ArrayList<Piece> remaining = bs.getRemainingPieces();
		Piece[][] rows = bs.getRowsAndColumns();
		int totalValue = 0;


		for (int i = 0; i < 10; i++) {
			int val = rateRow(rows[i], remaining);
			totalValue += val;
		}

		return totalValue;
	}

}
