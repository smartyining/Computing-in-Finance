package mySolution;

import junit.framework.TestCase;

/** This test verify the calculation of two payouts.
 * 
 * @author yininggao
 *
 */
public class test_PayOut extends TestCase {
	
	public void test1(){
		//create a new option
		Option option=new Option.OptionBuilder().setStrickPrice(100).build();
		//implement payout
		I_PayOut europe=new CallPayOut(option);
		I_PayOut asian=new AsianCallPayOut(option);
		
		//generate a new path: [99,99,99,99,99] therefore should not be called and value is 0
		StockPath path=new StockPath();
		path.add(new PriceNode(1,99));
		path.add(new PriceNode(2,99));
		path.add(new PriceNode(3,99));
		path.add(new PriceNode(4,99));
		path.add(new PriceNode(5,99));
	
		assertTrue(europe.getPayout(path)==0);
		assertTrue(asian.getPayout(path)==0);
		
		//generate a another path[99,99,99,99,101], europe should be 1 and asian should be 0
		StockPath path2=new StockPath();
		path2.add(new PriceNode(1,99));
		path2.add(new PriceNode(2,99));
		path2.add(new PriceNode(3,99));
		path2.add(new PriceNode(4,99));
		path2.add(new PriceNode(5,101));
	
		assertTrue(europe.getPayout(path2)==1);
		assertTrue(asian.getPayout(path2)==0);
		
		//generate a another path[101,101,101,101,101], value should be 1
		StockPath path3=new StockPath();
		path3.add(new PriceNode(1,101));
		path3.add(new PriceNode(2,101));
		path3.add(new PriceNode(3,101));
		path3.add(new PriceNode(4,101));
		path3.add(new PriceNode(5,101));
	
		assertTrue(europe.getPayout(path3)==1);
		assertTrue(asian.getPayout(path3)==1);
		
	}

}
