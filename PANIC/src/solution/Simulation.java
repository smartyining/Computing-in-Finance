package solution;

import java.io.File;
import java.io.FileWriter;

public class Simulation {
		
	private static FileWriter fileWriter;

	public static void main(String[] args) throws Exception {
		
		// If there's no collective panic, there should be  50% of stocks moving up on most dates		
		// Creat simulation data using random uniform (0,1)  number
		String resultsFile = "noPanic.txt";
		File file = new File( resultsFile );
		fileWriter = new FileWriter( file );
		for(int i=1;i<13;i++) { // month
			for(int j=1;j<31;j++) {     // date            
				for(int k=0;k<100;k++)  // simulate 100 stocks
					 //(YYYYMMDD),(stock),(open),(high),(low),(close),(volume),(adjustedClose)
					fileWriter.write(String.format( "2016%02d%02d,SI,1.5,1,1,%.2f,1,1\n", i,j, Math.random()+1));
			}
			
		}
		fileWriter.flush();
		fileWriter.close();
		// Read data
		DataHandler dataHandler=new DataHandler();
		dataHandler.getPriceRecords("noPanic.txt");
		dataHandler.processData();
		
		// Draw Hist
		// As you can see from the output, hist get the peak at 50%
		Histogram hist=new Histogram(dataHandler.getData(),100);
		hist.print();
		
		// If there's panic, the likelihood of any fraction is almost the same for any value
		// We'll simulate this using uniform(0,1) random numbers
		
		String resultsFile2 = "panic.txt";
		File file2 = new File( resultsFile2);
		fileWriter = new FileWriter( file2 );
		for(int i=1;i<13;i++) {
			for(int j=1;j<31;j++) {
				for(int k=0;k<10;k++){  //  Now i have 10 stocks random and 90 stocks mimic
				    double rd = Math.random()+1;
				    for(int t=0; t<9;t++)
				    	fileWriter.write(String.format( "2016%02d%02d,SI,1.5,1,1,%.2f,1,1\n", i,j, rd));
				}
			}
			
		}
		fileWriter.flush();
		fileWriter.close();
		// Read data
		DataHandler dataHandler2=new DataHandler();
		dataHandler2.getPriceRecords("Panic.txt");
		dataHandler2.processData();
		
		// Draw Hist
		// As you can see from the output, hist has a more flat shape
		Histogram hist2=new Histogram(dataHandler2.getData(),50);
		hist2.print();
		
		
	}

}
