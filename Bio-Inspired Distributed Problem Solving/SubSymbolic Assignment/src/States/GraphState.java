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
import java.util.Iterator;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class GraphState {
		private Node nodeList[] = null;
		private int K = 4;
		private final String className = GraphState.class.getName();
		private int mode;

		public Node[] getNodes(){
			return nodeList;
		}
		public void display(){
			new GUI(this, mode);
		}
		
		
		public Node findNodeByID(int id){
			for(Node n : nodeList){
				if(id==n.getID()){
					return n;
				}
			}
			return null;
		}
		
		public void setNodeColor(int id, int color){
			findNodeByID(id).setColor(color);
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
						nodeList[nodeIndex] = new Node(nodeIndex,xcoord,ycoord,-1);
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
			System.out.println("Graph loaded succesfully");
		}
		public GraphState(String filename, int mode) {
			this.mode = mode;
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
		/*
		public int getNumberOfConflicts(int var) {
			if (var >= 0 && var < nodeList.length)
				return nodeList[var].getNumberOfConflicts();
			else{
				throw new IndexOutOfBoundsException(className + "GetValue index out of bounds");
			}
		}
		*/
		
		public int getNumberOfConflicts(){
			int errors = 0;
			for(Node n : nodeList){
				errors += n.getConflict(this.mode);
			}
			return errors;
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
		public int getDegrees() {
			int max = 0;
			for (Node n : nodeList){
				int num = n.getNumberofNeighbours();
				max = Math.max(max, num);
			}
			
			return max;
		}
	}
