package players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import board.BoardState;
import board.Move;
import board.Piece;


public class HumanPlayer implements BasePlayer{
	
	String name = "HumanPlayer";
	@Override
	public Move getNextMove(BoardState state, Piece place) {
		
		// TODO Auto-generated method stub
		ArrayList<Piece> remaining = state.getRemainingPieces();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		if (place == null){
			System.out.println("Pick a piece to place amongst the following:");

			for (int i = 0; i < remaining.size();i++)
				System.out.print(remaining.get(i).getName() + " ");
			
			String pstr;
			try {
				pstr = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pstr = "";
			}
			for (int i = 0; i < remaining.size();i++){
				
				if (remaining.get(i).getName().equals(pstr)){
					place = remaining.get(i);
					remaining.remove(place);
				}
			}
		}

		int x = -1, y = -1;
		while ((x == -1 || y == -1) || !state.isEmpty(x,y)){
			x = -1; 
			y = -1;
			System.out.println("Please place piece: " + place.getName());
	
			while (x == -1){
	
				System.out.print("X-coordinate (0-indexed): ");
				String xstr;
				try {
					xstr = in.readLine();
					x = Integer.parseInt(xstr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					xstr = "";
				}
			}
			
			while (y == -1){
				System.out.print("Y-coordinate (0-indexed): ");
				String ystr;
				try {
					ystr = in.readLine();
					y = Integer.parseInt(ystr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ystr = "";
				}
			}
		}
		
		System.out.println("Remaining pieces are:");
		for (int i = 0; i < remaining.size();i++)
			System.out.print(remaining.get(i).getName() + " ");
		System.out.println();
		
		Piece give = null;
		while(give == null){

			System.out.println("Which piece do you wish to give your opponent?");
			String pstr;
			try {
				pstr = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pstr = "";
			}
			for (int i = 0; i < remaining.size();i++){
				
				if (remaining.get(i).getName().equals(pstr)){
					give = remaining.get(i);
				}
			}
			
		}
		

		//return new Move(StaticPieces.RLCH,StaticPieces.RLSH,0,0);
		
		return new Move(place,give,x,y);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
