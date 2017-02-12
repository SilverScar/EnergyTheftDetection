/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equitable_clustering_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author aditya
 */
public class Equitable_Clustering_GUI{

    public static JFrame main_window;
    public static JButton button_render;
    public static JButton button_KMeans;
    public static JButton button_equitable;
    public static JButton button_priority;
    public static JButton button_clear;
    public static JTextField text_input = new JTextField(20);
    public static JPanel panel;
    public static Color[] myColors = {Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.ORANGE,Color.BLACK,Color.CYAN,Color.LIGHT_GRAY,Color.PINK,Color.MAGENTA,Color.DARK_GRAY,Color.GRAY};
    
    static ArrayList<Point> dataPoints;
    static ArrayList<Point> new_centroids;
    static ArrayList<Point> old_centroids;
    static ArrayList<Integer> displacement;
    static ArrayList<ArrayList<Point>> clusters;
    static ArrayList<Graphics> dots;
    static int K = 0;
    static int cluster_priority_limit = 1;
    static int cluster_size_limit = 0;
    static int color_counter = 0;
    
    public static void main(String[] args) {
        
        buildMainWindow();
    }
    
    public static void buildMainWindow(){
        main_window = new JFrame();
        main_window.setTitle("Equitable Clustering");
        main_window.setSize(480,320);
        main_window.setLocation(220,180);
        main_window.setResizable(false);
        main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configureMainWindow();
        mainWindow_action();
        main_window.setVisible(true);
    }
    
    public static void configureMainWindow(){
        main_window.setBackground(new Color(255, 255, 255));
        main_window.setSize(720,485);
        main_window.getContentPane().setLayout(null);
      
        panel = new JPanel();
        panel.setSize(720, 485);
        panel.setBackground(Color.WHITE);
        //panel.setLayout(null);
        main_window.getContentPane().add(panel);
        
        button_render = new JButton();
        button_render.setBackground(new Color(0,0,255));
        button_render.setForeground(new Color(255,255,255));
        button_render.setText("Render");
        //main_window.getContentPane().add(button_render);
        panel.add(button_render);
        button_render.setBounds(5,50,120,25);
      
        button_KMeans = new JButton();
        button_KMeans.setBackground(new Color(0,0,255));
        button_KMeans.setForeground(new Color(255,255,255));
        button_KMeans.setText("K-Means");
        button_KMeans.setToolTipText("");
        //main_window.getContentPane().add(button_KMeans);
        panel.add(button_KMeans);
        button_KMeans.setBounds(170,50,120,25);
        button_KMeans.setEnabled(false);
        
        text_input.setForeground(new Color(0,0,255));
        //main_window.getContentPane().add(text_input);
        
        text_input.setBounds(5, 5, 510, 50);
        panel.add(text_input);
    
        button_equitable = new JButton();
        button_equitable.setBackground(new Color(0,0,255));
        button_equitable.setForeground(new Color(255, 255, 255));
        button_equitable.setText("Equitable");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_equitable);
        button_equitable.setBounds(335,50,120,25);
        button_equitable.setEnabled(false);
        
