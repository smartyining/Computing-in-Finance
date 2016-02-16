package mySolution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**This class draw 2D points to a panel frame and save to local file 
 * 
 * @author yininggao
 *
 */

public class DrawPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	protected List<Point> _points;  //store points
	protected static int _size=100;       //100 intervals
	protected static int MULTIPLIER=10;  //adjust image display size
	protected static int DOT_SIZE=10;    //set point size
	
	/**constructor*/
	public DrawPanel(){
		super();
	}
	
	/** This method loads point data
	 * 
	 * @param list
	 */
	public void addPoints(List<Point> list){
		this._points=list;
	}
	
	
    public void drawPoints(String s){  	
		final JFrame frame= new JFrame(s);    // create a new Jframe			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //	                                                                  // when it is closed
		frame.add(this);           
	    frame.setSize(_size*MULTIPLIER,_size*MULTIPLIER);         // window is 1000 pixels wide, 1000 high
	    frame.setVisible(true); 				 
    }
    
    /** Display in panel and save image
     *  Graphics ig is displayed  to panel
     *  Graphics ig2 is wrote to result.pny
     */
	public void paintComponent(Graphics ig){   		
		//draw centroid
	   for (Point p:this._points){	
		   ig.fillOval((int)(p.getValue()[0]*MULTIPLIER), (int)(p.getValue()[1]*MULTIPLIER), DOT_SIZE, DOT_SIZE); 
	   }
	   
		//draw grid
	    for(int i=0;i<_size;i++){
	    	ig.drawLine(0, i*MULTIPLIER, (_size-1)*MULTIPLIER, i*MULTIPLIER);
	    	ig.drawLine(i*MULTIPLIER, 0, i*MULTIPLIER, (_size-1)*MULTIPLIER);
	    }
}
	
    public static void saveImage(List<Point> lp,String ImagePath){
    	List<Point> points=lp;
    	BufferedImage bi = new BufferedImage(_size*MULTIPLIER, _size*MULTIPLIER, BufferedImage.TYPE_INT_ARGB); 
		Graphics  ig2 = bi.createGraphics();
		
		ig2.setColor(Color.black);
		
		//draw centroid
		for (Point p:points){
			ig2.fillOval((int)(p.getValue()[0]*MULTIPLIER), (int)(p.getValue()[1]*MULTIPLIER), DOT_SIZE, DOT_SIZE); 			 
		}
		   
		//draw grid
		for(int i=0;i<_size;i++){
		    ig2.drawLine(0, i*MULTIPLIER, (_size-1)*MULTIPLIER, i*MULTIPLIER);
		    ig2.drawLine(i*MULTIPLIER, 0, i*MULTIPLIER, (_size-1)*MULTIPLIER);
		}
		    
		File outputfile = new File(ImagePath);
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
    }
}
