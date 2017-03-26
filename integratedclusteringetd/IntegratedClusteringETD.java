/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedclusteringetd;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author aditya
 */
public class IntegratedClusteringETD {

    static int K = 0;
    static int cluster_priority_limit = 1;
    static int cluster_size_limit = 0;
    static int color_counter = 0;
    public static JFrame main_window;
    public static JButton button_render;
    public static JButton button_KMeans;
    public static JButton button_equitable;
    public static JButton button_priority;
    public static JButton button_clear;
    public static JButton button_etd;
    public static JTextField text_input = new JTextField(20);
    public static JPanel panel;
    public static Color[] myColors = {Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.ORANGE,Color.BLACK,Color.CYAN,Color.LIGHT_GRAY,Color.PINK,Color.MAGENTA,Color.DARK_GRAY,Color.GRAY};
    static ArrayList<SmartMeter> dataSmartMeters;
    static ArrayList<SmartMeter> new_centroids;
    static ArrayList<SmartMeter> old_centroids;
    static ArrayList<Integer> displacement;
    static ArrayList<ArrayList<SmartMeter>> clusters;
    static ArrayList<ArrayList<SmartMeter>> final_clusters;
    static ArrayList<Graphics> dots;
    
    public static void main(String[] args) {
        
        buildMainWindow();
    }
    
