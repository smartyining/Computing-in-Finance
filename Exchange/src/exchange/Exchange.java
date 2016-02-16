package exchange;

import java.util.HashMap;
import java.util.LinkedList;

import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import clientMessageFields.MarketId;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeMessageType;
import exchangeMessageFields.ExchangeOrderId;
import exchangeMessageFields.Explanation;
import exchangeMessageFields.FillPrice;
import messagesToClient.CancelRejected;
import messagesToClient.Cancelled;
import messagesToClient.Fill;
import messagesToClient.OrderAccepted;
import messagesToClient.OrderRejected;
import messagesToExchange.AbstractMessageToExchange;
import messagesToExchange.CancelOrder;
import messagesToExchange.GTCOrder;

public class Exchange {
	
	protected ClientConnection _clientConnection;
	protected HashMap<MarketId, OrderBook> _orderBooks;
	public LinkedList<AbstractMessageToExchange> _messagesReceived = new LinkedList<AbstractMessageToExchange>();
	protected ExchangeOrderId _id;
	protected HashMap<ExchangeOrderId,MarketId> _orderMap; //track accepted order
	
	public Exchange( ClientConnection clientConnection ) throws Exception {
		if( clientConnection == null )
			throw new Exception( "Argument is null" );
		_clientConnection = clientConnection;
		_orderBooks = new HashMap<MarketId,OrderBook>();
		_orderMap= new  HashMap<ExchangeOrderId,MarketId>();
		_id=new ExchangeOrderId(00000001L);//initiate ExchangeOrderId
	}
	
	protected ClientConnection getClientConnection() { return _clientConnection; }
	
	public void receiveMessage( AbstractMessageToExchange messageToExchange ) throws Exception {
		messageToExchange.getProcessedBy( this );
	}
	
	/** A GTCOrder has 3 results:
	 * Case 1  orderRejected: if marketId doesn't exist in orderbook
	 * Case 2  filled: if oppositeBook has an appropriate price
	 * Case 3 if exist unfilled part, this part goes to resting order:
	 * @param gtcOrder
	 * @throws Exception
	 */
	public void processGTC( GTCOrder gtcOrder ) throws Exception  {
		_messagesReceived.addLast( gtcOrder );	
		
		//Case 1 
		if(!_orderBooks.containsKey(gtcOrder.getMarketId())){
			Explanation explanation = new Explanation( " Order has a market id that cannot be found");
			OrderRejected orderRejected = new OrderRejected(gtcOrder.getClientId(), gtcOrder.getClientMessageId(),ExchangeMessageType.ORDER_REJECTED, explanation);			
			_clientConnection.sendMessage(orderRejected);			
		}			
		
		//Case 2
		else{
			OrderBook book=_orderBooks.get(gtcOrder.getMarketId());
			
			while (book.isAvailable(gtcOrder)){
				
				LinkedList<Object> fillInfo=book.fill(gtcOrder);
				FillPrice fillPrice=(FillPrice) fillInfo.remove();
				Quantity fillQuantity=(Quantity) fillInfo.remove();
				gtcOrder.fill(fillQuantity);	
			
				_id=_id.next();
				Fill fillMessage1 =new Fill(gtcOrder.getClientId(), gtcOrder.getClientMessageId(),ExchangeMessageType.FILL,_id, fillQuantity,fillPrice);
				Fill fillMessage2 =new Fill((ClientId) fillInfo.remove(),(ClientMessageId) fillInfo.remove(),ExchangeMessageType.FILL,_id, fillQuantity,fillPrice);
				_clientConnection.sendMessage(fillMessage1);
				_clientConnection.sendMessage(fillMessage2);		
				
				//if (gtcOrder.getQuantity()==0) break;
			}
			
			//Case 3
			if (gtcOrder.getQuantity()>0){
				_id=_id.next();
				_orderMap.put(_id,gtcOrder.getMarketId());
				book.enter(_id,gtcOrder);
				OrderAccepted orderAccepted=new	OrderAccepted(gtcOrder.getClientId(),gtcOrder.getClientMessageId(),ExchangeMessageType.ORDER_ACCEPTED,gtcOrder.getOrderQuantity(),_id);
				_clientConnection.sendMessage( orderAccepted);		
			}		
		}			
	}

	/** CancelOrder has two results:
	 * Case 1: CancelRejected : ExchangeOrderId is wrong ;Order no longer exist;
	 * Case 2: CancelAccepted
	 * @param cancelOrder
	 * @throws Exception
	 */
	public void processCancel( CancelOrder cancelOrder) throws Exception {
		_messagesReceived.addLast( cancelOrder );
		OrderBook book=_orderBooks.get(_orderMap.get(cancelOrder.getExchangeOrderId()));
		//Case 1:
		if (!_orderMap.containsKey(cancelOrder.getExchangeOrderId())){
			CancelRejected cancelRejected=new CancelRejected(cancelOrder.getClientId(),cancelOrder.getClientMessageId(),
					ExchangeMessageType.CANCEL_REJECTED, new Explanation( " ExchangeOrderId is wrong")); 
			_clientConnection.sendMessage( cancelRejected);
		}
		else{
			if (!book.contains(cancelOrder.getExchangeOrderId())){
				CancelRejected cancelRejected=new CancelRejected(cancelOrder.getClientId(),cancelOrder.getClientMessageId(),
						ExchangeMessageType.CANCEL_REJECTED, new Explanation( " Order no longer exist")); 
				_clientConnection.sendMessage( cancelRejected);
			}
			
			//Case 2
			else{
				book.cancel(cancelOrder.getExchangeOrderId());
				Cancelled cancelled=new Cancelled( cancelOrder.getClientId(), cancelOrder.getClientMessageId(),
						ExchangeMessageType.CANCELLED, _id.next());
				_clientConnection.sendMessage(cancelled);
			}
		}
		
		
	}

	public LinkedList<AbstractMessageToExchange> getMessagesReceived() { return _messagesReceived; }

	/**add new marketid to exchange
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void addMarket(MarketId id) throws Exception{
		OrderBook book=new OrderBook(id);
		_orderBooks.put(id, book);
		
	}
}
