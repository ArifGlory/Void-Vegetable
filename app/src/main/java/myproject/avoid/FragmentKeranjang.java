package myproject.avoid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterKeranjang;
import Kelas.Keranjang;
import Kelas.KeranjangTampil;
import Kelas.SharedVariable;
import myproject.avoid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentKeranjang extends Fragment {


    public FragmentKeranjang() {
        // Required empty public constructor
    }

    TextView txtTotal;
    Button btnOrder;
    RecyclerView recyclerKeranjang;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;
    DatabaseReference ref,refSayur;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusPsayur;
    public static String status = "0";
    private List<KeranjangTampil> keranjangTampilList;
    private String jumlahPesan = "0";
    AdapterKeranjang adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_keranjang, container, false);
        Firebase.setAndroidContext(view.getContext());
        FirebaseApp.initializeApp(view.getContext());
        ref = FirebaseDatabase.getInstance().getReference();
        refSayur =  FirebaseDatabase.getInstance().getReference();

        keranjangTampilList = new ArrayList<>();
        adapter = new AdapterKeranjang(view.getContext(),keranjangTampilList);


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerKeranjang = (RecyclerView) view.findViewById(R.id.rv_main_keranjang);
        recyclerKeranjang.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerKeranjang.setHasFixedSize(true);
        recyclerKeranjang.setItemAnimator(new DefaultItemAnimator());
        recyclerKeranjang.setAdapter(adapter);

        btnOrder = (Button) view.findViewById(R.id.btnOrder);
        txtTotal = (TextView) view.findViewById(R.id.txt_total);

        ref.child("keranjang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String uidPembeli = child.child("uidPembeli").getValue().toString();

                    if (uidPembeli.equals(SharedVariable.userID)){

                        String idSayur = child.child("idSayur").getValue().toString();
                        String idPenjual = child.child("idPenjual").getValue().toString();
                        jumlahPesan = child.child("jumlah").getValue().toString();

                        refSayur.child("psayur").child(idPenjual).child("sayurList").child(idSayur).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String urlGambar = dataSnapshot.child("downloadUrl").getValue().toString();
                                String namaSayur = dataSnapshot.child("namaSayur").getValue().toString();
                                String hargaSayur = dataSnapshot.child("harga").getValue().toString();
                                String idSayur = dataSnapshot.child("idSayur").getValue().toString();

                                KeranjangTampil keranjangTampil = new KeranjangTampil(namaSayur,
                                        hargaSayur,
                                        urlGambar,
                                        jumlahPesan,
                                        "qwerty",
                                        idSayur);
                                keranjangTampilList.add(keranjangTampil);
                                adapter.notifyDataSetChanged();
                               Toast.makeText(getActivity(),namaSayur,Toast.LENGTH_SHORT).show();
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

        return view;
    }




}
