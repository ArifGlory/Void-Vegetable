package Fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import Kelas.SharedVariable;
import myproject.avoid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPSayur extends Fragment {


    public FragmentPSayur() {
        // Required empty public constructor
    }

    public static ToggleButton toogle_status;
    TextView txtNotif;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusPsayur;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home_psayur, container, false);
        Firebase.setAndroidContext(getActivity());
        Firebase.setAndroidContext(this.getActivity());
        FirebaseApp.initializeApp(this.getActivity());
        ref = FirebaseDatabase.getInstance().getReference();


        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        toogle_status = view.findViewById(R.id.toggleButton1);
        txtNotif = view.findViewById(R.id.txtNotif);
        txtNamaPSayur = view.findViewById(R.id.txtNamaPSayur);

        txtNamaPSayur.setText(SharedVariable.nama);
        txtNotif.setText("Status : "+SharedVariable.statusPSayur);

        if (SharedVariable.statusPSayur.equals("on")){
            toogle_status.setChecked(true);
        }else {
            toogle_status.setChecked(false);
        }

        toogle_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (toogle_status.isChecked()) {
                    ref.child("psayur").child(SharedVariable.userID).child("status").setValue("on");
                    SharedVariable.statusPSayur = "on";
                    Toast.makeText(getActivity(), "Mode Berkendara diaktifkan, jangan lupa untuk menonaktifkan mode berkendara" +
                            "ketika selesai. ", Toast.LENGTH_SHORT).show();
                } else {
                    ref.child("psayur").child(SharedVariable.userID).child("status").setValue("off");
                    SharedVariable.statusPSayur = "off";
                    Toast.makeText(getActivity(), "Mode Berkendara dinonaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });






        return view;
    }




}
