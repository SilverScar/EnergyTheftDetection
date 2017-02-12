/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energy_theft_detection;

import java.util.ArrayList;

/**
 *
 * @author aditya
 */
public class SmartMeter extends Client {
    double[][] L;
    double[][] U;
    double[][] S;
    ArrayList<Double> Y;
    
    public SmartMeter(int id, int n, double[] meter_readings){
        super(id,n,meter_readings);
        this.L = new double[n][];
        this.U = new double[n][];
        this.Y = new ArrayList<>();
        this.S = new double[n][];
    }
    
    void printAssets(){
        int i,j;
        System.out.println("L:");
        for(i=0;i<=this.id;i++){
            for(j=0;j<n;j++)
                System.out.print(this.L[i][j]+" ");
            System.out.println("");
        }
        System.out.println("\nU:");
        for(i=0;i<=this.id;i++){
            for(j=0;j<n;j++)
                System.out.print(this.U[i][j]+" ");
            System.out.println("");
        }
        System.out.println("\nY:");
        for(j=0;j<=this.id;j++)
            System.out.print(this.Y.get(j)+" ");
    }
}
