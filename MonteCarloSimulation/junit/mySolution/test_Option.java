package mySolution;

import junit.framework.TestCase;

public class test_Option extends TestCase {
	
	public void test(){
		Option Option1=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();
	
		System.out.println(Option1.toString());
		assertTrue(Option1.getName()=="IBM");
		assertTrue(Option1.getType()=="Europe");
		assertTrue(Option1.getInitialPrice()==152.35);
		assertTrue(Option1.getPeriod()==252);
		assertTrue(Option1.getExpireDate()==20151156);
		
	}
	
	
}
