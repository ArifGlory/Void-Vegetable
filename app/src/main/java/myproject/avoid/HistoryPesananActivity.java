package myproject.avoid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterHistory;
import Adapter.AdapterKeranjang;
import Adapter.AdapterOrder;
import Kelas.KeranjangTampil;
import Kelas.Order;
import Kelas.SharedVariable;

public class HistoryPesananActivity extends AppCompatActivity {

    RecyclerView rvHistory;
    public static ProgressBar progressBar;
    DatabaseReference ref,refSayur,refHapus;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private List<Order> orderList;
    private List<String> keyList;
    AdapterHistory adapter;
    private String waktu;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pesanan);
        Firebase.setAndroidContext(HistoryPesananActivity.this);
        FirebaseApp.initializeApp(HistoryPesananActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();

        orderList = new ArrayList<>();
        keyList = new ArrayList<>();
        adapter = new AdapterHistory(HistoryPesananActivity.this,orderList,keyList);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rvHistory = (RecyclerView) findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(adapter);


        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderList.clear();
                keyList.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPembeli = child.child("uidPembeli").getValue().toString();
                    String key = child.getKey();

                    if (uidPembeli.equals(SharedVariable.userID)){
                        String status  = child.child("status").getValue().toString();
                        String tanggal = child.child("tanggal").getValue().toString();
                        String total = child.child("total").getValue().toString();
                        String uidPenjual = child.child("uidPenjual").getValue().toString();

                        order = new Order(status,
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),BerandaActivity.class);
        startActivity(i);
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
