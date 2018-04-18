package adapter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaksi {
    SimpleIntegerProperty no, id;
    SimpleStringProperty tanggal, keterangan, total;

    public Transaksi(Integer no, Integer id, String total, String tanggal, String keterangan) {
        this.no = new SimpleIntegerProperty(no);
        this.id = new SimpleIntegerProperty(id);
        this.total   = new SimpleStringProperty(total);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.keterangan = new SimpleStringProperty(keterangan);
    }

    public Integer getNo() {
        return no.get();
    }

    public Integer getId() {
        return id.get();
    }

    public String getTotal() {
        return total.get();
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public String getKeterangan() {
        return keterangan.get();
    }
}
