package data;

import model.Constants;
import model.Status;
import model.Tone;

import java.sql.Time;

import static model.Constants.AFClength;

/**
 * Created by Alex on 12.01.2017.
 */
public class Song {
    private String name;
    private String artist;
    private String album;
    private Time length;
    private double [] AFC = new double[AFClength];    // АЧХ
    private int [] wave;                       // волновое представление
    private double RMS;
    private Tone tone;
    private String fullName;

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

    public double getRMS() {
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

    public Tone getTone() {
        return tone;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAFC(double[] AFC) {
        if (AFC.length!= Constants.AFClength)
        {
            // SIZE ERROR!
            //
            // !!!!!!!!!!
        }
        else
        {
            this.AFC = AFC;
        }
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

    public void setRMS(double RMS) {
        this.RMS = RMS;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }

    public void setWave(int[] wave) {
        this.wave = wave;
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


}
