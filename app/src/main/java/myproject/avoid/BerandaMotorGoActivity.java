package myproject.avoid;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Kelas.BaseDrawerActivity;
import Kelas.SharedVariable;
import Kelas.UserPreference;
import Module.DirectionKobal;
import Module.DirectionKobalListener;
import Module.Route;

public class BerandaMotorGoActivity extends BaseDrawerActivity {

    public static ProgressBar progressBar;
    Intent i;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static String activeDevice = "";
    TextView txtNamaMotor,txtPlatNomor,txtNotif;
    CardView crd_mesin,crd_timer,crd_search,crd_lokasi,crd_notif;
    ImageView img_engine,img_search,img_lokasi,img_timer,img_notif;
    String Smesin,Ssecure,Slokasi,Sping,Snama,Splat,Swarning;
    private static final long time = 2;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;
    Double lat;
    Double lon,userLon,userLat;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    private LocationManager locationManager;
    private String provider;
    //private LocationRequest mlocationrequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 6000;
    public static String jarakMotor;
    int jrkMotor,a,b,c;

    final static int RQS1 = 1;
    private static final long delayAlarm = 1 *60 * 1000L;
    Date calNow;
    Calendar calSet,calBanding;
    UserPreference mUserPref;
    DialogInterface.OnClickListener listener;
    int jarakAman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berandamotorgo);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(BerandaMotorGoActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtNamaMotor = (TextView) findViewById(R.id.txtNamaMotor);






    }






}
