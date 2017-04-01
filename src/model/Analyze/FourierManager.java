package model.Analyze;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

import static data.Constants.AFClength;


/**
 * Created by Alex on 28.03.2016.
 */
public class FourierManager {
    private final static FourierManager INSTANCE = new FourierManager();
    public static FourierManager getInstance(){
        return INSTANCE;
    }

    private FourierManager(){

    }


    // x = x * (0.54-0.46*cos(2*PI*i/N));
    public static double[] HammingWindow(double[] array){
        double [] result = new double[array.length];
        for (int i=0;i<array.length; i++){
            result[i] = array[i] * (0.54-0.46*Math.cos(2*Math.PI*i/AFClength));
        }
        return result;
    }
    // x = x * (0.54-0.46*cos(2*PI*i/N));
    public static ArrayList<Complex> HammingWindow(ArrayList<Complex> array){
        ArrayList<Complex> result = new ArrayList<>();
        for (int i=0;i<array.size(); i++){
            double ampl = array.get(i).getReal();
            ampl = ampl * (0.54-0.46*Math.cos(2*Math.PI*i/AFClength));
            result.add(new Complex(ampl,0));
        }
        return result;
    }

    public static ArrayList<Complex> timeDecimationFFT(ArrayList<Complex> list, int dir){
        if(list.size() == 1)
            return list;
        //
        ArrayList<Complex> odd = new ArrayList<Complex>(list.size() / 2);
        ArrayList<Complex> even = new ArrayList<Complex>(list.size() / 2);
        for(int i = 0; i < list.size(); i += 2){
            odd.add(list.get(i + 1));
            even.add(list.get(i));
        }
        //
        odd = timeDecimationFFT(odd, dir);
        even = timeDecimationFFT(even, dir);
        //
        Complex wn = new Complex(1, 0);
        double arg = 2 * Math.PI * dir / list.size();
        Complex w = new Complex(Math.cos(arg), Math.sin(arg));
        //
        for(int i = 0; i < list.size() / 2; i++){
            list.set(i, even.get(i).add( odd.get(i).multiply(wn)));
            list.set(i + list.size() / 2, even.get(i).subtract( odd.get(i).multiply(wn)));
            wn = wn.multiply(w);
        }
        return list;
        //
    }

    public static void showList(ArrayList<Complex> list)
    {
        for (int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i).getReal()+"  "+list.get(i).getImaginary());
        }
    }



    public static ArrayList<Complex> DFT (double [] digitalValues) {

        ArrayList<Complex> list = new ArrayList(digitalValues.length);

        for (int m = 0; m < AFClength; m++) {

            Complex cm = new Complex(0, 0);
            double t = (2 * Math.PI * m) / AFClength;

            for (int n = 0; n < digitalValues.length; n++) {

                double value = digitalValues[n];

                double r = Math.cos(t*n);
                double im = Math.sin(t*n);

                Complex c = new Complex(r, -im).multiply(value);
                cm = cm.add(c);
            }
            list.add(cm);
        }

        return list;
    }


}
