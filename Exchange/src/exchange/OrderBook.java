package exchange;

import java.util.HashMap;
import java.util.LinkedList;
import clientMessageFields.MarketId;
import clientMessageFields.OrderSide;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeOrderId;
import exchangeMessageFields.FillPrice;
import messagesToExchange.GTCOrder;

/** Data Structure for offer/bid Book: BinaryTree+Linkedlist. This allow O(logn) find/insert
 *  OrderBook also has a HashMap for restingOrder, This allow O(1) to find the order 
 *  and then O(logn) to cancell it in the tree
 * 
 * @author yininggao
 *
 */
public class OrderBook {
	
	private OfferBook _offerBook;
	private BidBook _bidBook;
	private HashMap<ExchangeOrderId,RestingOrder> _orderMap; //track resting order
	private MarketId _marketId;

	public OrderBook(MarketId marketId) throws Exception {
		if( marketId == null )
			throw new Exception( "Argument is null" );
		_marketId = marketId;
		_offerBook=new OfferBook();
		_bidBook=new BidBook();
		_orderMap=new HashMap<ExchangeOrderId,RestingOrder>();
	}

	public MarketId getMarketId() {
		return _marketId;
	}
	
	//check if a order still exist
	public boolean contains(ExchangeOrderId id){
		return  _orderMap.containsKey(id);
	}
	
	public RestingOrder getOrder(ExchangeOrderId id){
		return _orderMap.get(id);
	}
	
	public I_Book getOppositeBook(GTCOrder order){
		OrderSide side=order.getOrderSide();
		I_Book oppositeBook;			
		if(side==OrderSide.BUY){ oppositeBook=this._offerBook; }
		else{ oppositeBook=this._bidBook;}
		return oppositeBook;
	}
	
	public I_Book getBook(GTCOrder order){
		OrderSide side=order.getOrderSide();
		I_Book book;			
		if(side==OrderSide.BUY){book=this._bidBook; }
		else{ book=this._offerBook;}
		return book;
	}
	public I_Book getBook(RestingOrder order){
		OrderSide side=order.getOrderSide();
		I_Book book;			
		if(side==OrderSide.BUY){book=this._bidBook; }
		else{ book=this._offerBook;}
		return book;
	}
	/** Determine if a GTCOrder can be filled 
	 * 
	 * @param order
	 * @return
	 */
	public boolean isAvailable(GTCOrder order){
		I_Book oppositeBook=getOppositeBook(order);
		
		if (oppositeBook.getOrder()==null) return false;
		else if(!(order.getQuantity()>0)) return false; 
		else if(!((order.getOrderSide()==OrderSide.BUY && order.getPrice()>= oppositeBook.getOrder().getPrice())
					|| (order.getOrderSide()==OrderSide.SELL && order.getPrice() <= oppositeBook.getOrder().getPrice())))
			 return false;
		else return true;
		}

	
    /** Fill a resting order
     * 
     * @param order
     * @return 
     * @return FillPrice, Quantity, FilledRestingOrder ClientID, ClientMessageID
     * @throws Exception
     */
	public LinkedList<Object> fill(GTCOrder order) throws Exception{
		LinkedList<Object> list=new LinkedList<Object>();
		I_Book oppositeBook=getOppositeBook(order);
		FillPrice fillPrice=new FillPrice(oppositeBook.getOrder().getPrice());
		Quantity fillQuantity=new Quantity(Math.min(order.getQuantity(), oppositeBook.getOrder().getQuantity()));
			
		list.add(fillPrice);
		list.add(fillQuantity);
		list.add(oppositeBook.getOrder().getClientId());
		list.add(oppositeBook.getOrder().getClientMessageId());
		
		//remove resting order from _orderMap
		_orderMap.remove(oppositeBook.getOrder().getExchangeOrderId());
		
		//substract quantity of resting order
		RestingOrder restingOrder=oppositeBook.getOrder();
		restingOrder.fill(fillQuantity);
		
		//remove resting order from oppositeBook if quantity=0
		if (!(oppositeBook.getOrder().getQuantity()>0)) oppositeBook.remove();		
		return list;	
	}
	
	/** Add new resting order to book
	 * 
	 * @param id
	 * @param order
	 * @throws Exception
	 */
	public void enter(ExchangeOrderId id,GTCOrder order) throws Exception {
		I_Book book=getBook(order);
		RestingOrder restingOrder=new RestingOrder(id,order);
		//add to book
		book.add(restingOrder);
		//add to _orderMap
		_orderMap.put(id, restingOrder);
	}

	
	public void cancel(ExchangeOrderId id) {
		//delete from the book
		I_Book book=getBook(_orderMap.get(id));
		book.delete(_orderMap.get(id));
		
		//delete from _orderMap
		_orderMap.remove(id);
	}
	
	/**Print the orderbook 
	 * 
	 */
	public String toString(){
		String str="OfferBook:\n"+_offerBook.toString()
				+"BidBook:\n"+_bidBook.toString();
		return str;
	}
	
	public int getOrderNum(){
		return _orderMap.size();
	}

}
