package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage; // initialize value of stage.
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainFrame.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Music Analyzer");
        stage.setMaximized(true);
        stage.show();
        //stage.setResizable(false);
    }



//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("../view/MainFrame.fxml"));
//        primaryStage.setTitle("Music Analyzer");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setMaximized(true);
//        primaryStage.show();
//    }


    public static void main(String[] args) {
        launch(args);
    }
}
