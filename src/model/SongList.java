package model;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alex on 14.01.2017.
 */
public class SongList {
    private static ObservableList<Song> list = FXCollections.observableArrayList();

    public static void add(Song song){
        if (!list.contains(song)) list.add(song);
    }


    public static ObservableList<Song> getList(){
        return list;
    }

    public void show(){
        System.out.println("Song list:");
        System.out.println();
        list.forEach(System.out::println);
        System.out.println("Total: "+list.size());
    }
}
