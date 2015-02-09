package States;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Sudoku extends AbstractState{
	private int[][] board;
	private int squares;
	private int size;
	LinkedList<Integer> variables;
	
	public Sudoku(String filename) {
		variables = new LinkedList<Integer>();
		loadBoard(filename);
		randomizeVariables();
	}
	
	public Sudoku(Sudoku oldSudoku){
		this.size = oldSudoku.size;
		this.board = new int[size][size];
		this.squares = oldSudoku.squares;
		variables = oldSudoku.getVars();
		for (int i = 0 ; i < size*size ; i++){
			setValue(i, oldSudoku.getValue(i));
		}
	}
	
	private void printList(LinkedList<Integer> list){
		for (int i : list){
			System.out.print(i+" ");
		}
	}
	
	private void printList(int[] list){
		for (int i : list){
			System.out.print(i+" ");
		}
	}
	
	private void loadBoard(String filename){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		}
		String line = null;
		
		try {
			
			int row = 0;
			
			while ((line = reader.readLine()) != null) {
				String substrings[] = line.split(" ");
				if (row == 0){
					this.size = Integer.parseInt(substrings[0]);
					this.board = new int[this.size][this.size];							
				}
				else if (row == 1){
					this.squares = Integer.parseInt(substrings[0]);
				}
				else{
					for (int i = 0; i < board.length; i++){
						board[row-2][i]=Integer.parseInt(substrings[i]);
					}
				}			
				row++;
			}
		} catch (IOException e) {
			System.err.println("Reading from file failed");
		}
		
		reserveVariables();
	}
	private void reserveVariables(){
		for (int var = 0; var < size*size; var++){
			if(getValue(var)==0)
				variables.add(var);
		}
	}
	
	private int getRandomValue(){
		int random = new Random().nextInt(size);
		return random + 1;
	}
	
	private void randomizeVariables(){
		for (int x : variables){
			setValue(x, getRandomValue() );
			
		}
		//System.out.println("Initial randomized board:");
		//display();
		//System.out.println("");
	}
	
	private int[] returnSquare(int i, int j){
		//size = 9;
		//squares = 3;
		
		int[] returnList = new int[size];
		
		int xSquare = i/squares; //(i-i%squares)/squares;
		int ySquare = j/squares; //(j-j%squares)/squares;
		
		for (int x = 0; x < squares; x++){
			for (int y = 0; y < squares; y++){
				returnList[x*squares+y] = board[squares*xSquare+x][squares*ySquare+y];
			}
		}
		return returnList;
	}
	
	private int[] returnRow(int i){
		int[] returnList = new int[size];
		for (int y = 0; y < size; y++){
			returnList[y] = board[i][y];
		}
		return returnList;
	}
	
	private int[] returnColumn(int j){
		int[] returnList = new int[size];
		for (int x = 0; x < size; x++){
			returnList[x] =  board[x][j];
		}
		return returnList;
	}
	
	private int[] convVarToCoord(int var){
		int[] list = new int[2];
		list[0] = var/size;
		list[1] = var%size;
		return list;
	}
	

	@Override
	public LinkedList<Integer> getVars() {
		return (LinkedList<Integer>) variables.clone();
	}

	@Override
	public int getNumberOfConflicts(int var) {
		int[] coord = convVarToCoord(var);
		int[] row = returnRow(coord[0]);
		int[] column = returnColumn(coord[1]);
		int[] square = returnSquare(coord[0], coord[1]);
		int varValue = getValue(var);
		int conflicts = -3;

		for (int k : square){
			if(varValue==k){
				conflicts++;
			}
		}
		for (int j : row){
			if(varValue==j){
				conflicts++;
			}
		}
		for (int i : column){
			if(varValue==i){
				conflicts++;
			}
		}

		return conflicts;
	}

	@Override
	public LinkedList<Integer> getPossibleValues() {
		LinkedList<Integer> values = new LinkedList<Integer>();
		for (int i = 1; i <= size; i++){
			values.add(i);
		}
		return values;
	}


	
	@Override
	public int getValue(int var) {
		int[] vars = convVarToCoord(var);
		return board[vars[0]][vars[1]];
	}

	@Override
	public void setValue(int var, int value) {
		int[] coord = convVarToCoord(var);
		board[coord[0]][coord[1]] = value;
	}

	@Override
	public void display() {
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				System.out.print(board[i][j]+" ");		
			}
			System.out.println();
		}
		System.out.println("Confl. per var: " + (float)getNumberOfConflicts()/variables.size());
	}

	@Override
	public AbstractState copy() {
		return new Sudoku(this);
	}


}
