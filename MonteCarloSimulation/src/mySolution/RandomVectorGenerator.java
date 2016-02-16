package mySolution;

import org.apache.commons.math3.random.*;

/**This class generate a uncorrelated standard Gaussian random vector
 * Using external lib
 * 
 * @author yininggao
 *
 */

public class RandomVectorGenerator implements I_RVG {
	
	int length=1;
	int seed=(int) System.currentTimeMillis();
	
	
	public RandomVectorGenerator(){};
	
	public RandomVectorGenerator(int length){
		this.length=length;
	}
	
	@Override
	public void setSeed(int seed) {
		this.seed=seed;
		
	}

	@Override
	public void setLength(int length) {
		this.length=length;
		
	}
	

	@Override
	public double[] nextVector() {
		RandomGenerator g1 = new JDKRandomGenerator();
		
		GaussianRandomGenerator g2 = new GaussianRandomGenerator(g1);
		UncorrelatedRandomVectorGenerator g3= new  UncorrelatedRandomVectorGenerator(length, g2);
				
		return g3.nextVector();
	}

}
