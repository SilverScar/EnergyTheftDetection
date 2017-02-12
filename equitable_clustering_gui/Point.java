/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equitable_clustering_gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author aditya
 */
public class Point {
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
