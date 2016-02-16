package mySolution;

public class StockPathGenerator implements I_StockPrice{
	
	private Option option;
	private I_RVG randomVectorGenerator;	
	private StockPath path;
	
	/** Constructor, implement RVG , using decorator method
	 * 
	 * @param option
	 */
	public StockPathGenerator (Option option){
		this.option=option;
		RandomVectorGenerator normGenerator = new RandomVectorGenerator(option.getPeriod()+1);
		I_RVG generator = new AntiTheticRVG(normGenerator);
		this.randomVectorGenerator=generator;
	}
	
	
	@Override
	public StockPath getPath(){
		generateNewPath();
		return path;
		
	}

	/**Generating new brownian motion stock path 
	 * 
	 */
	public void generateNewPath() {
		path = new StockPath();				
		double[] rv = randomVectorGenerator.nextVector();
		
		int t=option.getStartDate();
		double St = option.getInitialPrice();	
		double sigma=option.getVolatility();
		double r=option.getInterestRate();
		
		for(int i=0; i<option.getPeriod()+1;i++) {
			path.add(new PriceNode(t,St));
			t++;
			St = St * Math.exp((r - sigma*sigma/2)+sigma*rv[i]);			
		}
	}
	

}
