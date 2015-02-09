package board;
import java.util.ArrayList;


public class BoardState {
	private boolean debug = false;
	
	private Piece[][] board = new Piece[4][4];
	private ArrayList<Piece> remainingPieces;
	private Piece currentPiece = null;
	// add something with players.
	
	public BoardState(){
		//sets up empty board and adds all pieces to unused list
		for (int i = 0 ; i < 4 ; i++){
			for (int j = 0 ; j < 4 ; j++){
				board[i][j] = null;
			}
		}
		remainingPieces = new ArrayList<Piece>();

		remainingPieces.add(StaticPieces.RLCH);
		remainingPieces.add(StaticPieces.RLCN);
		
		remainingPieces.add(StaticPieces.RSCH);
		remainingPieces.add(StaticPieces.RSCN);

		remainingPieces.add(StaticPieces.RLSH);
		remainingPieces.add(StaticPieces.RLSN);
		
		remainingPieces.add(StaticPieces.RSSH);
		remainingPieces.add(StaticPieces.RSSN);

		remainingPieces.add(StaticPieces.BLCH);
		remainingPieces.add(StaticPieces.BLCN);
		
		remainingPieces.add(StaticPieces.BSCH);
		remainingPieces.add(StaticPieces.BSCN);

		remainingPieces.add(StaticPieces.BLSH);
		remainingPieces.add(StaticPieces.BLSN);
		
		remainingPieces.add(StaticPieces.BSSH);
		remainingPieces.add(StaticPieces.BSSN);
		
		
	}
	
	public BoardState(BoardState old) {
		//sets up empty board and adds all pieces to unused list
		for (int i = 0 ; i < 4 ; i++){
			for (int j = 0 ; j < 4 ; j++){
				board[i][j] = old.board[i][j];
			}
		}
		remainingPieces = new ArrayList<Piece>();
		
		for (int i = 0; i < old.remainingPieces.size(); i++){
			remainingPieces.add(old.remainingPieces.get(i));
		}
	}
	
	public int getTurnNumber(){
		return 16-remainingPieces.size();
	}

	public Piece getPiece(int x, int y){
		return board[x][y];
	}
	public boolean hasPiece(int x, int y){
		return !isEmpty(x,y);
	}
	public boolean isEmpty(int x, int y){
		return board[x][y] == null;
	}
	
	
	//returns a 10-by-4 list of all the 10 possible rows to win
	public Piece[][] getRowsAndColumns(){
		Piece[][] returnList = new Piece[10][4];
		//
		for (int x = 0 ; x < 4 ; x++){
			for (int y = 0 ; y < 4 ; y++){
				//Cols (nedover)
				returnList[x][y] = board[y][x];
				
				//Rows: (bortover)
				returnList[4+x][y] = board[x][y];
				
				
				//Diags
				if (x==y){
					returnList[8][x] = board[x][x];
					returnList[9][x] = board[x][3-x];
				}
			}
		}	
		return returnList;
	}
	
    
    public static ArrayList<Move> getAllMoves(BoardState b, Piece myPiece) {
        ArrayList<Move> moves = new ArrayList<Move>();
    	//Special case for last move when all pieces are taken but there is still the move to place the last piece on the board
    	if (b.getRemainingPieces().size() == 0){
    		for (int [] coord : b.getOpenSlots()){

            	moves.add(new Move(myPiece,null, coord[0], coord[1]));  
    		}
    	}
    	else{
    		for (int [] coord : b.getOpenSlots())
                for(Piece p : b.getRemainingPieces())
                    moves.add(new Move(myPiece, p, coord[0], coord[1]));  
    	}
        return moves;
    }
	
    
	public ArrayList<int[]> getOpenSlots(){
		 ArrayList<int[]> openSlots = new ArrayList<int[]>();
	        for (int i = 0; i < 4; i++) {
	            for (int j = 0; j < 4; j++) {
	                if (isEmpty(i, j)) {
	                    int[] coords = { i, j };
	                    openSlots.add(coords);
	                }
	            }
	        }
	        return openSlots;
	}
	
	
	//checks winning conditions or a draw (all pieces used)
	public boolean isGameOver(){
		if ( getOpenSlots().isEmpty() ){
			return true;
		}
		
		if (isQuarto())
			return true;
		
		return false;
	}
	
	public boolean isDraw(){
		return getOpenSlots().isEmpty() && !isQuarto();
	}
	
