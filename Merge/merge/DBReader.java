package merge;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import dbReaderFramework.I_DBReader;

/** This DBReader reads trades record in this format: 
 * 
 *  long secondsFromTheEpoch 
 *  short stockId
 *  int size 
 *  float price
 * 
 * For one sequence number, it reads records having this sequence number  and store it into a linkedlist
 * this linkedlist is then passed to DB processor
 * 
 * One difference from TAQTradesDBReader is that in getSequenceNumber() dataInputStream need to read one record
 * Before DB Manager decides when to read. Therefore in fact _nRecsRead is the pointer here which controls the 
 * Length of A chuck.
 * 
 * @author yininggao
 *
 */

public class DBReader implements I_DBReader {

	protected boolean  _isFinished;
	protected long   _currentSequenceNumber;  // The sequence number currently reading
	protected long   _lastSequenceNumberRead; // The sequence number last for previous chuck
	protected int _nRecsRead;    // Number of records read in the chuck
	protected DataInputStream _dataInputStream;
	protected LinkedList<Long> _secsFromEpoch;  // LinkedList store a chuck  data
	protected LinkedList<Short> _id;
	protected LinkedList<Integer> _size;
	protected LinkedList<Float> _price;
	
	public DBReader( String filename ) throws IOException {
		_secsFromEpoch = new LinkedList<Long>();
		_id = new LinkedList<Short>();
		_size = new LinkedList<Integer>();
		_price = new  LinkedList<Float>();
		_isFinished = false;
		_lastSequenceNumberRead = 0;
		_nRecsRead=0;
		FileInputStream in = new FileInputStream( filename );
		_dataInputStream = new DataInputStream( in ); // _dataInputStream is ready to read
		
	}

	@Override
	public int readChunk(long targetSequenceNum){
	 	_nRecsRead=0;
		if( _isFinished )
			return 0;		
		// Iterate over records until there is no data from input stream or we hit
		// a sequence number that is higher than the target sequence number		
		while( true ) {					
				try {
					if(_dataInputStream.available()<=0) { // If reach the end of the file
						_isFinished = true;
						break;
					}
					else {					
						if( getSequenceNumber() > targetSequenceNum ) {
							break;	
						}							
						_nRecsRead++;			//	_nRecsRead is important in indicating the size of a chuck		
						_lastSequenceNumberRead = targetSequenceNum;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
	
		}	
		return  _nRecsRead;
	}


	public long getSec() { return _secsFromEpoch.removeFirst(); }

	public short getId() { return _id.removeFirst(); }

	public int getSize() { return _size.removeFirst(); }
	
	public float getPrice() { return _price.removeFirst(); }

	@Override
	public void stop() {}

	@Override
	public boolean isFinished() { return _isFinished; }

	@Override
	public long getLastSequenceNumberRead() { return _lastSequenceNumberRead; }
	
	public int getNRecsRead() { return _nRecsRead; }

    // Get the current sequence number
	@Override
	public long getSequenceNumber() {
		try {			
			_currentSequenceNumber=_dataInputStream.readLong();
			_secsFromEpoch.add(_currentSequenceNumber);
			_id.add(_dataInputStream.readShort());
			_size.add(_dataInputStream.readInt());					
			_price.add(_dataInputStream.readFloat());	

		} catch (IOException e) {
			e.printStackTrace();
		}
		return _currentSequenceNumber;
	}

}
