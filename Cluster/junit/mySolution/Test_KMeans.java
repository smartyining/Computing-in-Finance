package mySolution;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/** Test KMeans class, using a simple SamplePoints.

 * 
 * @author yininggao
 *
 */
public class Test_KMeans extends TestCase {
	
	static List<Point> list = new ArrayList<Point>();	
	static int numberOfCluster=4;
	static KMeans myKMeans=new KMeans();
	
	
	//Test constructor and init() 
	public static void test1() throws Exception{			
		// create a list of Points		
		list.add(new Point(new double[] {-1.5, 2.0, 5.0}));
		list.add(new Point(new double[] {-2.0, -2.0, 0.0}));
		list.add(new Point(new double[] {1.0, 1.0, 2.0}));
		list.add(new Point(new double[] {1.5, 1.5, 1.2}));
		list.add(new Point(new double[] {1.0, 2.0, 5.6}));
		list.add(new Point(new double[] {1.0, -2.0, -2.0}));
		list.add(new Point(new double[] {1.0, -3.0, -2.0}));
		list.add(new Point(new double[] {1.0, -2.5, -4.5}));
		
		//test constructor
		myKMeans=new KMeans(list,numberOfCluster);
		assertTrue(myKMeans.points.size()==8);  //check size()
		assertTrue(myKMeans.points==list);
				
		
	   // test init() make sure pick a right random point
		myKMeans.init();
        
		List<Point> randomCentroids = new ArrayList<Point>();
		randomCentroids=myKMeans.getCentroids();  
		for(Point p : randomCentroids){
			assertTrue(list.contains(p)); //Initial centroids should be in points
		}
        
	}
	
	
	//Test assignCluster()
	public void test2(){	
		System.out.println("------------------Now Assign Points---------------");
		myKMeans.clearClusters();
	    myKMeans.assignCluster();	 
		for (Cluster c:myKMeans.clusters){				
			for(Point p:c.getPoints()){	
			//check Points in ith Cluster has ith getCluster Value
			   assertTrue(p.getCluster()==c.getId());  		
			}					
		}
		 System.out.println("-------------After Calculate Centroid---------------");
	}
	
	
	//test the calculation of centroids
	public void test3(){	
		myKMeans.calculateCentroids();
		
		for (Cluster c:myKMeans.clusters){
			double  v1 = 0;
			double  v2 = 0;
			double  v3 = 0;
			
			c.printCluster(); 
			for (Point p :c.getPoints()){
				System.out.println(p.toString());
				v1+=p.getValue()[0];
				v2+=p.getValue()[1];
				v3+=p.getValue()[2];
			}		
			//Centroids value should be the same as manually calculated
			assertTrue(c.getCentroid().getValue()[0]==v1/c.points.size());
			assertTrue(c.getCentroid().getValue()[1]==v2/c.points.size());
			assertTrue(c.getCentroid().getValue()[2]==v3/c.points.size());	
		
		}	
		System.out.println("--------------Now use iterating-----------");
		
	}
	
	//test if calculate() process terminates correctly
	//Compared to pre-calculated results:
	//Cluster1 -2.0, -2.0, 0.0
	//Cluster2 1.0, -2.5, -2.833333333333333
	//Cluster3 1.25, 1.25, 1.6
	//Cluster4 -0.25, 2.0, 5.3
	
	public void test4() throws Exception{
		KMeans km=new KMeans(list,numberOfCluster);
		km.init();
		km.calculate();
		System.out.println("--------------Now use fixed size method-----------");	
	
		//test fixed size method	
		int count=km.reAssign();
		System.out.print(count);
		for (Cluster c: km.clusters)
			assertTrue(c.getPoints().size()==2);  // Should have 4 clusters each has two points

		
	}
	
}



