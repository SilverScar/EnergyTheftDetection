/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedclusteringetd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author aditya
 */
public class SmartMeter {
    int id;
    int x,y; 
    int clusterID;
    int priority;
    final double theft_probability = 0.3;
    final double theft_rate_low = 1.1;
    final double theft_rate_high = 10;
    double[] meter_readings;
    double[][] L;
    double[][] U;
    double[][] S;
    double theft_rate;
    ArrayList<Integer> dist_others;
    ArrayList<Integer> id_others;
    ArrayList<Integer> dist_others_original;
    ArrayList<Integer> id_others_original;
    ArrayList<Double> Y;
    
    public SmartMeter(int id, int x, int y, int priority){
        this.id = id;
        this.x = x;
        this.y = y;
        this.priority = priority;
        this.clusterID = 0;
        this.dist_others = new ArrayList<>();
        this.id_others = new ArrayList<>();
        this.dist_others_original = new ArrayList<>();
        this.id_others_original = new ArrayList<>();
        this.Y = new ArrayList<>();
        Random r = new Random();
        double probability = r.nextDouble();
        if(probability <= this.theft_probability)
            this.theft_rate = this.theft_rate_low + (this.theft_rate_high-this.theft_rate_low)*r.nextDouble();
        else{
            this.theft_rate = 1.0;
        }
    }
    
    void restoreLists(){
        ArrayList<Integer> temp_dist = (ArrayList<Integer>)dist_others_original.clone();
        dist_others = temp_dist;
        ArrayList<Integer> temp_id = (ArrayList<Integer>)id_others_original.clone();
        id_others = temp_id;
    }
    
}

class DisplacementComparator implements Comparator<SmartMeter> {

    @Override
    public int compare(SmartMeter sm1, SmartMeter sm2) {
        return Collections.min(sm1.dist_others) - Collections.min(sm2.dist_others);
    }
}
