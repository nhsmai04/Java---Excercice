package org.btqt3.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.btqt3.FormApplication;
import org.btqt3.Model.User;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // LOGIN
    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (User.users.containsKey(user) &&
                User.users.get(user).equals(pass)) {

            showAlert("Success", "Login successful!");
        } else {
            showAlert("Error", "Wrong username or password!");
        }
    }

    // REGISTER
    @FXML
    private void handleRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (User.users.containsKey(user)) {
            showAlert("Error", "Username already exists!");
        } else {
            User.users.put(user, pass);
            showAlert("Success", "Register successful!");
        }
    }

    // CHUYỂN MÀN HÌNH
    @FXML
    private void goToRegister() throws Exception {
        FormApplication.showRegister();
    }

    @FXML
    private void goToLogin() throws Exception {
        FormApplication.showLogin();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}