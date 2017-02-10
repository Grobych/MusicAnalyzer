package model;

import java.sql.Time;

import static model.Constants.AFClenght;

/**
 * Created by Alex on 12.01.2017.
 */
public class Song {
    private String name;
    private String artist;
    private String album;
    private Time lenght;
    private double [] AFC = new double[AFClenght];    // АЧХ
    private int [] wave;                       // волновое представление
    private double RMS;
    private Tone tone;

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

    public Time getLenght() {
        return lenght;
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
        if (AFC.length!=Constants.AFClenght)
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

    public void setLenght(Time lenght) {
        this.lenght = lenght;
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
