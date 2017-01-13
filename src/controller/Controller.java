package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.FileChooser;
import model.FileWorker;
import model.Main;

public class Controller {
    FileWorker fileWorker = new FileWorker();
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
        System.out.println("Folder");
    }

    @FXML
    public void loadFileButtonClick(){
        System.out.println("File");
        fileWorker.getSongs(Main.stage);
    }
}
