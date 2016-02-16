package mySolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mySolution.Point;

public class Cluster {
	
	public List<Point> points;
	public Point centroid;
	public int id;

	public Cluster(){
	}
	
	public Cluster(int id) {
		this.id = id;
		this.points = new ArrayList<Point>();
		this.centroid = null;
	}

	public List<Point> getPoints() {
		return points;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public void deletePoint(Point point){
		points.remove(point);
	}
	
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}

	public int getId() {
		return id;
	}
	
	public void clear() {
		points.clear();
	}
	
	/** This comparator compares cluster in terms of their distance from centroid to a specific point
	 * 
	 * @param p
	 * @return
	 */
	public static Comparator<Cluster> getComparator(Point p) {	
		Comparator<Cluster> comparator = new Comparator<Cluster>() {
			@Override
			public int compare( Cluster c1, Cluster c2 ) {
				if( Point.distance(p,c1.getCentroid()) > Point.distance(p,c2.getCentroid()) )
					return 1 ;
				if( Point.distance(p,c1.getCentroid()) < Point.distance(p,c2.getCentroid()))
					return -1 ;
				return 0;
			}
		};
		return comparator;
	}
	
	public void printCluster() {
		System.out.println("Cluster " + id);
		System.out.println("Centroid: " + centroid.toString() + "");
		System.out.println("Points:");
		for(Point p : points) {
			System.out.println(p.toString());
		}
	}
	
	/** Search a list of clusters by id #id
	 * 
	 * @param id
	 * @return
	 */
	public static Cluster getCluster(int id, List<Cluster> kmcluster){
		Cluster current=new Cluster();
		for (Cluster c : kmcluster){
			if(c.getId()==id){
				current=c;		
				break;
			}
		}
		return current;
	}

}
