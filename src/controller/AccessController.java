package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccessController {

    @FXML Label title;
    @FXML HBox hBox;
    @FXML TextField username, password, keterangan, total;
    @FXML ToggleGroup radioStatus;
    @FXML DatePicker date;

    private UserController user           = new UserController();
    private TransaksiController transaksi = new TransaksiController();
    private Integer typeTransaksi, typeAccess, idData, idDataUser, tot;
    private String data, uname, pass, ket, tanggal = null;

    public void setType(int typeTransaksi, int typeAccess){
        this.typeTransaksi = typeTransaksi;
        this.typeAccess    = typeAccess;

        switch (typeTransaksi){
            case  3: data = "Pendapatan"; break;
            case  4: data = "Pengeluaran"; break;
            default: data = "User"; break;
        }

        if(typeAccess == 0){
            title.setText("Tambah Data " + data);
            hBox.getChildren().remove(1);
        } else {
            title.setText("Edit Data " + data);
            hBox.getChildren().remove(0);
        }
    }

    // ---------------- Transaksi

    public void tambahTransaksi(ActionEvent event) throws SQLException {
        ket = keterangan.getText();
        if(!ket.isEmpty() && tanggal != null && !total.getText().isEmpty()){
            tot = Integer.parseInt(total.getText());
            transaksi.tambahTransaksi(ket, tanggal, tot, typeTransaksi);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else MainController.warningAlert("Validasi Form", "Mohon lengkapi form!");
    }

    public void setTransaksi(Integer id) throws SQLException {
        this.idData = id;
        ResultSet res = transaksi.selectTransaksi(id);
        tanggal = res.getString("tanggal");
        keterangan.setText(res.getString("Keterangan"));
        total.setText(String.valueOf(res.getInt("total")));
        date.setValue(LocalDate.parse(tanggal));
    }

    public void editTransaksi(ActionEvent event) throws SQLException {
        ket = keterangan.getText();
        if(!ket.isEmpty() && tanggal != null && !total.getText().isEmpty()){
            tot = Integer.parseInt(total.getText());
            transaksi.editTransaksi(ket, tanggal, tot, idData);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else MainController.warningAlert("Validasi Form", "Mohon lengkapi form!");
    }

    public void dateListener(ActionEvent event){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        tanggal = formatter.format(date.getValue());
    }

    public void totalListener(){
        total.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]*")) total.setText(oldValue);
        });
    }

    // ---------------- User

    public void tambahUser(ActionEvent event) throws SQLException, IOException {
        uname = username.getText();
        pass  = password.getText();
        RadioButton status = (RadioButton) radioStatus.getSelectedToggle();
        if(!uname.isEmpty() && !pass.isEmpty()){
            user.tambahUser(uname, pass, status.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else MainController.warningAlert("Validasi Form!", "Mohon lengkapi form!");
    }

    public void setUser(Integer id) throws SQLException {
        this.idDataUser = id;
        ResultSet res = user.selectUser(id);
        username.setText(res.getString("username"));
        password.setText(res.getString("password"));
    }

    public void editUser(ActionEvent event) throws SQLException {
        uname = username.getText();
        pass  = password.getText();
        RadioButton status = (RadioButton) radioStatus.getSelectedToggle();
        if(!uname.isEmpty() && !pass.isEmpty()){
            user.editUser(idDataUser, username.getText(), password.getText(), status.getText());
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else MainController.warningAlert("Validasi Form", "Mohon lengkapi form!");
    }

}
