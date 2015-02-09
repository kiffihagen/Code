package board;


public final class StaticPieces {
	public static final Piece RLSN = new Piece("RLSN");
	public static final Piece RLSH = new Piece("RLSH");
	
	public static final Piece RLCN = new Piece("RLCN");
	public static final Piece RLCH = new Piece("RLCH");
	
	public static final Piece RSSN = new Piece("RSSN");
	public static final Piece RSSH = new Piece("RSSH");
	
	public static final Piece RSCN = new Piece("RSCN");
	public static final Piece RSCH = new Piece("RSCH");

	public static final Piece BLSN = new Piece("BLSN");
	public static final Piece BLSH = new Piece("BLSH");
	
	public static final Piece BLCN = new Piece("BLCN");
	public static final Piece BLCH = new Piece("BLCH");
	
	public static final Piece BSSN = new Piece("BSSN");
	public static final Piece BSSH = new Piece("BSSH");
	
	public static final Piece BSCN = new Piece("BSCN");
	public static final Piece BSCH = new Piece("BSCH");
	
	public static final Piece[] list = {RLSN,RLSH,RLCN,RLCH,RSSN,RSSH,RSCN,RSCH,BLSN,BLSH,BLCN,BLCH,BSSN,BSSH,BSCN,BSCH};
	
	public static final boolean isPiece(String i){
		for (int j = 0; j < list.length;j++){
			if (list[j].getName().equals(j))
				return true;
		}
		return false;
	}
	
}
