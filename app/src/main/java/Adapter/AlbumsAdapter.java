package Adapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import Kelas.Album;
import Kelas.Keranjang;
import Kelas.SharedVariable;
import myproject.avoid.FragmentKeranjang;
import myproject.avoid.ListSayurActivity;
import myproject.avoid.ListSayurPembeli;
import myproject.avoid.PesananActivity;
import myproject.avoid.R;
import myproject.avoid.TambahSayurActivity;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_status = new ArrayList();
    public static List<String> list_harga = new ArrayList();
    public static List<String> list_key = new ArrayList();
    public static List<String> list_jml = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,jmlSayur;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            jmlSayur = (TextView) view.findViewById(R.id.jmlSayur);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public AlbumsAdapter(final Context mContext) {
        this.mContext = mContext;
        this.albumList = albumList;
        Firebase.setAndroidContext(this.mContext);
        FirebaseApp.initializeApp(mContext.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        refUser = ref.child("users");
        fAuth = FirebaseAuth.getInstance();
        ref.child("psayur").child(ListSayurPembeli.keyPSayur).child("sayurList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_downloadURL.clear();
                list_harga.clear();
                list_nama.clear();
                list_key.clear();
                list_status.clear();
                list_jml.clear();

                ListSayurPembeli.progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String key = child.getKey();
                    String namaSayur = child.child("namaSayur").getValue().toString();
                    String statusSayur = child.child("statusSayur").getValue().toString();
                    String harga = child.child("harga").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();
                    String jml = (String) child.child("jumlahSayur").getValue();

                    if (statusSayur.equals("on")){
                        list_status.add(statusSayur);
                        list_nama.add(namaSayur);
                        list_key.add(key);
                        list_downloadURL.add(downloadURL);
                        list_harga.add(harga);
                        list_jml.add(jml);
                    }


                }
                ListSayurPembeli.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.title.setText(list_nama.get(position).toString());
        holder.count.setText("Rp. "+list_harga.get(position).toString());
        holder.jmlSayur.setText("Jumlah : "+list_jml.get(position).toString()+" Unit");


        // loading album cover using Glide library
        Glide.with(mContext).load(list_downloadURL.get(position).toString())
                .into(holder.thumbnail);

        holder.title.setTag(holder);
        holder.count.setTag(holder);
        holder.jmlSayur.setTag(holder);

        holder.title.setOnClickListener(clickListener);
        holder.count.setOnClickListener(clickListener);
        holder.jmlSayur.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            MyViewHolder viewHolder = (MyViewHolder) view.getTag();
            final int position = viewHolder.getPosition();

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
            alertDialogBuilder.setCancelable(false).setPositiveButton("Pesan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (SharedVariable.idPenjualAktifCart.equals("off")
                            || SharedVariable.idPenjualAktifCart.equals(ListSayurPembeli.keyPSayur)){
                        Keranjang keranjang = new Keranjang(fAuth.getCurrentUser().getUid(),
                                ListSayurPembeli.keyPSayur,
                                list_key.get(position).toString(),
                                editText.getText().toString());
                        String key = ref.child("keranjang").push().getKey();
                        ref.child("keranjang").child(key).setValue(keranjang);
                        FragmentKeranjang.status = "1";
                        SharedVariable.idPenjualAktifCart = ListSayurPembeli.keyPSayur;
                        Toast.makeText(mContext.getApplicationContext(), "Berhasil masuk ke keranjang", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext.getApplicationContext(), "Tidak bisa melakukan pembelian, mohon selesaikan pembelian anda pada penjual sebelumnya", Toast.LENGTH_SHORT).show();
                    }


                }
            }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();


        }
    };


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
        return list_nama == null ? 0 : list_nama.size();
    }

}
