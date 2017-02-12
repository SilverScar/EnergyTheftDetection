/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energy_theft_detection;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author aditya
 */
public class MessageListener extends Thread{
    ServerSocket server;
    int port;
    Client client;
    
    public MessageListener(int port, Client client){
        this.port = port;
        this.client = client;
        try {
            server = new ServerSocket(port);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        if(client.id==-1)
            System.out.println("Server started for Aggregator");
        else
            System.out.println("Server started for Smart Meter"+client.id);
        //System.out.println(server.getInetAddress());
    }

    @Override
    public void run(){
        Socket clientSocket;
        try {
            while((clientSocket = server.accept()) != null){
                //System.out.println("Client connected from : "+clientSocket.getRemoteSocketAddress().toString());
                Scanner IN = new Scanner(clientSocket.getInputStream());
                PrintWriter OUT = new PrintWriter(clientSocket.getOutputStream());
                int id = client.id,n=0;
                String flag = IN.nextLine();
                SmartMeter smartMeter;
                //System.out.println(flag);
                switch (flag) {
                    case "D":
                        //System.out.println("d1");
                        smartMeter = (SmartMeter)client;
                        n = Integer.parseInt(IN.nextLine());
                        String temp = IN.nextLine();
                        String[] aggregator_readings = temp.split(" ");
                        //System.out.println(aggregator_readings.toString());
                        for(int i=0;i<n;i++)
                            System.out.print(aggregator_readings[i]+" ");
                        for(int i=0;i<=id;i++){
                            smartMeter.L[i] = new double[n];
                            smartMeter.U[i] = new double[n];
                        }
                        //System.out.println("d2");
                        for(int i=0;i<id;i++){
                            String data = IN.nextLine();
                            String data_arr[] = data.split(" "); 
                            for(int j=0;j<n;j++)
                                smartMeter.L[i][j] = Double.parseDouble(data_arr[j]);
                        }
                        //System.out.println("d3");
                        for(int i=0;i<id;i++){
                            String data = IN.nextLine();
                            String data_arr[] = data.split(" "); 
                            for(int j=0;j<n;j++)
                                smartMeter.U[i][j] = Double.parseDouble(data_arr[j]);
                        }
                        //System.out.println("d4");
                        smartMeter.U[id][0] = smartMeter.readings[0];
                        for(int i=1;i<n;i++){
                            if(i>id)
                                smartMeter.U[id][i] = 0;
                            else{
                                double sum = 0.0;
                                for(int j=0;j<i;j++)
                                    sum+=(smartMeter.L[j][i]*smartMeter.U[id][j]);
                                smartMeter.U[id][i] = smartMeter.readings[i]-sum;
                            }
                        }
                        if(id==0){
                            smartMeter.Y.add(Double.parseDouble(aggregator_readings[id]));
                            for(int i=0;i<n;i++)
                                smartMeter.L[id][i] = smartMeter.readings[i]/smartMeter.readings[0];
                        }
                        else{
                            String data = IN.nextLine();
                            String[] data_arr = data.split(" ");
                            for(int i=0;i<id;i++){
                                smartMeter.Y.add(Double.parseDouble(data_arr[i]));
                            }
                            for(int i=0;i<n;i++){
                                if(i<id)
                                    smartMeter.L[id][i] = 0.0;
                                else{
                                    double sum = 0.0;
                                    for(int j=0;j<id;j++)
                                        sum+=(smartMeter.L[j][i]*smartMeter.U[id][j]);
                                    smartMeter.L[id][i] = (smartMeter.readings[i]-sum)/smartMeter.U[id][id];
                                }
                            }
                            double sum = 0.0;
                            for(int j=0;j<id;j++)
                                sum+=(smartMeter.L[j][id]*smartMeter.Y.get(j));
                            smartMeter.Y.add(Double.parseDouble(aggregator_readings[id])-sum);
                        }
                        OUT.println("ACK-D");
                        OUT.flush();
                        
                        if(id<n-1){
                            String msg = "D\n"+n+"\n";
                            for(int i=0;i<aggregator_readings.length;i++)
                                msg = msg+aggregator_readings[i]+" ";
                            msg = msg+"\n";
                            for(int i=0;i<=id;i++){
                                for(int j=0;j<n;j++)
                                    msg = msg + smartMeter.L[i][j] + " ";
                                msg = msg + "\n";
                            }
                            for(int i=0;i<=id;i++){
                                for(int j=0;j<n;j++)
                                    msg = msg + smartMeter.U[i][j] + " ";
                                msg = msg + "\n";
                            }
                            for(int i=0;i<=id;i++)
                                msg = msg + smartMeter.Y.get(i) + " ";
                            String ret_val = smartMeter.sendMessage(msg, smartMeter.id+1, smartMeter.ip);
                            System.out.println(ret_val);
                            
                        }
                        else{
                            String msg = "F\n1\n";
                            String ret_val = smartMeter.sendMessage(msg, -1, smartMeter.ip);
                            System.out.println(ret_val);
                            
                        }
                        break;
                    case "B":
                        smartMeter = (SmartMeter)client;
                        n = Integer.parseInt(IN.nextLine());
                        for(int i=n-1;i>=id;i--)
                            smartMeter.S[i] = new double[n];
                        for(int i=n-1;i>id;i--){
                            String data = IN.nextLine();
                            String[] data_arr = data.split(" ");
                            for(int j=0;j<n;j++)
                                smartMeter.S[i][j] = Double.parseDouble(data_arr[j]);
                        }
                        double sum = Double.parseDouble(IN.nextLine());
                        double honesty_coefficient = (smartMeter.Y.get(id)-sum)/smartMeter.U[id][id];
                        System.out.println("\n\nK"+id+": "+honesty_coefficient+"\n");
                        for(int i=0;i<n;i++)
                            smartMeter.S[id][i] = smartMeter.U[id][i]*honesty_coefficient;
                        sum=0;
                        OUT.println("ACK-B");
                        OUT.flush();
                        if(id>0){
                            for(int i=id;i<n;i++)
                                sum+=smartMeter.S[i][id-1];
                            String msg = "B\n"+n+"\n";
                            for(int i=n-1;i>=id;i--){
                                for(int j=0;j<n;j++)
                                    msg = msg+smartMeter.S[i][j]+" ";
                                msg = msg + "\n";
                            }
                            msg = msg+sum;
                            String ret_val = smartMeter.sendMessage(msg, smartMeter.id-1, smartMeter.ip);
                            System.out.println(ret_val);
                        }
                        else{
                            String msg = "F\n2\n";
                            String ret_val = smartMeter.sendMessage(msg, -1, smartMeter.ip);
                            System.out.println(ret_val);
                        }
                        break;
                    case "F":
                        int check = Integer.parseInt(IN.nextLine());
                        if(check==1)
                            System.out.println("Distributed Decomposition Finished!");
                        else
                            System.out.println("Backward Substitution Finished!");
                        OUT.println("ACK-F");
                        OUT.flush();
                        break;
                }
                clientSocket.close();
            }
        } catch (Exception ex) {
            System.out.println(ex+" @MessageListener");
        }
    }
}
