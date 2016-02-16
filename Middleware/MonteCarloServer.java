import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import mySolution.*;

/**
 * Once an option is added, Server maps it into a mapMessage and
 * Server send a batch simulation requests on a queue
 * Server has consumer listening to results over unique topic
 * @author yininggao
 *
 */
public class MonteCarloServer  {
	
	private StatsCollector stats; 
    private String brokerURL = "tcp://localhost:61616";
    private ConnectionFactory factory;
    private Connection connection;
    private static Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private MapMessage message;
    private int batchSize=100;  // Batch size
    private double prob=0.96; 
    private double accuracy=0.1;
    private double r;
    private int period;
    private int inqueue;// Make sure only one batch in queue
    
    // Constructor
    public MonteCarloServer() throws Exception{   	
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        stats=new StatsCollector();
        inqueue=0;
    }
    
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
      
    /** Convert each Option into a mapMessage
     * 
     * @param option
     * @throws JMSException
     */
    public void addOption(Option option) throws Exception{
    	r=option.getInterestRate();
    	period=option.getPeriod();
    	
		message = session.createMapMessage();			
		message.setString("name", option.getName());
		message.setDouble("volatility", option.getVolatility());
		message.setDouble("strickPrice", option.getStrickPrice());
		message.setInt("period", option.getPeriod());
		message.setDouble("initialPrice", option.getInitialPrice());		
		message.setInt("startDate", option.getStartDate());		
		message.setString("type", option.getType());
		message.setDouble("interestRate", option.getInterestRate());		
		
    }
	
	public double run() throws Exception{ 
		 // Create a producer for sending message
	      Destination destination = session.createQueue("Simulation 1");	     
	      producer = session.createProducer(destination);  
	      // Set a return topic name for the option, include it in the message
	      String returnTopicName = "Results:"+message.getString("name")+message.getString("type");	     
       	  message.setString("returnTo", returnTopicName);
          // Create a consumer listening  the returned payout over specific topic	
 	      Topic returnTopic = session.createTopic(returnTopicName);// Channel for clients sending message
 	      consumer= session.createConsumer(returnTopic);
 	      
       	  while(!isFinished()){
	       	  if(inqueue==0){// If inqueue=1, there's already one batch request waiting for client	       	  
		       	  // Send requests 
		          for (int i = 0; i< batchSize; i++){		            	  
		       	      producer.send(message);                             
			      } 
		          inqueue++;
		          System.out.println("One batch simulation request sent"); 
	       	  }         	  
	      
	       	consumer.setMessageListener(new MessageListener() {
 	         public void onMessage(Message message) { 
 	            if (message instanceof TextMessage){
 	               try{
 	            	   // Print payout to the screen
 	            	  System.out.println(Double.parseDouble( ((TextMessage) message).getText()));
 	                  stats.add(Double.parseDouble( ((TextMessage) message).getText())); 
 	                  message.acknowledge();
 	               } catch (JMSException e ){
 	                  e.printStackTrace();
 	               }
 	            }
 	         }
 	      });		  
       	  } 
       	  
	    System.out.println("Finished!");
   		producer.close();
        consumer.close();   	         
       	return stats.getMean()* Math.exp(-r*period);   	      
	 }

	//Calculate accuracy and determine whether to launch another batch
	public boolean isFinished(){		
		class bound{
			public double getBound(){
				double p=(1.0-prob)/2;
				double c0=2.515517;
				double c1=0.802853;
				double c2=0.010328;
				double d1=1.432788;
				double d2=0.189269;
				double d3=0.001308;
				
				double t=Math.sqrt(Math.log(1/(p*p)));
				double bound= t-(c0+c1*t+c2*t*t)/(1+d1*t+d2*t*t+d3*t*t*t);
				return bound;
			}
		}	
		if ((new bound()).getBound()*stats.getStd()/Math.sqrt(stats.getSize())<accuracy && stats.getSize()>100)
			return true;
		else
			return false;		
	}
	
	/**
	 * Start new servers
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	    MonteCarloServer server = new MonteCarloServer();       
    	//1.option: IBM, S_0:152.35, \sigma:0.01, period:252, K:165, Type:Europe		
		Option Option1=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();	
	    
		server.addOption(Option1);		    
	    System.out.println("Simulation task added, please add at least one client.");
        double payout=server.run();
	    System.out.println("The payout for" +Option1.toString()+" is: \n"+payout);	
	    
		//2. option :IBM, S_0:152.35, \sigma:0.01, period:252, K:164, Type:Asian	
		//Option Option2=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(164).setType("Asian").build();
	    //MonteCarloServer server2 = new MonteCarloServer();   
	    //server2.addOption(Option2);	    
	    //double payout2=server2.run();
	    //System.out.println("The payout for" +Option2.toString()+" is: \n"+payout2);	

	   
	}
}
