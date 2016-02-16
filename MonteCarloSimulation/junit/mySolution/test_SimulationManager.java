package mySolution;

import junit.framework.TestCase;

public class test_SimulationManager extends TestCase {
	
	public void test1(){
		Option option=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();	
		
		//create a new manager
		SimulationManager manager=new SimulationManager(option);
				
		//test bound calculation
		manager.setProb(0.96);
		manager.setTolerance(0.1);
		//System.out.println(manager.getBound()); //manually check if \sim 2.17
		
		//test isFinished()
		assertFalse(manager.isFinished());
		
		//test if terminate 
		manager.getPrice();
		System.out.println(manager.toString());
				
	}

}
