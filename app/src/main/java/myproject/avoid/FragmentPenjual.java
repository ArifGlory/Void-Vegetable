package myproject.avoid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import myproject.avoid.R;


public class FragmentPenjual extends Fragment {

    public static ToggleButton toogle_status;
    TextView txtNotif,txtNamaPenjual;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    UserPreference mUserpref;
    Intent i;
    DatabaseReference ref;
    private String status;

    public FragmentPenjual() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(getActivity());
        FirebaseApp.initializeApp(getActivity());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserpref = new UserPreference(getActivity());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_psayur, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toogle_status = view.findViewById(R.id.toggleButton1);
        txtNotif = view.findViewById(R.id.txtNotif);
        txtNamaPenjual = view.findViewById(R.id.txtNamaPSayur);

        ref.child("psayur").child(SharedVariable.userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = (String) dataSnapshot.child("status").getValue();
                SharedVariable.statusPSayur = status;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("statusPsayur :",SharedVariable.statusPSayur);
        txtNamaPenjual.setText(SharedVariable.nama);
        if (SharedVariable.statusPSayur.equals("on")){
            toogle_status.setChecked(true);
        }else {
            toogle_status.setChecked(false);
        }

        toogle_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (toogle_status.isChecked()){
                    Toast.makeText(getActivity(), "Mode Berjualan diaktifkan, jangan lupa untuk menonaktifkan mode berjualan " +
                            "ketika selesai. ", Toast.LENGTH_LONG).show();

                    ref.child("psayur").child(SharedVariable.userID).child("status").setValue("on");
                    txtNotif.setText("Aktif");
                }else {
                    Toast.makeText(getActivity(), "Mode Berjualan dimatikan", Toast.LENGTH_SHORT).show();

                    ref.child("psayur").child(SharedVariable.userID).child("status").setValue("off");
                    txtNotif.setText("Tidak Aktif");
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
