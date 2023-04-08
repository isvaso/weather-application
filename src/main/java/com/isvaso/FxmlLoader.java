package com.isvaso;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class FxmlLoader {

    private static final Logger logger = LogManager.getLogger(FxmlLoader.class);

    public Parent load(String location) throws IOException {
        logger.debug("Loading FXML from {}", location);

        return FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource(location)));
    }
}
