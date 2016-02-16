package mySolution;

/**Calculate the payout for an asian call: max(ave(S)-K,0)
 * 
 * @author yininggao
 *
 */
public class AsianCallPayOut implements I_PayOut{
	
	private double strickPrice;

	public AsianCallPayOut(Option option) {
		this.strickPrice = option.getStrickPrice();
	}
	
	
	@Override
	public double getPayout(StockPath path) {
		double[] prices =  path.getPrices();
		double sum=0;
		for (int i=0;i<prices.length;i++){
			sum+=prices[i];
		}
		return Math.max(sum/prices.length-strickPrice,0);
	}
}
