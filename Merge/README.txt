***************How to run****************

Run Test_TradesMerger.java in junit folder and perform the tasks.

***************About the design***************

I write three classes in merge:

BDReader:

 *  DBReader reads trades record in this format: 
 *
 *  long secondsFromTheEpoch 
 *  short stockId
 *  int size 
 *  float price
 * 
 * For one sequence number, it reads records have this sequence number and store it into a linked list which is then passed to DB processor


TradesMerger:

 * Task  "manager": After run launch, it firsts rewrite all file under   BASE_INPUT_DIR into BASE_OUTPUT_DIR and then merge them in output folder.
 * 
 * Every round it calls 2 DBReader and merge two file into one.
 * 

TradesRewriter:

 * Handles rewrite. It creates output fold and rewrite /trades/YYYYMMDD/*_trades.binRQ into /output/YYYYMMDD/<rd>_1.dat. It only read and rewrite the following data field:
 * 
 *  long secondsFromTheEpoch 
 *  short stockId
 *  int size 
 *  float price