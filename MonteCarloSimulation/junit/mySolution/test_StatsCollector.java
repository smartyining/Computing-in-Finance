package mySolution;

import junit.framework.TestCase;

/**This test verify the calculation of stasCollector
 * 
 * @author yininggao
 *
 */
public class test_StatsCollector extends TestCase {
	
	public void test1(){
		
		//creat a new double[]
		double[] payouts={1,2,3,4,5,6,7,8,9,10};
		
		//create a new statsCollector
		StatsCollector stats= new StatsCollector();
		for (double i: payouts){
			stats.add(i);
		}
		
		
		assertTrue(stats.getMean()==5.5);
		assertTrue(stats.getSize()==10);
		assertTrue(	String.format("%.2f",stats.getStd()).equals("2.87"));
	}

}
