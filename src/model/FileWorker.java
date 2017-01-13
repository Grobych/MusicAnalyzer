package model;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 12.01.2017.
 */
public class FileWorker {
    private FileChooser fileChooser = new FileChooser();

    public List<Song> getSongs(Stage scene){
        List<Song> list = new ArrayList<Song>();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
         List<File> files = fileChooser.showOpenMultipleDialog(scene);
        for (File tmp:
             files) {
            System.out.println(tmp.getName());
        }

        return list;
    }
}
