package controller;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

public class Controller {
    @FXML
    SplitPane mainSplitPane;

    @FXML
    public void onResize()
    {
        System.out.println("CHECK");
        double divider = 0.3;
        mainSplitPane.setDividerPositions(divider);
    }
}
