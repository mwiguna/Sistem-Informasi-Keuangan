package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransaksiController {
    Connection connection = ConnectionController.connector();
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public ResultSet showTransaksi(Integer type, String month, String year) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE strftime('%m', tanggal) = ? AND strftime('%Y', tanggal) = ? AND type = ? ORDER BY tanggal ASC";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, month);
            preparedStatement.setString(2, year);
            preparedStatement.setInt(3, type);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet showArusKas(String month, String year){
        String query = "SELECT * FROM transaksi WHERE strftime('%m', tanggal) = ? AND strftime('%Y', tanggal) = ? ORDER BY tanggal ASC";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, month);
            preparedStatement.setString(2, year);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            return null;
        }
    }

    public String getStartYear() {
        String query = "SELECT * FROM transaksi ORDER BY tanggal ASC LIMIT 1";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet.getString("tanggal");
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
            return null;
        }
    }

    public ResultSet selectTransaksi(Integer id) throws SQLException {
        String query = "SELECT * FROM transaksi WHERE id = ?";
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

    public void tambahTransaksi(String keterangan, String tanggal, Integer total, Integer type) throws SQLException {
        String query = "INSERT INTO transaksi (keterangan, tanggal, total, type) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, keterangan);
            preparedStatement.setString(2, tanggal);
            preparedStatement.setInt(3, total);
            preparedStatement.setInt(4, type);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
        } finally {
            preparedStatement.close();
        }
    }

    public void editTransaksi(String keterangan, String tanggal, Integer total, Integer id) throws SQLException {
        String query = "UPDATE transaksi SET keterangan = ?, tanggal = ?, total = ? WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, keterangan);
            preparedStatement.setString(2, tanggal);
            preparedStatement.setInt(3, total);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            MainController.errorAlert("Error!", e.toString());
        } finally {
            preparedStatement.close();
        }
    }

    public void hapusTransaksi(Integer id) throws SQLException {
        String query = "DELETE FROM transaksi WHERE id = ?";
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