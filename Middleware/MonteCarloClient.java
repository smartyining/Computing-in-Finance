import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import mySolution.*;
import mySolution.Option.OptionBuilder;

/** 
 * Client side has a consumer listen to the server's queue, once it receives a message, it launches 1000 simulations
 * A producer will send each payout in a textMessage back to server over an unique topic
 * 
 * @author yininggao
 *
 */
public class MonteCarloClient  {
	
	   private static String brokerURL = "tcp://localhost:61616";
	   private static ConnectionFactory factory;
	   private Connection connection;
	   private Session session;
	   private I_StockPrice generator;
	   private I_PayOut payout;
	   protected int id; // Client side has unique id
	   protected  MessageConsumer consumer; // To receive message
	   protected MessageProducer producer; // To send message
	   
	   // Constructor 
	   public MonteCarloClient(int id) throws Exception {
		      factory = new ActiveMQConnectionFactory(brokerURL);
		      connection = factory.createConnection();
		      connection.start();
		      session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		      this.id=id;
		   }
	   
	   
	   public void run() throws JMSException  {
		   // Set consumer, listenning to the queue
		   Destination destination = session.createQueue("Simulation 1");
		   consumer = session.createConsumer(destination);		   
		   consumer.setMessageListener(new MessageListener() {
			   
		   public void onMessage(Message message) {					   
	            if (message instanceof MapMessage){	 
                	// Producer sends payout back over  a specific topic
					try {					
						String	returnTo = ((MapMessage) message).getString("returnTo");					
						Topic returnTopic = session.createTopic(returnTo);
						producer = session.createProducer(returnTopic);	                 
						// Run simulation
						Option option=convertToOption((MapMessage) message);		                 
						for(int i=0;i<1000;i++ ){
							double payout=runSimulation(option);
							System.out.println(payout);
							TextMessage returnMessage = session.createTextMessage(Double.toString(payout));
							producer.send(returnMessage);			                 
						} 
						producer.close();
						message.acknowledge();	
						
					} catch (JMSException e) {
						e.printStackTrace();
					}
	            } 
	      }});
		   	   
      }	

	   // Run simulation using classes from MonteCarlo Assignments
	   public double runSimulation(Option option) throws JMSException{		    	
		    generator=new StockPathGenerator (option);				
		    if (option.getType()=="Asian")
				payout=new AsianCallPayOut(option);	
			else
				payout=new CallPayOut(option);	   
		    StockPath currentPath=generator.getPath();		    			    
			return  payout.getPayout(currentPath);						
	   }	   
	   
	   
	   // Convert a Map Message back into an option
	   // In order to run simulation
	   private Option convertToOption(MapMessage message) throws JMSException {
   			OptionBuilder option = new Option.OptionBuilder();
			option.setName(message.getString("name"));
	    	option.setInterestRate(message.getDouble("interestRate"));
	    	option.setVolatility(message.getDouble("volatility"));
	    	option.setStrickPrice(message.getDouble("strickPrice" ));
			option.setPeriod(message.getInt("period"));
			option.setInitialPrice(message.getDouble("initialPrice" ));
			option.setType(message.getString("type"));			
			return option.build();

	}
	   
	   // Creat a new client and run it;
	   // You can create more;
	   public static void main(String[] args) throws Exception{
		   MonteCarloClient client = new MonteCarloClient(1);
		   client.run();
	 }


}
