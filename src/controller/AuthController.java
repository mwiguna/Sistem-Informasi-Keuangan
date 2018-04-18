package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    Connection connection = ConnectionController.connector();;
    PreparedStatement preparedStatement;
    MainController main = new MainController();
    String status;

    @FXML TextField username, password;

    public void login(ActionEvent event) throws IOException, SQLException {
        if(loginProcess()) {
            main.openWindow("Sistem Manajemen Keuangan", "/layout/main.fxml");
            MainController controller = (MainController) main.loader.getController();
            controller.setTypeUser(status);
            main.endWindow(event, 1366, 720, true);
        }
    }

    public boolean loginProcess() throws SQLException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, password.getText());
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                status = resultSet.getString("status");
                return true;
            } else {
                MainController.errorAlert("Error!", "Username atau password tidak cocok");
                return false;
            }
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            return false;
        }
    }

}
