import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Summer task, Computing in Finance
 * @author Yining Gao
 * 
 */	

// class Records have two attributes :date and price
class Records{
	private int date;
	private double price;
	
	public Records(int date, double price){
		this.date=date;
		this.price=price;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}		
}

public class DataHandler {	
	
	private Scanner scan;
	ArrayList <Records> data;   
	
	public void quickSort(ArrayList <Records> arr, int low, int high, String sortBy, String sortPref) throws ParseException {	
		// pick the pivot
		int middle = low + (high - low) / 2;
		Records pivot = arr.get(middle);
		
		// make left < pivot and right > pivot
		int i = low, j = high;	
		while (i <= j) {

			if(sortBy=="byDate"){
				while (arr.get(i).getDate() < pivot.getDate()){
					i++;
				}
				while (arr.get(j).getDate() > pivot.getDate()){
					j--;
				}
			}else if(sortBy=="byPrice"){
				while (arr.get(i).getPrice() < pivot.getPrice()){
					i++;
				}
				while (arr.get(j).getPrice() > pivot.getPrice()) {
					j--;
				}
				
			}
			if (i <= j) {
				Records temp= new Records(0,0); //initialization
				temp.setPrice(arr.get(i).getPrice());
				temp.setDate(arr.get(i).getDate());
				arr.get(i).setDate(arr.get(j).getDate());
				arr.get(i).setPrice(arr.get(j).getPrice());
				arr.get(j).setDate(temp.getDate());
				arr.get(j).setPrice(temp.getPrice());
				i++;
				j--;
			}
		}	
	
		// recursively sort two sub parts
		if (low < j)
			quickSort(arr, low, j, sortBy, sortPref);
 
		if (high > i)
			quickSort(arr, i, high,sortBy, sortPref);
		
		//reverse if descending
		if(sortPref=="descending"){
			Collections.reverse(arr);
		}
	}
		
	
	public void bubbleSort(ArrayList <Records> arr, String sortBy, String sortPref) throws ParseException {

        int n = arr.size();
		Records temp= new Records(0,0); //initialization
   
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){  
                if((arr.get(j-1).getDate() > arr.get(j).getDate() && sortBy=="byDate") || (arr.get(j-1).getPrice() > arr.get(j).getPrice() && sortBy=="byPrice")){
                        //swap the elements!
	            		temp.setPrice(arr.get(j-1).getPrice());
	            		temp.setDate(arr.get(j-1).getDate());
	    				arr.get(j-1).setDate(arr.get(j).getDate());
	    				arr.get(j-1).setPrice(arr.get(j).getPrice());
	    				arr.get(j).setDate(temp.getDate());
	    				arr.get(j).setPrice(temp.getPrice());
                }
                   
            }
        }
		
		if(sortPref=="descending"){
			Collections.reverse(arr);
		}			
	}
	
	
	public void loadPriceData(String FileName, String SortMethod, String SortBy, String SortPref) throws IOException, ParseException{
		ArrayList <Records> result = new ArrayList <Records> ();	
		
	    scan = new Scanner(new File(FileName));
	    scan.nextLine();  //skip the first line
	    while (scan.hasNextLine()) {
	        String line = scan.nextLine();
	        String[] lineArray = line.split(",");	
	        
	        String[] s=lineArray[0].split("/");
	        
	        int date=Integer.parseInt(s[2])*10000+Integer.parseInt(s[0])*100+Integer.parseInt(s[1]); //convert 8/29/2000 to 20000829
	        
	        result.add(new Records(date, Double.parseDouble(lineArray[6])));  
	    }
	    
	    if( SortMethod=="QuickSort"){
	    	 quickSort(result, 0, result.size()-1, SortBy, SortPref);
	    }else if(SortMethod=="BubbleSort"){
	    	bubbleSort(result, SortBy, SortPref);
	    }
	    
	   this.data=result;
	 }
		

	public double[] getPrice(String fromDate, String toDate) throws ParseException{
		
		int i=0;   //start index
		int j=this.data.size()-1;     //to index
			
		String[] s1=fromDate.split("/");
		String[] s2=toDate.split("/");
		
		int from=Integer.parseInt(s1[2])*10000+Integer.parseInt(s1[0])*100+Integer.parseInt(s1[1]);
		int to=Integer.parseInt(s2[2])*10000+Integer.parseInt(s2[0])*100+Integer.parseInt(s2[1]);
	
		//find the price range
		while(this.data.get(i).getDate() < from){		
			i++;		
		}
		while(this.data.get(j).getDate() > to){
			j--;	
		}
		
		double[] prices=new double[j-i+1];
		
		for(int k=0; k<=j-i; k++){
			prices[k]=this.data.get(i+k).getPrice();
		}	
		
		if(prices.length<to-from+1){
			System.out.println("Warning: Some dates between"+fromDate+" and "+toDate+" might be missing!");
		}
		return prices;		
		
	}
	
	
	public double computeAverage(String fromDate, String toDate) throws ParseException{
		
		double[] prices= this.getPrice(fromDate, toDate); 
		
		double sum = 0; 

		for(int i=0; i < prices.length; i++){
		   sum += prices[i];
		}

		double average = sum/prices.length;
		return average;
	}

	
	public double computeMax(String fromDate, String toDate) throws ParseException{
		double[] prices= this.getPrice(fromDate, toDate); 
		
		double max=0;
		for(int i=0; i<=prices.length-1;i++){
			if(prices[i]>max){
				max=prices[i];
			}
		}
		return max;
	}
	
	
	public double[] computeMovingAverage(int size, String fromDate, String toDate) throws ParseException{
		double[] prices= this.getPrice(fromDate, toDate); 

		double[] ma=new double[prices.length-size+1];
		
		for (int i=0; i<=ma.length-1;i++){
			double sum=0;
			for(int j=i;j<=i+size-1;j++){
				sum+=prices[j];
			}
			ma[i]=sum/size;
		}				
		return ma;	
	}
		
	
	public void insertPrice(String record) throws FileNotFoundException, ParseException{		
		Records a= new Records(0,0); //initialization
	
        String[] lineArray = record.split(",");	       
        String[] s = lineArray[0].split("/");        
        int date=Integer.parseInt(s[2])*10000+Integer.parseInt(s[0])*100+Integer.parseInt(s[1]);
        
        a.setDate(date);
        a.setPrice(Double.parseDouble(lineArray[6])); //now a holds the new price record; 
		
		int flag=0; //indicate whether this date exsists
		int i=0; // i holds index
		
		 for(Records r: this.data) {
			 if(r.getDate()== a.getDate()){ //if two dates are equal
				flag=1;
				i= this.data.indexOf(r);
				break;
			  	}		 
		 }
		 if(flag==1){
			 System.out.println("Current records already have record for "+a.getDate()+", going to overwrite!");               //overwrite case
			 this.data.get(i).setPrice(a.getPrice());
		 }else{
			 System.out.println("Current records don't have record for "+a.getDate()+", going to insert!");              //insert case
			 
			//find insert point
			 while(i< this.data.size() && this.data.get(i).getDate() < a.getDate()){
			        i++;
			 }
			 this.data.add(i, a);
			 
		 }	 
	}
	
	
	public void correctPrices(String FileName) throws FileNotFoundException, ParseException{	
		
	    scan = new Scanner(new File(FileName));
	    scan.nextLine();  //skip the first line
	    while (scan.hasNextLine()) {
	        String line = scan.nextLine();
	        this.insertPrice(line);
	    }
	    System.out.println("Data corrected!");
		    		    
	}
		
	
	public static void main(String[] args) throws IOException, ParseException{
		
		String Filepath="prices.csv";
		
		DataHandler myDataHandler= new DataHandler();  //create new DataHandler object
		
		myDataHandler.loadPriceData(Filepath,"QuickSort","byDate","ascending"); //edit here if want to change method
		
		//correct the records
		myDataHandler.correctPrices("corrections.csv");			

		double[] myPrice=myDataHandler.getPrice("8/15/2004", "8/20/2004");   
		
		//output to result file
		PrintWriter writer = new PrintWriter("results.txt", "UTF-8");
		
		writer.println("The Prices of SPY between 8/15/2004 and 8/20/2004 are:");

		for (int i=0; i<=myPrice.length-1;i++) {
			writer.println(myPrice[i]);
		}

		//append average to text file
		double average=myDataHandler.computeAverage("8/15/2004", "9/15/2004");
		writer.write("The Average Price of SPY between 08/15/2004 and 09/15/2004 is: ");
		writer.write(Double.toString(average));
		writer.println("");
		
		//append max to text file
		double max=myDataHandler.computeMax("4/15/2004", "6/15/2004");
		writer.write("The Maximum Price of SPY between 04/15/2004 and 06/15/2004 is: ");
		writer.write(Double.toString(max));
		writer.println("");
		
		//append moving average to text file
		double[] result=myDataHandler.computeMovingAverage(10, "8/15/2004", "9/15/2004");
		writer.println("The Moving Average of SPY between 08/15/2004 and 09/15/2004 for WindowSize 10 is:");
		for (int i=0; i<=result.length-1;i++) {
			writer.println(result[i]);

		}	
		writer.close();
		System.out.println("Task finished! Please find the result in result.txt!");
		}
	
}
	
	
	
		
		
		
		
