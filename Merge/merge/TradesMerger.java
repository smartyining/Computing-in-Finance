package merge;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import dbReaderFramework.DBManager;
import dbReaderFramework.I_DBProcessor;
import dbReaderFramework.I_DBReader;
import dbReaderFramework.MergeClock;

/**
 * This is the merge "manager": After run launch, it firsts rewrite all file under BASE_INPUT_DIR into 
 * BASE_OUTPUT_DIR and then merge them in output folder.
 * 
 * Every round it calls 2 DBReader and merge two file into one.
 * 
 * @author yininggao
 *
 */
public class TradesMerger {
	
	protected static final String BASE_INPUT_DIR = "/Users/yininggao/trades";
	protected static final String BASE_OUTPUT_DIR = "/Users/yininggao/output";
	protected HashMap<String,Integer> _codeBook;
	
	/**
	 * Constructor
	 */
	public TradesMerger() { 
		_codeBook=new HashMap<String,Integer>();		
	}
	
	
	/**
	 * Launch Merger
	 * Step 1: rewrite ; Step 2: merge 
	 * @throws Exception
	 */
	public void launch() throws Exception {	
		rewrite();
		merge();			
	}
	
	/**
	 * At round r,Merge two _<r-1>.dat file at a time
	 * 
	 * @throws Exception
	 */
	public void merge() throws Exception {
		
		File dir = new File( BASE_OUTPUT_DIR);
		
		File[] dirList = dir.listFiles();
		
		for (File dateDir : dirList) {	// /20010620/ *_1.dat
			// Pass .DS_Store
			if (!dateDir.getName().startsWith(".")) {
			// Initialization
			int round=2;	// Current merge round
			int n_files= dateDir.listFiles().length; // How many files in this round

			while ( n_files > 1) {
				int n= (int) Math.floor(n_files/2); // How many times of merge at this round

				for (int i=0; i< n; i++) {
					// Create two merger 
					String first = dateDir.listFiles()[2*i].getPath();
					String second = dateDir.listFiles()[2*i+1].getPath();

					DBReader reader1 = new DBReader( first );
					DBReader reader2 = new DBReader( second );	
					
					// Put both readers into a list
					LinkedList<I_DBReader> readers = new LinkedList<I_DBReader>();
					readers.add( reader1 );
					readers.add( reader2 );
					// Output file name
					String outputPath=dateDir.getAbsolutePath()+"/"+String.format("%d_%d.dat", System.currentTimeMillis(),round);
					@SuppressWarnings("resource")
					final DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(outputPath));
						
					// Instantiate merge processor, telling it where to write merged output of two quotes files
					I_DBProcessor mergeProcessor = new I_DBProcessor() {
						@Override
						public boolean processReaders(
							long sequenceNumber,
							int  numReadersWithNewData,
							LinkedList<I_DBReader> readers
						) {
							if( reader1.getLastSequenceNumberRead() == sequenceNumber ) {							
								int nRecs = reader1.getNRecsRead();
								try {
									for( int i = 0; i < nRecs; i++ ) {
										outputFile.writeLong( reader1.getSec() );
										outputFile.writeShort( reader1.getId() );
										outputFile.writeInt( reader1.getSize() );
										outputFile.writeFloat( reader1.getPrice());	
									}
								} catch (IOException e) {
									return false;
								}
							}
							if( reader2.getLastSequenceNumberRead() == sequenceNumber ) {
								int nRecs = reader2.getNRecsRead();
								try {
									for( int i = 0; i < nRecs; i++ ) {
										outputFile.writeLong( reader2.getSec() );
										outputFile.writeShort( reader2.getId() );
										outputFile.writeInt( reader2.getSize() );
										outputFile.writeFloat( reader2.getPrice());	
									}
								} catch (IOException e) {
									return false;
								}
							}
							return true;
						}

						@Override
						public void stop() throws Exception {
							outputFile.flush();
							outputFile.close();
						}			
					};
				
				    // Create a list of processors, which, in this case, will
					// contain only one processor, the one we created above
					LinkedList<I_DBProcessor> processors = new LinkedList<I_DBProcessor>();
					processors.add( mergeProcessor );
						
					// Create a merge clock
					MergeClock clock = new MergeClock( readers, processors );
					
					// Hand all of the readers, processors, and clock to the DBManager
					DBManager dbm = new DBManager( readers, processors, clock );
				
					// Launch the DBManager
					dbm.launch();
					
					// If number of files is even
					if (2*i+1==n_files-2) {
						// Rename last single file
						dateDir.listFiles()[n_files-1].renameTo(new File(outputPath));
						break;
					}		
					}
				
			// Delete last round file
			clear(dateDir.getAbsolutePath(),round);
			round++;
			n_files= dateDir.listFiles().length;
			break;
			}
		}
		}
	}
	/**
	 * Rewrite records to a format that is easier to merge 
	 * @throws Exception
	 */
	public void rewrite() throws Exception {
		int id=100;		
		File dir = new File(BASE_INPUT_DIR);
		
		File[] dirList = dir.listFiles();
		
		if (dirList == null) 
			throw new Exception( "Null file directory." );
			  
		for (File dateDir : dirList) {
			if ( dateDir.getName().matches("\\d{8}")) {
				File[] stockList= dateDir.listFiles();		
				for (File stock: stockList)  {
					// Pass file .DS_Store
					if (!stock.getName().startsWith(".")) {
						String name=stock.getName().replaceAll("(\\w*)(_trades.binRT)", "$1");
						// Assign a code for each stock 
						if (!_codeBook.containsKey(name)) {
							_codeBook.put(name, id);
							id++;
						}				
						// Rewrite  records
						System.out.println(stock.getPath());
						TradesRewriter writer=new TradesRewriter(stock.getPath(),_codeBook.get(name));
						writer.write();
					}
				}
			}						
		}
		
	}

	
	/** Erase all  .dat files from previous round
	 * 
	 * @param currentPath
	 * @param round
	 */
	public void clear(String currentPath, int round) {
		File dir = new File( currentPath );
		File[] fileList = dir.listFiles();
		for (File file : fileList) {
			if(file.getName().endsWith(String.format("_%d.dat", round-1))) 
				file.delete();		
		}	
	}
		
	} 
		
	


