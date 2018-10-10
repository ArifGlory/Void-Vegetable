package myproject.avoid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Kelas.SharedVariable;
import Kelas.UserPreference;
import Kelas.Utils;

public class MainActivity extends AppCompatActivity {

    EditText emailid, password;
    Button loginButton;
    TextView forgotPassword, signUp,loginPenjual;
    Intent i;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    ProgressBar progressBar;
    DialogInterface.OnClickListener listener;
    private String userID;
    UserPreference mUserPref;
    ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(MainActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        mUserPref = new UserPreference(this);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fbUser!=null){
            // User already signed in

            // get the FCM token
            String token = FirebaseInstanceId.getInstance().getToken();
            // save the user info in the database to users/UID/
            // we'll use the UID as part of the path
           /* User user = new User(fbUser.getUid(), fbUser.getDisplayName(), token);
            database.child("users").child(user.uid).setValue(user);*/

            // go to intent activity
            // Intent intent = new Intent(this, SplashActivity.class);
            // startActivity(intent);
        }

        //deklarasi variabel
        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.loginBtn);
        signUp = (TextView) findViewById(R.id.createAccount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginPenjual = (TextView) findViewById(R.id.loginPenjual);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // buat pindah ke register activiti
                i = new Intent(getApplicationContext(),RegisterAcitivy.class);
                startActivity(i);
            }
        });

        loginPenjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),LoginPSayur.class);
                startActivity(i);
            }
        });

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String bag = mUserPref.getBagian();
                customToast("bagian : "+bag);
            }
        });
    }

    private void matikanKomponen(){
        emailid.setEnabled(false);
        password.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
    }

    private  void hidupkanKomponen(){
        emailid.setEnabled(true);
        password.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
    }

    private void checkValidation() {
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();
        matikanKomponen();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {

            customToast("Email dan password harus diisi");
            hidupkanKomponen();
        }
        else if (!m.find()) {
            customToast("Email anda tidak valid");
            hidupkanKomponen();
        }
        else {
            doLogin(getEmailId,getPassword);

        }

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

    private void doLogin(final String email,String passwordUser){
        fAuth.signInWithEmailAndPassword(email,passwordUser).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    hidupkanKomponen();
                    customToast("Login Gagal, periksa kembali email dan password anda");
                    //Toast.makeText(getApplicationContext(), "Login Gagal, periksa kembali email dan password anda", Toast.LENGTH_LONG).show();
                }else{
                    // Successfully signed in
                    SharedVariable.nama = fAuth.getCurrentUser().getDisplayName();
                    SharedVariable.userID = fAuth.getCurrentUser().getUid();
                    // get the Firebase user
                    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

                    // get the FCM token
                    String token = FirebaseInstanceId.getInstance().getToken();
                    userID = fbUser.getUid();
                    mUserPref.setBagian("pembeli");
                    i = new Intent(MainActivity.this,SplashActivity.class);
                    startActivity(i);

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakan anda ingin keluar dari aplikasi ?");
        builder.setCancelable(false);

        listener = new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    finishAffinity();
                    System.exit(0);
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
