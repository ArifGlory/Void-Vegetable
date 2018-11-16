package Kelas;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glory on 13/09/2018.
 */

public class SharedVariable {
    public static String nama = "Not Logged in";
    public static int jmlMotor = 0;
    public static String userID = "";
    public static List<String> list_sayur = new ArrayList();
    public static Double latitude;
    public static Double longitude;
    public static LatLng latlonMotor = new LatLng(0,0);
    public static int notifChance = 3;
    public static String jarakKeMotor = "";
    public static String check = "0";
    public static String statusPSayur = "off";
    public static String idPenjualAktifCart = "off";
}
