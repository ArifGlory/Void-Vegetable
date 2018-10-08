package Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Module.DirectionKobal;
import Module.DirectionKobalListener;
import Module.Route;
import myproject.avoid.ListSayurPembeli;
import myproject.avoid.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMaps extends Fragment implements OnMapReadyCallback, LocationListener, DirectionKobalListener {


    public FragmentMaps() {
        // Required empty public constructor
    }


    private GoogleMap mMap;
    public Marker marker_ghost;
    Intent i;
    Firebase Kref;
    public static List<String> list_keyDriver = new ArrayList();
    public static List<String> list_emailID = new ArrayList();
    int iconMarker = R.drawable.marker_void;
    private Button btnCari;
    private Double min;
    private int indexTerkecil;
    private FloatingActionButton fab1;

    private ProgressBar progressBar;
    private TextView tvDistance, tvDuration;


    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<String> listKey = new ArrayList<>();
    private List<Double> list_jarak = new ArrayList<>();
    private List<LatLng> list_LatLng = new ArrayList<>();
    private Double lat,lon, userLon,userLat;

    private double latitud, longitud;
    private LocationManager locationManager;
    private String provider;
   // private LocationRequest mlocationrequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 6000;
    FloatingActionButton btnRefresh;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    int a,b,c;
    Date calNow;
    Calendar calSet,calBanding;
    final static int RQS1 = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_maps, container, false);
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();

        list_jarak = new ArrayList<>();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        final SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

      /*  mlocationrequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)//10 detik ddalam 1milisecond
                .setFastestInterval(1 * 1000);//1detik dalam milisecond
        */
        fragment.getMapAsync(this);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("Location not avilable");
        }

       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, );
        getLocation();


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();
        LatLng lampung = new LatLng(-5.382351, 105.257791);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng tess = new LatLng(-5.381911,105.232662);


        getLocation();

       /* if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);





        Toast.makeText(getActivity().getApplication(),"Mengambil lokasi..." , Toast.LENGTH_LONG).show();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lampung, 14));
        ambilLokasiPSayur();

       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
           @Override
           public void onInfoWindowClick(final Marker marker) {
               marker_ghost = marker;
               String idmarker = marker.getId();
               String markerIndex = idmarker.substring(idmarker.indexOf("m")+1);
               int indexPSayur = Integer.parseInt(markerIndex);

               Toast.makeText(getActivity(),"index : "+markerIndex+ "| Key : ",Toast.LENGTH_SHORT).show();
               i = new Intent(getActivity(), ListSayurPembeli.class);
               i.putExtra("key",listKey.get(indexPSayur).toString());
               i.putExtra("nama",marker.getTitle().toString());
               startActivity(i);

           }
       });


    }

    public void ambilLokasiPSayur(){
//        mMap.clear();
        try{
            progressBar.setVisibility(View.VISIBLE);

            ref.child("psayur").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list_LatLng.clear();
                    String key = "";
                    int c = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String nama = (String) child.child("displayName").getValue();
                        String latlon = (String) child.child("latlon").getValue();
                        String status = (String) child.child("status").getValue();
                        String sublon = latlon.substring(latlon.indexOf(",")+1);
                        int index = latlon.indexOf(",");
                        String sublat = latlon.substring(0,index);
                        key = child.getKey();

                        Double lat = Double.valueOf(sublat);
                        Double lon = Double.valueOf(sublon);
                        LatLng posisiSayur  = new LatLng(lat,lon);


                        if (status.equals("on")){
                            list_LatLng.add(posisiSayur);
                            listKey.add(key);

                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(iconMarker))
                                    .position(posisiSayur)
                                    .title(nama).snippet("Sayur Segar Setiap hari"));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiSayur, 14));
                            c++;

                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch (Exception e){
            Log.e("Eror Maps Ambildata","Erornya : "+e);
            Toast.makeText(getActivity().getApplication(),"Gagal mengambil lokasi : "+e.toString() ,Toast.LENGTH_LONG).show();
        }
    }



    private void kirimRequest(){

    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());

      //  Toast.makeText(getActivity().getApplication(),"lat : "+lat+"& Lon :"+lng , Toast.LENGTH_SHORT).show();

        latitud = lat;
        longitud = lng;
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

    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public static String right(String value, int length) {
        // To get right characters from a string, change the begin index.
        return value.substring(value.length() - length);
    }


    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionKobalStart() {
        progressBar.setVisibility(View.VISIBLE);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionKobalSuccess(List<Route> routes) {
        progressBar.setVisibility(View.GONE);
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();


        int no = 0;
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title("Lokasi Saya")
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(iconMarker))
                    .title("Lokasi teman")
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            polylineOptions.add(route.startLocation);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylineOptions.add(route.endLocation);

            polylinePaths.add(mMap.addPolyline(polylineOptions));
            no++;
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            latitud = latitude;
                            longitud = longitude;
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            latitud = latitude;
                            longitud = longitude;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;

    }



    private void sendRequest(Double latTarget,Double lonTarget){

        list_jarak.clear();

        String origin = String.valueOf(userLat) + ","+ String.valueOf(userLon);
        String destination = String.valueOf(latTarget) + ","+ String.valueOf(lonTarget);

        if (origin.isEmpty()) {
            Toast.makeText(getActivity(), "Tolong masukkan origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(getActivity(), "Tolong masukkan destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

//            Toast.makeText(getActivity(),"origin : "+origin+"& destination : "+destination,Toast.LENGTH_SHORT).show();

        try {
            new DirectionKobal(this, origin, destination).execute();
            Log.d("origin:",origin);
            Log.d("destination:",destination);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Eror Direction : "+e.toString(),Toast.LENGTH_SHORT).show();
        }


    }
}
