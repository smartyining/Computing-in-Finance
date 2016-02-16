package mySolution;

import java.util.ArrayList;
import java.util.List;

/** a list of <date, price> nodes
 * 
 * @author yininggao
 *
 */
public class StockPath {
	
	private List<PriceNode> path;
	
	public StockPath(){
		path=new ArrayList<PriceNode>();
	}
	
	public void add(PriceNode pn){
		path.add(pn);
	}
	
	/** Get the prices of a stock Path
	 * 
	 * @return prices
	 */
	public double[] getPrices(){
		double[] prices=new double[path.size()];
		for (int i=0;i<prices.length;i++){
			prices[i]=path.get(i).getPrice();
		}
				
		return prices;
		
	}
	
	public int size(){
		return path.size();
	}
	
	/**Print stockpath to screen
	 * 
	 */
	public void print(){
		System.out.println("Date   Price");
		for(int i=0;i<path.size();i++){
			System.out.println(path.get(i).toString());
		}
		
	}
}
