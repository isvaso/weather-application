package com.isvaso;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppLauncher {

    private static final Logger logger = LogManager.getLogger(AppLauncher.class);

    public static void main(String[] args) {
        logger.info("Application started");
        Application.launch(WeatherWindow.class, args);
        logger.info("Application stopped");
    }
}
