package solution;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class Test_Histogram extends TestCase {
		
	public void test() throws Exception{
		List<Double> data=new LinkedList<Double>();
		
		data.add(0.0);
		data.add(1.3);
		data.add(1.9);
		data.add(3.3);
		data.add(4.0);
		data.add(5.0);
		
		// Print Hist to screen and manually check
		// In this case 6 points fall into 5 intervals
		Histogram myHist=new Histogram(data,5);
		myHist.print();
		
		
		// Test more data
		DataHandler dataHandler=new DataHandler();
		LinkedList<PriceRecord> _records=dataHandler.getPriceRecords("/Users/yininggao/Documents/workspace/Panic/samplePrices.csv");
		LinkedList<Double> prices=new LinkedList<Double>();
		for(PriceRecord p: _records){
			prices.add(p.getHigh());
		}

		Histogram myHist2=new Histogram(prices,10);
		myHist2.print();
		
	}

}
