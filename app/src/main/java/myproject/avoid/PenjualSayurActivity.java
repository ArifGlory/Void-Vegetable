package myproject.avoid;

import android.*;
import android.Manifest;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Kelas.SharedVariable;
import Kelas.UserPreference;

public class PenjualSayurActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    FragmentPenjual fragmentPenjual;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    UserPreference mUserpref;
    Intent i;
    DialogInterface.OnClickListener listener;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    Double lat;
    Double lon, userLon, userLat;
    private LocationManager locationManager;
    private String provider;
    //private LocationRequest mlocationrequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 6000;
    private String statusPsayur, bagian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjual_sayur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserpref = new UserPreference(this);
        bagian = mUserpref.getBagian();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView txtNamaProfil = (TextView) headerView.findViewById(R.id.txtNama);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
        txtNamaProfil.setText(SharedVariable.nama);
        if (!SharedVariable.fotoPsayur.equals("no")){
            Glide.with(getApplicationContext())
                    .load(SharedVariable.fotoPsayur)
                    .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfilPSayurActivity.class);
                startActivity(intent);
            }
        });

        fragmentPenjual = new FragmentPenjual();
        goToFragment(fragmentPenjual, true);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("Location not avilable");
        }

        getLocation();
        Toast.makeText(getApplicationContext(), "UserLat : " + userLat + " , UserLon : " + userLon, Toast.LENGTH_LONG).show();

        ref.child("psayur").child(SharedVariable.userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statusPsayur = dataSnapshot.child("status").getValue().toString();

                if (bagian.equals("psayur")) {

                    if (statusPsayur.equals("on")) {
                        if (userLat != null) {
                            String latlon = userLat + "," + userLon;
                            ref.child("psayur").child(SharedVariable.userID).child("latlon").setValue(latlon);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sedang mengambil lokasi.. mohon tunggu sebentar ", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return location;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            userLat = latitude;
                            userLon = longitude;
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    }
                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            userLat = latitude;
                            userLon = longitude;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;

    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakan anda ingin keluar dari aplikasi ?");
        builder.setCancelable(false);

        listener = new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    finishAffinity();
                    System.exit(0);
                }

                if(which == DialogInterface.BUTTON_NEGATIVE){
                    dialog.cancel();
                }
            }
        };
        builder.setPositiveButton("Ya",listener);
        builder.setNegativeButton("Tidak", listener);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.penjual_sayur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homeSayur) {
            // Handle the camera action
        } else if (id == R.id.nav_listSayurnya){
            Intent i = new Intent(getApplicationContext(), ListSayurActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_logoutSayur){
            fAuth.signOut();
            mUserpref.setBagian("none");
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_pesanan){
            Intent i = new Intent(getApplicationContext(), ListPesananPenjual.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());

     //   Toast.makeText(getApplicationContext(),"Onchange lat : "+lat+"& Onchange Lon :"+lng , Toast.LENGTH_SHORT).show();

        userLat = lat;
        userLon = lng;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
