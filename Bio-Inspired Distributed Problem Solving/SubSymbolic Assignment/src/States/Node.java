package States;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Node {
	private LinkedList<Node> neighbours = new LinkedList<Node>();
	private LinkedList<Integer> neighboursIDList = new LinkedList<Integer>();
	private int color;
	private int chosenColor;
	private int colorSignal;
	private double x,y;
	private int uniqueID;
	private boolean hasChosenColor;
	


	//private boolean[] availableColors;
	//private boolean[] receivedColors;
	private ArrayList<Integer> availableColors;
	private ArrayList<Integer> receivedColors;
	Random rand;
	
	
	private int signal;
	private int state;
	private int receivedSignal;
	
	public boolean hasConflicts(){
		for (Node n: neighbours){
			if (n.color == color)
				return true;
		}
		return false;
	}
	
	
	
	public void setColors(int i){
		//this.availableColors = new boolean[i];
		//this.receivedColors = new boolean[i];
		this.availableColors = new ArrayList<Integer>();
		this.receivedColors = new ArrayList<Integer>();
		rand = new Random();
		hasChosenColor = false;
		
		for (int j = 0; j < i; j++) {
			availableColors.add(j);	
			receivedColors.add(j);	
		}
	}
	
	
	public int generateColorSignal(){
		int r = -1;
		if(availableColors.size()>0){
			r = availableColors.get(rand.nextInt(availableColors.size()));
		}
		return r;
	}
	
	public void removeSignaledColors(){
		availableColors.removeAll(receivedColors);
	}
	
	public int getChosenColor(){
		return chosenColor;
	}
	
	public void setColorSignal(int i){
		this.colorSignal = i;
	}
	
	public int getSignal(){
		return this.signal;
	}
	
	public void setSignal(int n){
		this.signal = n;
	}
	
	public void resetSignals(){
		receivedSignal=0;
		signal=0;
		
	}
	
	public ArrayList<Integer> getAvailableColors(){
		return this.availableColors;
	}
	
	public ArrayList<Integer> getNeighbourColors(){
		ArrayList<Integer> cols = new ArrayList<Integer>();
		for(Node n : neighbours){
			if(n.hasChosenColor){
				cols.add(n.getChosenColor());
			}
		}
		return cols;
	}
	
	public void undoColorChoice(){
		int tempColor = this.chosenColor;
		this.hasChosenColor = false;
		this.chosenColor = -1;
		this.state = 0;
		for(Node n : neighbours){
			n.addAvailableColor(tempColor);
		}
		
	}
	
	private void addAvailableColor(int i){
		int sum = 0;
		
		for(Node n : neighbours){
			if(n.hasChosenColor)
				if(n.getChosenColor()==i)
					sum++;
		}
		
		if(sum==0)
			this.availableColors.add(i);
	}
	
	public ArrayList<Node> getConflictingNeighbours(int amoutOfColors, int number){
		ArrayList<Node> conflictNeighbours = new ArrayList<>();
		ArrayList<Node> tempNodeList = new ArrayList<>();
		
		for (int i = 0; i < amoutOfColors; i++) {
			for(Node n : neighbours){
				if(n.getHasChosenColor()){
					if(n.getChosenColor()==i){
						tempNodeList.add(n);
					}
				}
			}
			if(tempNodeList.size()==number){
				conflictNeighbours.addAll(tempNodeList);
			}
			tempNodeList.clear();
		}
		
		return conflictNeighbours;
	}
	
	public void receivedColor(int i){
		this.receivedColors.add(i);
	}
	
	public void resetColorSignals(){
		this.receivedColors.clear();
	}
	
	public void resetAllSignals(){
		signal=0;
		state=0;
		receivedSignal=0;
	}
	
	public int getState(){
		return state;
	}
	
	public void sendHasChosenColorSignal(){
		for(Node n : neighbours){
			n.receivedColor(this.colorSignal);
		}
	}
	
	public void sendSignal(int i){
		for(Node n : neighbours){
			n.getSignal(i);
		}
	}
	
	public void getSignal(int i){
		this.receivedSignal = i;
	}
	
	public boolean haveSignal(int i){
		return receivedSignal==i;
	}
	
	public Integer getColorSignal(){
		return this.colorSignal;
	}
	
	public boolean haveColorSignal(int i){
		return this.receivedColors.contains(i);
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public int getColor() {
		return this.color;
	}
	
	private void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Point2D getPosition(){
		return new Point2D.Double(x,y);
	}
	
	public void addNeighbour(final Node n){
		neighbours.add(n);
	}
	
	public int getConflict(int mode){
		int error = 0;
		if(mode==1){
			if(color==1){
				for(Node n : neighbours){
					if(n.getColor()==1){
						error++;
					}
				}
			}else if(color==2){
				boolean found = false;
				for(Node n : neighbours){
					if(n.getColor()==1){
						found= true;
					}
				}
				if(!found)
					error++;
			} else
				error++;
		} else {
			for (Node n: neighbours){
				if (n.color == this.color)
					error++;
			}
			if(this.color<0)
				error++;
		}
		return error;
	}
	
	public LinkedList<Node> getNeighbours(){
		return neighbours;
	}
	
	public Node(int uniqueID, double x, double y, int color) {
		this.uniqueID = uniqueID;
		setPosition( x,y);
		setColor(color);
		this.chosenColor=color;
	}
	
	public int getID(){
		return uniqueID;
	}
	public void updateCoord(double minX, double minY, double maxX, double maxY){
		double diffX = maxX - minX;
		double diffY = maxY - minY;
		
		double scaleX = 900.0 / diffX;
		double scaleY = 900.0 / diffY;
		
		double relX = x - minX;
		double relY = y - minY;
		
		x = 50 + relX * scaleX;
		y = 50 + relY * scaleY;
	}
	public Node(Node r){
		uniqueID = r.uniqueID;
		setPosition( r.x,r.y);
		setColor(r.color);
		for (Node n: r.neighbours){
			neighboursIDList.add(n.uniqueID);
			//neighbours.add(n);
		}
	}
	public Node copy(){
		return new Node(this);
	}
	
	
	public void updateLinks(Node[] newNodeList) {
		LinkedList<Node> newNeighbours = new LinkedList<Node>();
		/*
		for (newNode n: neighbours){
			int index = n.uniqueID;
			if (newNodeList[index].color != n.color){
				System.err.println("Eror");
			}
		}*/
		for (int n: neighboursIDList){
			newNeighbours.add(newNodeList[n]);
		}
		neighbours = newNeighbours;
	}

	public int getNumberofNeighbours() {
		return neighbours.size();
	}

	public void setState(int i) {
		this.state = i;
		
	}

	public void setMyColor(int colorSignal2) {
		this.chosenColor = colorSignal2;
		
	}
	
	public boolean getHasChosenColor() {
		return hasChosenColor;
	}

	public void setHasChosenColor(boolean hasChosenColor) {
		this.hasChosenColor = hasChosenColor;
	}
}

