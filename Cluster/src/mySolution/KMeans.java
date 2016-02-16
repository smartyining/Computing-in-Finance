package mySolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import mySolution.Point;
import mySolution.Cluster;

/**KMeans algorithm, assignCluster() assign each point to their nearest Cluster 
 * based on the result of former KMeans result, Reassign() reassign points to their
 * first nearest and not full cluster to obtain a fixed cluster size 
 * 
 * @author yininggao
 *
 */
public class KMeans{
 
    protected List<Point> points;
    protected List<Cluster> clusters;
    protected int NUM_POINTS=0;
    protected int NUM_CLUSTERS=0;
    protected int POINT_DIM=0;
    protected int iteration=0;
	protected List<Point> initialCentroid;
   
    
    public static KMeans newKMeans(List<Point> points, int num_clusters) throws Exception {    	
    	KMeans km= new KMeans(points, num_clusters); 
	    km.init();   	
	    km.calculate();
	    return km;

    }

    public KMeans(){
    	
    }
	public KMeans(List<Point> points, int num_clusters) {
		
    	this.points = points;
    	this.clusters = new ArrayList<Cluster>();   
    	this.NUM_POINTS=points.size();
    	this.NUM_CLUSTERS = num_clusters; 
    	this.POINT_DIM=points.get(0).getDim();
    	this.initialCentroid=new ArrayList<Point>();

    }
	
    /** Allocate random centroid as initialization
     * 
     * @return store initialCentroid
     * @throws Exception 
     */
    public List<Point> init() throws Exception {   	
    	//Set Random Centroids
    	Random rd=new Random();
    	Collections.shuffle(points, rd);

    	for (int i = 0; i<NUM_CLUSTERS; i++) {
    		Cluster cluster = new Cluster(i);   		
    		Point centroid = new Point(points.get(i).getValue());
    		cluster.setCentroid(centroid);
    		initialCentroid.add(new Point(centroid.getValue()));
    		clusters.add(cluster);
    	}
    	
    	printClusters();
    	
    	if (POINT_DIM==2)
    		draw(initialCentroid,"InitialCentroid");
    	
    	return initialCentroid;
    }

	private void printClusters() {
    	for (int i = 0; i<NUM_CLUSTERS; i++) {
    		Cluster c = clusters.get(i);
    		c.printCluster(); 
    	}
    }
    
	/** This process calculates the K Means, with iterating method.
	 * @throws Exception 
	 *
	 * */
    public void calculate() throws Exception {
        boolean finish = false;
       
        while(!finish) {
        	clearClusters();
        	
        	List<Point> lastCentroids = getCentroids();        	
        	//Assign each point to the nearest cluster
        	assignCluster();         
        	     	
        	calculateCentroids();  
        	
        	iteration++;
        	
        	List<Point> currentCentroids = getCentroids();
        	
        	//Calculates total distance between new and old Centroids
        	double distance = 0;
        	for(int i = 0; i<lastCentroids.size(); i++) {
        		distance += Point.distance(lastCentroids.get(i),currentCentroids.get(i));
        	}
        	System.out.println("----------Iteration: " + iteration+"---------------");
        	System.out.println("Centroid distances: " + distance);
        	printClusters();
        	        	
        	if(distance == 0) {
        		finish = true;
        	}
        }	     	
        	if (POINT_DIM==2)
        		draw(getCentroids(),"KMeans Result");      
    }
    
    protected void clearClusters() {
    	for(Cluster cluster : clusters) {
    		cluster.clear();
    	}
    }
    

    protected List<Point> getCentroids() {
    	List<Point> centroids = new ArrayList<Point>(this.NUM_CLUSTERS);
    	for(Cluster cluster : clusters) {
    		Point aux = cluster.getCentroid();
    		Point point = new Point(aux.getValue());
    		centroids.add(point);
    	}
    	return centroids;
    }
    
    /**This method assign each point to the nearest cluster     
     * 
     */
    protected void assignCluster() {                    
        for(Point point : points) {
        	Collections.sort(clusters, Cluster.getComparator(point));                       
            point.setCluster(clusters.get(0).getId());    
            clusters.get(0).addPoint(point);
        }
        
    }   
    
    /**This method assign fixed size nearest points to each centroid after apply basic KMeans
     * method by reassigning  points to other not full clusters
     * @return
     * @throws Exception 
     */
    protected int reAssign() throws Exception {
    	int cluster_size= NUM_POINTS/NUM_CLUSTERS;
    	int count=0;    
        System.out.println(cluster_size);
    	for (int j=0; j<NUM_CLUSTERS;j++) {  
    		// get Cluster id #j
    		Cluster now=Cluster.getCluster(j, clusters);    		     	        	
    		if(now.getPoints().size()> cluster_size){   
    			System.out.println("Current cluster is oversized: "+now.getId()+"it has size "+now.getPoints().size());
    			Collections.sort(now.getPoints(), Point.getComparator(now.getCentroid()));
    			
    			//loop points that need to be reassigned
    			for (Point p : now.getPoints().subList(cluster_size,now.getPoints().size())){     				
    				Collections.sort(clusters, Cluster.getComparator(p)); 
    				int i=1; 
    				while(!(clusters.get(i).getPoints().size()< cluster_size)&&i<NUM_CLUSTERS) {
    					i++;    
    				} 
    				
    				//assign to this position			
    				p.setCluster(clusters.get(i).getId()); 
    				clusters.get(i).addPoint(p);  
    				System.out.println("Move Point "+p.toString()+"to Cluster "+clusters.get(i).getId());
    				
    				count++;
    			}
    		//delete oversized point from Cluster now
    		now.points=now.getPoints().subList(0,cluster_size); 			
    		}
    	} 
    	//draw status after reassignment If data has two dimensions
    	if (POINT_DIM==2&& count>0)
    		draw(getCentroids(),"Fixed cluster size");
    	
    	calculateCentroids();   	
    return count;	 	  	
    }
    
    /**calculate the geometric centroid of each cluster
     * 
     */
    protected void calculateCentroids() {  	
        for(Cluster cluster : clusters) {
            double[] sum = new double[POINT_DIM];
            double[] ave= new double[POINT_DIM];
            
            List<Point> list = cluster.getPoints();
            int n_points = list.size();

            for (int i=0;i<POINT_DIM;i++){            	
            	for(Point point : list) {            	
            		sum[i]+= point.getValue()[i];
            	}
            	if(n_points>0) {ave[i]=sum[i]/n_points;};
            }
            
            Point centroid = cluster.getCentroid();
            if(n_points>0) {
                centroid.setValue(ave);
            }
        }
    }      
        
    
    protected int getIterationTimes(){
    	return iteration;
    }
    
    /**display 2 d points in panel
     * 
     * @param points
     */
    protected static void draw(List<Point> points,String name) throws Exception{
     if (points.get(0).getValue().length!=2)
    	 throw new Exception("Must be 2 dimension points!");
     else{
 	    DrawPanel dr=new DrawPanel();	    
 	    dr.addPoints(points);	    
 	    dr.drawPoints(name);
     }
    }
}
