package mySolution;

import junit.framework.TestCase;

public class test_PathGenerator extends TestCase {
	
	public void test1(){
		
		Option Option1=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();
		System.out.println(Option1.toString());
		
		StockPathGenerator generator=new StockPathGenerator(Option1);	
		StockPath path=generator.getPath();
		path.print();
		assertTrue(path.size()==253);
	}

}
