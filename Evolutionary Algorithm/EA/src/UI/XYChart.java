package UI;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

import Models.Population;

public class XYChart extends JFrame{
	private static final long serialVersionUID = 1L;
	XYSeries series1;
	XYSeries series2;
	XYSeries series3;
	
	XYSeriesCollection dataset;
	
	
	  public XYChart(String applicationTitle) {
	        super(applicationTitle);
	        // This will create the dataset 
	        this.dataset = new XYSeriesCollection();
	        this.series1 = new XYSeries("Best");
	        this.series2 = new XYSeries("Average");
	        this.series3 = new XYSeries("10 * StdDev");
	        
	  }

	  
	  	public void addDatapoints(int x,double best, double average, double stddev){
	        series1.add(x, best);
	        series2.add(x, average);
	        series3.add(x, stddev);

	  		
	  	}
	  	
	  	public void getPopData(Population p, int x){
	  		addDatapoints(x, p.getBest(), p.getAverage(), p.getStdDev());
	  	}
	  	
	  	public void createGraph(){
	  		 dataset.addSeries(series1);
		     dataset.addSeries(series2);
		     dataset.addSeries(series3);
		     
		     
		     
		     // Generate the graph
		        JFreeChart chart = ChartFactory.createXYLineChart(
		        "XY Chart", // Title
		        "generation", // x-axis Label
		        "fitness", // y-axis Label
		        dataset, // Dataset
		        PlotOrientation.VERTICAL, // Plot Orientation
		        true, // Show Legend
		        true, // Use tooltips
		        false // Configure chart to generate URLs?
		        );
		        
		        XYPlot plot = (XYPlot) chart.getPlot();
			    plot.getRenderer().setSeriesPaint(2, Color.yellow);
		        /*
		        try {
		        	ChartUtilities.saveChartAsJPEG(new File("C:\\chart.jpg"), chart, 500, 300);
		        	} catch (IOException e) {
		        	System.err.println("Problem occurred creating chart.");
		        	}
		        */
		        // we put the chart into a panel
		        ChartPanel chartPanel = new ChartPanel(chart);
		        // default size
		        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		        // add it to our application
		        setContentPane(chartPanel);
		       
	  	}
	  

	
	

}
