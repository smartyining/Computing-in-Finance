package exchange;

import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import clientMessageFields.LimitPrice;
import clientMessageFields.MarketId;
import clientMessageFields.OrderSide;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeOrderId;
import messagesToExchange.GTCOrder;

public class RestingOrder {
	private Quantity   _orderQuantity;
	private OrderSide  _orderSide;
	private LimitPrice _limitPrice;
	private MarketId   _marketId;
	private ExchangeOrderId _exchangeOrderId;  //id sent when order is accepted
	private ClientId 		_clientId; 
	private ClientMessageId	_clientMessageId;
	
	public RestingOrder(ExchangeOrderId id, GTCOrder order){
		_exchangeOrderId=id;
		_marketId=order.getMarketId();
		_limitPrice=order.getLimitPrice();
		_orderSide=order.getOrderSide();
		_orderQuantity=order.getOrderQuantity();
		_clientId=order.getClientId();
		_clientMessageId=order.getClientMessageId();
	}
	
	public ExchangeOrderId getExchangeOrderId(){
		return _exchangeOrderId;
	}
	
	public Long getQuantity() {
		return _orderQuantity.getValue();
	}

	public Quantity getOrderQuantity() {
		return _orderQuantity;
	}
	
	public OrderSide getOrderSide() {
		return _orderSide;
	}
	public LimitPrice getLimitPrice(){
		return _limitPrice;
	}
	public Double getPrice() {
		return _limitPrice.getValue();
	}
	
	public MarketId getMarketId() { return _marketId; }
	
	public ClientId getClientId() {
		return _clientId;
	}

	public ClientMessageId getClientMessageId() {
		return _clientMessageId;
	}
	
	//when resting order is filled, its quantity change value
	public void fill(Quantity quantity){
		_orderQuantity.setValue(_orderQuantity.getValue()-quantity.getValue());		
	}
	
	public String toString(){	
		String string= "Price:"+this.getLimitPrice().getValue()+" Quantity:"+this.getQuantity()+" ClientId:"+this.getClientId().getValue()+" ExchangeOrderId:"+this.getExchangeOrderId().getValue();
		return string;
	}

}
