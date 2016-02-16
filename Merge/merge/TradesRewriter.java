package merge;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import taqDBReaders.ReadGZippedTAQTradesFile;

/**
 * This class handles rewrite. It creates output fold and rewrite /trades/YYYYMMDD/*_trades.binRQ into
 * /output/YYYYMMDD/<rd>_1.dat. It only read and rewrite the following data field:
 * 
 *  long secondsFromTheEpoch 
 *  short stockId
 *  int size 
 *  float price
 *  
 * @author yininggao
 *
 */
public class TradesRewriter {
	
	private ReadGZippedTAQTradesFile _taq;
	private int _id;
	private String _outputPath;
	private int _nRecs;
	private String BASIC_OUTPUT_PATH="/users/yininggao/output/";
	
	/**
	 * Constructor
	 * input directory such as: users/yininggao/trades/20070620/IBM_trades.binRQ 
	 * output file such as: like users/yininggao/output/20070620/<rd>_1.dat	
	 * @param filePath
	 * @param id
	 * @throws IOException
	 */
	public TradesRewriter(String filePath, int id) throws IOException {
		_taq = new ReadGZippedTAQTradesFile(filePath); 
		_id=id;
		_nRecs=_taq.getNRecs();	
		// Make out put directory for each date
		String dir=BASIC_OUTPUT_PATH+filePath.replaceAll("(.*)(\\d{8})(.*)", "$2");
	    System.out.println(dir);
		File theDir = new File(dir);
		if (!theDir.exists()) {
		    if (!theDir.mkdirs()) {
		    	 throw new IOException("Unable to create " + dir);
		    }
		}
		
		_outputPath=dir+"/"+String.format("%d_1.dat", System.currentTimeMillis());     
		System.out.println(_outputPath);
	}

	
	/**
	 * Rewrite records into *_1.dat file
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(_outputPath));
		for ( int i=0; i<_nRecs; i++){
			outputFile.writeLong(_taq.getSecsFromEpoch());
			outputFile.writeShort(_id);
			outputFile.writeInt(_taq.getSize(i));			
			outputFile.writeFloat(_taq.getSize(i));			
		}
		outputFile.flush();
		outputFile.close();		
	}
	


}
