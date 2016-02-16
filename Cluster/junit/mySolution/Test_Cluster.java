package mySolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class Test_Cluster extends TestCase {
	
	public void test(){
		
		Cluster c1=new Cluster(1);
		Cluster c2=new Cluster(2);
		
		double[] a={1,3,2};
		double[] b={1,4,5};
		
		Point p1=new Point(a);
		Point p2=new Point(b);
		
		//test addPoint method
		
		c1.addPoint(p1);
		c1.addPoint(p2);
		
		
		//test setPoints method
		List<Point> l=new ArrayList<Point>();
		l.add(p1);
		l.add(p2);
		
		c2.setPoints(l);
	
        assertTrue(c1.points.toString().equals(c2.points.toString()));
        
        //test delete method
        c1.deletePoint(p1);
		System.out.println(c1.points.toString());
		
		//test comparator
		List<Cluster> lc=new ArrayList<Cluster>();
		for (int i=0; i<5;i++){
			Cluster c=new Cluster(i);
			c.setCentroid(new Point(new double[] {i,i+2,i+3}));
			lc.add(c);
		}
		double[] p={100,400,400};
		Collections.sort(lc,Cluster.getComparator(new Point(p)));		
		
		double[] t={4.0,6.0,7.0};
		for (int i=0;i<3;i++)
			assertTrue(lc.get(0).getCentroid().getValue()[i]==t[i]);
				
	}

}