        button_priority = new JButton();
        button_priority.setBackground(new Color(0,0,255));
        button_priority.setForeground(new Color(255, 255, 255));
        button_priority.setText("Priority");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_priority);
        button_priority.setBounds(335,50,120,25);
        button_priority.setEnabled(false);
        
        button_clear = new JButton();
        button_clear.setBackground(new Color(0,0,255));
        button_clear.setForeground(new Color(255, 255, 255));
        button_clear.setText("Clear");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_clear);
        button_clear.setBounds(335,50,120,25);
        button_clear.setEnabled(true);
        
        //main_window.pack();

    }
    
    public static void mainWindow_action(){
        button_render.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                action_button_render();
            }
        });
        
        button_KMeans.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                KMeansAlgo(K,dataPoints,old_centroids,new_centroids,displacement,clusters);
            }
        });
        
        button_equitable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                makeEquitable();
            }
        });
        
        button_priority.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<clusters.size();i++)
                    handlePriority(clusters.get(i));
                button_equitable.setEnabled(false);
                button_KMeans.setEnabled(false);
                button_priority.setEnabled(false);
                button_render.setEnabled(false);
            }
        });
        
        button_clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
                button_KMeans.setEnabled(false);
                button_equitable.setEnabled(false);
                button_priority.setEnabled(false);
                button_render.setEnabled(true);
                color_counter = 0;
            }
        });
        
    }
    
    public static void action_button_render(){
        String input = text_input.getText();
        String[] inp_arr = input.split(" ");
        int noOfPoints = Integer.parseInt(inp_arr[0]);
        cluster_size_limit = Integer.parseInt(inp_arr[1]);
        cluster_priority_limit = Integer.parseInt(inp_arr[2]);
        K = (int)Math.ceil(noOfPoints/(double)cluster_size_limit);
        
        dataPoints = new ArrayList<>();
        new_centroids = new ArrayList<>();
        old_centroids = new ArrayList<>();
        displacement = new ArrayList<>();
        clusters = new ArrayList<>();
        dots = new ArrayList<>();
        
        int it = 3;
        
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<>());
        }
        
        for (int i = 0; i < noOfPoints; i++) {
            int x_coordinate = Integer.parseInt(inp_arr[it++]);
            int y_coordinate = Integer.parseInt(inp_arr[it++])+50;
            int point_priority = Integer.parseInt(inp_arr[it++]);
            Point point = new Point(x_coordinate,y_coordinate,point_priority);
            Graphics g = panel.getGraphics();
            g.setColor(Color.BLACK);
            g.fillOval(x_coordinate, y_coordinate, 8, 8);
            g.dispose();
            dataPoints.add(point);
            if (i < K) 
                new_centroids.add(dataPoints.get(i));
        }
        button_KMeans.setEnabled(true);
    }
    
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
        
        //System.out.println("\nX----- K - Means Clustering -----X\n");
        for (int i = 0; i < new_centroids.size(); i++) {
            //System.out.println("Original Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        //System.out.println("");
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nOriginal Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[i]);
                g.fillOval(x_c, y_c, 8, 8);
                g.dispose();
            }
        }
        //System.out.println("Number of Iterations: " + iteration_count);
        button_KMeans.setEnabled(false);
        button_equitable.setEnabled(true);
    }
    
    /*
    * This method is Post-Processing step
    * changes the clusters obtained by
    * K-means algorithm so that each cluster
    * satisfies the threshold limit value
    */
    public static void makeEquitable(){
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
        
        //System.out.println("\nX----- Equitable Clustering -----X\n");
        for (int i = 0; i < new_centroids.size(); i++) {
            //System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        //System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nNew Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[i]);
                g.fillOval(x_c, y_c, 8, 8);
                g.dispose();
            }
        }
        button_equitable.setEnabled(false);
        button_KMeans.setEnabled(false);
        button_priority.setEnabled(true);
        button_render.setEnabled(false);
    }
    
    public static void handlePriority(ArrayList<Point> dataPoints){
        int cluster_priority = 0;
        for(int i=0;i<dataPoints.size();i++)
            cluster_priority+=dataPoints.get(i).priority;
        
        if(cluster_priority<=cluster_priority_limit){
            System.out.println("Mark 1");
            for(int j=0;j<dataPoints.size();j++){
                int x_c = dataPoints.get(j).x;
                int y_c = dataPoints.get(j).y;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                g.fillOval(x_c, y_c, 8, 8);
                g.dispose();
            }
            color_counter++;
            
        }
        else{
            System.out.println("Mark 2");
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
            decomposeCluster(k, dataPoints, new_centroids, displacement, clusters);
        }
            
    }
    
    public static void decomposeCluster(int K, ArrayList<Point> dataPoints, ArrayList<Point> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<Point>> clusters){
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
            System.out.println("Mark 3");
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
        
        /*for (int i = 0; i < new_centroids.size(); i++) {
            System.out.println("New Centroid" + (i + 1) + " " + new_centroids.get(i));
        }
        
        System.out.println("");
        
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("\nNew Cluster " + (i + 1));
            System.out.println(clusters.get(i).toString());
        }*/
        for (int i = 0; i < clusters.size(); i++) {
            //System.out.println("\nNew Cluster " + (i + 1));
            //System.out.println(clusters.get(i).toString());
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                g.fillOval(x_c, y_c, 8, 8);
                g.dispose();
            }
            color_counter++;
            
        }
        
    }
    
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
    public static boolean detectOverflow(int cluster_size_limit){
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).size()>cluster_size_limit)
                return true;
        }
        return false;
    }
    
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
