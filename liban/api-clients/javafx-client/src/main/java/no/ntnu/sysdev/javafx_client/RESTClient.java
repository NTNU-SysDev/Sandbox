package no.ntnu.sysdev.javafx_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTClient {

    private String host;
    private int port;
    private String httpResponse;

    public RESTClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    public String getHttpResponse() {
        return "HTTP/1.1 " + httpResponse;
    }

    private String request(String method, String file, String contentType, String payload) throws IOException {
        URL url = new URL("http", host, port, file);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        if (contentType != null) {
            con.setRequestProperty("Content-Type", contentType);
        }
        con.setRequestMethod(method);

        con.setDoInput(true);

        if (payload != null) {
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(payload.getBytes());
            outputStream.close();
        }

        con.connect();
        httpResponse = con.getResponseCode() + " " + con.getResponseMessage();

        StringBuilder resp = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String out;

        while ((out = reader.readLine()) != null) {
            resp.append(out);
        }

        con.disconnect();

        return resp.toString();
    }

    public ObservableList<User> getUsers() throws Exception {
        String jsonString = jsonString = request("GET", "/users", "application-x-www-form-urlencoded", null);
        return FXCollections.observableArrayList(User.createUsers(jsonString));
    }

    public void createUser(User user) throws IOException {
        JSONObject json = new JSONObject();

        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone", user.getPhone());
        json.put("age", user.getAge());

        System.out.println(json.toJSONString());
        String response = request("POST", "/createUser", "application/json", json.toJSONString());
        System.out.println(response);
    }

    public void deleteUser(String email) throws IOException {
        String response = request("DELETE", "/deleteUser", null, "email=" + email);
        System.out.println(response);
    }

}
