package controller;

import data.Song;
import data.SongList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.FileWorker;
import model.Log;
import org.apache.commons.math3.complex.Complex;
import threads.SongAnalyzeThread;
import threads.SongLoaderThread;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static jouvieje.bass.Bass.BASS_Free;

public class Controller implements Initializable, Closeable {
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
    TableView<Song> songTable;
    @FXML
    LineChart <Integer, Float> ACPChart;
    @FXML
    Label RMSLabel,MaxDeltaRMSLabel, AverageDeltaRMSLabel;


    @FXML
    public void onResize(){
        System.out.println("CHECK");
        double divider = 0.3;
        mainSplitPane.setDividerPositions(divider);
    }
    @FXML
    public void loadFolderButtonClick(){
        chooser.setTitle("Choose Folder");
        File defaultDirectory = new File("C:\\");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory==null) return;
        List<File> files = new ArrayList<>();
        FileWorker.getFilesFromFolder(selectedDirectory,files);
        SongLoaderThread slt = new SongLoaderThread(files);
        Thread loadThread = new Thread(slt);
        loadThread.start();
    }

    @FXML
    public void loadFileButtonClick() {
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files==null) return;
        SongLoaderThread slt = new SongLoaderThread(files);
        Thread loadThread = new Thread(slt);
        loadThread.start();
    }


    @FXML
    public void inicialisationTable(){
        songTable.refresh();
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("FullName"));
        tableStateColumn.setCellValueFactory(new PropertyValueFactory<Song,Integer>("Status"));
        songTable.setItems(SongList.getList());
    }
    @FXML
    public void showSongInfo(){
        Song song = songTable.getSelectionModel().getSelectedItem();
        RMSLabel.setText(song.getRMSString());
        MaxDeltaRMSLabel.setText(song.getMaxDeltaRMSString());
        AverageDeltaRMSLabel.setText(song.getAverageDeltaRMSString());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Log.addMessage("Running program...");
        inicialisationTable();
    }

    @Override
    public void close() throws IOException {
        Log.close();
        BASS_Free(); // Переписать
    }

    @FXML
    public void runButtonClick() {
        SongAnalyzeThread analyzer = new SongAnalyzeThread();
        Thread thread = new Thread(analyzer);
        thread.start();
    }

    private ObservableList<XYChart.Data> freqChartPoints(ArrayList<Complex> cx) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < cx.size(); i++) {
            Complex c = cx.get(i);
            points.add(createPoint(i, c.abs(), false));
        }

        return points;
    }
    private XYChart.Data createPoint(double x, double y, boolean isVisible) {

        XYChart.Data<Object, Object> point = new XYChart.Data<Object, Object>(x,y);

        Rectangle rect = new Rectangle(4, 4);
        rect.setVisible(isVisible);
        point.setNode(rect);

        return point;
    }
}
