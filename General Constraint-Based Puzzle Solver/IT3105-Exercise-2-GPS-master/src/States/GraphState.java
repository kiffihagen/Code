package States;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;


class Node{
	private LinkedList<Node> neighbours = new LinkedList<Node>();
	private LinkedList<Integer> neighboursIDList = new LinkedList<Integer>();
	private int color;
	private double x,y;
	private int uniqueID;
	public boolean hasConflicts(){
		for (Node n: neighbours){
			if (n.color == color)
				return true;
		}
		return false;
	}
	public int getNumberOfConflicts(){
		int conflicts = 0;
		for (Node n: neighbours){
			if (n.color == color)
				conflicts++;
		}
		return conflicts;
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
	public LinkedList<Node> getNeighbours(){
		return neighbours;
	}
	public Node(int uniqueID, double x, double y, int color) {
		this.uniqueID = uniqueID;
		setPosition( x,y);
		setColor(color);
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
	public void updateLinks(Node[] nodeList) {
		LinkedList<Node> newNeighbours = new LinkedList<Node>();
		/*
		for (Node n: neighbours){
			int index = n.uniqueID;
			if (nodeList[index].color != n.color){
				System.err.println("Eror");
			}
		}*/
		for (int n: neighboursIDList){
			newNeighbours.add(nodeList[n]);
		}
		neighbours = newNeighbours;
	}
}
class GUI{
    public GUI(final GraphState s) {
    	drawGraph(s);
    	
    }
    private void drawGraph(final GraphState s){
        Graph<Node,  Pair<Node>> g = new SparseGraph<Node, Pair<Node>>();
        Node []nodes = s.getNodes();
        for (int i = 0; i < nodes.length; i++){
          	 g.addVertex(nodes[i]);
        }
        for (int i = 0; i < nodes.length; i++){
        	LinkedList<Node> neighbours = nodes[i].getNeighbours();
          	 for (int j = 0; j < neighbours.size();j++)
          	 {
                 g.addEdge(new Pair<Node>(nodes[i], neighbours.get(j)), nodes[i],neighbours.get(j));
          	 }
        }


        Transformer<Node, Point2D> locationTransformer = 
		                new Transformer<Node, Point2D>() {
			@Override
			public Point2D transform(Node n) {
				Point2D p = n.getPosition();
				return p;
			}
		};  
		
        Layout<Node,  Pair<Node>> layout = new StaticLayout<Node,  Pair<Node>>(g,locationTransformer);
        
        BasicVisualizationServer<Node, Pair<Node>> vv = new BasicVisualizationServer<Node, Pair<Node>>(layout);
        vv.setPreferredSize(new Dimension(1000,1000)); 

        Transformer<Node,Paint> vertexColor = new Transformer<Node,Paint>() {
            @Override
			public Paint transform(Node i) {
            	switch (i.getColor()){
            	case 0:
            		return Color.BLUE;
            	case 1:
            		return Color.GREEN;
            	case 2:
            		return Color.RED;
            	case 3:
            		return Color.YELLOW;
            	default:
            		return Color.BLACK;
            	}
            }
        };
        
        Transformer<Pair<Node>,Paint> edgeColor = new Transformer<Pair<Node>,Paint>() {
            @Override
			public Paint transform(Pair<Node> i) {
            	if (i.getFirst().getColor() == i.getSecond().getColor())
            		return Color.RED;
            	else
            		return Color.BLACK;
            }
        };
        
        Transformer<Pair<Node>, Stroke> edgeStroke = new Transformer<Pair<Node>, Stroke>() {
            @Override
			public Stroke transform(Pair<Node> i) {
               	if (i.getFirst().getColor() == i.getSecond().getColor()){
            		return new BasicStroke(3.0f);
            	}
            	else{
            		return new BasicStroke(1.0f);
            	}
            }
        };
        
        Transformer<Node,Shape> vertexSize = new Transformer<Node,Shape>(){
            @Override
			public Shape transform(Node i){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                return circle;
            }
        };
        
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStroke);

        JFrame frame = new JFrame("Graph view");
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);    
    }

}
public class GraphState extends AbstractState{
	private Node nodeList[] = null;
	private int K = 4;
	private final String className = GraphState.class.getName();

	public Node[] getNodes(){
		return nodeList;
	}
	@Override
	public void display(){
		new GUI(this);
	}
	private void loadGraph(String filename){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		}
		String line = null;
		try {
			int row = 0;
			int nodeCount = 0;
			int edgeCount = 0;
			
			double minX = 1000, minY = 1000, maxX = -1000, maxY = -1000;
			
			while ((line = reader.readLine()) != null) {
				String substrings[] = line.split(" ");
				if (row == 0){
					nodeCount = Integer.parseInt(substrings[0]);
					edgeCount = Integer.parseInt(substrings[1]);
					nodeList = new Node[nodeCount];
				}
				else if (row > 0 && row <= nodeCount){
					int nodeIndex = Integer.parseInt(substrings[0]);
					double xcoord = Double.parseDouble(substrings[1]);
					double ycoord = Double.parseDouble(substrings[2]);
					if (xcoord > maxX){
						maxX = xcoord;
					}
					if (ycoord > maxY){
						maxY = ycoord;
					}
					if (xcoord < minX){
						minX = xcoord;
					}
					if (ycoord < minY){
						minY = ycoord;
					}
					nodeList[nodeIndex] = new Node(nodeIndex,xcoord,ycoord,new Random().nextInt(K));
				}
				else{
					int nodeIndex1 = Integer.parseInt(substrings[0]);
					int nodeIndex2 = Integer.parseInt(substrings[1]);
					Node n1 = nodeList[nodeIndex1];
					Node n2 = nodeList[nodeIndex2];
					n1.addNeighbour(n2);
					n2.addNeighbour(n1);
				}
				
				row++;
			}
			for (Node n: nodeList){
				n.updateCoord(minX,minY,maxX,maxY);
			}
		} catch (IOException e) {
			System.err.println("Reading from graph file failed");
		}
	}
	public GraphState(String filename) {
		loadGraph(filename);
	}
	public GraphState(GraphState old){
		nodeList = new Node[old.nodeList.length];
		for (int i = 0; i < old.nodeList.length; i++){
			nodeList[i] = old.nodeList[i].copy();
		}
		for (int i = 0; i < old.nodeList.length; i++){
			nodeList[i].updateLinks(nodeList);
		}
	}
	
	public LinkedList<Integer> getVars() {
		LinkedList<Integer> vars = new LinkedList<Integer>();
		for (int i = 0; i < nodeList.length; i++)
			vars.add(i);
		return vars;
	}

	public int getNumberOfConflicts(int var) {
		if (var >= 0 && var < nodeList.length)
			return nodeList[var].getNumberOfConflicts();
		else{
			throw new IndexOutOfBoundsException(className + "GetValue index out of bounds");
		}
	}

	public LinkedList<Integer> getPossibleValues() {
		LinkedList<Integer> values = new LinkedList<Integer>();
		for (int i = 0; i < K; i++)
			values.add(i);
		return values;
	}

	public int getValue(int var) {
		if (var >= 0 && var < nodeList.length)
			return 0;
		else{
			throw new IndexOutOfBoundsException(className + "GetValue index out of bounds");
		}
	}
	
	public void setValue(int var, int value) {
		if (var >= 0 && var < nodeList.length)
			nodeList[var].setColor(value);
		else{
			throw new IndexOutOfBoundsException(className + "SetValue index out of bounds");
		}
	}
	
	public AbstractState copy() {
		return new GraphState(this);
	}
	
}
