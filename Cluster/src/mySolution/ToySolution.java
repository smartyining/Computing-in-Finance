package mySolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToySolution {	
	
	public static int X_MIN=0;
	public static int Y_MIN=0;
	public static int X_MAX=100;
	public static int Y_MAX=100;
	public static int NUM_CLUSTERS=500;
	public static int iteration=0;
	public static int reassignment=0;
	
	public static List<Point> points = new ArrayList<Point>();	
	
	
	/**Create 100*100 points
	 * 
	 */
	public static void createPoint(){
		for(int i=X_MIN;i<X_MAX;i++){
			for(int j=Y_MIN; j<Y_MAX;j++){
				points.add(new Point(new double[]{(double)i,(double)j}));
			}
		}
		
	}
	
	/**get current time*/
	public static Date getDate(){
		Date date = new Date();
		return date;
	}
	
	/** Write a list of points into a .txt file
	 * 
	 * @param p
	 * @param path
	 * @throws IOException 
	 */
	public static void writeTxtFile(List<Point> lp, String path) throws IOException{
		FileWriter fw = new FileWriter(new File(path));	
		
		fw.write("File created at "+getDate().toString()+"\n");
		
		if (iteration>0)
			fw.write("Convergence achieved  after "+iteration+" times iterations.\n");
		
		if (reassignment>0) 
			fw.write("fixed cluster size achieved after "+reassignment+" times reassignment.\n ");			
		
	    for (Point p :lp){
	    	fw.write( String.format("%.2f",p.getValue() [0])+","+ String.format("%.2f\n",p.getValue() [1]));
	    }
	    
	    fw.close();	
		
	}

	public static void main(String[] args) throws Exception{
		String resultFile = "/Users/yininggao/Documents/workspace/ToyProblem/result.txt";
		String resultImage="/Users/yininggao/Documents/workspace/ToyProblem/result.png";
		
		String initialFile = "/Users/yininggao/Documents/workspace/ToyProblem/initial.txt";
		String initialImage="/Users/yininggao/Documents/workspace/ToyProblem/initial.png";
		
		String fixedSizeResultFile = "/Users/yininggao/Documents/workspace/ToyProblem/fixedSizeResult.txt";
		String fixedSizeResultImage="/Users/yininggao/Documents/workspace/ToyProblem/fixedSizeResult.png";
		
		//create 100*100 points
		createPoint();	
		
		//call KMeans method		
		KMeans km=KMeans.newKMeans(points,NUM_CLUSTERS);	
		iteration=km.getIterationTimes();
		
		//get initial status
		List<Point> initial=km.initialCentroid;
	    writeTxtFile(initial,initialFile);
	    DrawPanel.saveImage(initial, initialImage);	
		
		//get cluster assignment result
	    List<Point> results= new ArrayList<Point>();  
		results=km.getCentroids();
		writeTxtFile(results,resultFile);
		DrawPanel.saveImage(results, resultImage);
		
		//Adjust algorithm: using fixed size
	    List<Point> results2= new ArrayList<Point>();  
		reassignment=km.reAssign();
		results2=km.getCentroids();
		writeTxtFile(results2,fixedSizeResultFile);
		DrawPanel.saveImage(results2, fixedSizeResultImage);
	    
	}
}
