package Kelas;

/**
 * Created by Glory on 08/11/2018.
 */

public class OrderDetail {
    public String namaSayur;
    public String harga;
    public String jumlah;
    public String urlGambar;
    public String idOrder;

    public OrderDetail(String namaSayur, String harga, String jumlah, String urlGambar, String idOrder) {
        this.namaSayur = namaSayur;
        this.harga = harga;
        this.jumlah = jumlah;
        this.urlGambar = urlGambar;
        this.idOrder = idOrder;
    }

    public String getNamaSayur() {
        return namaSayur;
    }

    public void setNamaSayur(String namaSayur) {
        this.namaSayur = namaSayur;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
}
