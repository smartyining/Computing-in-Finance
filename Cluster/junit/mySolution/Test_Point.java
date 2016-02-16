package mySolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class Test_Point extends TestCase {
	
	public void testAll(){
		
		//test constructor
		Point p1=new Point();		
		Point p2=new Point(new double[] {1,2,3,4,5,6});		
		Point p3=new Point(new double[] {1,2,3,4,5,7});
		

		List<Point> list = new ArrayList<Point>();
		list.add(p1);	
		
		//test set and get method
		double[] a={1,2,3,4,5,6};
		
		p1.setValue(a);
		assertTrue(p1.getValue()==a);
		
		int n=1;
		
		p1.setCluster(n);
		assertTrue(p1.getCluster()==n);
		
		//test equals method		
		assertTrue(p1.equals(p2));
		
		//test distance calculation
		assertTrue(Point.distance(p1,p2)==0);			
		assertTrue(Point.distance(p3,p2)==1);
	}
	
	/**Test comparator 
	 * 
	 */
	public void testSort(){
		//set a centroid 
		Point c=new Point(new double[] {1,2,3,4,5,7});
		
		Point p1=new Point(new double[] {1,2,3,4,5,6});		
		Point p2=new Point(new double[] {1,2,3,4,5,7});
		Point p3=new Point(new double[] {1,2,3,4,5,9});		
		Point p4=new Point(new double[] {1,2,6,4,5,7});
		
		List<Point> list = new ArrayList<Point>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		
		Collections.sort(list,Point.getComparator(c));
		assertTrue(list.get(0)==p2);
		assertTrue(list.get(1)==p1);
		assertTrue(list.get(2)==p3);
		assertTrue(list.get(3)==p4);
		
	}
}
