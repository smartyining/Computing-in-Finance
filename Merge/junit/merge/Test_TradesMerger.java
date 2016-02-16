package merge;

import junit.framework.TestCase;

public class Test_TradesMerger extends TestCase {
	
	// Test if the task is performed correctly :
	// (1) Whether all *_1.dat fill will be merged into *_2.dat.....and finally the result
	// (2) Whether this works for all dates
	// (3) Others: the correctness of hashmap
	// Since the correctness of DBframework is already tested by professor, it won't be tested here
	
	
	public void test() throws Exception {
		TradesMerger merger= new TradesMerger();
		
		// Test rewrite 
		merger.rewrite();
					
		// Test merge and check in the output fold
		merger.merge();
	}
	

}
