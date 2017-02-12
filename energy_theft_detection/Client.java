/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energy_theft_detection;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author aditya
 */
public class Client {
    int id;
    int n;
    double[] readings;
    final int PORT = 8660;
    final String ip = "0.0.0.0";
    
    public Client(int id, int n, double[] readings){
        this.id = id;
        this.n = n;
        this.readings = readings;        
    }
    
    public String sendMessage(String msg, int node, String ip){
        String ret_val = "";
        try{
            try (Socket socket = new Socket(ip,node+PORT)) {
                PrintWriter OUT = new PrintWriter(socket.getOutputStream());
                Scanner IN = new Scanner(socket.getInputStream());
                OUT.println(msg+"\n"+socket.getInetAddress().getHostAddress());
                OUT.flush();
                ret_val = IN.nextLine();
                while(IN.hasNextLine())
                    ret_val = ret_val+"\n"+IN.nextLine();
                socket.close();
            }
        }
        catch(Exception ex){
            System.out.println(ex+" @Client");
        }   
        return ret_val;
    }
}

class Aggregator extends Client{

    public Aggregator(int id, int n, double[] readings) {
        super(id, n, readings);
    }
    
    void startDistributedDecomposition(){
        String msg = "D\n"+this.n+"\n";
        for(int i=0;i<n;i++)
            msg = msg + readings[i] + " ";
        String ret_val = sendMessage(msg, 0, this.ip);
        System.out.println("Detection: "+ret_val);
    }
    
    void startBackwardSubstition(){
        String msg = "B\n"+this.n+"\n0";
        String ret_val = sendMessage(msg, this.n-1, this.ip);
        System.out.println("Detection: "+ret_val);
    }
    
}
