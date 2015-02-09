package States;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class GUI {
	private int mode;
	    public GUI(final GraphState s, int mode) {
	    	this.mode = mode;
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
	            		return Color.GREEN;
	            	case 1:
	            		return Color.RED;
	            	case 2:
	            		return Color.BLUE;
	            	case 3:
	            		return Color.YELLOW;
	            	case 4:
	            		return Color.BLACK;
	            	case 5:
	            		return Color.ORANGE;
	            	case 6:
	            		return Color.PINK;
	            	case 7:
	            		return Color.CYAN;
	            	case 8:
	            		return Color.WHITE;
	            	case 9:
	            		return Color.MAGENTA;
	            	default:
	            		return Color.GRAY;
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
	        
	        
	        if(mode==1){
	            edgeColor = new Transformer<Pair<Node>,Paint>() {
		            @Override
					public Paint transform(Pair<Node> i) {
		            	if (i.getFirst().getColor()==1 && i.getSecond().getColor()==1)
		            		return Color.RED;
		            	else
		            		return Color.BLACK;
		            }
		        };
	        }
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
	        
	        if(mode==1){
	        	   edgeStroke = new Transformer<Pair<Node>, Stroke>() {
	   	            @Override
	   				public Stroke transform(Pair<Node> i) {
	   	               	if (i.getFirst().getColor()==1 && i.getSecond().getColor()==1){
	   	            		return new BasicStroke(3.0f);
	   	            	}
	   	            	else{
	   	            		return new BasicStroke(1.0f);
	   	            	}
	   	            }
	   	        };
	        }
	        
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
