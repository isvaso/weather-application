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

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField city;

    @FXML
    private Button getData;

    @FXML
    private Text pressure;

    @FXML
    private Text temp_feels;

    @FXML
    private Text temp_info;

    @FXML
    private Text temp_max;

    @FXML
    private Text temp_min;

    private static final String API_KEY = "46f312369fd5bb665d26534997bd7b2d";
    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final Logger logger = LogManager.getLogger(Controller.class);

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

    private String getMetricSign(String parameter) {

        return switch (parameter) {
            case "temperature" -> "°C";
            case "pressure" -> "hPa";
            default -> "";
        };
    }

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

    private JSONObject getWeatherData(String urlAddress) throws IOException {
        String output = getUrlContent(urlAddress);

        if (!output.isEmpty()) {
            JSONObject weatherData = new JSONObject(output);
            logger.debug("Received weather data: " + weatherData);
            return weatherData;
        }

        return null;
    }

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
