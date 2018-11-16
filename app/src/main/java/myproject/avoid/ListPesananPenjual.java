package myproject.avoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.AdapterHistory;
import Adapter.AdapterPesananPenjual;
import Kelas.Order;
import Kelas.SharedVariable;

public class ListPesananPenjual extends AppCompatActivity {

    Intent i;
    RecyclerView recycler_listResep;
    public static ProgressBar progressBar;
    TextView txtInfo;
    Spinner sp_filter;
    String filterPesanan;
    private List<Order> orderList;
    private List<String> keyList;
    AdapterPesananPenjual adapter;
    private String waktu;
    DatabaseReference ref,refSayur,refHapus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan_penjual);
        Firebase.setAndroidContext(ListPesananPenjual.this);
        FirebaseApp.initializeApp(ListPesananPenjual.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sp_filter = (Spinner) findViewById(R.id.sp_filter);

        orderList = new ArrayList<>();
        keyList = new ArrayList<>();

        recycler_listResep= (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new AdapterPesananPenjual(ListPesananPenjual.this,orderList,keyList);
        recycler_listResep.setAdapter(adapter);
        recycler_listResep.setLayoutManager(new LinearLayoutManager(this));

        Calendar calendar = Calendar.getInstance();
        int bulan = calendar.get(Calendar.MONTH)+1;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        waktu = dateFormat.format(new Date()); // Find todays date
        int indexWaktu = waktu.indexOf(" ");
        waktu = waktu.substring(0,indexWaktu);

        orderList.clear();
        adapter.notifyDataSetChanged();


        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterPesanan = adapterView.getItemAtPosition(i).toString();

                if (filterPesanan.equals("Pesanan Dikirim")){
                    orderList.clear();
                    adapter.notifyDataSetChanged();
                    getPesananKonfirmasi();
                }else if (filterPesanan.equals("Pesanan belum diverifikasi")){
                    orderList.clear();
                    adapter.notifyDataSetChanged();
                    getPesananBelumKonfirmasi();
                }else if (filterPesanan.equals("Pesanan Ditolak")){
                    orderList.clear();
                    adapter.notifyDataSetChanged();
                    getPesananDitolak();
                }else if (filterPesanan.equals("Pesanan Hari ini")){
                    orderList.clear();
                    adapter.notifyDataSetChanged();
                  getPesananHariIni();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAllPesanan(){

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPenjual = child.child("uidPenjual").getValue().toString();
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPenjual.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();


                       Order order = new Order(status,
                                tanggal,
                                total,
                                uidPembeli,
                                uidPenjual);
                        orderList.add(order);
                        keyList.add(key);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPesananBelumKonfirmasi(){

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPenjual = child.child("uidPenjual").getValue().toString();
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPenjual.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();

                        if (status.equals("0")){

                            Order order = new Order(status,
                                    tanggal,
                                    total,
                                    uidPembeli,
                                    uidPenjual);
                            orderList.add(order);
                            keyList.add(key);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPesananKonfirmasi(){

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPenjual = child.child("uidPenjual").getValue().toString();
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPenjual.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();

                        if (status.equals("1")){

                            Order order = new Order(status,
                                    tanggal,
                                    total,
                                    uidPembeli,
                                    uidPenjual);
                            orderList.add(order);
                            keyList.add(key);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPesananDitolak(){

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPenjual = child.child("uidPenjual").getValue().toString();
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPenjual.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();

                        if (status.equals("2")){

                            Order order = new Order(status,
                                    tanggal,
                                    total,
                                    uidPembeli,
                                    uidPenjual);
                            orderList.add(order);
                            keyList.add(key);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPesananHariIni(){

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPenjual = child.child("uidPenjual").getValue().toString();
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPenjual.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();
                        int index = tanggal.indexOf(" ");
                        String subTanggal = tanggal.substring(0,index);

                        if (subTanggal.equals(waktu)){

                            Order order = new Order(status,
                                    tanggal,
                                    total,
                                    uidPembeli,
                                    uidPenjual);
                            orderList.add(order);
                            keyList.add(key);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

