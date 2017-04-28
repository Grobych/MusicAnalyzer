package controller;

import model.util.Constants;
import data.Song;
import data.SongList;
import data.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.connect.Connection;
import model.util.FileWorker;
import model.util.Log;
import org.apache.commons.math3.complex.Complex;
import model.threads.SongAnalyzeThread;
import model.threads.SongLoaderThread;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    LineChart <String, Double> ACFChart, RMSChart;
    @FXML
    Label RMSLabel,MaxDeltaRMSLabel, AverageDeltaRMSLabel, toneLabel, tempLabel;

    XYChart.Series RMSseries = new XYChart.Series();
    XYChart.Series RMSRseries = new XYChart.Series();
    XYChart.Series AFCSeries = new XYChart.Series();

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

    public void showAlert(String alertMessage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    @FXML
    public void resultButtonClick(){
        for (Song song : SongList.getList()) {
            if (song.getStatus()!= Status.SUCCESS) {
                showAlert("Not all songs has been analyzed!");
                return;
            }
        }
        Connection connection = Connection.getInstance();
        connection.send("TEST");
        String answer = connection.receive();
        System.out.println("received: "+answer);
        if (answer.compareTo("SUCCESS")==0) {
            double prefRhythm = Double.parseDouble(connection.receive());
            double prefEmotional = Double.parseDouble(connection.receive());
            System.out.println(prefRhythm+" "+prefEmotional);
            answer = connection.receive();
            List<String> recommendationList = new ArrayList<>();
            if (answer.compareTo("SIMILAR")==0) {
                while (true){
                    answer = connection.receive();
                    System.out.println(answer);
                    if (answer.compareTo("END")!=0){
                        recommendationList.add(answer);
                    }
                    else break;
                }

            }
            Stage resultDialog = new Stage();
            resultDialog.setWidth(300);
            resultDialog.setHeight(400);

            VBox box = new VBox();
            String rhythmResult = new String("Preferred rhythm: "+String.valueOf(prefRhythm));
            String emotionalResult = new String("Preferred emotional: "+String.valueOf(prefEmotional));

            box.getChildren().add(new Label(rhythmResult));
            box.getChildren().add(new Label(emotionalResult));
            box.getChildren().add(new Label(""));
            box.setAlignment(Pos.CENTER);
            if (recommendationList.size()!=0){
                for (String s : recommendationList) {
                    box.getChildren().add(new Label(s));
                }
            } else {
                box.getChildren().add(new Label("No similar song :("));
            }

            Scene scene = new Scene(box);
            resultDialog.setScene(scene);
            resultDialog.show();
            return;
        }

            //connection.closeConnection();
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
        if (song==null) return;
        if (song.getRMS()!=null){
            RMSLabel.setText(song.getRMSString());
            MaxDeltaRMSLabel.setText(song.getMaxDeltaRMSString());
            AverageDeltaRMSLabel.setText(song.getAverageDeltaRMSString());
            ObservableList points = getChartPoints(song.getRMS().getRMSlist());
            if (RMSseries!=null)RMSseries.getData().removeAll();
            RMSseries.setData(points);
        }
        if (song.getAFC()!=null){
            ObservableList points = getChartPoints(song.getAFC());
            if (AFCSeries!=null) AFCSeries.getData().removeAll();
            AFCSeries.setData(points);
        }
        if (song.getTone()!=null){
            toneLabel.setText(song.getTone());
        }
        if (song.getBpm()!=0){
            tempLabel.setText(String.valueOf(song.getBpm()));
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Log.addMessage("Running program...");
        inicialisationTable();
        RMSChart.getData().add(RMSseries);
        RMSChart.getData().add(RMSRseries);
        ACFChart.getData().add(AFCSeries);
    }

    @Override
    public void close() throws IOException {
        Log.close();
        Connection.getInstance().closeConnection();
    }

    @FXML
    public void runButtonClick() {
        FileWorker.deleteFilesFromFolder(new File(Constants.tempFolderServer));
        SongAnalyzeThread analyzer = new SongAnalyzeThread();
        Thread thread = new Thread(analyzer);
        thread.start();
//        SongAnalyzeThread analyzer2 = new SongAnalyzeThread();
//        Thread thread2 = new Thread(analyzer2);
//        thread2.start();
    }

    private ObservableList<XYChart.Data> getChartPoints(double [] cx) {

        ObservableList<XYChart.Data> points = FXCollections.observableArrayList();

        for (int i = 0; i < cx.length; i++) {
            points.add(createPoint(i, cx[i], false));
        }

        return points;
    }
    private ObservableList<XYChart.Data> getChartPoints(ArrayList<Complex> cx) {

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
