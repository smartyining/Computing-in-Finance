package utils;

public class SortField {
	
	public static final SortField PRICE = new SortField( "Price" );
	public static final SortField DATE = new SortField( "Date" );
	
	private String _sortField;
	
	private SortField( String sortField ) { _sortField = sortField; }
	
	public String toString() { return ( String.format( "SortField(%s)", _sortField ) ); }
	
}
