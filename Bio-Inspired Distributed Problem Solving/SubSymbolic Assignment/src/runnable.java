import java.util.ArrayList;


public class runnable {

	public static void main(String[] args) {
		Runner r = new Runner();
		
		r.printme();
		//r.createGraph();
		//r.printGraph();
		

		
		try {
			r.runSim();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
