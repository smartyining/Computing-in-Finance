package mySolution;

/**decorator for rvg
 * 
 * @author yininggao
 *
 */
public class AntiTheticRVG implements I_RVG {

	private Boolean flag = false;	
	private double[] previousVector;
	private RandomVectorGenerator generator;
	
	/**Constructor
	 * 
	 * @param generator
	 */
	public AntiTheticRVG(RandomVectorGenerator generator) {
		this.generator = generator;
	}


	@Override
	public double[] nextVector() { 
		if (flag) {
			flag = false;
			for (int i=0; i<previousVector.length; i++) {
				previousVector[i] *= -1;
			}
		} else {
			flag = true;
			previousVector = generator.nextVector();	
		}
		return this.previousVector;	
	}


	@Override
	public void setSeed(int seed) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setLength(int length) {
		// TODO Auto-generated method stub
		
	}



}
