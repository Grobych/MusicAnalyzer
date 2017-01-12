package model;

import java.sql.Time;

import static model.Constants.AFClenght;

/**
 * Created by Alex on 12.01.2017.
 */
public class Song {
    private String name;
    private String artist;
    private Time lenght;
    private double [] AFC = new double[AFClenght];    // АЧХ
    private int [] wave;                       // волновое представление
    private double RMS;
    private Tone tone;

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
}
