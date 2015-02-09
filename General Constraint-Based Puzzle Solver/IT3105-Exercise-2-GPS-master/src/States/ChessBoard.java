package States;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessBoard extends AbstractState  {
	private int[][] queens;
	
	public ChessBoard(int size){
		this.queens = new int[size][4];
		for (int i = 0 ; i < size ; i++){
				queens[i][0] = i;
		}
		for (int k = 0 ; k < size ; k++){
			setRandomRow(k);
		}
	}
	
	public ChessBoard(ChessBoard oldBoard){
		this.queens = new int[oldBoard.getSize()][4];
		for (int i = 0 ; i < oldBoard.getSize() ; i++){
			queens[i][0] = i;
			setQueen(i,oldBoard.getValue(i));
				
		}
	}
	
	private void printQueens(){
		for (int i = 0 ; i < queens.length ; i++){
			System.out.println("Y: " +queens[i][0] + " X: " + queens[i][1] + " Diff " + queens[i][2] + " Sum " + queens[i][3]);
		}
	}
	
	
	private void setQueen(int var, int place){
		queens[var][1] = place;
		queens[var][2] = queens[var][0] - queens[var][1];
		queens[var][3] = queens[var][0] + queens[var][1];
	}
	
	private int getSize(){
		return queens.length;
	}
	
	private int getQueenLoc(int var){
		return queens[var][1];
	}
	
	
	private void setRandomRow(int y){
		int loc = new Random().nextInt(queens.length);
		setQueen(y,loc);
	}
	
	@Override
	public int getNumberOfConflicts(){
		Set<Integer> rowList = new HashSet();
		Set<Integer> diffList = new HashSet();
		Set<Integer> sumList = new HashSet();
		int confs = 0;
		for (int i = 0 ; i < queens.length ; i++){
			if (!rowList.add(queens[i][1]))
				confs++;
			if (!diffList.add(queens[i][2]))
				confs++;
			if (!sumList.add(queens[i][3]))
				confs++;
		}
		return confs;
	}
	
	
	
	private int findConflicts(int var){
		int x = queens[var][1];
		int diff = queens[var][2];
		int sum = queens[var][3];
		int confs = 0;
		for (int i = 0 ; i < queens.length ; i++){
			if(i!=var){
				if(queens[i][1]==x)
					confs++;
				if(queens[i][2]==diff)
					confs++;
				if(queens[i][3]==sum)
					confs++;
			}
		}
		//System.out.println("Found " + confs + " conflicts");
		return confs;
	}
	
	@Override
	public AbstractState copy(){
		return new ChessBoard(this);
	}
	

	@Override
	public LinkedList<Integer> getVars() { 
		LinkedList<Integer> vars = new LinkedList<Integer>();
		for (int i = 0; i < queens.length; i++)
			vars.add(i);
		return vars;
	}

	@Override
	public int getNumberOfConflicts(int var) {
		return findConflicts(var);
	}

	@Override
	public LinkedList<Integer> getPossibleValues() {
		LinkedList<Integer> vars = new LinkedList<Integer>();
		for (int i = 0; i < queens.length; i++)
			vars.add(i);
		return vars;
	}


	@Override
	public int getValue(int var) {
		return getQueenLoc(var);
	}

	@Override
	public void setValue(int var, int value) {
		setQueen(var,value);
	}

	@Override
	public void display() {
		if (getSize() <= 100)
			new GUI(this);	
	}
	
	class GUI{
		private JFrame frame;
		private JPanel boardPanel;
		private BufferedImage queen;
		private BufferedImage notQueen;
		public GUI(final ChessBoard s) {
	    	try {		
	    		queen = ImageIO.read(new File("./Resources/queen.png"));
	    		notQueen = ImageIO.read(new File("./Resources/notqueen.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	drawBoard(s);
	    }
		private void drawBoard(final ChessBoard s){
			frame = new JFrame("K-Queens");
			  
	        GridLayout g = new GridLayout(s.getSize(),s.getSize());
	        g.setHgap(2);
	        g.setVgap(2);
			boardPanel = new JPanel(g);
			JPanel layout = new JPanel(new GridBagLayout());
			layout.add(boardPanel);
			
			frame.add(layout);
			for (int i = 0; i < s.getSize()*s.getSize(); i++)
				boardPanel.add(new JLabel());
			
			updateBoard(s);
	        frame.pack();
	        frame.setVisible(true);
			
		}
		private void updatePiece(boolean isQueen, int x, int y, int size){
			JLabel t = (JLabel) boardPanel.getComponent(x+y*size);
			BufferedImage b;
			
			if (isQueen)
				b = queen;
			else
				b = notQueen;
			
			ImageIcon j = new ImageIcon(b);
			t.setIcon(j);
		}
		
		public void updateBoard(ChessBoard s){
				for (int i = 0; i < s.getSize(); i++)
					for (int j = 0; j < s.getSize();j++)
						updatePiece((getValue(i)==j),i,j,s.getSize());
		}
	}


}
