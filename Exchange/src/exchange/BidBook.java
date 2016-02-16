package exchange;

public class BidBook implements I_Book {
	
	private BinaryTree<Double,RestingOrder> _bidbook;
	
	public BidBook(){
		_bidbook=new BinaryTree<Double,RestingOrder> ();
	}
	
	/** Get the best price order in the book
	 *  This is a bidBook, therefore return the first order in the max price level
	 */
	@Override
	public RestingOrder getOrder(){
		if ( _bidbook.max()!=null)
			return _bidbook.max().getlist().getFirst();
		else
			return null;
	}

	@Override
	public void add(RestingOrder order) {
		 _bidbook.add(order.getPrice(), order);
		
	}

	@Override
	public void delete(RestingOrder order) {
		 _bidbook.delete(order.getPrice(),order);
		
	}

	@Override
	public void remove(){
		_bidbook.deleteMax();
	}
	
	@Override
	public boolean isEmpty(){
		return _bidbook.isEmpty();
	}
	
	public String toString(){
		return _bidbook.reverseWalk();
	}
}
