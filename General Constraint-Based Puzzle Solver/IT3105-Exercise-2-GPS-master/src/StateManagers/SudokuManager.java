package StateManagers;

import States.Sudoku;

public class SudokuManager extends LocalStateManager{
	public static final String className = SudokuManager.class.getName();
	private String sudokufile;

	
	public SudokuManager(String soduFile) {
		super(new Sudoku(soduFile));
		this.sudokufile = soduFile;
	}

	@Override
	public String getName() {
		return className + " " + sudokufile;
	}

	@Override
	public LocalStateManager copy() {
		return new SudokuManager(sudokufile);
	}

}
