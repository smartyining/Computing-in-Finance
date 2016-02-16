package exchange;

public class OfferBook implements I_Book{
	
	private BinaryTree<Double,RestingOrder> _offerbook;
	
	public OfferBook(){
		_offerbook= new  BinaryTree<Double,RestingOrder>();
	}
	
	/** Get the best price order in the book
	 *  This is offerBook, therefore get the first order in lowest price level
	 */
	@Override
	public RestingOrder getOrder(){
		if(_offerbook.min() !=null)
			return _offerbook.min().getlist().getFirst();
		else
			return null;
	}

	@Override
	public void add(RestingOrder order) {
		_offerbook.add(order.getPrice(), order);
		
	}

	@Override
	public void delete(RestingOrder order) {
		_offerbook.delete(order.getPrice(),order);
		
	}
	
	/**remove the best price order from the book
	 * 
	 */
	@Override
	public void remove(){
		_offerbook.deleteMin();
	}
	
	public String toString(){
		return _offerbook.inOrderWalk();
	}

	@Override
	public boolean isEmpty(){
		return _offerbook.isEmpty();
	}
}
