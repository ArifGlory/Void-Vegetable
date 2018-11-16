package myproject.avoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterKeranjang;
import Kelas.KeranjangTampil;

public class DetailOrderActivity extends AppCompatActivity {

    TextView txtStatus,txtTanggal,txtTotal;
    RecyclerView rvPesanan;
    DatabaseReference ref,refSayur,refHapus;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    Intent i;
    private String tgl,status,total,keyOrder;
    private List<KeranjangTampil> keranjangTampilList;
    private String jumlahPesan = "0";
    AdapterKeranjang adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        Firebase.setAndroidContext(DetailOrderActivity.this);
        FirebaseApp.initializeApp(DetailOrderActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();

        i = getIntent();
        tgl = i.getStringExtra("tanggal");
        status = i.getStringExtra("status");
        total = i.getStringExtra("total");
        keyOrder = i.getStringExtra("key");

        keranjangTampilList = new ArrayList<>();
        adapter = new AdapterKeranjang(DetailOrderActivity.this,keranjangTampilList);

        if (status.equals("0")){
            status = "Menunggu Konfirmasi";
        }else if (status.equals("1")){
            status = "Pesanan diterima, sedang diantar";
        }else if (status.equals("2")){
            status = "Pesanan Ditolak";
        }

        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtTanggal = (TextView) findViewById(R.id.txt_tanggal);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        rvPesanan = (RecyclerView) findViewById(R.id.rv_main_order);
        rvPesanan.setLayoutManager(new LinearLayoutManager(this));
        rvPesanan.setHasFixedSize(true);
        rvPesanan.setItemAnimator(new DefaultItemAnimator());
        rvPesanan.setAdapter(adapter);

        txtStatus.setText(status);
        txtTanggal.setText(tgl);
        txtTotal.setText("Rp. "+total);


        ref.child("order_detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                keranjangTampilList.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){

                    String idOrder = child.child("idOrder").getValue().toString();
                    if (idOrder.equals(keyOrder)){
                        String hargaSayur = child.child("hargaSayur").getValue().toString();
                        String jumlahPesan = child.child("jumlah").getValue().toString();
                        String namaSayur = child.child("namaSayur").getValue().toString();
                        String urlGambar = child.child("urlGambar").getValue().toString();
                        String keyOrderDetail  = child.getKey();

                        KeranjangTampil keranjangTampil = new KeranjangTampil(namaSayur,
                                hargaSayur,
                                urlGambar,
                                jumlahPesan,
                                keyOrderDetail);
                        keranjangTampilList.add(keranjangTampil);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
