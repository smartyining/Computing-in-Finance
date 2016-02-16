Author: Yining Gao N13725742

**********************How to run my program and expected output*******************

Run ToySolution.java in mySolution Package (need to change destination path). 

It should have six output files: both a png and txt version of initial/KMeans/fixed size version of the solution.

*****************************How do I design this algorithm******************************
In basic KMeans algorithm, iteration stops when for all clusters, last time’s centroid equals to current centroid.

Fixed size algorithm is based on the result of standard KMeans. I reassigned point in oversized cluster to it’s nearest available cluster.


**********************************Metric: Quality of Solution******************************
You can see directly from png figures that standard KMeans algorithm has a better result than the fixed size
modification.

Also, in my txt file, there are iteration times and  fixed size reassignment count, which might tell about the running time.


*********************************Reusable Part of My Solution****************************
I designed points as a double array, therefore, this program can address almost any KMeans cluster problem.

My Draw Panel can draw a list of 2 dimension points.