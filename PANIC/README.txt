——————About the design———
DataHandler:  
(i) Read data into linked list of Price record object;
(ii) Calculate change of price for each stock on each date (close-open) and aggregate the result of the percentage of price moving up for each date into a double[].

Histogram: 
input: double[]
output: frequency int[]

Simulation:
(i) No panic: create 100 stocks prices records for 1 year. All open price=1.5, close price is random in[1,2].Therefore about 50% would be price moving up.  A close to gaussian distribution shown in histogram.

(ii) Panic: create 100 stocks prices records for 1 year. 10% of them is randomly created, 90% of them mimic the other 10%. A more flat distribution shown in histogram.

———How to run——-
Run simulation to see the results.



