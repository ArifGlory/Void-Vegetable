package myproject.avoid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Fragment.FragmentPSayur;
import Fragment.Home;
import Kelas.BaseActivity;
import Kelas.BaseDrawerActivity;
import Kelas.SharedVariable;
import Kelas.UserPreference;
import butterknife.BindView;

public class PSayurActivity extends BaseDrawerActivity{

    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    UserPreference mUserPref;
    Intent i;

    public static ToggleButton toogle_status;
    TextView txtNotif;
    public static ProgressBar progressBar;
    public static TextView txtNamaPSayur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psayur);


        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserPref = new UserPreference(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toogle_status = (ToggleButton) findViewById(R.id.toggleButton1);
        txtNotif = (TextView) findViewById(R.id.txtNotif);
        txtNamaPSayur = (TextView) findViewById(R.id.txtNamaPSayur);

        txtNamaPSayur.setText(SharedVariable.nama);
        txtNotif.setText("Status : "+SharedVariable.statusPSayur);


        toogle_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (toogle_status.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Mode Berkendara diaktifkan, jangan lupa untuk menonaktifkan mode berkendara" +
                            "ketika selesai. ", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Mode Berkendara dinonaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
