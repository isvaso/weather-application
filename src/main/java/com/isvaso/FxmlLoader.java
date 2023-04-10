package com.isvaso;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * A utility class for loading FXML files.
 */
public class FxmlLoader {

    private static final Logger logger = LogManager.getLogger(FxmlLoader.class);

    /**
     * Loads an FXML file from the specified location.
     *
     * @param location the location of the FXML file
     * @return the root node of the loaded FXML file
     * @throws IOException if an I/O error occurs while loading the FXML file
     */
    public Parent load(String location) {
        logger.debug("Loading FXML from {}", location);
        Parent parent = null;

        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource(location)));
            logger.debug("FXML from {} was loaded", location);
        } catch (Exception e) {
            logger.error("FXML from {} wasn't loaded", location);
            logger.error(e);
            System.exit(0);
        }
        return parent;
    }
}
