package dbReaderFramework;


/**
 * This is a fake TAQ trades DBReader. It allows us to test
 * the DBReader framework with known results.
 * 
 * The idea is to make the data some function of the record number
 * so we can easily tell whether the merged records are in order.
 *
 */
public class FakeTAQTradesDBReader implements I_DBReader {
	
	protected long    _lastSequenceNumberRead;
	protected boolean _isFinished;
	
	protected long    _fake930AMStartingTime; // We'll pretend this is 930AM
	protected long    _intervalBetweenRecords; // In millis
	protected int     _counter;
	
	protected int     _nRecs;
	protected long    _millisFromEpoc;
	protected int     _size;
	protected float   _price;
	
	/**
	 * Create a new fake TAQ trades reader.
	 * 
	 * @param fake930AMStartingTime Some arbitrary starting time which is supposed to represent the start of the trading day.
	 * @param intervalBetweenRecords This reader will report records at some pre-specified interval, e.g. every two minutes.
	 */
	public FakeTAQTradesDBReader( long fake930AMStartingTime, long intervalBetweenRecords ) {

		_fake930AMStartingTime = fake930AMStartingTime;
		_intervalBetweenRecords = intervalBetweenRecords;
		
		_isFinished = false;
		// We want to count the records so we can produce data 
		// that is related to the record count
		_counter = 0; 

		// Produce the first fake record, the record for the 
		// start of the day as specified above
		createFakeRec();
	}
	
	private void createFakeRec() {
		
		_lastSequenceNumberRead = getSequenceNumber();
		
		// Create a fake record where the fields are related
		// to the sequence number
		_millisFromEpoc = _lastSequenceNumberRead;
		_size = _counter * 3;
		_price = 100.00F + ( _counter * 0.10F );

		// Record how many records were 'read'
		_nRecs = 1;
		
	}

	@Override
	public int readChunk( long sequenceNum ) {
		if( sequenceNum >= getSequenceNumber() ) {
			
			// Generate a fake record
			createFakeRec();
		
			// Increment records counter
			_counter++;

			// If this puts us past end of day, we're finished
			if( ( _counter * _intervalBetweenRecords ) > ( 13 * 60 * 60 * 1000 / 2 ) )
				_isFinished = true;
			
			return _nRecs; // Return number of records read
			
		} else {
			
			// We have no records with sequence numbers that are
			// less than or equal to the sequence number up to
			// which we have been asked to read
			return 0; // No records were read
			
		}
	}
	
	public int   getNRecs()          { return _nRecs;          }
	public long  getMillisFromEpoc() { return _millisFromEpoc; }
	public int   getSize()           { return _size;           }
	public float getPice()           { return _price;          }

	@Override
	public long getSequenceNumber() {
		return _fake930AMStartingTime + ( _counter * ( _intervalBetweenRecords ) );
	}

	@Override
	public void stop() {
		// Nothing to do here because the class is fake.
		// There are no files close.
		_isFinished = true;
	}

	@Override
	public boolean isFinished() {
		return _isFinished;
	}

	@Override
	public long getLastSequenceNumberRead() {
		return _lastSequenceNumberRead;
	}

}