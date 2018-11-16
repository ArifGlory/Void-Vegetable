package Kelas;

/**
 * Created by Glory on 07/11/2018.
 */

public class Order {
    public  String status;
    public  String tanggal;
    public String total;
    public  String uidPembeli;
    public String uidPenjual;

    public Order(String status,String tanggal,String total,String uidPembeli,String uidPenjual) {
        this.status = status;
        this.tanggal = tanggal;
        this.total = total;
        this.uidPembeli = uidPembeli;
        this.uidPenjual = uidPenjual;
    }

    public String getUidPenjual() {
        return uidPenjual;
    }

    public void setUidPenjual(String uidPenjual) {
        this.uidPenjual = uidPenjual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUidPembeli() {
        return uidPembeli;
    }

    public void setUidPembeli(String uidPembeli) {
        this.uidPembeli = uidPembeli;
    }
}

