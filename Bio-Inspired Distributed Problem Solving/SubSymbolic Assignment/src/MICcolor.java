import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import States.GraphState;
import States.Node;


public class MICcolor {
	private GraphState g;
	int numberOfNodes, degree, maxRuns;
	double broadcast;
	int colors;
	BufferedReader br;
	String temp;
	ArrayList<Node> Nodes;
	Random rand;
	double changerate;
	
	
	public MICcolor(GraphState g, int maxRuns, int colors,double changerate){
		this.changerate = changerate;
		this.colors=colors;
		this.g = g;
		this.maxRuns = maxRuns;
		this.degree = g.getDegrees();
		this.numberOfNodes = g.getNodes().length;
		br = new BufferedReader(new InputStreamReader(System.in));
		temp = "";
		Nodes = new ArrayList<Node>();
		rand = new Random();	
	}
	
	public void run(){
		
		Node[] list = g.getNodes();
		
		for(Node n : list){
			n.setColors(colors);
			Nodes.add(n);
		}
		
		double p = 1/(1.0*degree);
		
		double a1 = (1.0*maxRuns);
		double a2 = Math.log(numberOfNodes)/Math.log(2);
		int iterations = (int) Math.ceil(a1*a2);
		System.out.println("Nodenumber: "+numberOfNodes);
		System.out.println("1*maxruns = "+a1);
		System.out.println("Math.log(numberOfNodes)/Math.log(2) = "+a2);
		System.out.println("Iterations = "+iterations);
		System.out.println("Degree = "+degree);
		System.out.println("P = "+p);
		int counter = 0;
		
		while(p<1){
			
			for (int i = 0; i < iterations; i++) {
				
				counter++;
				System.out.println("Round "+counter);
				
				//cleaning stuff, clear received signals and state
				clearReceivedColorSignals(true);
				
				//Part 1a
				for(Node n : Nodes){
					if(!n.getHasChosenColor()){ //if node does not have a color
						if(rand.nextDouble()<p){ //if random number
							//get random color:
							int col = n.generateColorSignal();
							if(col>=0){ //available color
								n.setColorSignal(col);
								n.setState(1);
								n.sendHasChosenColorSignal();
							} else {
								System.out.println("No color found");
								//Message neighbours with already chosen color
								
								ArrayList<Node> nBors = n.getConflictingNeighbours(colors, 1);
								
								
								//If no neighbours have a unique color, look for doubles etc.
								int lookingFor = 2;
								while(nBors.size()==0 && lookingFor<5){
									nBors = n.getConflictingNeighbours(colors, lookingFor);
									lookingFor++;
								}
								
								
								for(Node nb : nBors){
									if(rand.nextDouble()<changerate){
										nb.undoColorChoice();
										System.out.println("Node "+nb.getID()+" undid color choice");
									}
								}
								
								//
							}
											
						}
					}
				}//end of part 1a
				
				//Part 1b
				for(Node n : Nodes){
					if(n.getState()==1 && !n.getHasChosenColor()){
						if(n.haveColorSignal(n.getColorSignal())){
							System.out.println("Picked same color as neighbour, stepping down");
							n.setState(0);
						}
					}
				}//end of part 1b
				
				//part 2a
				clearReceivedColorSignals(false);
				for(Node n : Nodes){
					if(n.getState()==1 && !n.getHasChosenColor()){
						//saving color
						System.out.println("Found a color to use");
						int c = n.getColorSignal();
						
						n.setMyColor(c);
						n.setHasChosenColor(true);
						n.sendHasChosenColorSignal();
						
						ArrayList<Integer> cols = n.getNeighbourColors();
						if(cols.contains(c)){
							System.out.println("SUPERERROR 101     SUPERERROR 101     SUPERERROR 101     SUPERERROR 101     SUPERERROR 101     SUPERERROR 101     SUPERERROR 101");
							System.out.println("Total number of colors = "+colors);
							System.out.println("Neighbour colors = "+n.getNeighbourColors().toString());
							System.out.println("Available colors = "+n.getAvailableColors().toString());
							System.out.println("Chosen color = "+c);
						}
						
					}
				}//end of part 2a
				
				//part 2b
				for(Node n : Nodes){
					n.removeSignaledColors();
				}
				
			//showGraph();	//very frequent
			}//end of iterations
			//showGraph(); //4-5 times
			
			p = 2*p;
		}//end of while	
		updateColors();
		System.out.println("Degrees: "+degree);
	}//end of run
	
	private void clearReceivedColorSignals(boolean state){
		for(Node n : Nodes){
			n.resetColorSignals();
			if(state){
				n.setState(0);
			}
		}
	}
	
	private void updateColors(){
		for(Node n : Nodes){
			int id = n.getID();
			g.findNodeByID(id).setColor(n.getChosenColor());
		}
	}
	
	private void showGraph(){
		updateColors();
		
		g.display();
		
		System.out.println("Press button to continue");
		try {	
			temp = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			temp = "2";
		}
		if(temp.equals("2")){
			System.exit(0);
		}
	}

}
