package no.ntnu.sysdev.javafx_client;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
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

    // Runs automatically upon running the project
    public void initialize() {
        prepareElements();
        setEvents();
        disableButtons(true);
    }

    private void prepareElements() {
        btnRefresh.setTooltip(new Tooltip("Refresh users"));
        btnDelete.setTooltip(new Tooltip("Delete user(s)"));

        colName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<User, Integer>("phone"));
        colAge.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));

        tblUserList.setEditable(true);
        tblUserList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colName.setCellFactory(TextFieldTableCell.forTableColumn());

        tabPane.getSelectionModel().select(tabManageUser);
    }

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
            ObservableList<User> selectedUsers = tblUserList.getSelectionModel().getSelectedItems();

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

        txtPort.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("(\\d)*")) {
                txtAge.setText(newText);
            } else {
                txtAge.setText(oldText);
            }
        });

        // Refresh users when changing tabs
        tabManageUser.setOnSelectionChanged(evt -> refreshUsers());
    }

    private void createUser(String name, String email, String phone, int age) {
        try {
            client.createUser(new User(name, email, phone, age));
        } catch (IOException e) {
            System.out.println("Couldn't create user. (" + client.getHttpCode() + ")");
        }
    }

    private void refreshUsers() {
        try {
            tblUserList.setItems(client.getUsers());
        } catch (Exception e) {
            System.out.println("Couldn't refresh user list (" + client.getHttpCode() + ")");
            disconnect();
        }
    }

    private void deleteUser(User user) {
        try {
            client.deleteUser(user.getEmail());
        } catch (IOException e) {
            System.out.println("Couldn't delete user (" + client.getHttpCode() + ")");
        }
    }

    private void disableButtons(boolean state) {
        List<Button> buttons = Arrays.asList(btnDelete, btnRefresh, btnCreateUser);

        for (Button b : buttons) {
            b.setDisable(state);
        }

        tabAddUser.setDisable(state);
    }

    private void connect() {
        disableButtons(true);
        tblUserList.setItems(null);

        try {
            client = new RESTClient(txtHost.getText(), Integer.parseInt(txtPort.getText()));
            disableButtons(false);
            refreshUsers();
        } catch (IOException ioe) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Couldn't connect to API.", ButtonType.OK);
            a.show();
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Port number must be set and be numeric.", ButtonType.OK);
            a.show();
        }
    }

    private void disconnect() {
        client = null;
        disableButtons(true);
    }

    //TODO: Implement editUser method
    private void editUser(User user) {

    }
}
