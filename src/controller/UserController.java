package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    Connection connection = ConnectionController.connector();
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public ResultSet showUser() throws SQLException {
        String query = "SELECT * FROM users";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            return null;
        }
    }

    public ResultSet selectUser(Integer id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            return null;
        }
    }

    public void tambahUser(String username, String password, String status) throws SQLException {
        String query = "INSERT INTO users (username, password, status) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
        } finally {
            preparedStatement.close();
        }
    }

    public void editUser(Integer id, String username, String password, String status) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, status = ? WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, status);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            MainController.errorAlert("Error Edit Data!", e.toString());
        } finally {
            preparedStatement.close();
        }
    }

    public void hapusUser(Integer id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
        } finally {
            preparedStatement.close();
        }
    }

}
