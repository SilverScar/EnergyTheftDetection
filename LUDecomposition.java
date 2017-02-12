/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ludecomposition;

/**
 *
 * @author aditya
 */
import java.util.Scanner;
public class LUDecomposition {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double [][]mat = new double[n][n];
        double []p = new double[n];
        int i,j,k;
        for(i=0; i<n; i++){
            for(j=0; j<n; j++)
                mat[i][j] = sc.nextDouble();
            p[i] = sc.nextDouble();
        }
        double [][]l = new double[n][n];
        for(i=0;i<n;i++)
            l[i][i] = 1;

        for(i=0;i<n-1;i++){
            for(j=i+1;j<n;j++)
                l[i][j] = 0;
        }

        double [][]u = new double[n][n];

        for(i=1;i<n;i++){
            for(j=0;j<i;j++)
                u[i][j] = 0;
        }

        for(i=0;i<n;i++)
            u[0][i] = mat[0][i];

        for(i=1;i<n;i++){
            for(j=0;j<i;j++){
                if(j==0)
                    l[i][j] = mat[i][j]/u[0][0];
                else{
                    double val = mat[i][j];
                    for(k=0;k<j;k++)
                        val-=(l[i][k]*u[k][j]);
                    val/=u[j][j];
                    l[i][j] = val;
                }
            }

            for(j=i;j<n;j++){
                double val = mat[i][j];
                for(k=0;k<i;k++)
                    val-=(l[i][k]*u[k][j]);
                u[i][j] = val;
            }
        }

        System.out.println("The L Component is:");
        for(i=0; i<n; i++)
        {
            for(j=0; j<n; j++)
                System.out.print(" "+l[i][j]);
            System.out.println();
        }
        System.out.println("The U Component is:");
        for(i=0; i<n; i++)
        {
            for(j=0; j<n; j++)
                System.out.print(" "+u[i][j]);
            System.out.println();
        }
        
        double[] y = new double[n];
        double[] x = new double[n];
        
        computeY(n,l,p,y);
        computeX(n,u,y,x);
        
        for(i=0;i<n;i++){
            System.out.println("x"+(i+1)+" = "+x[i]);
        }
        sc.close();
    }
    
    public static void computeY(int n, double[][] l, double[] p, double[] y){
        System.out.println("The Y Component is:");
        int i,j;
        y[0] = p[0];
        for(i=1;i<n;i++){
            double val = p[i];
            for(j=0;j<i;j++){
                val -= l[i][j]*y[j];
            }
            y[i] = val;
            System.out.println(y[i]);
        }
    }
    
    public static void computeX(int n, double[][] u, double[] y, double[] x){
        int i,j;
        x[n-1] = y[n-1]/u[n-1][n-1];
        for(i=n-2;i>=0;i--){
            double val = y[i];
            for(j=n-1;j>i;j--){
                val -= u[i][j]*x[j];
            }
            x[i] = val/u[i][i];
        }
    }
    
}
