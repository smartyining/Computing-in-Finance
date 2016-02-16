package utils;

public class SortOrder {
	
	public static final SortOrder ASCENDING = new SortOrder( "Ascending", 1 );
	public static final SortOrder DESCENDING = new SortOrder( "Descending", -1 );
	
	private String _sortOrder;
	private int    _comparisonMultiplier;
	
	private SortOrder( String sortOrder, int comparisonMultiplier ) { 
		_sortOrder = sortOrder;
		_comparisonMultiplier = comparisonMultiplier;
	}
	
	public int getComparisonMultiplier() { return _comparisonMultiplier; }
	
	public String toString(){ return _sortOrder; };
	
}

