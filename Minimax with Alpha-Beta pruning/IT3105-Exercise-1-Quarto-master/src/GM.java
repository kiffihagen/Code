import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import players.BasePlayer;
import players.HumanPlayer;
//import players.ai.AlphaBetaTrainer;
import players.ai.NoviceAI;
import players.ai.RandomAI;
import players.ai.minmax.MinMaxAI;
import board.BoardState;
import board.Move;
import board.Piece;


public class GM implements Runnable {
	private boolean verboseOutput;
	private long delay;
	private BoardState state;
	private BoardGUI g;
	private BasePlayer p1,p2;
	static BufferedReader br;

	
	public int winner = -1;
	public GM(boolean guiEnabled,boolean verboseOutput,long delay,BasePlayer p1, BasePlayer p2){
		
		this.verboseOutput = verboseOutput;
		this.delay = delay;
		if (guiEnabled)
			g = new BoardGUI();
		else
			g = null;
		state = new BoardState();
		if (g != null)  g.updateBoard(state);
		if (p1 == null)
			this.p1 = new RandomAI(verboseOutput);
		else
			this.p1 = p1;
		if (p2 == null)
			this.p2 = new NoviceAI(verboseOutput);
		else
			this.p2 = p2;
	}
	private void printError(String str){
		System.err.println("GameMaster: " + str);
	}
	private static void sleep(long time){

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void printMessage(String msg){
		if (verboseOutput) System.out.println(msg);
	}
	
	public static void printMenu(int number){
		
		
		System.out.println("Select Player " +number+":");
		System.out.println("1: Human");
		System.out.println("2: Random AI");
		System.out.println("3: Novice AI");
		System.out.println("4: MinMax AI");
		System.out.println("Enter choice:");
		

	}
	
	public static void printTournamentMenu(){
		
		
		System.out.println("Select Player:");
		System.out.println("1: Random AI");
		System.out.println("2: Novice AI");
		System.out.println("3: MinMax AI");
		System.out.println("Enter choice:");
		

	}
	
	
	public static BasePlayer findPlayer(String in){
		

		int temp = Integer.parseInt(in);
		switch (temp){
		case 1:
			return new HumanPlayer();
		case 2:
			return new RandomAI(false);
		case 3:
			return new NoviceAI(false);
		case 4:
			System.out.println("Depth for search: 1-5");
			System.out.println("Enter depth:");
			String sIn;
			try {	
				sIn = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sIn = "";
			}
			
			//need exception handling
			return new MinMaxAI(false,Integer.parseInt(sIn));
			
		default:
			return new NoviceAI(false);
		}
	}
	@Override
	public void run(){

		ArrayList<Piece> p = state.getRemainingPieces();
		Piece randomPiece = p.get(0);
		state.pickPiece(randomPiece);
				
		Move p1move  = new Move(null,null,-1,-1);
		Move p2move = new Move(null,randomPiece,-1,-1);
		boolean r  = false;
		
		
		while (!state.isGameOver()){
			printMessage("************* PLAYER 1 TURN *************");
			p1move = p1.getNextMove(state.deepCopy(), p2move.getPieceToGiveOpponent());
			r = state.placePiece(p1move.getPieceToPlace(), p1move.getX(),p1move.getY());
			if (!r){
				printError(" Player 1s placing of the piece was invalid.");
				return;
			}
			r = state.pickPiece(p1move.getPieceToGiveOpponent());
			if (!r){
				printError(" Player 1 picked an invalid piece for the opponent.");
				return;
			}
			if (g != null) g.updateBoard(state);
			printMessage("************* END OF PLAYER 1 TURN *************");
			
			if (state.isGameOver()){
				
				if (state.isQuarto()){
					winner = 1;
					printMessage(p1.getClass().getName()+" won!");
				} else	{
					winner = 0;
					printMessage("Game was a draw. (After first player");
				}
	
					
				break;
			}
			sleep(delay);
			printMessage("************* PLAYER 2 TURN *************");
			p2move = p2.getNextMove(state.deepCopy(), p1move.getPieceToGiveOpponent());
			r = state.placePiece(p2move.getPieceToPlace(), p2move.getX(),p2move.getY());
			if (!r){
				printError(" Player 2s placing of the piece was invalid.");
				return;
			}
			r = state.pickPiece(p2move.getPieceToGiveOpponent());
			if (!r){
				printError(" Player 2 picked an invalid piece for the opponent.");
				return;
			}
			if (g != null) g.updateBoard(state);
			printMessage("************* END OF PLAYER 2 TURN *************");
			
			
			if (state.isGameOver()){
				if (state.isDraw()){
					winner = 0;
					printMessage("Game was a draw.");
				}
				else{
					printMessage(p2.getClass().getName()+" won!");
					winner = 2;
				}
				break;
			}


			sleep(delay);
		}
		if (winner == -1) winner = 0;
		
		
		//if (g != null ) g.cleanup();
	}
	public static void main(String[] args)
    {
		while(true){
		
			BasePlayer player1;
			BasePlayer player2;
			String temp;
			br = new BufferedReader(new InputStreamReader(System.in));
		
			printMenu(1);
			try {	
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				temp = "3";
			}
			player1 = findPlayer(temp);
		
			printMenu(2);
		
			try {	
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				temp = "3";
			}
		
			player2 = findPlayer(temp);


		
		
			GM g = new GM(true,true,1000,player1,player2);
			g.run();
			System.out.println("Do you want to play again?");
			System.out.println("1: Yes");
			System.out.println("2: No");
			
			try {	
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				temp = "2";
			}
			if (Integer.parseInt(temp)!=1){
				System.out.println("Exiting game, thanks for playing");
				System.exit(0);				
			}
			
		
		}
	}
}
