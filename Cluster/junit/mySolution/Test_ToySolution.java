package mySolution;

import junit.framework.TestCase;

public class Test_ToySolution extends TestCase {
	
	//test if correctly generate 100*100 points
	public void testCreatePoint(){	
		ToySolution.createPoint();
		assertTrue(ToySolution.points.size()==10000);
		
	}
	
	//test draw
	public void testDraw(){
		ToySolution.createPoint();
		DrawPanel.saveImage(ToySolution.points, "testDrawPanel.png");	
	}
	
	

}
