package myproject.avoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.AdapterKeranjang;
import Kelas.Keranjang;
import Kelas.KeranjangTampil;
import Kelas.Order;
import Kelas.SharedVariable;

public class KeranjangActivity extends AppCompatActivity {

    TextView txtTotal;
    Button btnOrder;
    RecyclerView recyclerKeranjang;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;
    DatabaseReference ref,refSayur,refHapus;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusPsayur;
    public static String status = "0";
    private List<KeranjangTampil> keranjangTampilList;
    private String jumlahPesan = "0";
    AdapterKeranjang adapter;
    int subtotal,total;
    private String waktu;
    Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        Firebase.setAndroidContext(KeranjangActivity.this);
        FirebaseApp.initializeApp(KeranjangActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();
        refHapus = FirebaseDatabase.getInstance().getReference();

        keranjangTampilList = new ArrayList<>();
        adapter = new AdapterKeranjang(KeranjangActivity.this,keranjangTampilList);
        total = 0;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerKeranjang = (RecyclerView) findViewById(R.id.rv_main_keranjang);
        recyclerKeranjang.setLayoutManager(new LinearLayoutManager(this));
        recyclerKeranjang.setHasFixedSize(true);
        recyclerKeranjang.setItemAnimator(new DefaultItemAnimator());
        recyclerKeranjang.setAdapter(adapter);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        txtTotal = (TextView) findViewById(R.id.txt_total);
       // Toast.makeText(getApplicationContext(),SharedVariable.idPenjualAktifCart,Toast.LENGTH_SHORT).show();

        ref.child("keranjang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                keranjangTampilList.clear();
                adapter.notifyDataSetChanged();

                for (final DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPembeli = child.child("uidPembeli").getValue().toString();

                    if (uidPembeli.equals(SharedVariable.userID)){

                        String idSayur = child.child("idSayur").getValue().toString();
                        String idPenjual = child.child("idPenjual").getValue().toString();
                        final String jml = child.child("jumlah").getValue().toString();

                        refSayur.child("psayur").child(idPenjual).child("sayurList").child(idSayur).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String urlGambar = dataSnapshot.child("downloadUrl").getValue().toString();
                                String namaSayur = dataSnapshot.child("namaSayur").getValue().toString();
                                String hargaSayur = dataSnapshot.child("harga").getValue().toString();
                                String keySayur = dataSnapshot.getKey();


                                KeranjangTampil keranjangTampil = new KeranjangTampil(namaSayur,
                                        hargaSayur,
                                        urlGambar,
                                        jml,
                                        "qwerty",
                                        keySayur);
                                subtotal = Integer.parseInt(jml) * Integer.parseInt(hargaSayur);
                                total = total + subtotal;
                                keranjangTampilList.add(keranjangTampil);
                                adapter.notifyDataSetChanged();
                                txtTotal.setText("Rp. "+total);
                                Toast.makeText(getApplicationContext(),namaSayur,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keranjangTampilList.isEmpty()){
                    customToast("Keranjang kosong");
                }else {

                    Calendar calendar = Calendar.getInstance();
                    int bulan = calendar.get(Calendar.MONTH)+1;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    waktu = dateFormat.format(new Date()); // Find todays date

                    String keyOrder = ref.child("order").push().getKey();
                    //memberikan Order Id di data order Detail
                    for (int c=0;c<keranjangTampilList.size();c++){
                        keranjangTampilList.get(c).setIdOrder(keyOrder);
                    }

                    //push data order
                    order = new Order("0",
                            waktu,
                            String.valueOf(total),
                            SharedVariable.userID,
                            SharedVariable.idPenjualAktifCart
                            );
                    ref.child("order").child(keyOrder).setValue(order);

                    //push data order detail
                    for (int d=0;d<keranjangTampilList.size();d++){
                        String keyOrderDetail =  ref.child("order_detail").push().getKey();
                        ref.child("order_detail").child(keyOrderDetail).setValue(keranjangTampilList.get(d));
                    }

                    customToast("pembelian selesai, silakan lihat progres pemesanan di menu Riwayat Pesanan");

                    //hapus data keranjang
                    refHapus.child("keranjang").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot child : dataSnapshot.getChildren()){
                                String uidPembeli = child.child("uidPembeli").getValue().toString();

                                if (uidPembeli.equals(SharedVariable.userID)){
                                    refHapus.child("keranjang").child(child.getKey().toString()).setValue(null);
                                }
                            }

                            if (!keranjangTampilList.isEmpty()){
                                keranjangTampilList.clear();
                            }
                            total = 0;
                            adapter.notifyDataSetChanged();
                            txtTotal.setText("Rp. 0");
                            SharedVariable.idPenjualAktifCart = "off";

                            Intent intent = new Intent(getApplicationContext(),HistoryPesananActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
    }

    public  void customToast(String s){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(s);
        Toast toast = new Toast(getApplicationContext());// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }
}