    public static void buildMainWindow(){
        main_window = new JFrame();
        main_window.setTitle("AMI Clustering");
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
        main_window.setSize(860,485);
        main_window.getContentPane().setLayout(null);
      
        panel = new JPanel();
        panel.setSize(860, 485);
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
        //text_input.setText(default_input);
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
        
        button_etd = new JButton();
        button_etd.setBackground(new Color(0,0,255));
        button_etd.setForeground(new Color(255, 255, 255));
        button_etd.setText("ETD");
        //main_window.getContentPane().add(button_equitable);
        panel.add(button_etd);
        button_etd.setBounds(335,50,120,25);
        button_etd.setEnabled(false);
        
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
                System.out.println("<-------------- K-Means Clustering -------------->\n");
                KMeansAlgo(K,dataSmartMeters,old_centroids,new_centroids,displacement,clusters,true);
            }
        });
        
        button_equitable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<-------------- Equitable Shifting -------------->\n");
                makeEquitable();
            }
        });
        
        button_priority.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("<-------------- Priority Decomposition -------------->\n");
                for(int i=0;i<clusters.size();i++)
                    handlePriority(clusters.get(i));
                button_equitable.setEnabled(false);
                button_KMeans.setEnabled(false);
                button_priority.setEnabled(false);
                button_render.setEnabled(false);
                button_etd.setEnabled(true);
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
                button_etd.setEnabled(false);
                color_counter = 0;
            }
        });
        
        button_etd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i=0;
                for (ArrayList<SmartMeter> cluster : final_clusters) {
                    int n = cluster.size();
                    SmartMeter[] smartMeters = new SmartMeter[n];
                    cluster.toArray(smartMeters);
                    EnergyTheftDetection etd = new EnergyTheftDetection(i,smartMeters,n);
                    Thread thread = new Thread(etd);
                    thread.start();
                    i++;
                }
                main_window.dispose();
            }
        });
        
    }
    
    public static void action_button_render(){
        String input = text_input.getText();
        String[] inp_arr = input.split(" ");
        int noOfSmartMeters = Integer.parseInt(inp_arr[0]);
        cluster_size_limit = Integer.parseInt(inp_arr[1]);
        cluster_priority_limit = Integer.parseInt(inp_arr[2]);
        K = (int)Math.ceil(noOfSmartMeters/(double)cluster_size_limit);
        System.out.println("No. of Smart Meters: "+noOfSmartMeters+"\nSize Threshold: "+cluster_size_limit+"\nPriority Threshold: "+cluster_priority_limit+"\n");
        dataSmartMeters = new ArrayList<>();
        new_centroids = new ArrayList<>();
        old_centroids = new ArrayList<>();
        displacement = new ArrayList<>();
        clusters = new ArrayList<>();
        final_clusters = new ArrayList<>();
        dots = new ArrayList<>();
        
        int it = 3;
        
        for (int i = 0; i < K; i++) {
            clusters.add(new ArrayList<>());
        }
        
        for (int i = 0; i < noOfSmartMeters; i++) {
            int x_coordinate = Integer.parseInt(inp_arr[it++]);
            int y_coordinate = Integer.parseInt(inp_arr[it++])+50;
            int smartMeter_priority = Integer.parseInt(inp_arr[it++]);
            SmartMeter smartMeter = new SmartMeter(i,x_coordinate,y_coordinate,smartMeter_priority);
            Graphics g = panel.getGraphics();
            g.setColor(Color.BLACK);
            if(smartMeter.priority == 1)
                g.fillOval(x_coordinate, y_coordinate, 8, 8);
            else if(smartMeter.priority == 2)
                g.fillRect(x_coordinate, y_coordinate, 7, 7); 
            else{
                int xsmartMeters[] = {x_coordinate, x_coordinate-4, x_coordinate+3};
                int ysmartMeters[] = {y_coordinate-4, y_coordinate+4, y_coordinate+3};
                int nsmartMeters = 3;
    
                g.fillPolygon(xsmartMeters, ysmartMeters, nsmartMeters);
            }
            g.dispose();
            dataSmartMeters.add(smartMeter);
            if (i < K) 
                new_centroids.add(dataSmartMeters.get(i));
        }
        button_KMeans.setEnabled(true);
    }
    
    public static void KMeansAlgo(int K, ArrayList<SmartMeter> dataSmartMeters, ArrayList<SmartMeter> old_centroids, ArrayList<SmartMeter> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<SmartMeter>> clusters, boolean flag){
        
        int iteration_count = 1;
        boolean is_looping = true;
        
        
        do {         
            /*
            * Calculate euclidean distance of each smartMeter to each centroid
            * and allocate the smartMeter to the cluster having nearest centroid
            * Time Complexity: O(nk)
            */
            for (SmartMeter smartMeter : dataSmartMeters) {
                for (SmartMeter c : new_centroids) {
                    int dist = (int)Math.sqrt((c.x - smartMeter.x)*(c.x - smartMeter.x)+(c.y - smartMeter.y)*(c.y - smartMeter.y));
                    displacement.add(dist);
                }
                int clusterID = displacement.indexOf(Collections.min(displacement));
                smartMeter.clusterID = clusterID;
                clusters.get(clusterID).add(smartMeter);
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
                for (ArrayList<SmartMeter> cluster : clusters) {
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
            int cluster_priority = 0;
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                if(i<myColors.length)
                    g.setColor(myColors[i]);
                else
                    g.setColor(myColors[0]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xsmartMeters[] = {x_c, x_c-4, x_c+3};
                    int ysmartMeters[] = {y_c-4, y_c+4, y_c+3};
                    int nsmartMeters = 3;
    
                    g.fillPolygon(xsmartMeters, ysmartMeters, nsmartMeters);
                }
                g.dispose();
            }
            if(flag)
                System.out.println("Cluster "+(i+1)+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
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
        PriorityQueue<SmartMeter> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each smartMeter to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (SmartMeter smartMeter : dataSmartMeters) {
            for(int i=0;i<K;i++){   
                if(i != smartMeter.clusterID){
                    smartMeter.id_others.add(i);
                    smartMeter.id_others_original.add(i);
                    smartMeter.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - smartMeter.x)*(new_centroids.get(i).x - smartMeter.x)+(new_centroids.get(i).y - smartMeter.y)*(new_centroids.get(i).y - smartMeter.y)));
                    smartMeter.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - smartMeter.x)*(new_centroids.get(i).x - smartMeter.x)+(new_centroids.get(i).y - smartMeter.y)*(new_centroids.get(i).y - smartMeter.y)));
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
            * Add each smartMeter of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(SmartMeter smartMeter: clusters.get(max_ind)){
                min_heap.add(smartMeter);
            }
            
            
            while(!min_heap.isEmpty() && clusters.get(max_ind).size() > cluster_size_limit){
                SmartMeter smartMeter = min_heap.poll();
                //System.out.println(smartMeter.toString()+" "+smartMeter.dist_others.toString()+" "+smartMeter.id_others.toString());
                if(smartMeter.dist_others.isEmpty()){
                    smartMeter.restoreLists();
                    continue;
                }
                int min_index = smartMeter.dist_others.indexOf(Collections.min(smartMeter.dist_others));
                if(!hset.contains(smartMeter.id_others.get(min_index))){
                    clusters.get(smartMeter.clusterID).remove(smartMeter);
                    smartMeter.clusterID = smartMeter.id_others.get(min_index);
                    clusters.get(smartMeter.id_others.get(min_index)).add(smartMeter);
                    smartMeter.restoreLists();
                }
                else{
                    smartMeter.dist_others.remove(min_index);
                    smartMeter.id_others.remove(min_index);
                    min_heap.add(smartMeter);
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
            int cluster_priority = 0;
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                if(i<myColors.length)
                    g.setColor(myColors[i]);
                else
                    g.setColor(myColors[0]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xsmartMeters[] = {x_c, x_c-4, x_c+3};
                    int ysmartMeters[] = {y_c-4, y_c+4, y_c+3};
                    int nsmartMeters = 3;
    
                    g.fillPolygon(xsmartMeters, ysmartMeters, nsmartMeters);
                }
                g.dispose();
            }
            System.out.println("Cluster "+(i+1)+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
        }
        button_equitable.setEnabled(false);
        button_KMeans.setEnabled(false);
        button_priority.setEnabled(true);
        button_render.setEnabled(false);
    }
    
    public static void handlePriority(ArrayList<SmartMeter> dataSmartMeters){
        int cluster_priority = 0;
        for(int i=0;i<dataSmartMeters.size();i++)
            cluster_priority+=dataSmartMeters.get(i).priority;
        
        if(cluster_priority<=cluster_priority_limit){
            //System.out.println("Mark 1");
            
            for(int j=0;j<dataSmartMeters.size();j++){
                int x_c = dataSmartMeters.get(j).x;
                int y_c = dataSmartMeters.get(j).y;
                int priority = dataSmartMeters.get(j).priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xsmartMeters[] = {x_c, x_c-4, x_c+3};
                    int ysmartMeters[] = {y_c-4, y_c+4, y_c+3};
                    int nsmartMeters = 3;
    
                    g.fillPolygon(xsmartMeters, ysmartMeters, nsmartMeters);
                }
                g.dispose();
            }
            final_clusters.add(dataSmartMeters);
            if(color_counter < myColors.length-1)
                color_counter++;
            System.out.println("Cluster "+color_counter+": Size - "+dataSmartMeters.size()+"\tPriority - "+cluster_priority+"\n");
        }
        else{
            //System.out.println("Mark 2");
            int k = (int)Math.ceil(cluster_priority/(double)cluster_priority_limit);
            ArrayList<SmartMeter> new_centroids = new ArrayList<>();
            ArrayList<SmartMeter> old_centroids = new ArrayList<>();
            ArrayList<Integer> displacement = new ArrayList<>();
            ArrayList<ArrayList<SmartMeter>> clusters = new ArrayList<>();
            
            for (int i = 0; i < k; i++) 
                clusters.add(new ArrayList<>());
            
            for (int i = 0; i < k; i++) 
                new_centroids.add(dataSmartMeters.get(i));
            
            KMeansAlgo(k, dataSmartMeters, old_centroids, new_centroids, displacement, clusters,false);
            decomposeCluster(k, dataSmartMeters, new_centroids, displacement, clusters);
        }
            
    }
    
    public static void decomposeCluster(int K, ArrayList<SmartMeter> dataSmartMeters, ArrayList<SmartMeter> new_centroids, ArrayList<Integer> displacement, ArrayList<ArrayList<SmartMeter>> clusters){
        DisplacementComparator comparator = new DisplacementComparator();
        PriorityQueue<SmartMeter> min_heap = new PriorityQueue<>(comparator);
        
        /*
        * Calculate euclidean distance of each smartMeter to each centroid
        * except its own and make two duplicate lists of these values
        * Time Complexity: O(nk)
        */
        for (SmartMeter smartMeter : dataSmartMeters) {
            for(int i=0;i<K;i++){
                if(i != smartMeter.clusterID){
                    smartMeter.id_others.add(i);
                    smartMeter.id_others_original.add(i);
                    smartMeter.dist_others.add((int)Math.sqrt((new_centroids.get(i).x - smartMeter.x)*(new_centroids.get(i).x - smartMeter.x)+(new_centroids.get(i).y - smartMeter.y)*(new_centroids.get(i).y - smartMeter.y)));
                    smartMeter.dist_others_original.add((int)Math.sqrt((new_centroids.get(i).x - smartMeter.x)*(new_centroids.get(i).x - smartMeter.x)+(new_centroids.get(i).y - smartMeter.y)*(new_centroids.get(i).y - smartMeter.y)));
                }
            }
        }
        HashSet<Integer> hset = new HashSet<>();
        // Max Iterations: k-1
        while(true){
            //System.out.println("Mark 3");
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
            * Add each smartMeter of the cluster in Priority Queue
            * Time Complexity: O(nlogn)
            */
            for(SmartMeter smartMeter: clusters.get(max_ind)){
                min_heap.add(smartMeter);
            }
            
            
            while(!min_heap.isEmpty() && max_priority > cluster_priority_limit){
                SmartMeter smartMeter = min_heap.poll();
                //System.out.println(smartMeter.toString()+" "+smartMeter.dist_others.toString()+" "+smartMeter.id_others.toString());
                if(smartMeter.dist_others.isEmpty()){
                    smartMeter.restoreLists();
                    continue;
                }
                int min_index = smartMeter.dist_others.indexOf(Collections.min(smartMeter.dist_others));
                if(!hset.contains(smartMeter.id_others.get(min_index))){
                    clusters.get(smartMeter.clusterID).remove(smartMeter);
                    smartMeter.clusterID = smartMeter.id_others.get(min_index);
                    clusters.get(smartMeter.id_others.get(min_index)).add(smartMeter);
                    smartMeter.restoreLists();
                    max_priority-=smartMeter.priority;
                }
                else{
                    smartMeter.dist_others.remove(min_index);
                    smartMeter.id_others.remove(min_index);
                    min_heap.add(smartMeter);
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
            int cluster_priority = 0;
            final_clusters.add(clusters.get(i));
            for(int j=0;j<clusters.get(i).size();j++){
                int x_c = clusters.get(i).get(j).x;
                int y_c = clusters.get(i).get(j).y;
                int priority = clusters.get(i).get(j).priority;
                cluster_priority+=priority;
                Graphics g = panel.getGraphics();
                g.setColor(myColors[color_counter]);
                if(priority == 1)
                    g.fillOval(x_c, y_c, 8, 8);
                else if(priority == 2)
                    g.fillRect(x_c, y_c, 7, 7); 
                else{
                    int xsmartMeters[] = {x_c, x_c-4, x_c+3};
                    int ysmartMeters[] = {y_c-4, y_c+4, y_c+3};
                    int nsmartMeters = 3;
    
                    g.fillPolygon(xsmartMeters, ysmartMeters, nsmartMeters);
                }
                g.dispose();
            }
            if(color_counter < myColors.length-1)
                color_counter++;    
            System.out.println("Cluster "+color_counter+": Size - "+clusters.get(i).size()+"\tPriority - "+cluster_priority+"\n");
        }
        
    }
    
    public static boolean compareLists(int k, ArrayList<SmartMeter> old_centroids, ArrayList<SmartMeter> new_centroids){
        for (int i = 0; i < k; i++) {
            if(old_centroids.get(i).x != new_centroids.get(i).x || old_centroids.get(i).y != new_centroids.get(i).y)
                return true;
        }
        return false;
    }
    
    /*
    * This method detects if some cluster 
    * has more than specified no. of smartMeters
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
    * taking average of all smartMeters in the cluster
    * Time Complexity: O(n)
    */
    public static SmartMeter getCentroid(ArrayList<SmartMeter> list){
        int xsum = 0;
        int ysum = 0;
        int len = list.size();
        for (SmartMeter value : list) {
            xsum = xsum + value.x;
            ysum = ysum + value.y;
        }
        return new SmartMeter(-1,xsum/len,ysum/len,0);
    }
    
}
