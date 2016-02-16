package mySolution;

/** This class controls when simulation stops.
 * 
 * @author yininggao
 *
 */
public class SimulationManager {
	
	private double tolerance=0.1;
	private double prob=0.96;
	private I_StockPrice generator;
	private I_PayOut payout;
	private StatsCollector stats;
	private boolean isFinished=false;
	private double r; //interest rate
	private int period; //duration
	private double price;
	
	/**Constructor : implements path generator,  payout and statsCollector
	 * 
	 * @param option
	 */
	public SimulationManager(Option option){
		generator=new StockPathGenerator (option);		
		if (option.getType()=="Europe")
			payout=new CallPayOut(option);
		else
			payout=new AsianCallPayOut(option);		
		stats=new StatsCollector();
		r=option.getInterestRate();
		period=option.getPeriod();
	}
	
	/**Simulation loop
	 * The price of an option is the current value of the mean of payout
	 */
	public double getPrice(){
		while(!isFinished()){
			StockPath currentPath=generator.getPath();
			stats.add(payout.getPayout(currentPath));
		}
		price=stats.getMean()* Math.exp(-r*period);
		return price;		
	}
	
	public void setProb(double p){
		this.prob=p;		
	}
	
	public void setTolerance(double t){
		this.tolerance=t;
	}
	
	/**Calculate quantile for standard Guassian distribution
	 * 
	 * @return quantile
	 */
	public double getBound(){
		double p=(1.0-prob)/2;
		double c0=2.515517;
		double c1=0.802853;
		double c2=0.010328;
		double d1=1.432788;
		double d2=0.189269;
		double d3=0.001308;
		
		double t=Math.sqrt(Math.log(1/(p*p)));
		double bound= t-(c0+c1*t+c2*t*t)/(1+d1*t+d2*t*t+d3*t*t*t);
		return bound;
	}
	
	/** according to CLT, finished if bound*\sigma/sqrt(n) is less than tolerance
	 *  you need to set a lower bound for n. eg:n>100
	 * @return
	 */
	public boolean isFinished(){
		if (stats.getSize()==0)  
			isFinished=false;
		else
			isFinished=getBound()*stats.getStd()/Math.sqrt(stats.getSize())<tolerance && stats.getSize()>100;
		return isFinished;
	}
	
	/**
	 * Converts to a String representation
	 */
	@Override
	public String toString() {
		if(isFinished)
			return "Option Price (Current Value of Expected Payout):  "+String.format("%.2f", price)+" StockPath generated:  "+stats.getSize();
		else 
			return "Simulation has not finished";
	}
}







