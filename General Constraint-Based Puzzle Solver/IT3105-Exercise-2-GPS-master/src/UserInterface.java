import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import LocalSearch.ConstraintBasedLocalSearch;
import LocalSearch.MinConflicts;
import LocalSearch.SimulatedAnnealing;
import StateManagers.EquationManager;
import StateManagers.GraphColorManager;
import StateManagers.KQueensManager;
import StateManagers.LocalStateManager;
import StateManagers.SudokuManager;


public class UserInterface {

	private static BufferedReader br;
	private static boolean debug = false;
	private static boolean isProblem = false;
	private static int numberNeighbours = 50;
	private static int MaxTemprature = 20;
	private static double DeltaTemprature = 0.02;
	private static int maxRuns = 10000;
	
	//(int numberNeighbours,double MaxTemprature, double DeltaTemprature, double targetScore,boolean debug, int maxRuns, boolean linear)

	

	public static void main(String[] args) {
		
		while(true){
			
			String temp;
			br = new BufferedReader(new InputStreamReader(System.in));
		
			printProblemsMenu();
			try {	
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				temp = "2";
			}
			
			LocalStateManager currentManager = findManager(temp);
			
			if(isProblem){
				printSearchMethod();
				
				try {	
					temp = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					temp = "1";
				}
				
				ConstraintBasedLocalSearch currentSearch = findMethod(temp);
				
				howManyRuns();
				int runs = Integer.parseInt(getInput());
				if(runs==1){
					currentSearch.setStateManager(currentManager);
					currentSearch.solve();
					currentManager.getState().display();
				} else if (runs>1 && runs<100){
					StatisticsRunner srunner = new StatisticsRunner(runs);
					srunner.testSolver(currentSearch, currentManager, runs);
				} else {
					System.out.println("An invalid number was entered");
				}
			}
			//System.out.println("Going back to mainmeny");
		}
	
	}
	

	private static ConstraintBasedLocalSearch findMethod(String temp) {
		if(Integer.parseInt(temp)==1)
			if(DeltaTemprature<=0)
				return new SimulatedAnnealing(numberNeighbours,MaxTemprature,0,debug, maxRuns, true);
			else 
				return new SimulatedAnnealing(numberNeighbours,MaxTemprature,DeltaTemprature,0,debug, maxRuns, true);
		else
			return new MinConflicts(debug);
	}
	private static LocalStateManager findManager(String in) {
		if (in=="")
			in="0";
		int temp = Integer.parseInt(in);
		int number = 0;
		String sIn = "0";
		


		switch (temp){
		case 1:
			System.out.println("Difficulty of puzzle: 1: Easy, 2: Medium or 3: Hard.");
			System.out.println("Enter difficulty (1, 2 or 3):");
			
			try {	
				sIn = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isProblem = true;
			
			return new GraphColorManager("graph-color-"+sIn+".txt");
		case 2:
			System.out.println("Enter the size of the board:");
			
			try {	
				sIn = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isProblem = true;
			return new KQueensManager(Integer.parseInt(sIn));
		case 3:
			System.out.println("Difficulty of puzzle: 1: Easy, 2: Medium or 3: Hard.");
			System.out.println("Enter difficulty (1, 2 or 3):");
			
			try {	
				sIn = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isProblem = true;
			return new SudokuManager("sudoku"+sIn+".txt");
		case 4:
			System.out.println("Enter the dimensions of the equation:");
			
			try {	
				sIn = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isProblem = true;
			return new EquationManager(Integer.parseInt(sIn));

		case 5:
			isProblem = false;
			debug = !debug;
			System.out.println("Debugging "+(debug ? "enabled" : "disabled"));
			return null;
		case 6:
			boolean done = false;
			
			while(!done){
				System.out.println("Current parameters are:");
				System.out.println("1: Neighbours generated: 	"+numberNeighbours);
				System.out.println("2: Maximum/Initial temperature: "+MaxTemprature);
				System.out.println("3: Delta temperature: 		" + (DeltaTemprature==0 ? (MaxTemprature/maxRuns) : DeltaTemprature));
				System.out.println("4: Max iterations: 		"+maxRuns);
				System.out.println("5: Exit to main-menu");
				System.out.println("Enter your selection:");
				
				try {	
					sIn = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int selection = Integer.parseInt(sIn);
				
				
				switch(selection){
				case 1:
					System.out.println("Enter new value for neighbours generated:");
					try {	
						sIn = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					numberNeighbours = Integer.parseInt(sIn);
					System.out.println("New value for neighbours generated: "+numberNeighbours);
					break;
				case 2:
					System.out.println("Enter new value for Maximum/Initial temperature:");
					try {	
						sIn = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MaxTemprature = Integer.parseInt(sIn);
					System.out.println("New value for Maximum/Initial temperature: "+MaxTemprature);
					break;
				case 3:
					System.out.println("Enter new value for Delta Temperature:");
					try {	
						sIn = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DeltaTemprature = Double.parseDouble(sIn);
					System.out.println("New value for Maximum/Initial temperature: "+(DeltaTemprature==0 ? (MaxTemprature/maxRuns) : DeltaTemprature));
					break;
				case 4:
					System.out.println("Enter new value for Max iterations:");
					try {	
						sIn = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					maxRuns = Integer.parseInt(sIn);
					System.out.println("New value for Max iterations: "+maxRuns);
					break;
				default:
					done=true;
					break;
					
				}

			}
			isProblem = false;
			return null;
		case 7:
			System.out.println("Exiting GPS");
			System.exit(0);
			
		default:
			System.out.println("No valid choices selected");
			isProblem = false;
			return null;
		}
	}
	
	private static void howManyRuns(){
		System.out.println("How many times do you want to run the simulation?");
		System.out.println("Enter a number:");
	}
	
	private static void printProblemsMenu() {
		System.out.println("");
		System.out.println("");
		System.out.println("Select Puzzle:");
		System.out.println("1: Graphcoloring");
		System.out.println("2: K-Queens");
		System.out.println("3: Sudoku");
		System.out.println("4: Equation");
		System.out.println("");
		System.out.println("5: Toggle Debug-mode on/off");
		System.out.println("6: Modify variables for Simulated Annealing");
		System.out.println("7: Exit program");
		System.out.println("Enter choice:");
		
	}
	
	private static String getInput(){
		String temp = "";
		try {	
			temp = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	
	private static void printSearchMethod(){
		System.out.println("Select Local Search Method:");
		System.out.println("1: Simulated Annealing");
		System.out.println("2: Minimal Conflicts");
		System.out.println("Enter choice:");
	}
}
