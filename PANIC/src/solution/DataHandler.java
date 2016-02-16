package solution;

import java.util.*;

import utils.SortOrder;
import utils.TextFileReader;


/**
 *  This class reads price records data;
 *  And calculate prices moving up ratio: #(close-open)>0/#records, for each date
 *  Tested in Test_DataHandler, using sample 2 days data.
 *  
 * @author yininggao
 *
 */
public class DataHandler {

	private LinkedList<Double> _data; // Ratio of price moving up for each date
	private LinkedList<PriceRecord> _records; // Price Records
	
	/**
	 * Constructor - sets the ready flag to false
	 */
	public DataHandler() { 
		_data=new LinkedList<Double>();
	}
	
	/**
	 * Reads text file and converts it to an array of PriceRecord1 objects 
	 * Calculate the ratio of price moving up for each date
	 * And keep this ratio in a linkedlist
	 * 
	 * @param filePathName 
	 * @throws Exception
	 */
	public void processData() throws Exception {	
		//sort data by date;
		sortData();
		
		// Calculate the ratio of price moving up for each date
		Long date=(long) 00000000;
		int n=0; // count of prices moving up for each date
		int m=0; // count of all price records for each date
		
		while(!_records.isEmpty()) {
			PriceRecord record=_records.removeFirst();
			if(record.getDate()!=date) {
				if(m>0)
					_data.add((double)n/m); 	// update _records				
				// continue counting for next date
				date=record.getDate();
				n=0;
				m=0;
			}
			if(record.getClose()-record.getOpen()>0)
				n++;  // count of prices moving up  add 1
			m++;     // count of all price records  add 1
		}
		_data.add((double)n/m); 	// update last number
	}
	
	/**
	 * Get price moving up ratio
	 * @return Ratio data
	 */
	public LinkedList<Double> getData() {
		return _data;
	}
	
	/**
	 * Reads in price records from text file and converts them to objects
	 * 
	 * @param filePathName Name of file in which records are text representation of PriceRecord1 objects
	 * @return Linked list of PriceRecord1 objects
	 * @throws Exception If file cannot be read or the data doesn't make sense
	 */
	public LinkedList<PriceRecord> getPriceRecords( String filePathName ) throws Exception {
		LinkedList<String> lines = TextFileReader.readLines( filePathName );
		_records=PriceRecord.parseRecords( lines );
		return _records;
	}
	

	/**
	 * Make sure records are sorted by date
	 */
	protected void sortData() {
		Comparator<PriceRecord> comparator;
		comparator = PriceRecord.getDateComparator(SortOrder.ASCENDING);
		Collections.sort(_records,comparator);
	}
	

	
	
}
