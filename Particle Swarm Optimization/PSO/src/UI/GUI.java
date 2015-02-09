package UI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import Models.Particle;
import Models.Swarm;





public class GUI extends JPanel implements ActionListener{
	Timer timer;
	Swarm swarm;
	Image roost;
	
	
	  public GUI(Swarm swarm) {
		    		    ImageIcon ii = new ImageIcon("./Resources/roost2.jpg");
		    		    //ImageIcon ii = new ImageIcon("./Resources/kake.png");
	        roost = ii.getImage();

		  	this.swarm = swarm;
		  	
	        setFocusable(true);
	        setBackground(Color.WHITE);
	        setDoubleBuffered(true);


	        //redraw every 2ms
	        timer = new Timer(2, this);
	        timer.start();
	    }
	  
	    public void paint(Graphics g) {
	        super.paint(g);
	        Graphics2D g2d = (Graphics2D)g;
	        g2d.drawImage(roost,495,445,this);
	        //g2d.drawImage(roost,450,400,this);

	        
	        List<Particle> boids = swarm.getParticles();


	        for (int i = 0; i < boids.size() ; i++ ) {
	            Particle p = boids.get(i);
	            g2d.drawImage(p.getImage(), ((int) (500+(p.getX()*500))), (int) (450 - p.getY()*450), this);
	        }

	        Toolkit.getDefaultToolkit().sync();
	        g.dispose();
	    }


	@Override
	public void actionPerformed(ActionEvent arg0) {
	        repaint();
		
	}
}
