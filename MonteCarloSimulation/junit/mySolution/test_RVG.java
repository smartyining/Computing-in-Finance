package mySolution;

import java.util.Arrays;

import junit.framework.TestCase;

public class test_RVG extends TestCase {
	
	/**test standard Guanssian random vector generator
	 * 
	 */
	public void test1(){
		RandomVectorGenerator generator=new RandomVectorGenerator();

		System.out.println(Arrays.toString(generator.nextVector()));
		
		generator.setLength(10);
		
		double[] rd=generator.nextVector();
		assertTrue(rd.length==10);
		
		System.out.println(Arrays.toString(rd));
	}
	
	/**test antithetic decorator
	 * 
	 */
	public void test2(){
		System.out.println("=========Now test antithetic rvg=======");
		RandomVectorGenerator rawGenerator=new RandomVectorGenerator();
		AntiTheticRVG generator= new AntiTheticRVG(rawGenerator);
		
		System.out.println(Arrays.toString(generator.nextVector()));
		System.out.println(Arrays.toString(generator.nextVector()));
		
		//should have opposite value
		assertTrue(generator.nextVector()[0]+generator.nextVector()[0]==0);
		
	}

}