	public boolean isQuarto(){
		Piece[][] checkList = getRowsAndColumns();
		
		for (int i = 0 ; i < 10 ; i++){
			if (compareRow(checkList[i])) {
				printError("Winning row:" + i);
				return true;
			}
		}
		return false;
	}
	
	
	//check if a row of pieces has atleast one feature in common
	public boolean compareRow(Piece[] row){
		
        boolean color = true;
        boolean size = true;
        boolean squareness = true;
        boolean structure = true;
		
		if (row == null) {
			return false;
		}
		for (int i = 0 ; i < row.length ; i++){
			if (row[i] == null){
				return false;
			}
			color = color && 			(row[0].getFeatures()[0] == row[i].getFeatures()[0]);
			size = size && 				(row[0].getFeatures()[1] == row[i].getFeatures()[1]);
			squareness = squareness && 	(row[0].getFeatures()[2] == row[i].getFeatures()[2]);
			structure = structure && 	(row[0].getFeatures()[3] == row[i].getFeatures()[3]);		
		}
		printError(color + " " + size + " " + squareness + " " + structure);
		return color || size || squareness || structure;
	
	}
	
	
	void printError(String error){
		if (debug)
			System.err.println(BoardState.class.getName()+ ": " + error);
	}
	//should probably be rewritten to a void and handle exceptions elsewhere.
	public boolean placePiece(Piece piece, int x, int y){
		if (x < 0 || x > 3 || y < 0 || y > 3 ){			//making sure coords are ok
			printError("Coordinates out of bounds when placing piece");
			return false;
			
		} else if (!isEmpty(x,y)) {	//making sure slot is empty
			printError("Slot not empty when placing piece");
			return false;
		} else if (currentPiece != null && !currentPiece.equals(piece) )	{		//making sure the chosen piece is being used
			printError("Player tried to use a different piece than the one supplied");
			return false;
		} else {
			if (piece == null){

				printError("Player tried to place a null piece");
				
				return false;
			}
			board[x][y] = piece;
			return true;
		}
	}
	
	public boolean isWinnablePiece(Piece p){	
		for (int[] coord : this.getOpenSlots()){
			BoardState testBoard = this.deepCopy();
			testBoard.forcePlace(p, coord[0], coord[1]);
			if (testBoard.isGameOver()){
				return true;
			}
		}
		return false;
	}
	
	public void forcePlace(Piece piece, int x, int y){
				board[x][y] = piece;
	}
	
	public void forceUsePiece(Piece piece, int x, int y){
		forcePlace(piece,  x,  y);
		String name = piece.getName();
		Piece tempPiece = getPiece(name);
		this.remainingPieces.remove(tempPiece);
	}
	
	public void forceMove(Move move){
		forceRemovePiece(move.getPieceToGiveOpponent());
		if (move.getPieceToPlace()==null){
			forceUsePiece(null,move.getX(),move.getY());	
		}	else
			forceUsePiece(move.getPieceToPlace(),move.getX(),move.getY());	
	}

	public void forceRemovePiece(Piece piece){
		if(!remainingPieces.isEmpty()){
			//System.out.println("Removing piece: "+piece.getName());
			this.remainingPieces.remove(piece);
		}
	}
	public boolean pickPiece(Piece piece){
		if (remainingPieces.isEmpty()) {
			return true;
		}
		if (remainingPieces.contains(piece)){
			currentPiece = piece;
			remainingPieces.remove(piece);
			return true;
		}
		return false;
	}
	
	public ArrayList<Piece> getRemainingPieces(){
		//think a shallow copy is sufficient here
		ArrayList<Piece> remList = new ArrayList<Piece>(remainingPieces);	
		return remList;
	}
	
	public Piece getPiece(String s){
		for (Piece piece : remainingPieces){
			if (piece.getName().equals(s))
				return piece;
			
		}
		return null;
	}
	
	public void printRemainingPieces(){
		//think a shallow copy is sufficient here
		for (Piece piece : remainingPieces)
			System.out.println(piece.getName());
		
	}
	
	public Piece[][] getBoard(){
            Piece[][] boardCopy = new Piece[board.length][board[0].length];
            for(int i = 0; i < board.length; i++) {
                    for(int j = 0; j < board[i].length; j++) {
                    	boardCopy[i][j] = board[i][j];
                    }
            }
            return boardCopy;
	}
	
	
	public BoardState deepCopy(){
		BoardState r = new BoardState(this);
		return r;
		
	}
	public String toStringHash(){
		StringBuilder ss = new StringBuilder();
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				if (!isEmpty(i, j))
				ss.append(board[i][j].getName() + " ");
				else 
				ss.append("null ");
			}
		}
		if (currentPiece == null){
			ss.append("null ");
		}
		else
			ss.append(currentPiece.getName());
		return ss.toString();
		
		
		
	}
	public void printBoard(){
		for (int i = 0; i < 4; i++){

			for (int j = 0; j < 4; j++)
				if (isEmpty(i,j)){
					System.out.print("null ");
				}
				else{
				System.out.print(board[i][j].name + " ");
				}
			System.out.println();
		}
	
		
	}
	
	public Piece[][] getRawBoard(){
		return board;
	}
	

}
