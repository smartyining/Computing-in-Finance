package merge;

import java.io.IOException;

import junit.framework.TestCase;

public class Test_DBReader extends TestCase {
	
	public void test() throws IOException {
	
	// This test runs after  Test_TAQTradesRewriter
	// Have /users/yininggao/output/20070620/1449271971717_1.dat in the directory
	// Test DBReader , whether can read *_1.dat file correctly
	DBReader myReader=new DBReader("/users/yininggao/output/20070620/1449372181089_1.dat");
	assertTrue(myReader._isFinished==false);
	
	// Fake a  targetSequenceNum=1182312000, which is the secfromepc of first data line
	// Thus data of same seconds should be read into memory
	int n_records=myReader.readChunk(1182312000L);
	
	System.out.println(String.format("Has read %d records", n_records));
	
	// Get data and verify	
	assertTrue(myReader.getSequenceNumber()==1182312000L);
	assertTrue(myReader.getId()==100);
	System.out.println(myReader.getSize()); // This is an int
	System.out.println(myReader.getPrice()); // This is float
	
	// Verify the length of linkedlist
	// There should be 23595 records in the linkedlist
	// Exceed this number will return error
	for(int i=0; i<23595;i++)
		myReader.getNRecsRead();
	
	// Check _lastSequenceNumberRead 
	assertTrue(myReader.getLastSequenceNumberRead()==1182312000L);
	
	}

}
