package mySolution;

import java.util.Arrays;
import java.util.Comparator;


/**This class convert a double array to a Point Object
 *
 * 
 * @author yininggao
 *
 */

public class Point {

    private double[] _value =new double[0];
    private int dim=0;
    private int cluster_number = 0;
    
    public Point() {
    }  

    public Point(double[] x){
        this.setValue(x);
    }
    
    public void setValue(double[] x) {
        this._value = x;
    }
    
    public double[] getValue()  {
        return this._value;
    }   
    
    public int getDim(){       //return the dimension of point
    	this.dim=this._value.length;
    	return dim;
    }
    
    public void setCluster(int n) {
        this.cluster_number = n;
    }
    
    public int getCluster() {
        return this.cluster_number;
    }
    
    /**Calculates the distance between two points.
     * 
     * @param p
     * @param centroid
     * @return Euclidean distance
     */
    public static double distance(Point p, Point centroid) {
    	double sum=0;
    	for (int i=0;i<p.getDim();i++){
    		sum+=Math.pow((p._value[i]-centroid._value[i]),2);
    	}    	
        return Math.sqrt(sum);
    }
    
    public boolean equals(Object object){
    	if(this==object){
    		return true;
    	}
    	if(!(object instanceof Point)){
    		return false;
    	}
    	Point p=(Point) object;
    	return (Arrays.equals(p.getValue(), _value));
    }
    
    /**Points should be comparable in terms of their distance to a specific centroid
     * 
     * @param centroid
     * @return
     */    

	public static Comparator<Point> getComparator(Point c) {
		
		Comparator<Point> comparator = new Comparator<Point>() {
			@Override
			public int compare( Point p1, Point p2 ) {
				if( distance(p1,c) > distance(p2,c) )
					return 1 ;
				if( distance(p1,c) < distance(p2,c))
					return -1 ;
				return 0;
			}
		};
		return comparator;
	}
       
    public String toString(){
    	return Arrays.toString(this.getValue());
    }

  
 
}