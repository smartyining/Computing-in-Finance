package mySolution;

/** <DateTime,Price> node for each price 
 * 
 * @author yininggao
 *
 */
public class PriceNode {
	
	private int DateTime;
	private double Price;
	
	public PriceNode(){		
	}

	public PriceNode(int Date, double Price){
		this.DateTime=Date;
		this.Price=Price;
	}
	
	public int getDateTime() {
		return DateTime;
	}

	public void setDateTime(int dateTime) {
		DateTime = dateTime;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}
	
	@Override
	public String toString(){
		return String.format("%d,%.2f", DateTime , Price);
	}
	
}
