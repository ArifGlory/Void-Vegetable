package Kelas;

/**
 * Created by Glory on 08/11/2018.
 */

public class Keranjang {

    public String uidPembeli;
    public String idPenjual;
    public String idSayur;
    public String jumlah;

    public Keranjang(String uidPembeli, String idPenjual, String idSayur, String jumlah) {
        this.uidPembeli = uidPembeli;
        this.idPenjual = idPenjual;
        this.idSayur = idSayur;
        this.jumlah = jumlah;
    }

    public String getUidPembeli() {

        return uidPembeli;
    }

    public void setUidPembeli(String uidPembeli) {
        this.uidPembeli = uidPembeli;
    }

    public String getIdPenjual() {
        return idPenjual;
    }

    public void setIdPenjual(String idPenjual) {
        this.idPenjual = idPenjual;
    }

    public String getIdSayur() {
        return idSayur;
    }

    public void setIdSayur(String idSayur) {
        this.idSayur = idSayur;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
