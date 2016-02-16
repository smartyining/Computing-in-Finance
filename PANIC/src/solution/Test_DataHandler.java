package solution;

import java.util.LinkedList;

import junit.framework.TestCase;

public class Test_DataHandler extends TestCase {
	
	private LinkedList<PriceRecord> _records;
	private LinkedList<Double> _data;
	
	// Test if file can be read correctly 
	public void test() throws Exception{
		DataHandler dataHandler=new DataHandler();
		
		// Sample file contains 200 records from 2 days
		_records=dataHandler.getPriceRecords("/Users/yininggao/Documents/workspace/Panic/samplePrices.csv");
		
		// Test first line of data
		assertTrue(_records.getFirst().getDate()==20130930);
		assertTrue(_records.getFirst().getCode().equals("UA"));
		assertTrue(_records.getFirst().getOpen()==79.08);
		assertTrue(_records.getFirst().getClose()==79.45);
		
		// Test calculate price movement ratio
		dataHandler.processData();
		_data=dataHandler.getData();
		
		// There should be 75% price moving up: I pre-calculated this number in excel
		// Same for the second date: I faked the same data from first date.	
		// If DataHandler works for 2 days, it should also works for more days.
		assertTrue(_data.size()==2); // Data size: 2 days
		assertTrue(_data.remove()==0.75); 
		assertTrue(_data.remove()==0.75);
				
	}
}
