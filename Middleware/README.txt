***********About the design*************

Once an option is added, My Server first maps it into a mapMessage and than sends a batch simulation requests on a queue called ”Simulation 1”, waiting for client to start simulation tasks.

On Client side, it has a consumer listen to ”Simulation 1”, once it receives a message, it launches 1000 simulations. A producer will send each payout as textMessage back to the Server on a specific topic (the name of the topic is determined by server, named according to the option, therefore simulation results for different options won’t mixed up)


After Server receiving all the results, it calculates accuracy and decides whether to continue or quit. I put a while loop here. 

If we run multiple clients, messages in the queue will be distributed over multiple clients. If you need to calculate another option, creat a another server and add this option.

***************Run this program******************

Start MonteCarloServer.java and then start MonteCarloClient.java. Need java files in MySolution.

************** What else********************
When there are multiple clients, the server might want to know which client this result is from. In that case, we need to add client id/address in returnTopicName. 
