package merge;

import java.io.IOException;

import junit.framework.TestCase;

public class Test_TradesRewriter extends TestCase {
		
	// Test rewriter: whether file created
	// Whether file correctly writen will be tested in Test_DBReader part
	public void test() throws IOException {
		TradesRewriter rewriter = new TradesRewriter("/users/yininggao/trades/20070620/IBM_trades.binRT",100);
		rewriter.write();

	
	}

}
