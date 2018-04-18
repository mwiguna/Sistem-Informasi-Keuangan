package controller;

import adapter.ArusKas;
import adapter.Transaksi;
import adapter.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private NumberFormat numberFormat = NumberFormat.getInstance(new Locale("id", "id"));
    private Integer idData = null, idDataUser = null, totalTransaksi = 0, currentTab = 0, startYear = 0;
    private String comboMonth, comboYear;
    FXMLLoader loader  = null;
    Stage primaryStage = null;
    Parent  root       = null;

    // Class
    private UserController user           = new UserController();
    private AccessController access       = new AccessController();
    private TransaksiController transaksi = new TransaksiController();

    @FXML private TabPane tab;
    @FXML private VBox node;
    @FXML private Label endPendapatan, endPengeluaran, endLapPendapatan, endLapPengeluaran, endArusKas;
    @FXML private ComboBox monthPendapatan, yearPendapatan, monthPengeluaran, yearPengeluaran, monthLapPendapatan, yearLapPendapatan, monthLapPengeluaran, yearLapPengeluaran, monthArusKas, yearArusKas;
    private ObservableList<String> listMonth = FXCollections.observableArrayList("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember");
    private ObservableList<String> listYear  = FXCollections.observableArrayList();

    // Table Transaksi
    @FXML private TableView<Transaksi> tablePendapatan;
    @FXML private TableView<Transaksi> tablePengeluaran;
    @FXML private TableView<Transaksi> tableLapPendapatan;
    @FXML private TableView<Transaksi> tableLapPengeluaran;
    @FXML private TableColumn<Transaksi, Integer> noPendapatan;
    @FXML private TableColumn<Transaksi, String>  totalPendapatan;
    @FXML private TableColumn<Transaksi, String>  tanggalPendapatan;
    @FXML private TableColumn<Transaksi, String>  keteranganPendapatan;
    @FXML private TableColumn<Transaksi, Integer> noPengeluaran;
    @FXML private TableColumn<Transaksi, String>  totalPengeluaran;
    @FXML private TableColumn<Transaksi, String>  tanggalPengeluaran;
    @FXML private TableColumn<Transaksi, String>  keteranganPengeluaran;
    @FXML private TableColumn<Transaksi, Integer> noLapPendapatan;
    @FXML private TableColumn<Transaksi, String>  totalLapPendapatan;
    @FXML private TableColumn<Transaksi, String>  tanggalLapPendapatan;
    @FXML private TableColumn<Transaksi, String>  keteranganLapPendapatan;
    @FXML private TableColumn<Transaksi, Integer> noLapPengeluaran;
    @FXML private TableColumn<Transaksi, String>  totalLapPengeluaran;
    @FXML private TableColumn<Transaksi, String>  tanggalLapPengeluaran;
    @FXML private TableColumn<Transaksi, String>  keteranganLapPengeluaran;

    // Table Arus Kas
    @FXML private TableView<ArusKas> tableArusKas;
    @FXML private TableColumn<ArusKas, Integer> noArusKas;
    @FXML private TableColumn<ArusKas, String>  tanggalArusKas;
    @FXML private TableColumn<ArusKas, String>  keteranganArusKas;
    @FXML private TableColumn<ArusKas, String>  pendapatanArusKas;
    @FXML private TableColumn<ArusKas, String>  pengeluaranArusKas;
    @FXML private TableColumn<ArusKas, String>  saldoArusKas;

    // Table User
    @FXML private TableView<User> tableUser;
    @FXML private TableColumn<User, Integer> noUser;
    @FXML private TableColumn<User, String>  usernameUser, statusUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabListener();
        setAllCombo();
        try { setTableLaporanPendapatan(); }
        catch (SQLException e) { errorAlert("Error Set Table Laporan Pendapatan!", e.toString()); }

        listenerUser();
        listenerArusKas();
        listenerTransaksi(tablePendapatan);
        listenerTransaksi(tablePengeluaran);
        listenerTransaksi(tableLapPendapatan);
        listenerTransaksi(tableLapPengeluaran);
    }


    // -------------------- Transaksi --------------------- //


    public void tambahTransaksi(ActionEvent event) throws IOException {
        openWindow("Tambah Data", "/layout/transaksi.fxml");
        setType(0);
        access.totalListener();
        stageListener();
        endWindow(event, 400, 300, false);
    }

    public void editTransaksi(ActionEvent event) throws IOException, SQLException {
        if(idData != null) {
            openWindow("Edit Data", "/layout/transaksi.fxml");
            setType(1);
            access.totalListener();
            access.setTransaksi(idData);
            stageListener();
            endWindow(event, 400, 300, false);
        } else warningAlert("Edit Data", "Harap pilih dahulu data yang ingin diedit.");
    }

    public void hapusTransaksi(ActionEvent event) throws IOException, SQLException {
        if(idData != null) confirmAlert();
        else warningAlert("Hapus Data", "Harap pilih dahulu data yang ingin dihapus.");
    }

    public void setTablePendapatan() throws SQLException {
        setCombo(monthPendapatan, yearPendapatan);
        setTableTransaksi(transaksi.showTransaksi(3, comboMonth, comboYear), tablePendapatan, noPendapatan, tanggalPendapatan, keteranganPendapatan, totalPendapatan);
        endPendapatan.setText("Rp. " + numberFormat.format(totalTransaksi));
    }

    public void setTablePengeluaran() throws SQLException {
        setCombo(monthPengeluaran, yearPengeluaran);
        setTableTransaksi(transaksi.showTransaksi(4, comboMonth, comboYear), tablePengeluaran, noPengeluaran, tanggalPengeluaran, keteranganPengeluaran, totalPengeluaran);
        endPengeluaran.setText("Rp. " + numberFormat.format(totalTransaksi));
    }

    public void setTableLaporanPendapatan() throws SQLException {
        setCombo(monthLapPendapatan, yearLapPendapatan);
        setTableTransaksi(transaksi.showTransaksi(3, comboMonth, comboYear), tableLapPendapatan, noLapPendapatan, tanggalLapPendapatan, keteranganLapPendapatan, totalLapPendapatan);
        endLapPendapatan.setText("Rp. " + numberFormat.format(totalTransaksi));
    }

    public void setTableLaporanPengeluaran() throws SQLException {
        setCombo(monthLapPengeluaran, yearLapPengeluaran);
        setTableTransaksi(transaksi.showTransaksi(4, comboMonth, comboYear), tableLapPengeluaran, noLapPengeluaran, tanggalLapPengeluaran, keteranganLapPengeluaran, totalLapPengeluaran);
        endLapPengeluaran.setText("Rp. " + numberFormat.format(totalTransaksi));
    }

    public void setTableArusKas() throws SQLException {
        setCombo(monthArusKas, yearArusKas);
        ObservableList<ArusKas> listArusKas = FXCollections.observableArrayList();
        int i = 1;
        totalTransaksi = 0;
        ResultSet res  = transaksi.showArusKas(comboMonth, comboYear);
        while(res.next()) {
            Integer pendapatan  = 0;
            Integer pengeluaran = 0;
            if(res.getInt("type") == 3){
                pendapatan = res.getInt("total");
                totalTransaksi += pendapatan;
            } else {
                pengeluaran = res.getInt("total");
                totalTransaksi -= pengeluaran;
            }

            listArusKas.add(new ArusKas(i, res.getInt("id"), numberFormat.format(pendapatan), numberFormat.format(pengeluaran), numberFormat.format(totalTransaksi), res.getString("tanggal"), res.getString("keterangan")));
            i++;
        } endArusKas.setText("Rp. " + numberFormat.format(totalTransaksi));

        noArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, Integer>("no"));
        tanggalArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, String>("tanggal"));
        keteranganArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, String>("keterangan"));
        pendapatanArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, String>("pendapatan"));
        pengeluaranArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, String>("pengeluaran"));
        saldoArusKas.setCellValueFactory(new PropertyValueFactory<ArusKas, String>("saldo"));
        tableArusKas.setItems(listArusKas);
    }

    public void setTableTransaksi(ResultSet res, TableView<Transaksi> table, TableColumn no, TableColumn tanggal, TableColumn keterangan, TableColumn total) throws SQLException {
        ObservableList<Transaksi> listTransaksi = FXCollections.observableArrayList();
        int i = 1;
        totalTransaksi = 0;
        while(res.next()) {
            totalTransaksi += res.getInt("total");
            listTransaksi.add(new Transaksi(i, res.getInt("id"), numberFormat.format(res.getInt("total")), res.getString("tanggal"), res.getString("keterangan")));
            i++;
        }

        no.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("no"));
        tanggal.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("tanggal"));
        keterangan.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("keterangan"));
        total.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("total"));
        table.setItems(listTransaksi);
    }

    public void listenerTransaksi(TableView<Transaksi> table){
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Transaksi transaksi = table.getSelectionModel().getSelectedItem();
            try { idData = transaksi.getId(); }
            catch (Exception e){}
        });
    }

    public void listenerArusKas(){
        tableArusKas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ArusKas arusKas = tableArusKas.getSelectionModel().getSelectedItem();
            try { idData = arusKas.getId(); }
            catch (Exception e){}
        });
    }


    // -------------------- User --------------------- //


    public void tambahUser(ActionEvent event) throws IOException {
        openWindow("Tambah Data User", "/layout/user.fxml");
        setType(0);
        stageListener();
        endWindow(event, 400, 300, false);
    }

    public void editUser(ActionEvent event) throws IOException, SQLException {
        if(idDataUser != null){
            openWindow("Edit Data User", "/layout/user.fxml");
            setType(1);
            access.setUser(idDataUser);
            stageListener();
            endWindow(event, 400, 300, false);
        } else warningAlert("Edit Data", "Harap pilih dahulu data yang ingin diedit.");
    }

    public void hapusUser(ActionEvent event) throws IOException, SQLException {
        if(idDataUser != null) confirmAlert();
        else warningAlert("Hapus Data", "Harap pilih dahulu data yang ingin dihapus.");
    }

    public void setTableUser() throws SQLException {
        ObservableList<User> listUser = FXCollections.observableArrayList();
        ResultSet res = user.showUser();
        int i = 1;
        while(res.next()) {
            listUser.add(new User(i, Integer.parseInt(res.getString("id")), res.getString("status"), res.getString("username")));
            i++;
        }

        noUser.setCellValueFactory(new PropertyValueFactory<User, Integer>("no"));
        statusUser.setCellValueFactory(new PropertyValueFactory<User, String>("status"));
        usernameUser.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableUser.setItems(listUser);
    }

    public void listenerUser(){
        tableUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            User user = tableUser.getSelectionModel().getSelectedItem();
            try { idDataUser = user.getId(); }
            catch (Exception e){}
        });
    }


    // -------------------- Listener --------------------- //


    public void setType(int typeAccess){
        access = (AccessController) loader.getController();
        access.setType(currentTab, typeAccess);
    }

    public void setTypeUser(String status){
        if(status.equals("Akuntan")) tab.getTabs().remove(5);
        else if (status.equals("Owner")) tab.getTabs().remove(3, 6);
    }

    public void tabListener(){
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentTab = tab.getSelectionModel().getSelectedIndex();
            idData     = null;
            idDataUser = null;
            setAllTable();
        });
    }

    public void setAllTable(){
        if(currentTab.equals(0)) {
            try { setTableLaporanPendapatan(); }
            catch (SQLException e) { errorAlert("Error Set Laporan Pendapatan!", e.toString()); }
        } else if(currentTab.equals(1)){
            try { setTableLaporanPengeluaran(); }
            catch (SQLException e) { errorAlert("Error Set Laporan Penngeluaran!", e.toString()); }
        } else if(currentTab.equals(2)){
            try { setTableArusKas(); }
            catch (SQLException e) { errorAlert("Error Set Laporan Arus Kas!", e.toString()); }
        } else if(currentTab.equals(3)){
            try { setTablePendapatan(); }
            catch (SQLException e) { errorAlert("Error Set Pendapatan!", e.toString()); }
        } else if(currentTab.equals(4)){
            try { setTablePengeluaran(); }
            catch (SQLException e) { errorAlert("Error Set Pengeluaran!", e.toString()); }
        } else {
            try { setTableUser(); }
            catch (SQLException e) { errorAlert("Error Set User!", e.toString()); }
        }
    }

    public void stageListener(){
        primaryStage.setOnHiding(ev -> {
            try  {
                if(currentTab.equals(3)) setTablePendapatan();
                else if(currentTab.equals(4)) setTablePengeluaran();
                else setTableUser();
                idData = null;
                idDataUser = null;
            }
            catch (SQLException e) { MainController.errorAlert("Error Listener!", e.toString()); }
        });
    }

    public void setAllCombo(){
        LocalDateTime currentTime = LocalDateTime.now();
        int thisMonth = currentTime.getMonthValue();
        int thisYear  = currentTime.getYear();

        ComboBox[] months = {monthPendapatan, monthPengeluaran, monthLapPendapatan, monthLapPengeluaran, monthArusKas};
        for(ComboBox month: months) {
            month.setItems(listMonth);
            month.getSelectionModel().select(thisMonth - 1);
        }

        DateTimeFormatter getYear = DateTimeFormatter.ofPattern("yyy");
        startYear = Integer.valueOf(getYear.format(LocalDate.parse(transaksi.getStartYear())));
        if(startYear == 0) startYear = thisYear;
        ComboBox[] years  = {yearPendapatan, yearPengeluaran, yearLapPendapatan, yearLapPengeluaran, yearArusKas};

        int indexYear = thisYear - startYear;
        for(ComboBox year: years){
            year.setItems(listYear);
            while(startYear <= thisYear) {
                year.getItems().add(startYear.toString());
                startYear++;
            } year.getSelectionModel().select(indexYear);
        }
    }

    public void setCombo(ComboBox month, ComboBox year){
        Integer cMonth = month.getSelectionModel().getSelectedIndex() + 1;
        comboYear      = year.getValue().toString();
        if(cMonth < 10) comboMonth = "0" + cMonth;
        else comboMonth = cMonth.toString();
    }

    public void comboListener(ActionEvent event){
        setAllTable();
    }


    // -------------------- Window --------------------- //


    public void logout(ActionEvent event) throws IOException {
       openWindow("Halaman Login", "/layout/login.fxml");
       endWindow(event, 600, 400, true);
    }

    public void openWindow(String title, String layout) throws IOException {
        loader = new FXMLLoader(getClass().getResource(layout));
        root   = loader.load();
        primaryStage = new Stage();
        primaryStage.setTitle(title);
    }

    public void endWindow(ActionEvent event, int width, int height, boolean hide){
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/sample/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        if(hide){
            try{ ((Node)(event.getSource())).getScene().getWindow().hide(); }
            catch (Exception e){ node.getScene().getWindow().hide(); }
        }
    }

    public void confirmAlert() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Data");
        alert.setContentText("Yakin ingin menghapus data ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(currentTab.equals(3)){
                transaksi.hapusTransaksi(idData);
                setTablePendapatan();
            } else if(currentTab.equals(4)) {
                transaksi.hapusTransaksi(idData);
                setTablePengeluaran();
            } else {
                user.hapusUser(idDataUser);
                setTableUser();
            }
        }
    }

    public static void errorAlert(String title, String e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(e);
        alert.showAndWait();
    }

    public static void warningAlert(String title, String e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(e);
        alert.showAndWait();
    }

}