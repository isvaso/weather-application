package com.isvaso;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeatherWindow extends Application {

    private static final Logger logger = LogManager.getLogger(WeatherWindow.class);
    private final String FXML_LOCATION = "../../sample.fxml";
    private final String WINDOW_NAME = "Weather Application";
    private final int WINDOW_WIDTH = 300;
    private final int WINDOW_HEIGHT = 450;

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Weather window starts");
        Parent root = new FxmlLoader().load(FXML_LOCATION);
        stage.setTitle(WINDOW_NAME);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.setResizable(false);
        stage.show();
        logger.info("Weather window launched");
    }
}
