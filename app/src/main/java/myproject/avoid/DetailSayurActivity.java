package myproject.avoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import Kelas.SharedVariable;

public class DetailSayurActivity extends AppCompatActivity {

    ImageView imgBrowse;
    EditText etNama,etHarga,etJumlah;
    Button btnUpload;
    public static ProgressBar progressBar;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    FirebaseUser fbUser;
    Uri uri;
    private Boolean isFabOpen = false;
    DialogInterface.OnClickListener listener;
    Spinner spSatuan;
    String satuan;

    FloatingActionButton fabSetting,fabEdit,fabDelete;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private String namaSayur,hargaSayur,jmlSayur,downloadURL,keySayur;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sayur);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(DetailSayurActivity.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }

        i = getIntent();
        namaSayur = i.getStringExtra("nama");
        hargaSayur = i.getStringExtra("harga");
        downloadURL = i.getStringExtra("url");
        keySayur = i.getStringExtra("key");
        jmlSayur = i.getStringExtra("jumlah");

        ref = FirebaseDatabase.getInstance().getReference();

        imgBrowse = (ImageView) findViewById(R.id.img_browse);
        etNama = (EditText) findViewById(R.id.userEmailId);
        etHarga = (EditText) findViewById(R.id.etHargaSayur);
        btnUpload = (Button) findViewById(R.id.signUpBtn);
        etJumlah = (EditText) findViewById(R.id.etJumlahSayur);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spSatuan = (Spinner) findViewById(R.id.sp_satuan);

        etHarga.setEnabled(false);
        etNama.setEnabled(false);
        btnUpload.setEnabled(false);
        etJumlah.setEnabled(false);
        imgBrowse.setEnabled(false);
        spSatuan.setEnabled(false);

        etNama.setText(namaSayur);
        etHarga.setText(hargaSayur);
        etJumlah.setText(jmlSayur);
        Glide.with(getApplicationContext())
                .load(downloadURL)
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgBrowse);

        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabSetting = (FloatingActionButton) findViewById(R.id.fabSetting);
        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFB();
            }
        });
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNama.setEnabled(true);
                etJumlah.setEnabled(true);
                etHarga.setEnabled(true);
                btnUpload.setText("Ubah");
                btnUpload.setEnabled(true);
                imgBrowse.setEnabled(true);
                animateFB();
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSayurActivity.this);
                builder.setMessage("Anda yakin ingin menghapus Sayur ini ?");
                builder.setCancelable(false);

                listener = new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).setValue(null);
                            i = new Intent(getApplicationContext(),ListSayurActivity.class);
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
        });

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {*/
                // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("image/*");
                //startActivityForResult(intent, RC_IMAGE_GALLERY);
                // }

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailSayurActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkValidation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (uri != null || !namaSayur.equals(etNama.getText().toString()) || !jmlSayur.equals(etJumlah.getText().toString())
                || !hargaSayur.equals(etHarga.getText().toString())  ){
            i = new Intent(getApplicationContext(),ListSayurActivity.class);
            startActivity(i);
        }
        super.onBackPressed();
    }

    public void animateFB(){

        if(isFabOpen){

            fabSetting.startAnimation(rotate_backward);
            fabEdit.startAnimation(fab_close);
            fabDelete.startAnimation(fab_close);
            fabEdit.setClickable(false);
            fabDelete.setClickable(false);
            isFabOpen = false;
            Log.d("fab", "close");

        } else {

            fabSetting.startAnimation(rotate_forward);
            fabEdit.startAnimation(fab_open);
            fabDelete.startAnimation(fab_open);
            fabEdit.setClickable(true);
            fabDelete.setClickable(true);
            isFabOpen = true;
            Log.d("fab","open");

        }
    }

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        etHarga.setEnabled(false);
        etNama.setEnabled(false);
        imgBrowse.setEnabled(false);
        etJumlah.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        etHarga.setEnabled(true);
        etNama.setEnabled(true);
        imgBrowse.setEnabled(true);
        etJumlah.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNama.getText().toString();
        String getJumlah = etJumlah.getText().toString();
        String gethHarga = etHarga.getText().toString();
        // matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                || getJumlah.equals("") || getJumlah.length() == 0
                || gethHarga.equals("") || gethHarga.length() == 0) {

            customToast("Semua Field harus diisi");
            hidupkanKomponen();
        }else if (uri == null){
            //ke proses untuk update tapi tanpa ganti URl Gambar
            updateWithoutChangeURI(etNama.getText().toString(),etHarga.getText().toString(),etJumlah.getText().toString());
        }else {

            uploadGambar(uri);

        }
    }

    private void updateWithoutChangeURI(String nama,String harga,String jumlah){
        progressBar.setVisibility(View.VISIBLE);
        ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("namaSayur").setValue(nama);
        ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("harga").setValue(harga);
        ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("jumlahSayur").setValue(jumlah);

        customToast("Berhasil Diubah");
        progressBar.setVisibility(View.GONE);
        etJumlah.setText(jumlah);
        etHarga.setText(harga);
        etNama.setText(nama);

        etNama.setEnabled(false);
        etJumlah.setEnabled(false);
        etHarga.setEnabled(false);
        btnUpload.setEnabled(false);
        btnUpload.setText("......");
        imgBrowse.setEnabled(false);
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
                Toast.makeText(DetailSayurActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(DetailSayurActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // save image to database

                String nm = etNama.getText().toString();
                String jml = etJumlah.getText().toString();
                String hrga = etHarga.getText().toString();

                ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("namaSayur").setValue(etNama.getText().toString());
                ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("harga").setValue(etHarga.getText().toString());
                ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("downloadUrl").setValue(downloadUrl.toString());
                ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(keySayur).child("jumlahSayur").setValue(etJumlah.getText().toString());

                customToast("Berhasil Diubah");
                progressBar.setVisibility(View.GONE);
                etJumlah.setText(jml);
                etHarga.setText(hrga);
                etNama.setText(nm);

                etNama.setEnabled(false);
                etJumlah.setEnabled(false);
                etHarga.setEnabled(false);
                btnUpload.setEnabled(false);
                imgBrowse.setEnabled(false);
                btnUpload.setText("......");
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            uri = data.getData();
            final String tipe = GetMimeType(DetailSayurActivity.this,uri);
            //Toast.makeText(TambahMenuActivity.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            imgBrowse.setImageURI(uri);
        }
    }

    public  void customToast(String s) {
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
