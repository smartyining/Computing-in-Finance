package mySolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** Implement hw_1 IBM call option
 * 
 * @author yininggao
 *
 */
public class MonteCarloSimulation {
	
	private static FileWriter writer;
	
	/** Runs simulation and output to txt file.
	 * 
	 * @param option
	 * @throws IOException
	 */
	public static void runSimulation(Option option) throws IOException{		
		System.out.println("Current option:");
		System.out.println(option.toString());
		
		//create a simulation manager 
		SimulationManager manager=new SimulationManager(option);
		
		manager.setProb(0.96);
		manager.setTolerance(0.1);
		
		//begin simulate
		manager.getPrice();
		System.out.println(manager.toString());
		
		//write to output file
		File obj_file=new File(option.getName()+"_"+option.getType()+".txt");
	    writer = new FileWriter(obj_file);
	    writer.write(option.toString()+'\n');
	    writer.write(manager.toString());
	    writer.close();	
	
	}
	
	
	public static void  main(String[] args) throws IOException{
		
		//1.option: IBM, S_0:152.35, \sigma:0.01, period:252, K:165, Type:Europe		
		Option Option1=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(165).setType("Europe").build();	
		
		//2. option :IBM, S_0:152.35, \sigma:0.01, period:252, K:164, Type:Asian	
		Option Option2=new Option.OptionBuilder().setInitialPrice(152.35).setName("IBM").setVolatility(0.01).setPeriod(252).setStrickPrice(164).setType("Asian").build();

		//run simulation
		runSimulation(Option1);
		runSimulation(Option2);
					
	}

};
