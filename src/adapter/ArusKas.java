package adapter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ArusKas {
    SimpleIntegerProperty no, id;
    SimpleStringProperty tanggal, keterangan, pendapatan, pengeluaran, saldo;

    public ArusKas(Integer no, Integer id, String pendapatan, String pengeluaran, String saldo,  String tanggal, String keterangan) {
        this.no = new SimpleIntegerProperty(no);
        this.id = new SimpleIntegerProperty(id);
        this.pendapatan  = new SimpleStringProperty(pendapatan);
        this.pengeluaran = new SimpleStringProperty(pengeluaran);
        this.saldo       = new SimpleStringProperty(saldo);
        this.tanggal     = new SimpleStringProperty(tanggal);
        this.keterangan  = new SimpleStringProperty(keterangan);
    }

    public Integer getNo() {
        return no.get();
    }

    public Integer getId() {
        return id.get();
    }

    public String getPendapatan() {
        return pendapatan.get();
    }

    public String getPengeluaran() {
        return pengeluaran.get();
    }

    public String getSaldo() {
        return saldo.get();
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public String getKeterangan() {
        return keterangan.get();
    }
}
