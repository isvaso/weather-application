package com.isvaso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * This class is a controller for the weather application GUI. It handles user
 * input and updates the GUI with weather information obtained from an external
 * weather API.
 */
public class Controller {

    /**
     * The ResourceBundle containing localizable text for the GUI.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * The location of the FXML file for the GUI.
     */
    @FXML
    private URL location;

    /**
     * The text field where the user enters the name of a city
     * to get weather information for.
     */
    @FXML
    private TextField city;

    /**
     * The button the user clicks to get weather information for the entered city.
     */
    @FXML
    private Button getData;

    /**
     * The text element in the GUI displaying the pressure value.
     */
    @FXML
    private Text pressure;

    /**
     * The text element in the GUI displaying the temperature "feels like" value.
     */
    @FXML
    private Text temp_feels;

    /**
     * The text element in the GUI displaying the current temperature value.
     */
    @FXML
    private Text temp_info;

    /**
     * The text element in the GUI displaying the maximum temperature value.
     */
    @FXML
    private Text temp_max;

    /**
     * The text element in the GUI displaying the minimum temperature value.
     */
    @FXML
    private Text temp_min;

    /**
     * The API key to use for accessing the external weather API.
     */
    private static final String API_KEY = "46f312369fd5bb665d26534997bd7b2d";

    /**
     * The base URL of the external weather API.
     */
    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final Logger logger = LogManager.getLogger(Controller.class);

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    void initialize() {
        logger.info("Start initialize()");
        city.setOnMouseClicked(actionEvent -> {
            city.setText("");
        });
        city.setOnAction(actionEvent -> {
            getDataAction();
        });

        getData.setOnAction(actionEvent -> {
            getDataAction();
        });
    }

    /**
     * Returns the metric sign for a specified parameter,
     * e.g. "°C" for "temperature".
     *
     * @param parameter the name of the parameter to get the metric sign for.
     * @return the metric sign for the specified parameter.
     */
    private String getMetricSign(String parameter) {

        return switch (parameter) {
            case "temperature" -> "°C";
            case "pressure" -> "hPa";
            default -> "";
        };
    }

    /**
     * Sends a GET request to the specified URL and returns the response.
     *
     * @param urlAddress the URL to send the GET request to
     * @return the response received from the server
     * @throws IOException if an error occurs while sending the
     * GET request or reading the response
     */
    private String getUrlContent(String urlAddress) throws IOException {
        logger.debug("Sending GET request to: " + urlAddress);
        URL url = new URL(urlAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String output = response.toString();
            logger.debug("Received output from URL: " + output);
            return response.toString();
        } else {
            logger.error("Error Response Code: " + responseCode);
            throw new IOException("Error Response Code: " + responseCode);
        }
    }

    /**
     * Handles the action of getting the weather data
     * for the user's specified city.
     */
    private void getDataAction() {
        String getUserCity = city.getText().trim();
        logger.info("User input city: " + getUserCity);

        if (!getUserCity.isEmpty()) {
            String urlAddress = API_BASE_URL + "?q=" + getUserCity
                    + "&APPID=" + API_KEY + "&units=metric";
            try {
                JSONObject weatherData = getWeatherData(urlAddress);
                updateUI(weatherData);
            } catch (IOException e) {
                logger.error("Error occurred while sending GET " +
                        "request to URL: " + urlAddress, e);
                city.setText("City not found");
            }
        }
    }

    /**
     * Retrieves weather data from the specified URL and returns
     * it as a JSONObject.
     *
     * @param urlAddress the URL to retrieve weather data from
     * @return the weather data as a JSONObject
     * @throws IOException if an error occurs while sending the
     * GET request or reading the response
     */
    private JSONObject getWeatherData(String urlAddress) throws IOException {
        String output = getUrlContent(urlAddress);

        if (!output.isEmpty()) {
            JSONObject weatherData = new JSONObject(output);
            logger.debug("Received weather data: " + weatherData);
            return weatherData;
        }

        return null;
    }

    /**
     * Gets data from a JSON Object and passes it to the application's GUI
     *
     * @param weatherData JSON Object with the weather data
     */
    private void updateUI(JSONObject weatherData) {
        if (weatherData != null) {
            double temperature = weatherData
                    .getJSONObject("main")
                    .getDouble("temp");
            double feelsLike = weatherData
                    .getJSONObject("main")
                    .getDouble("feels_like");
            double tempMax = weatherData.
                    getJSONObject("main")
                    .getDouble("temp_max");
            double tempMin = weatherData
                    .getJSONObject("main")
                    .getDouble("temp_min");
            double pressureValue = weatherData
                    .getJSONObject("main")
                    .getDouble("pressure");

            logger.debug("Updating UI with temperature: {}°C, " +
                            "feels like: {}°C, " +
                            "max temperature: {}°C, " +
                            "min temperature: {}°C, " +
                            "pressure: {} hPa",
                    temperature, feelsLike, tempMax, tempMin, pressureValue);

            temp_info.setText("Temperature: "
                    + temperature + getMetricSign("temperature"));
            temp_feels.setText("Feels: "
                    + feelsLike + getMetricSign("temperature"));
            temp_max.setText("Max: "
                    + tempMax + getMetricSign("temperature"));
            temp_min.setText("Min: "
                    + tempMin + getMetricSign("temperature"));
            pressure.setText("Pressure: "
                    + pressureValue + getMetricSign("pressure"));
        }
    }
}
