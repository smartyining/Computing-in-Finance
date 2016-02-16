package mySolution;

/**StatsCollector add each payout to a ArrayList and calcualte current payouts mean/var/N
 * 
 * @author yininggao
 *
 */
public class StatsCollector {

	private double mean=0;
	private double std=100;
	private double meanSq=0; 	// second order of mean: in order to calculate std
	private int N=0;
	
	public StatsCollector(){
	}
	
	public void add(double e){
		N++;
		mean =(N-1.0)/N*mean + e/N;
		meanSq =(N-1.0)/N*meanSq + e*e/N;
		if (N>1)
			std = Math.sqrt(meanSq-mean*mean);
			
	}
	
	public double getMean(){
		return mean;
	}
	
	public double getStd(){
		return std;
	}
	
	public int getSize(){
		return N;
	}
}	

	

