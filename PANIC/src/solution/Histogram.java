package solution;
import java.util.List;

/** Histogram Class calculate the frequence of a list of double numbers
 * 
 * @author yininggao
 *
 */
public class Histogram {
    private int _bins=5;   // #bins default
    private int _max;            // x-axis range;
    private int _min;
    private int[] _freq;     // frequency
    private double len;         // length of bin

    
    // Constructor : takes a list of data and number of bins
    public Histogram(List<Double> data, int Bins) {
    	_bins=Bins;
    	_max= (int) -Double.POSITIVE_INFINITY;
    	_min= (int) Double.POSITIVE_INFINITY;
    	_freq=new int[_bins];   	
    	
    	// Search for the max and min : O(n)
    	for(double t: data){
    		if (t>_max) { _max=(int) Math.ceil(t);}
    		if (t<_min) { _min=(int) Math.floor(t);}   		
    	} 
		
    	len= (double)(_max-_min)/_bins;

    	addDataPoint(data);   	
    }

    
    // Add one occurrence of the value t. 
    protected void addDataPoint(List<Double> data) {   	
    	// Calculate which bin data point falls into: O(n)
    	for (double t:data){
        	int i=  (int) (((double) t-_min)/len);
    		if(t==_min+(i+1)*len && i<_freq.length) // If value falls at the right side of interval
    			_freq[i+1]++;
    		else if(t==_max) // If equals _max, fall into last interval
    			_freq[i-1]++;
    		else
    			_freq[i]++;
    	}
    } 

    // Draw histogram
    public void print(){
    	System.out.println("-------------Histogram------------");
    	for(int i=0; i<_freq.length-1;i++) {
    		System.out.format("[ %.2f ,%.2f )|", _min+i*len,_min+(i+1)*len);   		
    		for(int j=0; j<_freq[i];j++){
    			System.out.print("*");
    		}
    		System.out.print("\n");  		
    	}
		System.out.format("[ %.2f ,%.2f ]|", _max-len, (float)_max);   		
		for(int j=0; j<_freq[_freq.length-1];j++){
			System.out.print("*");
		}
		System.out.print("\n");  
    }

} 