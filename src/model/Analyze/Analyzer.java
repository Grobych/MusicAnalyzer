package model.Analyze;

import data.Dynamic;
import data.Song;
import data.Status;
import javazoom.jl.decoder.*;
import jouvieje.bass.Bass;
import jouvieje.bass.BassInit;
import jouvieje.bass.exceptions.BassException;
import jouvieje.bass.structures.HSTREAM;
import jouvieje.bass.utils.BufferUtils;
import model.Log;
import model.Utils;
import org.apache.commons.math3.complex.Complex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static data.Constants.AFClength;
import static data.Constants.mp3FrameLen;
import static jouvieje.bass.Bass.*;
import static jouvieje.bass.defines.BASS_DATA.BASS_DATA_FFT1024;
import static jouvieje.bass.defines.BASS_POS.BASS_POS_BYTE;
import static jouvieje.bass.defines.BASS_STREAM.BASS_STREAM_DECODE;
import static sun.management.Agent.error;

/**
 * Created by Alex on 17.02.2017.
 */
public class Analyzer {
    boolean init = false;

    private double buffer[] = null;
    private int size;

    public Song analyzeSong(Song song){
        System.out.println(song.getPath());
        song.setStatus(Status.IN_PROCESS);
        try {
            parseMP3File(song.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BitstreamException e) {
            e.printStackTrace();
        }
        Dynamic dynamic = RMSAnalyzer.getDynamic(buffer,mp3FrameLen);
        song.setRMS(dynamic);
        song.setStatus(Status.SUCCESS);

        buffer = null;
        return song;
    }


    public void parseMP3File(String filename) throws FileNotFoundException, BitstreamException {
        Bitstream bitStream;
        bitStream = new Bitstream(new FileInputStream(filename));
        Decoder decoder = new Decoder();
        SampleBuffer samples = null;
        double result[] = new double[75000000];
        int i =0;
        double sampleBuffer[] = new double[mp3FrameLen];
        try {

            while (true) {
                Header header = bitStream.readFrame();
                if (header == null){
                    System.out.println("Empty header!");
                    break;
                }
                samples = (SampleBuffer) decoder.decodeFrame(header, bitStream); //returns the next 2304 samples
                sampleBuffer = Utils.shortToDouble(samples.getBuffer());
                //System.out.println("RMS frame "+getRMS(array));
                int offset = i*mp3FrameLen;
                for (int pos = 0; pos < mp3FrameLen; pos++){
                    result[offset+pos] = sampleBuffer[pos];
                }
                bitStream.closeFrame();
                samples.clear_buffer();
                i++;
            }

            size = i*mp3FrameLen;
            samples.close();
            System.out.println(size);
            buffer = Arrays.copyOfRange(result,0,size);
            result = null;
        } catch (BitstreamException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }finally {
            bitStream.close();
        }
    }


    private static void load()
    {
        String fileNatives = OperatingSystem.getOSforLWJGLNatives();
        System.setProperty("org.lwjgl.librarypath", System.getProperty("/lib/jars") + File.separator + fileNatives);
    }

    public void initialization(){
        Log.addMessage("Bass Initialisation");

        checkLibrary();
        if(!init) {
            return;
        }

		/* Initialize default output device */
        if(!BASS_Init(-1, 44100, 0, null, null)) {
            Log.addMessage("Can't initialize device");
            error("Can't initialize device");
            return;
        }

        Log.addMessage("Bass Initialisation success! ");
    }

    private void checkLibrary() {
        try {
            final String nativesPath = "lib/NativeBass-1.1.2/lib/win64/";

            System.setProperty("org.lwjgl.librarypath", nativesPath);
            System.setProperty("java.library.path", nativesPath);
            BassInit.loadLibraries();
        } catch(BassException e) {
            System.out.print("NativeBass error!\n" + e.getMessage());
            return;
        }

        if(BassInit.NATIVEBASS_LIBRARY_VERSION() != BassInit.NATIVEBASS_JAR_VERSION()) {
            Log.addMessage("Error!  NativeBass library version (%08x) is different to jar version (%08x)\n" +
                    BassInit.NATIVEBASS_LIBRARY_VERSION() + BassInit.NATIVEBASS_JAR_VERSION());
            System.out.print("Error!  NativeBass library version (%08x) is different to jar version (%08x)\n" +
                    BassInit.NATIVEBASS_LIBRARY_VERSION() + BassInit.NATIVEBASS_JAR_VERSION());
            return;
        }

		/*==================================================*/

        init = true;
    }

    public float[] getACP(){
        ByteBuffer buffer = BufferUtils.newByteBuffer(4096);
        float result[] = new float[512];
        try {
            HSTREAM stream = Bass.BASS_StreamCreateFile(false,"C:\\test.mp3",0,0,BASS_STREAM_DECODE);
            if (stream.asInt() == 0){
                System.out.println("Stream error");
            }

            int step = 512*1024;
            int duration = (int)BASS_ChannelGetLength(stream.asInt(), BASS_POS_BYTE)/step;
            System.out.println("Duration: "+duration);

            for(int i = 0; i < duration; i++){
                BASS_ChannelGetData(stream.asInt(), buffer, BASS_DATA_FFT1024);

                for(int j = 0; j < 512; j++){
                    //System.out.print((String.format("%(.2f ", buffer.asFloatBuffer().get(j)*1000)));
                    result[j]+=buffer.asFloatBuffer().get(j);
                }
                buffer.clear();
//                System.out.println();
                BASS_ChannelSetPosition(stream.asInt(), step * i, BASS_POS_BYTE);
            }
            for (int i=0; i < 512; i++){
                result[i]/=duration;
            }
//            float min = 1000;
//            for(int i = 0; i < duration; i++){
//                if(array[i] < min && array[i] != 0.0f) min = array[i];
//            }
//            for(int i = 0; i < duration; i++){
//                if(array[i] != 0.0f){
//                    array[i] -= min * 0.8;
//                }
//            }


//            BASS_StreamFree(stream);

            BASS_StreamFree(stream);




        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }


    public ArrayList<Complex> getACPSPI(){
        short temp[] = new short[2304];
        ArrayList<Complex> spectrogramm = new ArrayList<>();
        ArrayList tempList = new ArrayList<>();
        for (int i=0;i<AFClength / 2; i++){
            spectrogramm.add(new Complex(0,0));
        }
        Bitstream bitStream = null;
        try {
            bitStream = new Bitstream(new FileInputStream("C:\\test2.mp3"));
            Decoder decoder = new Decoder();
            int j = 0;
            while(true){
                Header header = bitStream.readFrame();
                if (header==null) break;
                SampleBuffer samples = (SampleBuffer)decoder.decodeFrame(header, bitStream); //returns the next 2304 samples

                //tempList.addAll(Utils.shortToDouble(temp));

                if(j%5000==0){
                    //spectrogramm.clear();
                    temp = samples.getBuffer();
                    ArrayList<Complex> list = Utils.digitalToComplex(temp,AFClength);
                    list = FourierManager.HammingWindow(list);
                    list = FourierManager.timeDecimationFFT(list,1);
                    for (int i = 0; i< AFClength / 2; i++) {
                        //Complex note = (list.get(i).add(spectrogramm.get(i).getArgument()).divide(2));
//                        System.out.println(list.get(i) + " ");
                        spectrogramm.set(i,list.get(i));
                    }
                    System.out.println("\nSize spec "+ spectrogramm.size() + "Size temp "+ temp.length + "Size list " + list.size());

                    System.out.println("CHECK "+j);

                }
//                if(j%10==0){
//                    temp = samples.getBuffer();
//                }
                bitStream.closeFrame();
                j++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BitstreamException e) {

            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }


        return spectrogramm;
    }

}

//
//    public Song analyzeSong(Song song){
//        System.out.println(song.getPath());
//        song.setStatus(Status.IN_PROCESS);
//        try {
//            parseMP3File(song.getPath());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (BitstreamException e) {
//            e.printStackTrace();
//        }
//        Dynamic dynamic = RMSAnalyzer.getDynamic(buffer,mp3FrameLen);
//        song.setRMS(dynamic);
//        System.out.println(RMSAnalyzer.getAsDB(song.getRMS().getRMS()));
//        song.setStatus(Status.SUCCESS);
//
//        buffer = null;
//        return song;
//    }
//
//
//    public void parseMP3File(String filename) throws FileNotFoundException, BitstreamException {
//        List<Double> list = new ArrayList<>();
//        Bitstream bitStream;
//        bitStream = new Bitstream(new FileInputStream(filename));
//        Decoder decoder = new Decoder();
//        SampleBuffer samples = null;
//        double sampleBuffer[] = new double[mp3FrameLen];
//        try {
//
//            while (true) {
//                Header header = bitStream.readFrame();
//                if (header == null){
//                    System.out.println("Empty header! "+list.size());
//                    break;
//                }
//                samples = (SampleBuffer) decoder.decodeFrame(header, bitStream); //returns the next 2304 samples
//                sampleBuffer = Utils.shortToDouble(samples.getBuffer());
//                //System.out.println("RMS frame "+getRMS(array));
//                for (int i=0;i<sampleBuffer.length; i++){
//                    list.add(sampleBuffer[i]);
//                }
//                bitStream.closeFrame();
//                samples.clear_buffer();
//            }
//            buffer = list.stream().mapToDouble(Double::doubleValue).toArray();
//            list.stream().close();
//            list.clear();
//
//            samples.close();
//            System.out.println(buffer.length);
//
//        } catch (BitstreamException e) {
//            e.printStackTrace();
//        } catch (DecoderException e) {
//            e.printStackTrace();
//        }finally {
//            bitStream.close();
//        }
//    }