package no.ntnu.sysdev.javafx_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Responsible of communicating with the API and provide
 * information to classes when needed.
 */
public class RESTClient {

    private String host;
    private int port;
    private String httpResponse;

    /**
     * Stores host and port of the API server.
     *
     * @param host          host name, excluding the protocol type at the beginning of the url
     * @param port          port number
     */
    public RESTClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Returns the last server-generated HTTP response from the last request done.
     *
     * @return  previous HTTP code and message
     */
    public String getHttpResponse() {
        return "HTTP/1.1 " + httpResponse;
    }

    /**
     * Main method that handles communication.
     *
     * @param method        HTTP method (GET, POST, DELETE, etc...)
     * @param file          document file (that comes after the domain name)
     * @param contentType   HTTP header type (ex: application/json)
     * @param payload       HTTP parameters (ex: name=bob&age=2)
     * @return              response body from the server
     * @throws IOException  when something went wrong with the communication
     */
    private String request(String method, String file, String contentType, String payload) throws IOException {
        URL url = new URL("http", host, port, file);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Sets Content-Type header when defined
        if (contentType != null) {
            con.setRequestProperty("Content-Type", contentType);
        }

        // Sets HTTP method
        con.setRequestMethod(method);
        con.setDoInput(true);

        // Send parameters when defined
        if (payload != null) {
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(payload.getBytes());
            outputStream.close();
        }
        con.connect();
        
        // Stores the HTTP response of the current connection
        httpResponse = con.getResponseCode() + " " + con.getResponseMessage();

        // Stores request body in a buffer
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String out;

        // Creates a string of the server response body
        while ((out = reader.readLine()) != null) {
            response.append(out);
        }

        con.disconnect();

        return response.toString();
    }

    /**
     * Requests to receive all users.
     *
     * @return              a list that can be used for the table in GUI
     * @throws Exception    when users cannot be created from the JSON string or if request to server fails
     *
     * @see #request(String, String, String, String)
     * @see User#createUsers(String)
     */
    public ObservableList<User> getUsers() throws Exception {
        // Gets JSON containing all users
        String jsonString = jsonString = request("GET", "/users", "application-x-www-form-urlencoded", null);

        // Convert JSON using the method inside User class and creates a list for GUI table
        return FXCollections.observableArrayList(User.createUsers(jsonString));
    }

    /**
     * Creates a user.
     *
     * @param user          user you want to add
     * @throws IOException  when request fails
     *
     * @see #request(String, String, String, String)
     */
    public void createUser(User user) throws IOException {
        JSONObject json = new JSONObject();

        // Extracts data from User object and creates a JSON object
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone", user.getPhone());
        json.put("age", user.getAge());

        // Sends a "create user"-request to the server with JSON in string format
        String response = request("POST", "/createUser", "application/json", json.toJSONString());
        System.out.println(response);
    }

    /**
     * Deletes a user.
     *
     * @param email         email of the user you want to delete
     * @throws IOException  when request fails
     *
     * @see #request(String, String, String, String)
     */
    public void deleteUser(String email) throws IOException {
        String response = request("DELETE", "/deleteUser", null, "email=" + email);
        System.out.println(response);
    }

}
