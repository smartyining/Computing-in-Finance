package mySolution;


/**
 * This is to test JavaCL_PathGenerator and JavaCl_SimulationManager
 * It might take a while to run two batches
 * Here's the result:
 * IBM,152.35,165.00,252,0.01,0.0001,Europe
 * Option Price (Current Value of Expected Payout):  6.22 StockPath generated:  20000000
 * 
 * @author yininggao
 *
 */
public class Test {
	
	public static void main(String[] args) {
		
		//1.option: IBM, S_0:152.35, \sigma:0.01, period:252, K:165, Type:Europe		
		Option option=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();	
		
		
		JavaCL_PathGenerator generator = new JavaCL_PathGenerator (option);
		double[] test = generator.getLastDayPrice();
		for (double t : test)
			System.out.println(t);

		System.out.println("Current option:");
		System.out.println(option.toString());
		
		//create a simulation manager 
		JavaCL_SimulationManager manager=new JavaCL_SimulationManager(option);
		
		manager.setProb(0.96);
		manager.setTolerance(0.1);
		
		//begin simulate
		manager.getPrice();
		System.out.println(manager.toString());
		
	}

}
