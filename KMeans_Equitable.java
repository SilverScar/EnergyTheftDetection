/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans_equitable;

/**
 *
 * @author aditya
 */
import java.util.*;


public class KMeans_Equitable {
    
//    static ArrayList<Point> dataPoints;
//    static ArrayList<Point> new_centroids;
//    static ArrayList<Point> old_centroids;
//    static ArrayList<Integer> displacement;
//    static ArrayList<ArrayList<Point>> clusters;
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int noOfPoints = Integer.parseInt(input.next());
        int cluster_size_limit = Integer.parseInt(input.next());
        int cluster_priority_limit = Integer.parseInt(input.next());
        int K = (int)Math.ceil(noOfPoints/(double)cluster_size_limit);
        
        ArrayList<Point> dataPoints = new ArrayList<>();
        ArrayList<Point> new_centroids = new ArrayList<>();
        ArrayList<Point> old_centroids = new ArrayList<>();
        ArrayList<Integer> displacement = new ArrayList<>();
        ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
        
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<>());
        }
        
        for (int i = 0; i < noOfPoints; i++) {
            int x_coordinate = Integer.parseInt(input.next());
            int y_coordinate = Integer.parseInt(input.next());
            int point_priority = Integer.parseInt(input.next());
            Point point = new Point(x_coordinate,y_coordinate,point_priority);
            dataPoints.add(point);
            if (i < K) 
                new_centroids.add(dataPoints.get(i));
        }
        System.out.println("\nX----- K - Means Clustering -----X\n");
        KMeansAlgo(K,dataPoints,old_centroids,new_centroids,displacement,clusters);
        for (int i = 0; i < new_centroids.size(); i++) {
            System.out.println("Original Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        System.out.println("");
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("\nOriginal Cluster " + (i + 1));
            System.out.println(clusters.get(i).toString());
        }
        makeEquitable(K,cluster_size_limit,dataPoints,new_centroids,displacement,clusters);
        System.out.println("\nX----- Priority Clustering -----X\n");
        for(int i=0;i<clusters.size();i++)
            handlePriority(cluster_priority_limit, clusters.get(i));
    }
    
    /*
    * The method runs the general K-means Clustering algorithm 
    * on the given data set of Geometric Points in 2D Space
    * Time Complexity: O(I*nk), where I: no of iterations
    */
    public static void KMeansAlgo(int K, ArrayList<Point> dataPoints, ArrayList<Point> old_centroids, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters){
        
        int iteration_count = 1;
        boolean is_looping = true;
        
        
        do {         
            /*
            * Calculate euclidean distance of each point to each centroid
            * and allocate the point to the cluster having nearest centroid
            * Time Complexity: O(nk)
            */
            for (Point point : dataPoints) {
                for (Point c : new_centroids) {
                    int dist = (int)Math.sqrt((c.x - point.x)*(c.x - point.x)+(c.y - point.y)*(c.y - point.y));
                    displacement.add(dist);
                }
                int clusterID = displacement.indexOf(Collections.min(displacement));
                point.clusterID = clusterID;
                clusters.get(clusterID).add(point);
                displacement.removeAll(displacement);
                
            }
            
            /*
            * Update Old Centroids and calculate new centroids
            * Time Complexity: O(nk)
            */
            for (int i = 0; i < K; i++) {
                if (iteration_count == 1)
                    old_centroids.add(new_centroids.get(i));
                else 
                    old_centroids.set(i, new_centroids.get(i));
                if (!clusters.get(i).isEmpty()) 
                    new_centroids.set(i, getCentroid(clusters.get(i)));
            }
            
            is_looping = compareLists(K,old_centroids,new_centroids);
            
            if (is_looping) {
                for (ArrayList<Point> cluster : clusters) {
                    cluster.removeAll(cluster);
                }
            }
            iteration_count++;
        } while (is_looping);
        
        
        //System.out.println("Number of Iterations: " + iteration_count);
    }
    
    /*
    * This method is Post-Processing step
    * changes the clusters obtained by
    * K-means algorithm so that each cluster
    * satisfies the threshold limit value
    */
    public static void makeEquitable(int K, int cluster_size_limit, ArrayList<Point> dataPoints, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters){
        DisplacementComparator comparator = new DisplacementComparator();
        PriorityQueue<Point> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each point to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (Point point : dataPoints) {
            for(int i=0;i<K;i++){
                if(i != point.clusterID){
                    point.id_others.add(i);
                    point.id_others_original.add(i);
                    point.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                    point.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                }
            }
        }
        HashSet<Integer> hset = new HashSet<>();
        // Max Iterations: k-1
        while(true){
            
            int max_ind = 0,max_size=0;
            
            /*
            * Get the cluster with maximum size
            * Time Complexity: O(k)
            */
            for(int i=0;i<K;i++){
                if(!hset.contains(i) && clusters.get(i).size() > max_size){
                    max_size = clusters.get(i).size();
                    max_ind = i;
                }
            }
            if(max_size <= cluster_size_limit)
                break;
            hset.add(max_ind);
            
            /*
            * Add each point of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(Point point: clusters.get(max_ind)){
                min_heap.add(point);
            }
            
            
            while(!min_heap.isEmpty() && clusters.get(max_ind).size() > cluster_size_limit){
                Point point = min_heap.poll();
                //System.out.println(point.toString()+" "+point.dist_others.toString()+" "+point.id_others.toString());
                if(point.dist_others.isEmpty()){
                    point.restoreLists();
                    continue;
                }
                int min_index = point.dist_others.indexOf(Collections.min(point.dist_others));
                if(!hset.contains(point.id_others.get(min_index))){
                    clusters.get(point.clusterID).remove(point);
                    point.clusterID = point.id_others.get(min_index);
                    clusters.get(point.id_others.get(min_index)).add(point);
                    point.restoreLists();
                }
                else{
                    point.dist_others.remove(min_index);
                    point.id_others.remove(min_index);
                    min_heap.add(point);
                }
            }
            min_heap.clear();
        }
        
        /*
        * Recompute Final Centroids
        * Time Complexity: O(nk)
        */
        for (int i = 0; i < K; i++) {
            if (!clusters.get(i).isEmpty()) 
                new_centroids.set(i, getCentroid(clusters.get(i)));
        }
        
        System.out.println("\nX----- Equitable Clustering -----X\n");
        for (int i = 0; i < new_centroids.size(); i++) {
            System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("\nNew Cluster " + (i + 1));
            System.out.println(clusters.get(i).toString());
        }
        
    }
    
    public static void handlePriority(int cluster_priority_limit, ArrayList<Point> dataPoints){
        int cluster_priority = 0;
        for(int i=0;i<dataPoints.size();i++)
            cluster_priority+=dataPoints.get(i).priority;
        System.out.println("\n\nPriority: "+cluster_priority);
        if(cluster_priority<=cluster_priority_limit){
            System.out.println("Cluster:");
            System.out.println(dataPoints.toString());
            System.out.println("");
        }
        else{
            int k = (int)Math.ceil(cluster_priority/(double)cluster_priority_limit);
            ArrayList<Point> new_centroids = new ArrayList<>();
            ArrayList<Point> old_centroids = new ArrayList<>();
            ArrayList<Integer> displacement = new ArrayList<>();
            ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
            
            for (int i = 0; i < k; i++) 
                clusters.add(new ArrayList<>());
            
            for (int i = 0; i < k; i++) 
                new_centroids.add(dataPoints.get(i));
            
            KMeansAlgo(k, dataPoints, old_centroids, new_centroids, displacement, clusters);
            decomposeCluster(k, cluster_priority_limit, dataPoints, new_centroids, displacement, clusters);
        }
            
    }
    
    public static void decomposeCluster(int K, int cluster_priority_limit, ArrayList<Point> dataPoints, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters){
        DisplacementComparator comparator = new DisplacementComparator();
        PriorityQueue<Point> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each point to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (Point point : dataPoints) {
            for(int i=0;i<K;i++){
                if(i != point.clusterID){
                    point.id_others.add(i);
                    point.id_others_original.add(i);
                    point.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                    point.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - point.x)*(new_centroids.get(i).x - point.x)+(new_centroids.get(i).y - point.y)*(new_centroids.get(i).y - point.y)));
                }
            }
        }
        HashSet<Integer> hset = new HashSet<>();
        // Max Iterations: k-1
        while(true){
            
            int max_ind = 0,max_priority=0,curr_priority=0;
            
            /*
            * Get the cluster with maximum size
            * Time Complexity: O(k)
            */
            for(int i=0;i<K;i++){
                if(!hset.contains(i)){
                    for(int j=0;j<clusters.get(i).size();j++)
                        curr_priority+=clusters.get(i).get(j).priority;
                    if(curr_priority>max_priority){
                        max_priority = clusters.get(i).size();
                        max_ind = i;
                    }
                }
                curr_priority = 0;
            }
            if(max_priority <= cluster_priority_limit)
                break;
            hset.add(max_ind);
            
            /*
            * Add each point of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(Point point: clusters.get(max_ind)){
                min_heap.add(point);
            }
            
            
            while(!min_heap.isEmpty() && max_priority > cluster_priority_limit){
                Point point = min_heap.poll();
                //System.out.println(point.toString()+" "+point.dist_others.toString()+" "+point.id_others.toString());
                if(point.dist_others.isEmpty()){
                    point.restoreLists();
                    continue;
                }
                int min_index = point.dist_others.indexOf(Collections.min(point.dist_others));
                if(!hset.contains(point.id_others.get(min_index))){
                    clusters.get(point.clusterID).remove(point);
                    point.clusterID = point.id_others.get(min_index);
                    clusters.get(point.id_others.get(min_index)).add(point);
                    point.restoreLists();
                    max_priority-=point.priority;
                }
                else{
                    point.dist_others.remove(min_index);
                    point.id_others.remove(min_index);
                    min_heap.add(point);
                }
            }
            min_heap.clear();
        }
        
        /*
        * Recompute Final Centroids
        * Time Complexity: O(nk)
        */
        for (int i = 0; i < K; i++) {
            if (!clusters.get(i).isEmpty()) 
                new_centroids.set(i, getCentroid(clusters.get(i)));
        }
        
        for (int i = 0; i < new_centroids.size(); i++) {
            System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("\nNew Cluster " + (i + 1));
            System.out.println(clusters.get(i).toString());
        }
    }
            
    
    /*
    * This method checks if centroids have changed
    * Time Complexity: O(k)
    */
    public static boolean compareLists(int k, ArrayList<Point> old_centroids, ArrayList<Point> new_centroids){
        for (int i = 0; i < k; i++) {
            if(old_centroids.get(i).x != new_centroids.get(i).x || old_centroids.get(i).y != new_centroids.get(i).y)
                return true;
        }
        return false;
    }
    
    /*
    * This method detects if some cluster 
    * has more than specified no. of points
    * Time Complexity: O(k)
    */
    /*public static boolean detectOverflow(int cluster_size_limit, ArrayList<ArrayList<Point>> clusters){
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).size()>cluster_size_limit)
                return true;
        }
        return false;
    }*/
    
    /*
    * This method computes the centroid of a cluster by
    * taking average of all points in the cluster
    * Time Complexity: O(n)
    */
    public static Point getCentroid(ArrayList<Point> list){
        int xsum = 0;
        int ysum = 0;
        int len = list.size();
        for (Point value : list) {
            xsum = xsum + value.x;
            ysum = ysum + value.y;
        }
        return new Point(xsum/len,ysum/len,0);
    }
    
}

/*
* Class Structure of 2D Geometric Point
*/
class Point{
    int x,y; 
    int clusterID;
    int priority;
    ArrayList<Integer> dist_others;
    ArrayList<Integer> id_others;
    ArrayList<Integer> dist_others_original;
    ArrayList<Integer> id_others_original;
    
    Point(int x, int y, int priority){
        this.x = x;
        this.y = y;
        this.priority = priority;
        this.clusterID = 0;
        this.dist_others = new ArrayList<>();
        this.id_others = new ArrayList<>();
        this.dist_others_original = new ArrayList<>();
        this.id_others_original = new ArrayList<>();
    }
    
    void restoreLists(){
        ArrayList<Integer> temp_dist = (ArrayList<Integer>)dist_others_original.clone();
        dist_others = temp_dist;
        ArrayList<Integer> temp_id = (ArrayList<Integer>)id_others_original.clone();
        id_others = temp_id;
    }
    
    @Override
    public String toString(){
        return "("+x+" "+y+")";
    }
}

class DisplacementComparator implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return Collections.min(p1.dist_others) - Collections.min(p2.dist_others);
    }
}

