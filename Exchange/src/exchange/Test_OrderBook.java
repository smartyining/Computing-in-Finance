package exchange;

import java.util.LinkedList;

import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import clientMessageFields.LimitPrice;
import clientMessageFields.MarketId;
import clientMessageFields.OrderMessageType;
import clientMessageFields.OrderSide;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeOrderId;
import exchangeMessageFields.FillPrice;
import junit.framework.TestCase;
import messagesToExchange.GTCOrder;

public class Test_OrderBook extends TestCase {
	
	public void test1() throws Exception{
	// Instantiate a market id
		String marketName1 = "IBM";
		MarketId marketId1 = new MarketId( marketName1 );
		
	//Instantiate an orderbook
		OrderBook orderBook=new OrderBook(marketId1);
	
	//Instantiate an GTO order buy 200qty @100price
		GTCOrder gtcOrder1 = new GTCOrder(
				new ClientId( 13472L ), 
				new ClientMessageId( new Long( 999L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(200L), 
				new LimitPrice(  new Double( 100D ) )
			);	
		
	//Test isAvalable
		assertFalse(orderBook.isAvailable(gtcOrder1));
	//Instantiate a ExchangeOrderId
		Long exchangeOrderIdValue1 = 334455L;
		ExchangeOrderId exchangeOrderId1 = new ExchangeOrderId( exchangeOrderIdValue1 );
	
	//Test enter
		orderBook.enter(exchangeOrderId1, gtcOrder1);
		assertTrue(orderBook.getBook(gtcOrder1).getOrder().getQuantity().equals(200L));		
		assertTrue(orderBook.contains(exchangeOrderId1 ));
	
	//Instantiate an GTO order :sell 300qty at 100price
		GTCOrder gtcOrder2 = new GTCOrder(
				new ClientId( 13472L ), 
				new ClientMessageId( new Long( 999L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(300L), 
				new LimitPrice(  new Double( 100D ) )
			);	
		
	//Test fill
		assertTrue(orderBook.isAvailable(gtcOrder2));
		
		LinkedList<Object> list=orderBook.fill(gtcOrder2);
		
		assertTrue(((FillPrice) list.remove()).getValue()== 100D);
		assertTrue(((Quantity) list.remove()).getValue()== 200L);
		
		assertFalse(orderBook.contains(exchangeOrderId1));
		assertTrue(orderBook.getBook(gtcOrder1).isEmpty());
		
	//Test cancel
		orderBook.enter(exchangeOrderId1, gtcOrder1);

		assertFalse(orderBook.getBook(gtcOrder1).isEmpty());
		orderBook.cancel(exchangeOrderId1);
		assertTrue(orderBook.getBook(gtcOrder1).isEmpty());

	}

}
