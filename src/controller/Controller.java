package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.FileWorker;
import model.Main;
import model.Song;
import model.SongLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    FileWorker fileWorker = new FileWorker();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser chooser = new DirectoryChooser();
    @FXML
    SplitPane mainSplitPane;
    @FXML
    Button loadFolderButton, loadFileButton;
    @FXML
    public void onResize(){
        System.out.println("CHECK");
        double divider = 0.3;
        mainSplitPane.setDividerPositions(divider);
    }
    @FXML
    public void loadFolderButtonClick(){
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("C:\\");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(null);
        List<File> files = new ArrayList<>();
        FileWorker.getFilesFromFolder(selectedDirectory,files);
        List<Song> list = SongLoader.getSongList(files);

        list.forEach(System.out::println);
    }

    @FXML
    public void loadFileButtonClick(){
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        List<Song> list = SongLoader.getSongList(files);
        list.forEach(System.out::println);
    }


}
