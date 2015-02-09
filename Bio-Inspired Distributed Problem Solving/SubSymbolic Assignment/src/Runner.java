import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sun.awt.im.SimpleInputMethodWindow;
import States.GraphState;


public class Runner {
	String filename;
	int problem;
	GraphState graph;
	int maxruns;
	int colors;
	double changeChance;
	
	public Runner(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("config.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		}
		String line = null;
		int row = 0;
		
		try {
			while ((line = reader.readLine()) != null) {
				String substrings[] = line.split(" ");
				if (row == 0){
					this.filename = substrings[0];
				} else if (row == 1){
					this.problem = Integer.parseInt(substrings[0]);
				} else if (row == 2){
					this.maxruns = Integer.parseInt(substrings[0]);
				}else if (row == 3){
					this.colors = Integer.parseInt(substrings[0]);
				}else if (row == 4){
					this.changeChance = Double.parseDouble(substrings[0]);
				}
				row++;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void runSim() throws InterruptedException{
		createGraph();
		if(problem==2){
			MICcolor micc = new MICcolor(graph, maxruns, colors, changeChance);
			micc.run();
		} else {
			MIS mis = new MIS(graph, maxruns);
			mis.run();
		}

		printGraph();
		int errors = graph.getNumberOfConflicts();
		System.out.println("Number of errors: "+errors);
		
		
	}
	
	public void printGraph(){
		this.graph.display();
	}
	
	public void printme(){
		System.out.println(filename);
		System.out.println(problem);
		System.out.println(maxruns);
	}
	
	public void createGraph(){
		this.graph = new GraphState(filename,problem);
	}

}
