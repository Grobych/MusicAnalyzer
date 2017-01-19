package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    FileWorker fileWorker = new FileWorker();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser chooser = new DirectoryChooser();
    SongList songList = new SongList();
    @FXML
    SplitPane mainSplitPane;
    @FXML
    Button loadFolderButton, loadFileButton;
    @FXML
    TableColumn tableNameColumn, tableStateColumn;
    @FXML
    TableView songTable;



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
        SongLoader.loadSongList(files);
        songList.show();
        updateTable();
    }

    @FXML
    public void loadFileButtonClick() {
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        SongLoader.loadSongList(files);
        songList.show();
    }


    @FXML
    public void updateTable(){
        songTable.refresh();
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("Name"));
        tableStateColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("Status"));
        songTable.setItems(SongList.getList());
    }

}
