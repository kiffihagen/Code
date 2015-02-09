import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import States.GraphState;
import States.Node;


public class MIS {
	private GraphState g;
	int numberOfNodes, degree, maxRuns;
	double broadcast;
	BufferedReader br;
	String temp;
	ArrayList<Node> Nnodes;
	ArrayList<Node> Lnodes;
	ArrayList<Node> NLnodes;
	ArrayList<Node> toberemoved;
	Random rand;
	
	public MIS(GraphState g, int maxRuns){
		this.g = g;
		this.maxRuns = maxRuns;
		this.degree = g.getDegrees();
		this.numberOfNodes = g.getNodes().length;
		br = new BufferedReader(new InputStreamReader(System.in));
		temp = "";
		Nnodes = new ArrayList<Node>();
		Lnodes = new ArrayList<Node>();
		NLnodes = new ArrayList<Node>();
		toberemoved = new ArrayList<Node>();
		rand = new Random();

		
	}
	
	public void run() throws InterruptedException{
	
		Node[] list = g.getNodes();
		
		for(Node n : list){
			Nnodes.add(n);
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
		
		while(p<1){
			
			for (int i = 0; i < iterations; i++) {
				if(Nnodes.size()>0){
					//Round 1, set states
					resetAllSignals();
					for(Node n : Nnodes){
										
						if(rand.nextDouble()<p){
							//send B if broadcast
							n.setSignal(1);
							n.sendSignal(1);
							n.setState(1);
						}
					}
					//if received B, set state 0
					for(Node n : Nnodes){
						if(n.haveSignal(1)){
							n.setState(0);
						}
					}
					
					//Round 2, organize leaders
					resetSignals();
					for(Node n : Nnodes){
						
						if(n.getState()==1){
							n.setSignal(1);
							n.sendSignal(1);
							
							//add to leaders
							Lnodes.add(n);
							System.out.println("I was added to leaders, I am node# "+n.getID()+" and I have "+n.getNumberofNeighbours()+" neighbours");
							toberemoved.add(n);
							
		
							
						}
					}
					//remove leader from n
					for(Node n : toberemoved){
						Nnodes.remove(n);
					}
					toberemoved.clear();
					
					//add to non leaders
					int numSigs = 0;
					for(Node n : Nnodes){
						if(n.haveSignal(1)){
							numSigs++;
							NLnodes.add(n);
							System.out.println("I was added to non leaders, I am node# "+n.getID());
							toberemoved.add(n);
						}
					}
					System.out.println(numSigs+" har the signal from a leader");
					//remove nonleaders from n
					for(Node n : toberemoved){
						Nnodes.remove(n);
					}
					toberemoved.clear();
					
					System.out.println("Leader nodes = "+Lnodes.size());
					System.out.println("Non leader nodes = "+NLnodes.size());
					System.out.println("Remaining unallocated nodes = "+Nnodes.size());
					//showGraph();
					
				}
			}
			//inkrement probability
			p = 2*p;
			

			

			
		}
		
		updateColors();
		
	}
	
	private void resetSignals(){
		for(Node n : Nnodes){
			n.resetSignals();
		}
	}
	
	private void resetAllSignals(){
		for(Node n : Nnodes){
			n.resetAllSignals();
		}
	}
	
	private void updateColors(){
		for(Node n : Lnodes){
			int id = n.getID();
			g.findNodeByID(id).setColor(1);
		}
		for(Node n : NLnodes){
			int id = n.getID();
			g.findNodeByID(id).setColor(2);
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
