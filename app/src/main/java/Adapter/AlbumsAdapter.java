package Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import Kelas.SharedVariable;
import myproject.avoid.ListSayurActivity;
import myproject.avoid.ListSayurPembeli;
import myproject.avoid.R;

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
    public static List<String> list_downloadURL = new ArrayList();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
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

                ListSayurPembeli.progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String key = child.getKey();
                    String namaSayur = child.child("namaSayur").getValue().toString();
                    String statusSayur = child.child("statusSayur").getValue().toString();
                    String harga = child.child("harga").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();

                    if (statusSayur.equals("on")){
                        list_status.add(statusSayur);
                        list_nama.add(namaSayur);
                        list_key.add(key);
                        list_downloadURL.add(downloadURL);
                        list_harga.add(harga);
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

        // loading album cover using Glide library
        Glide.with(mContext).load(list_downloadURL.get(position).toString())
                .into(holder.thumbnail);


    }


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
