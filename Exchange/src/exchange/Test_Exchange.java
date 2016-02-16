package exchange;

import messagesToClient.CancelRejected;
import messagesToClient.Cancelled;
import messagesToClient.Fill;
import messagesToClient.OrderAccepted;
import messagesToClient.OrderRejected;
import messagesToExchange.CancelOrder;
import messagesToExchange.GTCOrder;
import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import clientMessageFields.LimitPrice;
import clientMessageFields.MarketId;
import clientMessageFields.OrderMessageType;
import clientMessageFields.OrderSide;
import clientMessageFields.Quantity;
import exchangeMessageFields.ExchangeMessageType;
import exchangeMessageFields.ExchangeOrderId;
import exchangeMessageFields.Explanation;
import exchangeMessageFields.FillPrice;

public class Test_Exchange extends junit.framework.TestCase {
	
	public void test1() throws Exception {
		// Instantiate client connection, which is used by exchange to communicate
		// with clients
			ClientConnection clientConnection = new ClientConnection();
		// Instantiate exchange using client connection
			// For debugging purposes, we want an exchange that
			// saves all all of the messages it receives so in our
			// unit test, we can check what it received
			Exchange exchange = new Exchange( clientConnection );
		// Make sure exchange saves client connection
			assertTrue( exchange.getClientConnection() == clientConnection );
		// Instantiate exchange connection, which is used by clients to communicate
		// with exchange
			ExchangeConnection exchangeConnection = new ExchangeConnection(exchange);
		// Instantiate one client
			ClientId clientId1 = new ClientId( 13472L );
			Client client1 = new Client( clientId1, exchangeConnection );
		// Make sure client saves client id
			assertTrue( client1.getClientId().equals( clientId1 ) );
		// Make sure client saved echange connection
			assertTrue( client1.getExchangeConnection() == exchangeConnection );
		// Add client to client connection
			clientConnection.addClient( client1 );
		// Make sure client connection saved client
			Client retrievedClient = clientConnection.getClient( clientId1 );
			assertTrue( retrievedClient == client1 );
		// Instantiate a good till cancelled order to send to exchange
			// Instantiate client message id - This is the id that clients will use
			// to identify their message when the exchange sends them some
			// response regarding their message
				Long clientMessageIdValue1 = new Long( 999L );
				ClientMessageId clientMessageId1 = new ClientMessageId( clientMessageIdValue1 );
			// Make sure client message id saved the id value
				assertTrue( clientMessageId1.getValue().equals( clientMessageIdValue1 ) );
			// Instantiate a market id
				String marketName1 = "IBM";
				MarketId marketId1 = new MarketId( marketName1 );
			// Make sure market id saved market name
				assertTrue( marketId1.getValue().equals( marketName1 ) );
			// Instantiate an order side
				OrderSide orderSide1 = OrderSide.BUY;
			// Instantiate an order quantity
				Long orderQuantityValue1 = 200L;
				Quantity quantity1 = new Quantity( orderQuantityValue1 );
			// Make sure order quantity save order quantity value
				assertTrue( quantity1.getValue().equals( orderQuantityValue1 ) );
			// Instantiate a limit price
				Double limitPriceValue1 = new Double( 100D );
				LimitPrice limitPrice1 = new LimitPrice( limitPriceValue1 );
			// Make sure that limit price saved the limit price value
				assertEquals( limitPrice1.getValue(), limitPriceValue1, 0.001 );
			GTCOrder gtcOrder1 = new GTCOrder(
				clientId1, 
				clientMessageId1, 
				OrderMessageType.GTC, 
				marketId1, 
				orderSide1, 
				quantity1, 
				limitPrice1
			);
		// Send message to exchange
			exchangeConnection.sendMessage( gtcOrder1 );
		// Make sure that exchange received the message we sent to it
			GTCOrder receivedOrder = (GTCOrder) exchange.getMessagesReceived().removeFirst();
			assertTrue( receivedOrder == gtcOrder1 );
		// We now test sending a cancel order to the exchange.
		// First we instantiate a cancel order.
			// We can re-use the clientId from the previous example
			// We can re-use the clientMessageId from the previous example
			// Make a message type
				OrderMessageType messageType2 = OrderMessageType.CANCEL;
			// Make an exchange order id
				Long exchangeOrderIdValue1 = 334455L;
				ExchangeOrderId exchangeOrderId1 = new ExchangeOrderId( exchangeOrderIdValue1 );
			// Make sure the exchange order id saved the exchange order id value
				assertTrue( exchangeOrderId1.getValue().equals( exchangeOrderIdValue1 ) );
			CancelOrder cancelOrder1 = new CancelOrder( clientId1, clientMessageId1, messageType2, exchangeOrderId1 );
		// We send the cancel order to the exchange
			exchangeConnection.sendMessage( cancelOrder1 );
		// Did the exchange receive and save this cancel order?
			assertTrue( exchange.getMessagesReceived().removeFirst() == cancelOrder1 );
		// We will now try sending messages to the client just
		// as the exchange would. The client is already in the
		// client connection's map of clients so
		// First, we instantiate a CancelRejected message
			// We can re-use the clientId
			// We can re-use the clientMessageId
			// We instantiate a new message type
				ExchangeMessageType exchangeMessageType1 = ExchangeMessageType.CANCEL_REJECTED;
			// We instantiate a new explanation for why the error happened
				String explanationText1 = "No such exchange order id";
				Explanation explanation1 = new Explanation( explanationText1 );
			// We make sure that the explanation text was saved
				assertTrue( explanation1.getValue().equals( explanationText1 ) );
			CancelRejected cancelRejected1 = new CancelRejected( clientId1, clientMessageId1, exchangeMessageType1, explanation1 );
		// We try sending this message as if it were being sent by the exchange
			clientConnection.sendMessage( cancelRejected1 );
		// We confirm that the client received the message we sent
			assertTrue( client1.getMessagesFromExchange().removeFirst() == cancelRejected1 );
		// Now, we instantiate a Cancelled message
			// We can re-use the client id
			// We can re-use the client message id
			// We instantiate an exchange message type for this order
				ExchangeMessageType exchangeMessageType2 = ExchangeMessageType.CANCELLED;
			// We can re-use the exchange order id
			Cancelled cancelled1 = new Cancelled( clientId1, clientMessageId1, exchangeMessageType2, exchangeOrderId1 );
		// We try sending this message to the client
			clientConnection.sendMessage( cancelled1 );
		// We confirm that the client received the message we sent
			assertTrue( client1.getMessagesFromExchange().removeFirst() == cancelled1 );
		// Now, we instantiate a Fill message
			// We can re-use client id
			// We can re-use client message id
			// We instantiate a new message type
				ExchangeMessageType exchangeMessageType3 = ExchangeMessageType.FILL;
			// We instantiate a quantity field
				Quantity quantity2 = new Quantity( 50L );
			// We instantiate a fill price field
				Double fillPriceValue = 89.95D;
				FillPrice fillPrice1 = new FillPrice( fillPriceValue );
			// We make sure fill price saved the fill price value
				assertEquals( fillPrice1.getValue(), fillPriceValue, 0.0001 );
			Fill fill1 = new Fill( clientId1, clientMessageId1, exchangeMessageType3, exchangeOrderId1, quantity2, fillPrice1 );
		// We try sending this message to the client
			clientConnection.sendMessage( fill1 );
		// We confirm that the client received the message
			assertTrue( client1.getMessagesFromExchange().removeFirst() == fill1 );
		// Now, we instantiate an order rejected message
			// We can re-use the client id
			// We can re-use the client message id
			// We instantiate a new message type
				ExchangeMessageType exchangeMessageType4 = ExchangeMessageType.ORDER_REJECTED;
			// We instantiate a new explanation
				String explanationText2 = "No such market";
				Explanation explanation2 = new Explanation( explanationText2 );
			OrderRejected orderRejected1 = new OrderRejected( clientId1, clientMessageId1, exchangeMessageType4, explanation2 );
		// We try sending this message to the client
			clientConnection.sendMessage( orderRejected1 );
		// We confirm that the client received the message
			assertTrue( client1.getMessagesFromExchange().removeFirst() == orderRejected1 );
		// Now, we instantiate an order accepted message
			// We can reuse client id
			// We can reuse client message id
			// We need a new exchange message type
				ExchangeMessageType exchangeMessageType5 = ExchangeMessageType.ORDER_ACCEPTED;
			// We need a new quantity
				Quantity quantity3 = new Quantity( 100L );
			// We need a new exchange order id
				ExchangeOrderId exchangeOrderId2 = new ExchangeOrderId( 9987L );
			OrderAccepted orderAccepted = new OrderAccepted(
					clientId1, 
					clientMessageId1,
					exchangeMessageType5,
					quantity3, // The quantity that was actually made into a resting order
					exchangeOrderId2
			);
		// We try sending the order to the client
			clientConnection.sendMessage( orderAccepted );
		// We confirm that the client received the order
			assertTrue( client1.getMessagesFromExchange().removeFirst() == orderAccepted );
	} // test1
	
}
