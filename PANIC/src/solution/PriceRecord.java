package solution;

import java.util.Comparator;
import java.util.LinkedList;

import utils.DoubleComparator;
import utils.SortOrder;

public class PriceRecord {
	
	private long   _date;
	private String _code;
	private double _open;
	private double _high;
	private double _low;
	private double _close;
	private long   _volume;
	private double _adjustedClose;
	
	public long   getDate()          { return _date;          }
	public String getCode()			 { return _code;          }
	public double getOpen()          { return _open;          }
	public double getHigh()          { return _high;          }
	public double getLow()           { return _low;           }
	public double getClose()         { return _close;         }
	public long   getVolume()        { return _volume;        }
	public double getAdjustedClose() { return _adjustedClose; }
	
	/**
	 * Converts a long date in the format YYYYMMDD to a String representation of a date
	 * in the format "MM/DD/YYYY"
	 * 
	 * @param date Long representation of a date
	 * @return String representation of a date
	 */
	protected static String dateToString( long date ) {
		String sDate = String.format( "%d", date );
		return sDate.substring( 4, 6 ) + "/" + sDate.substring( 6, 8 ) + "/" + sDate.substring( 0, 4 ); 
	}
	
	/**
	 * Instantiate a price record using a String representation in the format
	 * "(MM/DD/YYYY),(open),(high),(low),(close),(volume),(adjustedClose)"
	 * 
	 * Perform some error checking
	 * 
	 * @param dataLine String representation of a price record
	 * @throws Exception Thrown if the price record is mis specified or if the values are outside accepted range
	 */
	public PriceRecord( String dataLine ) throws Exception {
		
		if( dataLine == null )
			throw new Exception( "Null string passed to constructor of PriceRecord class" );
		
		String[] dataFields = dataLine.split( "," );
		if( dataFields.length != 8 )
			throw new Exception( String.format( "Less than 8 fields in [%s]", dataLine ) );
		
		// Parse date
		
			String dateFields = dataFields[0];
			if( dateFields.length()!= 8 )
				throw new Exception( String.format( "Bad date field [%s] in record [%s]", dataFields[ 0 ], dataLine ) );
			
			// Convert date to numeric value
			_date = ( Integer.parseInt( dateFields));
			
			// What's a reasonable range, a range beyond which we want users of this library to be alerted?
			// We won't implement this part because, for the sake of this exercise, we don't care
			// if( ( _date < 19000101 ) || ( _date > 20300101 ) )
				// throw new Exception( String.format( "Bad date range [%s] in record [%s]", dataFields[ 0 ], dataLine ) );
			
		// Parse prices
			_code  = dataFields[ 1 ];
			_open  = Double.parseDouble( dataFields[ 2 ] );
			_high  = Double.parseDouble( dataFields[ 3 ] );
			_low   = Double.parseDouble( dataFields[ 4 ] );
			_close = Double.parseDouble( dataFields[ 5 ] );
			_adjustedClose = Double.parseDouble( dataFields[ 7 ] );
			
			// Error check - Prices can't be less than zero
			if( ( _open <= 0 ) || ( _high <= 0 ) || ( _low <= 0 ) || ( _close <= 0 ) || ( _adjustedClose <= 0 ) )
				throw new Exception( String.format( "Bad price in record [%s]", dataLine ) );
			
			// Should we use a DoubleComparator with tolerance for above? What if these
			// are options prices for way way way out of the money options? Could they
			// round down to zero? Probably not, but hey, it's something to consider.

		// Parse volume
			
			_volume = Long.parseLong( dataFields[ 6 ] );
			
			// Error check - Volume can't be less than zero
			if( _volume < 0 )
				throw new Exception( String.format( "Bad volume [%s] in record [%s]", dataFields[ 5 ], dataLine ) );
			
			// What's a good upper limit on volume? We'll leave this alone for now.
		
	}
	
	/**
	 * Method to convert a list of String representations of PriceRecord into a list of PriceRecord objects
	 * 
	 * @param dataLines List of String representations of PriceRecord
	 * @return LinkedList of price records
	 * @throws Exception Thrown if parsing string fails (see PriceRecord constructor)
	 */
	public static LinkedList<PriceRecord> parseRecords( LinkedList<String> dataLines ) throws Exception {
		if( dataLines == null )
			throw new Exception( "Null list passed to parse records method" );
		LinkedList<PriceRecord> priceRecords = new LinkedList<PriceRecord>();
		for( String dataLine : dataLines )
			priceRecords.add( new PriceRecord( dataLine ) );
		return priceRecords;
	}
	
	/**
	 * Static factor method for instantiating a comparator that will compare price
	 * records by date
	 * 
	 * @param sortOrder Ascending or descending
	 * @return 1, 0, or -1 depending on which date is greater
	 */
	public static Comparator<PriceRecord> getDateComparator( final SortOrder sortOrder ) {
		
		Comparator<PriceRecord> comparator = new Comparator<PriceRecord>() {
			private int _comparisonMultiplier = sortOrder.getComparisonMultiplier();
			@Override
			public int compare( PriceRecord p1, PriceRecord p2 ) {
				if( p1.getDate() > p2.getDate() )
					return 1 * _comparisonMultiplier;
				if( p1.getDate() < p2.getDate() )
					return -1 * _comparisonMultiplier;
				return 0;
			}
		};
		return comparator;
	}
	
	/**
	 * Static factory method for instantiating a comparator that will compare price
	 * records by the adjusted close price
	 * 
	 * @param sortOrder Ascending or descending
	 * @param tolerance Tolerance for comparing adjust close price, which is a double
	 * @return 1, 0, or -1, depending on which adjusted close price is greater
	 */
	public static Comparator<PriceRecord> getAdjustedCloseComparator( final SortOrder sortOrder, final double tolerance ) {
		return new Comparator<PriceRecord>() {
			
			private int    _comparisonMultiplier = sortOrder.getComparisonMultiplier();
			private double _tolerance            = tolerance;
			
			@Override
			public int compare( PriceRecord p1, PriceRecord p2 ) {
				return _comparisonMultiplier * DoubleComparator.compare( 
					p1.getAdjustedClose(), 
					p2.getAdjustedClose(), 
					_tolerance 
				);
			}
		};
	}
	
	/**
	 * Converts a PriceRecord object to a String representation
	 */
	@Override
	public String toString() {
		return String.format(
			"%s,%s,%.2f,%.2f,%.2f,%.2f,%d,%.2f",
			dateToString( _date ),
			_code,
			_open,
			_high,
			_low,
			_close,
			_volume,
			_adjustedClose
		);
	}
	
}
