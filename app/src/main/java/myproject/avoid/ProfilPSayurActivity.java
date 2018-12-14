package myproject.avoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import Kelas.Sayur;
import Kelas.SharedVariable;

public class ProfilPSayurActivity extends AppCompatActivity {

    TextView txtNamaPenjual,txtEmail,txtPhone;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    FirebaseUser fbUser;
    ImageView imgProfil;
    public static ProgressBar progressBar;

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    DialogInterface.OnClickListener listener;
    Uri uri,file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_psayur);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(ProfilPSayurActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        txtNamaPenjual = (TextView) findViewById(R.id.txtNamaPSayur);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPhone = (TextView) findViewById(R.id.txtCall);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgProfil = (ImageView) findViewById(R.id.imgProfil);

        txtNamaPenjual.setText(SharedVariable.nama);
        txtEmail.setText(fbUser.getEmail().toString());

        ref.child("psayur").child(SharedVariable.userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nope = dataSnapshot.child("phone").getValue().toString();
                String urlFoto = dataSnapshot.child("foto").getValue().toString();
                if (!urlFoto.equals("no")){
                    Glide.with(getApplicationContext())
                            .load(urlFoto)
                            .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgProfil);
                }
                txtPhone.setText(nope);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfilPSayurActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);

                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            uri = data.getData();
            final String tipe = GetMimeType(ProfilPSayurActivity.this,uri);
            //Toast.makeText(ProfilPSayurActivity.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();
            uploadGambar(uri);
            imgProfil.setImageURI(uri);
        }
        else if (requestCode == 100 && resultCode == RESULT_OK){
            uri = file;
            imgProfil.setImageURI(uri);
        }
    }

    private void uploadGambar(final Uri uri){

        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference userRef = imagesRef.child(fbUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = fbUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        UploadTask uploadTask = fileRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(ProfilPSayurActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(ProfilPSayurActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // save image to database
                ref.child("psayur").child(SharedVariable.userID).child("foto").setValue(downloadUrl.toString());
                imgProfil.setImageURI(uri);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RC_IMAGE_GALLERY);
            }
        }
    }

    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
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
