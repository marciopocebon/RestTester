package RestTester;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Controller {

    public TextField remoteUrl;
    public ComboBox<String> httpMethodBox;
    public TextArea responseBody;
    public TextArea requestHeaders;
    public TextArea requestData;
    public Button headersResetButton;
    public Button dataResetButton;
    protected HttpURLConnection conn;

    // Special FXML function that is called after constructor where the FXM objects are available
    public void initialize() {
        httpMethodBox.getItems().setAll("GET", "POST", "PUT", "PATCH", "DELETE");
        httpMethodBox.setValue("GET");
    }

    public void makeRequest() {
        try {
            URL requestUrl = new URL(remoteUrl.getText());
            this.conn = (HttpURLConnection) requestUrl.openConnection();
            this.conn.setRequestMethod(httpMethodBox.getValue());
            if (httpMethodBox.getValue().equals("POST")
             || httpMethodBox.getValue().equals("PUT")) {
                this.setPostData();
            }
            this.setHeadersFromForm();

            BufferedReader in = new BufferedReader(new InputStreamReader(this.conn.getInputStream()));
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

    // Set all custom headers listed in requestHeaders
    private void setHeadersFromForm() {
        if (requestHeaders.getText().length() == 0) {
            return;
        }
        for (String rawString : requestHeaders.getText().split("\n")) {
            String[] splitString = rawString.split(":");
            if (splitString.length < 2) {
                continue;
            }
            String headerKey = splitString[0].trim();
            String headerValue = splitString[1].trim();
            this.conn.setRequestProperty(headerKey, headerValue);
        }
    }

    private void setPostData() {
        String data = requestData.getText();
        String type = "application/x-www-form-urlencoded";
        String encodedData;
        this.conn.setDoOutput(true);
        try {
            encodedData = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            responseBody.setText("***Error. UTF-8 not supported by this system.***");
            return;
        }
        this.conn.setRequestProperty("Content-Type", type);
        this.conn.setRequestProperty("Custom-Header", "something");
        this.conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));

        try {
            DataOutputStream wr = new DataOutputStream(this.conn.getOutputStream());
            wr.writeBytes(encodedData);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Callback for key presses in the URL field. Will handle the ENTER button.
    public void handleUrlFieldKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            makeRequest();
        }
    }

    public void clearHeaderField(ActionEvent actionEvent) {
        requestHeaders.clear();
    }

    public void clearPostField(ActionEvent actionEvent) {
        requestData.clear();
    }
}
