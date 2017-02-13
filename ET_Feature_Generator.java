/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package et_feature_generator;

/**
 *
 * @author aditya
 */
import java.util.*;
public class ET_Feature_Generator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        int t = stdIn.nextInt();
        System.out.println("@RELATION Silver\n\n@ATTRIBUTE FO NUMERIC\n@ATTRIBUTE F1 NUMERIC\n@ATTRIBUTE F2 NUMERIC\n@ATTRIBUTE class {accept,reject}\n\n@DATA");
        Random rand = new Random();
        while(t>0){
            int flag = rand.nextInt(2);
            int n = rand.nextInt(900)+20;
            double prob = 0.1 + 0.5*rand.nextDouble();
            if(flag==0)
                generateRejections(n,prob);
            else
                generateAcceptance(n,prob);
            t--;
        }
    }
    
    public static void generateAcceptance(int n, double prob){
        int dishonest_meters = 0;
        Random rand = new Random();
        for(int i=0;i<n;i++){
            double toss = rand.nextDouble();
            if(toss < prob)
                dishonest_meters++;
        }
        System.out.println(n+","+prob+","+dishonest_meters+",accept");
    }
    
    public static void generateRejections(int n, double prob){
        int dishonest_meters = 0;
        Random rand = new Random();
        SmartMeter[] smartMeters = new SmartMeter[n];
        for(int i=0;i<n;i++){
            double[] meter_readings = new double[n];
            for(int j=0;j<n;j++)
                meter_readings[j] = rand.nextInt(40)+1;
            smartMeters[i] = new SmartMeter(meter_readings,n,i,prob);
        }
        
        double[] aggregator_readings = new double[n];
        double[] honesty_coefficients = new double[n];
        for(int i=0;i<n;i++){
            aggregator_readings[i] = 0.0;
            for(int j=0;j<n;j++){
                if(smartMeters[j].getTheftRate() >= 1.1)
                    dishonest_meters++;
                aggregator_readings[i]+=(smartMeters[j].meter_readings[i]);
            }
        }
        tamperValues(n, smartMeters, aggregator_readings, dishonest_meters);
        distributedDecomposition(smartMeters, 0, n, null, null, null, aggregator_readings);
        backwardSubstitution(smartMeters, n-1, n, 0, null, honesty_coefficients);
        dishonest_meters=0;
        for(int i=0;i<n;i++){
            if(honesty_coefficients[i]>0.5 && honesty_coefficients[i]<1.5)
                continue;
            dishonest_meters++;
        }
        System.out.println(n+","+prob+","+dishonest_meters+",reject");
    }
    
    public static void tamperValues(int n, SmartMeter[] smartMeters, double[] aggregator_readings, int dishonest_meters){
        Random rand = new Random();
        for(int i=0;i<dishonest_meters;i++){
            int uid = rand.nextInt(n-1);
            int span = rand.nextInt(n)+n/10;
            int st_ind = rand.nextInt(n-1);
            double theft_rate = 1.1 + 8.9*rand.nextDouble();
            for(int j=st_ind;j<n && j<st_ind+span-1;j++)
                aggregator_readings[j]+=smartMeters[uid].meter_readings[j]*(theft_rate-1);
        }
    }
    
    public static void distributedDecomposition(SmartMeter[] smartMeters, int id, int n, double[][] l, double[][] u, ArrayList<Double> y, double[] aggregator_readings){
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
    
    public static void backwardSubstitution(SmartMeter[] smartMeters, int id, int n, double sum, double[][] s, double[] honesty_coefficients){
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

class SmartMeter {
    int id;
    double[] meter_readings;
    double[][] L;
    double[][] U;
    double[][] S;
    ArrayList<Double> Y;
    final double theft_probability;
    private double theft_rate;
    final double theft_rate_low = 1.1;
    final double theft_rate_high = 10;
    
    public SmartMeter(double[] meter_readings, int n, int id, double theft_probability){
        this.id = id;
        this.meter_readings = meter_readings;
        this.L = new double[n][];
        this.U = new double[n][];
        this.Y = new ArrayList<>();
        this.S = new double[n][];
        this.theft_probability = theft_probability;
        Random r = new Random();
        double probability = r.nextDouble();
        if(probability <= this.theft_probability)
            theft_rate = this.theft_rate_low + (this.theft_rate_high-this.theft_rate_low)*r.nextDouble();
        else{
            theft_rate = 1.0;
        }
    }
    
    public double getTheftRate(){
        return this.theft_rate;
    }
}
