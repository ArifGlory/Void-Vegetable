package myproject.avoid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import Adapter.RecycleAdapterDetailPesanan;
import Kelas.KeranjangTampil;

public class DetailPesananActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static ProgressBar progressBar;
    DatabaseReference ref,refSayur;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    Intent i;
    public static String keyPembeli,keyOrder,namaPembeli,phone,status;
    public static TextView txtNamaPembeli,txtPhone,txtStatus;
   // RecycleAdapterDetailPesanan adapter;
    Button btnTerima,btnTolak,btnLihatBukti;
    private String urlBukti;
    DialogInterface.OnClickListener listener;
    private List<KeranjangTampil> keranjangTampilList;
    private String jumlahPesan = "0";
    AdapterKeranjang adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Pesanan");
        initCollapsingToolbar();
        Firebase.setAndroidContext(DetailPesananActivity.this);
        FirebaseApp.initializeApp(DetailPesananActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();

        i = getIntent();
        keyPembeli = i.getStringExtra("keyPembeli");
        keyOrder = i.getStringExtra("keyOrder");
        status = i.getStringExtra("status");

        keranjangTampilList = new ArrayList<>();
        adapter = new AdapterKeranjang(DetailPesananActivity.this,keranjangTampilList);


        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(DetailPesananActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtNamaPembeli = (TextView) findViewById(R.id.txtNamaPembeli);
        btnTerima = (Button) findViewById(R.id.btnTerima);
        btnTolak = (Button) findViewById(R.id.btnTolak);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtNamaPembeli.setText("Pembeli : ");
        txtPhone.setText("No. HP : ");

        if (status.equals("0")){
            txtStatus.setText("Menunggu Konfirmasi");
        }else if (status.equals("1")){
            txtStatus.setText("Dikonfirmasi , sedang dikirim");
            btnTerima.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
        }else if (status.equals("2")){
            txtStatus.setText("Ditolak");
            btnTerima.setVisibility(View.GONE);
            btnTolak.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ref.child("users").child(keyPembeli).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("displayName").getValue().toString();
                String phone  = dataSnapshot.child("phone").getValue().toString();
                txtNamaPembeli.setText(name);
                txtPhone.setText("HP : "+phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refSayur.child("order_detail").addValueEventListener(new ValueEventListener() {
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

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!status.equals("1")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananActivity.this);
                    builder.setMessage("Terima Order ini?");
                    builder.setCancelable(false);

                    listener = new DialogInterface.OnClickListener()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == DialogInterface.BUTTON_POSITIVE){
                                //status diterima
                                ref.child("order").child(keyOrder).child("status").setValue("1");

                                Intent i = new Intent(getApplicationContext(),ListPesananPenjual.class);
                                startActivity(i);
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
            }
        });
        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!status.equals("2")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananActivity.this);
                    builder.setMessage("Tolak Order ini?");
                    builder.setCancelable(false);

                    listener = new DialogInterface.OnClickListener()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == DialogInterface.BUTTON_POSITIVE){
                                //status diterima
                                ref.child("order").child(keyOrder).child("status").setValue("2");

                                Intent i = new Intent(getApplicationContext(),ListPesananPenjual.class);
                                startActivity(i);
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
            }
        });

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
