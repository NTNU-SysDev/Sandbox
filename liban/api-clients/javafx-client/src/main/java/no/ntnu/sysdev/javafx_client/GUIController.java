package no.ntnu.sysdev.javafx_client;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GUIController {

    // Client
    private RESTClient client;

    // FX Elements
    public Button btnCreateUser;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnConnect;

    public TextField txtName;
    public TextField txtEmail;
    public TextField txtPhone;
    public TextField txtAge;
    public TextField txtHost;
    public TextField txtPort;

    public TabPane tabPane;

    public Tab tabManageUser;
    public Tab tabAddUser;

    public TableView tblUserList;

    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colPhone;
    public TableColumn colAge;

    /**
     * Runs automatically upon running the project.
     */
    public void initialize() {
        prepareElements();
        setEvents();
        disableButtons(true);
    }

    /**
     * Changes the property of the elements.
     */
    private void prepareElements() {
        // Alternative text when hovering over the buttons
        btnRefresh.setTooltip(new Tooltip("Refresh users"));
        btnDelete.setTooltip(new Tooltip("Delete user(s)"));

        // Assigns the data types for each column
        colName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<User, Integer>("phone"));
        colAge.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));

        // Enables multi-selection on the GUI table
        tblUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Sets default tab to "Manage User" (instead of default "Add User")
        tabPane.getSelectionModel().select(tabManageUser);
    }

    /**
     * Sets the action to preform when certain events happens.
     */
    private void setEvents() {
        // Create user from text fields when button is pressed
        btnCreateUser.setOnMouseClicked(evt -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            int age = Integer.parseInt(txtAge.getText());

            createUser(name, email, phone, age);
        });

        // Gathers all selected users from the table and deletes them
        btnDelete.setOnMouseClicked(evt -> {
            // Stores all the selected rows from the GUI table
            ObservableList<User> selectedUsers = tblUserList.getSelectionModel().getSelectedItems();

            // Loops though them and deletes them individually
            for (User user : selectedUsers) {
                deleteUser(user);
            }

            refreshUsers();
        });

        btnConnect.setOnMouseClicked(evt -> connect());
        btnRefresh.setOnMouseClicked(evt -> refreshUsers());

        // Restricts to numeric values
        txtAge.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("(\\d)*")) {
                txtAge.setText(newText);
            } else {
                txtAge.setText(oldText);
            }
        });

        // Restricts to numeric values
        txtPort.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("(\\d)*")) {
                txtPort.setText(newText);
            } else {
                txtPort.setText(oldText);
            }
        });

        // Refresh users when changing tabs
        tabManageUser.setOnSelectionChanged(evt -> refreshUsers());
    }

    /**
     * Create a new user.
     *
     * @param name      user's name
     * @param email     user's email
     * @param phone     user's phone
     * @param age       user's age
     *
     * @see RESTClient#createUser(User)
     */
    private void createUser(String name, String email, String phone, int age) {
        try {
            client.createUser(new User(name, email, phone, age));

            txtName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtAge.setText("");
        } catch (IOException e) {
            System.out.println("Couldn't create user. (" + client.getHttpResponse() + ")");
        }
    }

    /**
     * Updates the GUI table with updated users.
     *
     * @see RESTClient#getUsers()
     */
    private void refreshUsers() {
        try {
            tblUserList.setItems(client.getUsers());
        } catch (Exception e) {
            // Show error and disconnect
            System.out.println("Couldn't refresh user list (" + client.getHttpResponse() + ")");
            disconnect();
        }
    }

    /**
     * Deletes a user.
     *
     * @param user  user to delete
     *
     * @see RESTClient#createUser(User)
     */
    private void deleteUser(User user) {
        try {
            client.deleteUser(user.getEmail());
        } catch (IOException e) {
            System.out.println("Couldn't delete user (" + client.getHttpResponse() + ")");
        }
    }

    /**
     * Disables/enables buttons that requires an established connection.
     *
     * @param state connectivity state to an API server
     */
    private void disableButtons(boolean state) {
        List<Button> buttons = Arrays.asList(btnDelete, btnRefresh, btnCreateUser);

        // Loops through the buttons stored in the list and sets their state
        for (Button b : buttons) {
            b.setDisable(state);
        }

        // Disables access to the "Add User" tab
        tabAddUser.setDisable(state);
    }

    /**
     * Connects to the API server.
     */
    private void connect() {
        //Disables buttons and empties the GUI table
        disableButtons(true);
        tblUserList.setItems(null);

        try {
            // Tries to connect, enable buttons and refresh the users
            client = new RESTClient(txtHost.getText(), Integer.parseInt(txtPort.getText()));
            disableButtons(false);
            refreshUsers();
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Port number must be set and be numeric.", ButtonType.OK);
            a.show();
        }
    }

    /**
     * Cuts the connection.
     */
    private void disconnect() {
        client = null;
        disableButtons(true);
    }
}
