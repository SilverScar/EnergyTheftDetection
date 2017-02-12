/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energy_theft_detection;

import java.util.Scanner;

/**
 *
 * @author aditya
 */
public class Energy_Theft_Detection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = Integer.parseInt(stdIn.next());
        System.out.print("Enter Smart Meter ID: ");
        int id = Integer.parseInt(stdIn.next());
        double[] readings = new double[n];
        System.out.print("Enter Readings: ");
        for(int i=0;i<n;i++)
            readings[i] = Double.parseDouble(stdIn.next());
        Client client;
        if(id<0)
            client = new Aggregator(id, n, readings);
        else
            client = new SmartMeter(id,n,readings);
        MessageListener messageListener = new MessageListener(id+8660, client);
        messageListener.start();
        
        while(true){
            int flag = Integer.parseInt(stdIn.next());
            if(flag==0)
                System.exit(0);
            else if(flag==1){
                Aggregator aggregator = (Aggregator)client;
                aggregator.startDistributedDecomposition();
            }
            else if(flag==2){
                Aggregator aggregator = (Aggregator)client;
                aggregator.startBackwardSubstition();
            }
            else if(flag==3){
                SmartMeter smartMeter = (SmartMeter)client;
                smartMeter.printAssets();
            }
        }
    }
    
    
    
}
