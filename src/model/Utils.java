package model;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 28.03.2017.
 */
public class Utils {
    public static Complex[] shortToComplex(short[] array){
        Complex complex[] = new Complex[array.length];
        for (int i=0; i<array.length; i++) {
            Complex number = new Complex(array[i],0.0);
            complex[i] = number;
        }
        return complex;
    }
    public static double[] shortToDouble(short[] array){
        double result[] = new double[array.length];
        for (int i=0; i<array.length; i++) {
            result[i] = (double) array[i];
        }
        return result;
    }
    public static double[] shortToDouble(short[] array, int n){
        double result[] = new double[n];
        for (int i=0; i<n; i++) {
            result[i] = (double) array[i];
        }
        return result;
    }
    public static double[] listToDouble(List list, int n){
        double result[] = new double[n];
        for (int i=0; i<n; i++) {
            result[i] = (double) list.get(i);
        }
        return result;
    }

    public static ArrayList<Complex> digitalToComplex(double [] list){
        ArrayList<Complex> complexList = new ArrayList<>(list.length);
        for(int i=0;i<list.length;i++)
        {
            Complex temp = new Complex(list[i],0);
            complexList.add(temp);
        }
        return complexList;
    }
    public static ArrayList<Complex> digitalToComplex(short [] list, int n){
        ArrayList<Complex> complexList = new ArrayList<>();
        for(int i=0;i<n;i++)
        {
            System.out.print("Short: " + list[i] + " ");
            double amp = list[i]/Math.pow(2,15);
            System.out.print("Div: " + amp + " ");
            Complex temp = new Complex(amp,0);
            System.out.println("Complex: " + temp);
            complexList.add(temp);
        }
        return complexList;
    }


}
