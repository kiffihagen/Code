package board;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Piece {
	boolean[] feature = new boolean[4];
	ImageIcon img;
	String name;
	
	public String getName(){
		return name;
	}
	
	public Piece (boolean red, boolean small, boolean round, boolean hollow){
		feature[0] = red;			//red or blue
		feature[1] = small;			//small or large
		feature[2] = round;			//round or square
		feature[3] = hollow;		//hollow or solid
		name = "";
		if (red) name += "R";
		else name += "B";

		if (small) name += "L";
		else name += "S";

		if (round) name += "C";
		else name += "S";
		
		if (hollow) name += "H";
		else name += "N";
		
		img = loadImage(name + ".jpg");
	}
	
	public Piece (String strFeatures){
		if (strFeatures.substring(0, 1).equals("R"))	feature[0] = true;
		else feature[0] = false;
		
		if (strFeatures.substring(1, 2).equals("S"))	feature[1] = true;
		else feature[1] = false;
		
		if (strFeatures.substring(2, 3).equals("C"))	feature[2] = true;
		else feature[2] = false;

		if (strFeatures.substring(3, 4).equals("H"))	feature[3] = true;
		else feature[3] = false;
		
		img = loadImage(strFeatures + ".jpg");
		name = strFeatures;
	}
	public ImageIcon getIcon(){
		return img;
	}
	ImageIcon loadImage(String filename){
		BufferedImage pict = null;
		try {
			pict = ImageIO.read(new File("./Resources/" + filename));
			return new ImageIcon(pict);
		} catch (IOException e) {
			e.printStackTrace();
			return  null;
		}
	}
	
	//unused so far
	public boolean sameFeature(Piece other){
		for (int i = 0; i < 4; i++){
			if (this.feature[i]==other.feature[i]){
				return true;
			}
		}
		return false;
	}
	
	public boolean[] getFeatures(){
		return feature;
	}
	
	public boolean isEqual(Piece other){
		boolean sameFeature = true;
		for (int i = 0; i < 4; i++){
				sameFeature = sameFeature && this.feature[i]==other.feature[i];
		}
		return sameFeature;
	}
	
	
	

}
