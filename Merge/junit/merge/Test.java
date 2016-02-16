package merge;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * This is to test my understanding about how data inputStream.readNext() works
 * @author yininggao
 *
 */
public class Test extends TestCase {
	protected DataInputStream _dataInputStream;
	public int count;
	public void test() throws IOException {
		String filename = "/users/yininggao/output/20070620/1449373528158_1.dat";
		FileInputStream in = new FileInputStream( filename );
		_dataInputStream = new DataInputStream( in ); // _dataInputStream is ready to read
		//System.out.println(_dataInputStream.available());
		 
		while(_dataInputStream.available()>0) { // If reach the end of the file
			count++;
	         System.out.println(_dataInputStream.readLong());
	         System.out.println(_dataInputStream.readShort());
	         System.out.println(_dataInputStream.readInt());					
	         System.out.println(_dataInputStream.readFloat());	
		}

	}

}
