package data;


import model.analyze.rms.RMSAnalyzer;

import java.sql.Time;
import java.text.DecimalFormat;

/**
 * Created by Alex on 12.01.2017.
 */
public class Song {

    private String name;
    private String artist;
    private String album;
    private Time length;
    private double [] AFC = null;    // АЧХ
    private int [] wave;                       // волновое представление
    private Dynamic RMS;
    private String tone;
    private String fullName;
    private String path;
    private int bpm;

    public String getFullName() {
        if (fullName==null){
            fullName = artist.concat(" - "+name);
        }
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private Status status = Status.WAITING;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getArtist() {
        return artist;
    }

    public Dynamic getRMS() {
        return RMS;
    }

    public String getName() {
        return name;
    }

    public Time getLength() {
        return length;
    }

    public double[] getAFC() {
        return AFC;
    }

    public int[] getWave() {
        return wave;
    }

    public String getTone() {
        return tone;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public int getBpm() {
        return bpm;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAFC(double[] AFC) {
        this.AFC = AFC;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setLength(Time length) {
        this.length = length;
    }

    public void setRMS(Dynamic RMS) {
        this.RMS = RMS;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public void setWave(int[] wave) {
        this.wave = wave;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString()
    {
        String result = new String();
        if (name==null) result = result.concat("\nName is not set!\n");
        else result = result.concat("\nName: "+this.name);
        if (name==null) result = result.concat("\nAuthor is not set!\n");
        else result =  result.concat("\nAuthor: "+this.artist);
        if (name==null) result = result.concat("\nAlbum is not set!\n");
        else result = result.concat("\nAlbum: "+this.album);
        result = result.concat("\n");
        return result;

    }

    public String getRMSString(){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getRMS()));
        format =format.concat("dB");
        return new String(format);
    }

    public String getMaxDeltaRMSString(){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getMaxDeltaRMS()));
        format = format.concat("dB");
        return new String(format);
    }

    public String getAverageDeltaRMSString(){
        String pattern = "##0.0000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(RMSAnalyzer.getAsDB(RMS.getAverageDeltaRMS()));
        format = format.concat("dB");
        return new String(format);
    }

}
