package mySolution;

import static org.bridj.Pointer.*;
import java.util.Random;
import org.bridj.Pointer;
import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;

/**
 * This class use JavaCL to generate stock price in a year
 * 
 * @author yininggao
 *
 */
public class JavaCL_PathGenerator {
	
	private static int _batchSize = 10000000; // Batch size
	private static int period ;         
	private static float r ;                  // Annual interest rate
	private static float sigma ;              // Annual sigma
	private static float initial ;
	
	// Constructor
	public JavaCL_PathGenerator (Option option) {
		period = option.getPeriod();
		initial = (float) option.getInitialPrice();	
		sigma = (float) (option.getVolatility()*Math.sqrt(period));
		r = (float) option.getInterestRate()*period;
	}
	
				
	// two long batches of numbers,  1M uniform number each
    private float[] getUniformVector() {
    	float[] vector=new float[_batchSize];
        Random rd=new Random();
        for (int i=0 ; i< _batchSize ; i++) 
        	vector[i] = rd.nextFloat();
        return vector;  	
    }
		
	public double[] getLastDayPrice() {
        // Creating the platform which is out computer.
        CLPlatform clPlatform = JavaCL.listPlatforms()[0];
        // Getting the GPU device
        CLDevice device = clPlatform.getBestDevice();
                  
        CLContext context = JavaCL.createContext(null, device);
        CLQueue queue = context.createDefaultQueue();
    	
        // Read the program sources and compile them :       
        String src = 
        		"__kernel void get_last_day_price(__global const float* a, __global const float* b, __global float* out1,__global float* out2, int n, float r, float sigma, float initial) \n" +
                "{\n" +
        		"#ifndef M_PI\n" +
                "#define M_PI 3.14159265358979323846\n"+
                "#endif\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n)\n" +
                "        return;\n" +
                "\n" +
                "    out1[i] = sqrt(-2*log(a[i]))*cos(2*M_PI*b[i]);\n" +
                "    out2[i] = sqrt(-2*log(a[i]))*sin(2*M_PI*b[i]);\n" +
                "\n" +
                "    out1[i] = initial* exp((r - sigma*sigma/2)+sigma*out1[i]);\n" + 
                "    out2[i] = initial* exp((r - sigma*sigma/2)+sigma*out2[i]);\n" + 
                "}";
        
        CLProgram program = context.createProgram(src);       
        program.build();
        
        CLKernel kernel = program.createKernel("get_last_day_price");
        final Pointer<Float>
        	aPtr = allocateFloats(_batchSize),
        	bPtr = allocateFloats(_batchSize);
        
        // launch two batches
        float[] batch_1=getUniformVector() ;
        float[]	batch_2=getUniformVector() ;
        		
        for (int i = 0; i < _batchSize; i++) {
        	 aPtr.set(i,batch_1[i]);
        	 bPtr.set(i,batch_2[i]);
        }
        
        // Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
        CLBuffer<Float>
                a = context.createFloatBuffer(CLMem.Usage.Input, aPtr),
                b = context.createFloatBuffer(CLMem.Usage.Input, bPtr);

        // Create an OpenCL output buffer :
        CLBuffer<Float> out1 = context.createFloatBuffer(CLMem.Usage.Output, _batchSize);
        CLBuffer<Float> out2 = context.createFloatBuffer(CLMem.Usage.Output, _batchSize);
        
        // get_payout(__global const float* a, __global const float* b, __global float* out1,__global float* out2, int n,double r, double sigma, double initial)
        kernel.setArgs(a, b, out1,out2, _batchSize,r, sigma, initial);
        CLEvent event = kernel.enqueueNDRange(queue, new int[]{_batchSize});
        event.invokeUponCompletion(new Runnable() {
            @Override
            public void run() { }
        });
        final Pointer<Float> cPtr1 = out1.read(queue,event);
        final Pointer<Float> cPtr2 = out2.read(queue,event);
        
        double[] price =new double[2*_batchSize];
        for (int i=0; i<_batchSize; i++) {
        	price[i] = (double) cPtr1.get(i);
        	price[_batchSize+i] = (double) cPtr2.get(i);
        }

        return price;
            
	}

	
	

	
	
}


