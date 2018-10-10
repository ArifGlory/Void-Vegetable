package Kelas;

/**
 * Created by Glory on 28/09/2018.
 */

public class Sayur {
    public String namaSayur;
    public String harga;
    public String key;
    public String downloadUrl;
    public String statusSayur;
    public String jumlahSayur;

    public Sayur(){

    }

    public Sayur(String namaSayur, String harga, String key, String downloadUrl,String statusSayur,String jumlahSayur) {
        this.namaSayur = namaSayur;
        this.harga = harga;
        this.key = key;
        this.downloadUrl = downloadUrl;
        this.statusSayur = statusSayur;
        this.jumlahSayur = jumlahSayur;
    }
}
