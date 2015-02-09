package StateManagers;

import States.AbstractState;
import States.ChessBoard;

public class KQueensManager extends LocalStateManager {
	public static final String className = KQueensManager.class.getName();
	private int size;
	
	public KQueensManager(int size) {
		super(new ChessBoard(size));
		this.size = size;
	}
	
	
	@Override
	public String getName() {
		return className + " K = " + size;
	}

	@Override
	public LocalStateManager copy() {
		return new KQueensManager(size);
	}

}
