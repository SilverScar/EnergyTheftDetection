/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedclusteringetd;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aditya
 */
public class EnergyTheftDetection implements Runnable {
    
    int n;
    int clusterID;
    final int sampling_time = 2000;
    double[] aggregator_readings;
    double[] honesty_coefficients;
    SmartMeter[] smartMeters;
    
    public EnergyTheftDetection(int clusterID, SmartMeter[] smartMeters, int n){
        this.clusterID = clusterID;
        this.n = n;
        this.smartMeters = smartMeters;
        for(int i=0;i<n;i++){
            smartMeters[i].L = new double[n][];
            smartMeters[i].U = new double[n][];
            smartMeters[i].S = new double[n][];
            smartMeters[i].meter_readings = new double[n];
        }
        this.aggregator_readings = new double[n];
        this.honesty_coefficients = new double[n];
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Random random = new Random();
        for(int i=0;i<n;i++){
            double sum = 0;
            for(int j=0;j<n;j++){
                int rand_num = random.nextInt(40)+1;
                smartMeters[j].meter_readings[i] = rand_num;
                sum+=(rand_num*smartMeters[j].theft_rate);
            }
            aggregator_readings[i] = sum;
            System.out.println("Cluster:"+clusterID+" T"+i);
            if(i<n-1)
                try{Thread.sleep(sampling_time);}catch(Exception exception){System.out.println(exception);};
        }
        distributedDecomposition(smartMeters, 0, n, null, null, null, aggregator_readings);
        backwardSubstitution(smartMeters, n-1, n, 0, null, honesty_coefficients);
        System.out.println("\n\n");
        for(int i=0;i<n;i++){
            if(honesty_coefficients[i]>0.9 && honesty_coefficients[i]<1.1)
                honesty_coefficients[i] = 1.0;
            System.out.println("SM: "+smartMeters[i].id+"\t\tHC: "+(double)Math.round(honesty_coefficients[i]*100)/100+"\t\tTR: "+(double)Math.round(smartMeters[i].theft_rate*100)/100);
        }
        long endTime   = System.currentTimeMillis();
        double totalTime = (endTime - startTime)/60000.0;
        System.out.println("\nCluster:"+clusterID+"\t\tRun-Time: "+(double)Math.round(totalTime*100)/100+"min.");
        
    }
    
    private static void distributedDecomposition(SmartMeter[] smartMeters, int id, int n, double[][] l, double[][] u, ArrayList<Double> y, double[] aggregator_readings){
        if(id==n)
            return;
        for(int i=0;i<=id;i++){
            smartMeters[id].L[i] = new double[n];
            smartMeters[id].U[i] = new double[n];
        }
        for(int i=0;i<id;i++){
            smartMeters[id].L[i] = l[i];
            smartMeters[id].U[i] = u[i];
        }
        smartMeters[id].U[id][0] = smartMeters[id].meter_readings[0];
        for(int i=1;i<n;i++){
            if(i>id)
                smartMeters[id].U[id][i] = 0;
            else{
                double sum = 0.0;
                for(int j=0;j<i;j++)
                    sum+=(smartMeters[id].L[j][i]*smartMeters[id].U[id][j]);
                smartMeters[id].U[id][i] = smartMeters[id].meter_readings[i]-sum;
            }
        }
        if(id==0){
            smartMeters[id].Y.add(aggregator_readings[id]);
            for(int i=0;i<n;i++)
                smartMeters[id].L[id][i] = smartMeters[id].meter_readings[i]/smartMeters[id].meter_readings[0];
        }
        else{
//            for(int i=0;i<id;i++){
//                for(int j=0;j<n;j++)
//                    System.out.print(l[i][j]+" ");
//            }
//            System.out.println();
//            for(int i=0;i<id;i++){
//                for(int j=0;j<n;j++)
//                    System.out.print(u[i][j]+" ");
//            }
//            System.out.println();
            smartMeters[id].Y = y;
            for(int i=0;i<n;i++){
                if(i<id)
                    smartMeters[id].L[id][i] = 0.0;
                else{
                    double sum = 0.0;
                    for(int j=0;j<id;j++)
                        sum+=(smartMeters[id].L[j][i]*smartMeters[id].U[id][j]);
                    smartMeters[id].L[id][i] = (smartMeters[id].meter_readings[i]-sum)/smartMeters[id].U[id][id];
                }
            }
            double sum = 0.0;
            for(int j=0;j<id;j++)
                sum+=(smartMeters[id].L[j][id]*smartMeters[id].Y.get(j));
            smartMeters[id].Y.add(aggregator_readings[id]-sum);
        }
        //System.out.println(smartMeters[id].Y.toString());
        distributedDecomposition(smartMeters, id+1, n, smartMeters[id].L, smartMeters[id].U, smartMeters[id].Y, aggregator_readings);
    }
    
    private static void backwardSubstitution(SmartMeter[] smartMeters, int id, int n, double sum, double[][] s, double[] honesty_coefficients){
        for(int i=n-1;i>=id;i--)
            smartMeters[id].S[i] = new double[n];
        for(int i=n-1;i>id;i--)
            smartMeters[id].S[i] = s[i];
            
        if(id==n-1)
            honesty_coefficients[id] = smartMeters[id].Y.get(id)/smartMeters[id].U[id][id];
        else
            honesty_coefficients[id] = (smartMeters[id].Y.get(id)-sum)/smartMeters[id].U[id][id];
        
        for(int i=0;i<n;i++)
            smartMeters[id].S[id][i] = smartMeters[id].U[id][i]*honesty_coefficients[id];
        
        sum=0;
        if(id>0){
            for(int i=id;i<n;i++)
                sum+=smartMeters[id].S[i][id-1];
            //System.out.println(honesty_coefficients[id]);

            backwardSubstitution(smartMeters, id-1, n, sum, smartMeters[id].S, honesty_coefficients);
        }
    }
    
}
