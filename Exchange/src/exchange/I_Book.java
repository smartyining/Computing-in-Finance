package exchange;

public interface I_Book {
	
	//get the best price order
	public default RestingOrder getOrder(){
		return null;
	}

	public default void add(RestingOrder order){
	}
	
	public default void delete(RestingOrder order){		
	}

	//remove the best price order
	public default void remove(){		
	}
	
	public default boolean isEmpty(){
		return true;
	}
}
