package RestTester;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public class Controller {

    public TextField remoteUrl;
    public ComboBox httpMethodBox;
    public TextArea responseBody;

    // Special FXML function that is called after constructor where the FXM objects are available
    public void initialize() {
        httpMethodBox.getItems().setAll("GET", "POST", "PUT", "PATCH", "DELETE");
        httpMethodBox.setValue("GET");
    }

    public void makeRequest() {
        try {
            URL requestUrl = new URL(remoteUrl.getText());
            HttpURLConnection conn = (HttpURLConnection)requestUrl.openConnection();
            conn.setRequestMethod(httpMethodBox.getValue().toString());
            conn.setRequestProperty("User-Agent", "HTTP Tester 1.0");
            conn.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            char[] htmlBuffer = new char[500 * 1024]; // 500Kb buffer. No HTML page alone should rightly be over 1/2 Mb
            in.read(htmlBuffer);
            in.close();
            responseBody.setText(String.valueOf(htmlBuffer));
        } catch (MalformedURLException e) {
            responseBody.setText("***Malformed URL: " + remoteUrl.getText() + "***");
        } catch (IOException e) {
            responseBody.setText("***IO error***");
        } catch (IllegalArgumentException e) {
            responseBody.setText("***Error. Please check URL.***");
        }
    }

    public void handleUrlFieldKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            makeRequest();
        }
    }
}
