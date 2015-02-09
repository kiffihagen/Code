package players.ai;

import java.util.ArrayList;
import java.util.Random;

import board.BoardState;
import board.Move;
import board.Piece;

public class RandomAI extends BaseAI {
	
	final String name = RandomAI.class.getName();

	public RandomAI(boolean verboseOutput) {
		super(verboseOutput);
	}

	@Override
	public Move getNextMove(BoardState b, Piece place) {
		ArrayList<Piece> remaining = b.getRemainingPieces();
		remaining.remove(place);
		
		Random r = new Random(System.currentTimeMillis());
		
		if (place == null){
			printMessage(BaseAI.class.getName() + " First round picking a piece");
			place = remaining.get(r.nextInt(remaining.size()));
			remaining.remove(place);
		}
		//If there is no more pieces this is the last move and there is no choice but one...
		if (b.getRemainingPieces().size() == 0){
			for (int [] coord : b.getOpenSlots())
			return new Move(place,null,coord[0],coord[1]);
		}
		

		printMessage(BaseAI.class.getName() + " Placing the piece");
		int x = r.nextInt(4);
		int y = r.nextInt(4);
		while(!b.isEmpty(x, y)){
			printMessage(BaseAI.class.getName() + " (" + x + "," + y + ") was taken, trying again");
			x = r.nextInt(4);
			y = r.nextInt(4);
		}
		

		printMessage(BaseAI.class.getName() + " Picking a piece for the opponent");
		Piece give = remaining.get(r.nextInt(remaining.size()));
		
		printMessage(b.isEmpty(x, y)+ "");
		printMessage(BaseAI.class.getName() + ": Piece " + place.getName() + " was placed at (" + x + "," + y + "). Chose " + give.getName() + " for the opponent.");
		return new Move(place,give,x,y);
	}
	@Override
	public String getName() {
		return this.name;
	}

}
