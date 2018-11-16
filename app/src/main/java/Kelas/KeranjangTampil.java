package Kelas;

/**
 * Created by Glory on 13/11/2018.
 */

public class KeranjangTampil {
    public String namaSayur;
    public String hargaSayur;
    public String urlGambar;
    public String jumlahPesan;
    public String idOrder;

    public KeranjangTampil(String namaSayur, String hargaSayur, String urlGambar, String jumlahPesan, String idOrder) {
        this.namaSayur = namaSayur;
        this.hargaSayur = hargaSayur;
        this.urlGambar = urlGambar;
        this.jumlahPesan = jumlahPesan;
        this.idOrder = idOrder;
    }

    public String getNamaSayur() {
        return namaSayur;
    }

    public void setNamaSayur(String namaSayur) {
        this.namaSayur = namaSayur;
    }

    public String getHargaSayur() {
        return hargaSayur;
    }

    public void setHargaSayur(String hargaSayur) {
        this.hargaSayur = hargaSayur;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }

    public String getJumlah() {
        return jumlahPesan;
    }

    public void setJumlah(String jumlah) {
        this.jumlahPesan = jumlah;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
}
