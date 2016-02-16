package mySolution;

/**Calcualte the payout of a Europe call: max(S(t)-K,0)
 * 
 * @author yininggao
 *
 */
public class CallPayOut implements I_PayOut {
	
	private double strickPrice;

	public CallPayOut(Option option) {
		this.strickPrice = option.getStrickPrice();
	}
	
	@Override
	public double getPayout(StockPath path) {
		double[] prices =  path.getPrices();
		return Math.max(prices[prices.length-1]-strickPrice,0);
	}

}
