
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Menu {
	BufferedReader br;
	private boolean runMore;
	
	public Menu(){
		br = new BufferedReader(new InputStreamReader(System.in));
		runMore = true;
	}

	private void mainmenu() {
		System.out.println("Menu:");
		System.out.println("1: Circle-problem");
		System.out.println("2: Knapsack-problem");
		System.out.println("3: Exit");
	}
	
	
	
	public void run() throws IOException{
		while (runMore){
			mainmenu();
			int choice = getIntInput(4);
			
			if(choice==1){//circle
				pl("Cicle problem selected");
				
				pl("Select dimensions:");
				pl("1: 1-dimension");
				pl("2: 2-dimensions");
				int dims = getIntInput(2);
				
				pl("Should inertia weight be enabled:");
				pl("1: Yes");
				pl("2: No");
				boolean inertia = getIntInput(1) == 1 ? true : false;
				
				pl("Enter how many boids to use:");
				int amount = getIntInput(50);
					
				pl("Enter weights for local attraction");
				double localW = getDoubleInput(1.0);
				pl("Enter weights for global attraction");
				double globalW = getDoubleInput(1.0);
				
				pl("Enter amount of neighbours for nearest neighbour topology");
				pl("Enter 0 for a fully connected swarm");
				int nBours = getIntInput(0);
				
				new CircleProblem(0.001 , amount , 1.0 , dims, 1000, nBours, inertia,localW,globalW);
				
				
				
			}else if(choice==2){//knapsack
				pl("Knapsack problem selected");
				
				pl("Should the volume be included?");
				pl("1: Yes");
				pl("2: No");
				
				boolean volume = getIntInput(2) == 1 ? true : false;
				
				pl("Enter how many boids to use:");
				int amount = getIntInput(50);
				
				pl("Enter weights for local attraction");
				double localW = getDoubleInput(1.0);
				pl("Enter weights for global attraction");
				double globalW = getDoubleInput(1.0);
				
				pl("Enter for how many iterations it should run:");
				int runs = getIntInput(1000);
				
				if (volume){
					new KnapsackProblem(10000, amount, runs, true,localW,globalW);
				}else{
					new KnapsackProblem(10000, amount, runs, false,localW,globalW);
				}
				
				
			}else {
				pl("Exiting program");
				System.exit(0);
			}
			
			
		}
	}
	
	private int getIntInput(int defaultNumber) throws IOException{
		String temp = br.readLine();
		 try {
			    return Integer.parseInt(temp);
			  } catch (NumberFormatException e) {
				  pl("You entered "+temp+" wich isnt a valid number");
				  pl("Using default number: "+defaultNumber+" instead.");
			    return defaultNumber;
			  }
	}
	
	private double getDoubleInput(double defaultNumber) throws IOException{
		String temp = br.readLine();
		 try {
			    return Double.parseDouble(temp);
			  } catch (NumberFormatException e) {
				  pl("You entered "+temp+" wich isnt a valid number");
				  pl("Using default number: "+defaultNumber+" instead.");
			    return defaultNumber;
			  }
	}
	
	
	private void pl(String s){
		System.out.println(s);
	}
}
