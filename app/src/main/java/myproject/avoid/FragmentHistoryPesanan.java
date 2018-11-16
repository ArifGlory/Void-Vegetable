package myproject.avoid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Adapter.AdapterOrder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistoryPesanan extends Fragment {


    public FragmentHistoryPesanan() {
        // Required empty public constructor
    }

    RecyclerView recyclerHistoryPesan;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusPsayur;
    AdapterOrder adapter;
    private ArrayList<String> Faktur = new ArrayList<String>();
    private ArrayList<String> Tanggal = new ArrayList<String>();
    private ArrayList<String> Total = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pesanan_pembeli, container, false);
        Firebase.setAndroidContext(getActivity());
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerHistoryPesan = (RecyclerView) view.findViewById(R.id.recycler_pesanan_pembeli);
        recyclerHistoryPesan.setHasFixedSize(true);
        recyclerHistoryPesan.setLayoutManager(new GridLayoutManager(view.getContext(), 1));

        adapter = new AdapterOrder(getActivity(),Faktur,Tanggal,Total);
        recyclerHistoryPesan.setAdapter(adapter);

        Log.d("size adapter : ",""+adapter.getItemCount());


        return view;
    }




}
