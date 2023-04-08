package com.isvaso;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class that launches the weather application.
 */
public class AppLauncher {

    private static final Logger logger = LogManager.getLogger(AppLauncher.class);

    /**
     * The main method that launches the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("Application started");
        Application.launch(WeatherWindow.class, args);
        logger.info("Application stopped");
    }
}
