package exchange;

import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import clientMessageFields.LimitPrice;
import clientMessageFields.MarketId;
import clientMessageFields.OrderMessageType;
import clientMessageFields.OrderSide;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeMessageType;
import exchangeMessageFields.ExchangeOrderId;
import junit.framework.TestCase;
import messagesToClient.Fill;
import messagesToClient.OrderAccepted;
import messagesToClient.OrderRejected;
import messagesToExchange.CancelOrder;
import messagesToExchange.GTCOrder;

/** 
 * 
 * @author yininggao
 *
 */
public class Test_MyExchange extends TestCase {
	
	public void test1() throws Exception{
		
		// Instantiatation
		Exchange exchange = new Exchange( new ClientConnection());
		ExchangeConnection exchangeConnection = new ExchangeConnection(exchange);		
		ClientId clientId1 = new ClientId( 13472L );
		ClientId clientId2 = new ClientId( 13470L );
		Client client1 = new Client( clientId1, exchangeConnection );
		Client client2 = new Client( clientId2, exchangeConnection );
		exchange._clientConnection.addClient( client1 );
		exchange._clientConnection.addClient( client2 );
		
		// Instantiate a market id
		String marketName1 = "IBM";
		MarketId marketId1 = new MarketId( marketName1 );
		
		// Instantiate a market id
		String marketName2 = "APPLE";
		MarketId marketId2 = new MarketId( marketName2 );		
		
		// Initialize exchange : add marketid
		exchange.addMarket(marketId1);
		
		// Instantiate a GTCorder whose marketid doesn't exsit in exchange
		GTCOrder gtcOrder1 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 990L ) ), 
				OrderMessageType.GTC, 
				marketId2, 
				OrderSide.BUY, 
				new Quantity(200L), 
				new LimitPrice(  new Double( 100D ) )
			);	
		
		// Instantiate a buy GTCorder from client 1
		GTCOrder gtcOrder2 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 991L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(200L), 
				new LimitPrice(  new Double( 100D ) )
			);	
		
		// Instantiate a sell GTCorder from Client 2
		GTCOrder gtcOrder3 = new GTCOrder(
				clientId2, 
				new ClientMessageId( new Long( 992L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(400L), 
				new LimitPrice(  new Double( 100D ) )
			);	
				
		//testOrderRejected: this part has also been tested in Test_Exchange  
		//gtcOrder1 is rejected because "APPLE" doesn't exist in exchange hashmap
		exchangeConnection.sendMessage( gtcOrder1 );				
		OrderRejected message= (OrderRejected)client1.getMessagesFromExchange().remove();
		assertTrue(message.getMessageType()==ExchangeMessageType.ORDER_REJECTED);
							
		//Now new orders come:
		exchangeConnection.sendMessage( gtcOrder2 );//Client 1 buy IBM  @100 200qty ,
		
	    //Print out orderbook to see if correct
		System.out.println("========After ClientId 13472L buy IBM  @100 200qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
				
		exchangeConnection.sendMessage( gtcOrder3 );//Client 2 sell IBM @100 400qty
		System.out.println("========After ClientId 13470L sell IBM  @100 400qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		//there should be four messages sent to clients:
		OrderAccepted acceptMessage= (OrderAccepted)client1.getMessagesFromExchange().remove();
		Fill fillMessage1= (Fill)client1.getMessagesFromExchange().remove();
		Fill fillMessage2= (Fill)client2.getMessagesFromExchange().remove();
		OrderAccepted acceptMessage2= (OrderAccepted)client2.getMessagesFromExchange().remove();
		
		assertTrue(acceptMessage.getMessageType()==ExchangeMessageType.ORDER_ACCEPTED);//client1 should receive accept message  
		
		//there should be 200 fill
		assertTrue(fillMessage1.getQuantity().getValue().equals(200L));
		assertTrue(fillMessage1.getMessageType()==ExchangeMessageType.FILL);//client1 should receive fill message    
	    assertTrue(fillMessage2.getMessageType()==ExchangeMessageType.FILL);//client2 should receive fill message 
	    
	    assertTrue(acceptMessage2.getMessageType()==ExchangeMessageType.ORDER_ACCEPTED);//client2 should receive accept message  
	    
	    //exchangeOrderId should be 2,3,3,4(as I set in exchangeOrderId next() method to generate next exchangeOrderID automatically by adding 1)
	    assertTrue(acceptMessage.getExchangeOrderId().getValue().equals(2L));
	    assertTrue(fillMessage1.getExchangeOrderId().getValue().equals(3L));
	    assertTrue(fillMessage2.getExchangeOrderId().getValue().equals(3L)); //same exchangeOrderId with the last order
	    assertTrue(acceptMessage2.getExchangeOrderId().getValue().equals(4L));
	    
	   //exchange should add accepted exchangeOrderId to orderidmap
	    assertTrue(exchange._orderMap.get(acceptMessage.getExchangeOrderId())==marketId1); 
	    assertTrue(exchange._orderMap.get(acceptMessage2.getExchangeOrderId())==marketId1); 
	 
	    //Orderbook exchangOrderId hashmap should now have 1 resting order
	   // System.out.println("\nThere's now "+exchange._orderBooks.get(marketId1).getOrderNum()+" resting orders in the book:");
	   // System.out.println(exchange._orderBooks.get(marketId1).getOrder(acceptMessage2.getExchangeOrderId()).toString()+"\n");
	    
	    
		//adding more orders
		ClientId clientId3 = new ClientId( 13474L );
		ClientId clientId4 = new ClientId( 13476L );
		Client client3 = new Client( clientId3, exchangeConnection );
		Client client4 = new Client( clientId4, exchangeConnection );
		exchange._clientConnection.addClient( client3 );
		exchange._clientConnection.addClient( client4 );
		ClientMessageId messageId1= new ClientMessageId( new Long( 996L ) );
		
	    // ClientId 13474L buy @99.0 400qty
		GTCOrder gtcOrder4 = new GTCOrder(
				clientId3 ,
				 messageId1, 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(400L), 
				new LimitPrice(  new Double( 99D ) )
			);	
		
		exchangeConnection.sendMessage( gtcOrder4);
		client3.getMessagesFromExchange().remove();
		System.out.println("========After ClientId 13474L buy IBM @99.0 400qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		
	    // ClientId 13474L buy @99.9 400qty
		// This order is to test if 99.9 order would be placed above 99.0 order
		GTCOrder gtcOrder5 = new GTCOrder(
				clientId3 ,
				new ClientMessageId( new Long( 997L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(300L), 
				new LimitPrice(  new Double( 99.9D ) )
			);	

		exchangeConnection.sendMessage( gtcOrder5);
		client3.getMessagesFromExchange().remove();
		System.out.println("========After ClientId 13474L buy IBM @99.9 300qty=========");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		
	    // ClientId 13474L sell @129.9 100qty
		// This order is to test if 129.9 order would be placed below 100.0 order
		GTCOrder gtcOrder6 = new GTCOrder(
				clientId3 ,
				new ClientMessageId( new Long( 998L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(100L), 
				new LimitPrice(  new Double( 129.9D ) )
			);	
	
		exchangeConnection.sendMessage( gtcOrder6);
		client3.getMessagesFromExchange().remove();
		System.out.println("========After ClientId 13474L sell IBM @129.9 100qty=========");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		//now comes a sell @99, 500qty order:
		//This is to test if two orders in bidbook will reflect respectively 
		//: OrderId 6 should be filled and OrderId 5 should be partially filled
	    // ClientId 13474L buy @99.9 400qty
		// This order is to test if 99.9 order would be placed above 99.0 order
		GTCOrder gtcOrder7 = new GTCOrder(
				clientId4 ,
				new ClientMessageId( new Long( 1000L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(500L), 
				new LimitPrice(  new Double(99D ) )
			);	
		
		exchangeConnection.sendMessage( gtcOrder7);
		client3.getMessagesFromExchange().remove();//2 message in fill for client3
		client3.getMessagesFromExchange().remove();
		
		System.out.println("========After ClientId 13476L sell @99, 500qty =========");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		//now test a buy @125 250qty order
		//this is to test that an order can be partially filled and the rest goes to resting order
		//200 should be filled and 50 goes to bidbook
		GTCOrder gtcOrder8 = new GTCOrder(
				clientId4 ,
				new ClientMessageId( new Long( 1002L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(250L), 
				new LimitPrice(  new Double(125D ) )
			);	
		
		exchangeConnection.sendMessage( gtcOrder8);
		
		System.out.println("========After ClientId 13476L sell @99, 500qty =========");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
			
		
		//Test CANCEL order situation
		//test a partcially filled resting order can'be cancelled
		//in this case exchangeOrderId 5 can't be cancelled
		CancelOrder cancelorder1=new CancelOrder(
				clientId3,
				messageId1,
				OrderMessageType.CANCEL,
				new ExchangeOrderId(5L)
			) ;
		
		exchangeConnection.sendMessage(cancelorder1);
		assertTrue(client3.getMessagesFromExchange().remove().getMessageType().equals(ExchangeMessageType.CANCEL_REJECTED));
		
		
		//test a cancel success: exchangeOrderId 
		//add more gtcorder
		GTCOrder gtcOrder9 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 991L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(200L), 
				new LimitPrice(  new Double( 100D ) )
			);	
		exchangeConnection.sendMessage( gtcOrder9 );//Client 1 buy IBM  @100 200qty ,
		client1.getMessagesFromExchange().remove(); //remove accept message
		
		System.out.println("========After ClientId 13472L buy IBM  @100 200qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		//Cancel rejected: if exchangeOrderID don't exist
		CancelOrder cancelorder2=new CancelOrder(
				clientId1,
				new ClientMessageId( new Long( 991L ) ),
				OrderMessageType.CANCEL,
				new ExchangeOrderId(19L)
			) ;
		
		exchangeConnection.sendMessage(cancelorder2);
		assertTrue(client1.getMessagesFromExchange().remove().getMessageType().equals(ExchangeMessageType.CANCEL_REJECTED));
	
		//cancel this order
		CancelOrder cancelorder3=new CancelOrder(
				clientId1,
				new ClientMessageId( new Long( 991L ) ),
				OrderMessageType.CANCEL,
				new ExchangeOrderId(12L)
			) ;
		
		exchangeConnection.sendMessage(cancelorder3);
		assertTrue(client1.getMessagesFromExchange().remove().getMessageType().equals(ExchangeMessageType.CANCELLED));
	
		System.out.println("========After ClientId 13472L cancel  exchangeOrderId 12=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
		
		//test more orderbook quality
		//when resting order at the same price: time priority
		GTCOrder gtcOrder10 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 1004L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(130L), 
				new LimitPrice(  new Double( 125D ) )
			);	
		exchangeConnection.sendMessage( gtcOrder10 );//Client 1 buy IBM  @100 200qty ,
		
		System.out.println("========After ClientId 13472L buy IBM  @125 130qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
	
		//Price priority
		GTCOrder gtcOrder11 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 1006L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.BUY, 
				new Quantity(30L), 
				new LimitPrice(  new Double( 128D ) )
			);	
		exchangeConnection.sendMessage( gtcOrder11 );//Client 1 buy IBM  @100 200qty ,
		
		System.out.println("========After ClientId 13472L buy IBM  @128 30qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
	
		//Price priority
		GTCOrder gtcOrder12 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 1006L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(30L), 
				new LimitPrice(  new Double( 129.3D ) )
			);	
		exchangeConnection.sendMessage( gtcOrder12 );//Client 1 buy IBM  @100 200qty ,
		
		System.out.println("========After ClientId 13472L sell IBM  @129.3 30qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
	
		//fill all resting order
		GTCOrder gtcOrder13 = new GTCOrder(
				clientId1, 
				new ClientMessageId( new Long( 1008L ) ), 
				OrderMessageType.GTC, 
				marketId1, 
				OrderSide.SELL, 
				new Quantity(500L), 
				new LimitPrice(  new Double( 99D ) )
			);	
		exchangeConnection.sendMessage( gtcOrder13 );//Client 1 buy IBM  @100 200qty ,
		
		System.out.println("========After ClientId 13472L sell IBM  @99 500qty=========\n");
		System.out.println(exchange._orderBooks.get(marketId1).toString());
	
	}

}
