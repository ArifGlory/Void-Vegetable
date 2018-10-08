package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import Kelas.SharedVariable;
import myproject.avoid.ListSayurActivity;
import myproject.avoid.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterListSayur extends RecyclerView.Adapter<RecycleViewHolderListSayur> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_status = new ArrayList();
    public static List<String> list_harga = new ArrayList();
    public static List<String> list_key = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String statusSayur;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterListSayur(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("psayur").child(SharedVariable.userID).child("sayurList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_downloadURL.clear();
                list_harga.clear();
                list_nama.clear();
                list_key.clear();
                list_status.clear();

                ListSayurActivity.progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String key = child.getKey();
                    String namaSayur = child.child("namaSayur").getValue().toString();
                    String statusSayur = child.child("statusSayur").getValue().toString();
                    String harga = child.child("harga").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();

                    list_status.add(statusSayur);
                    list_nama.add(namaSayur);
                    list_key.add(key);
                    list_downloadURL.add(downloadURL);
                    list_harga.add(harga);
                }
                ListSayurActivity.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public RecycleViewHolderListSayur onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_listsayur, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListSayur viewHolderChat = new RecycleViewHolderListSayur(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolderListSayur holder, final int position) {

        Resources res = context.getResources();

      // holder.txtNamaMotor.setText(nama[position].toString());
        //holder.txtPlatNomor.setText(plat[position].toString());
        //holder.contentWithBackground.setGravity(Gravity.LEFT);
       holder.txtNamaSayur.setText(list_nama.get(position).toString());
       holder.txtHarga.setText("Rp. "+list_harga.get(position).toString());
        statusSayur = list_status.get(position).toString();
        if (statusSayur.equals("off")){
            holder.toogleSayur.setChecked(false);
        }else {
            holder.toogleSayur.setChecked(true);
        }
        Glide.with(context.getApplicationContext())
                .load(list_downloadURL.get(position).toString())
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_iconlistMotor);

        holder.txtHarga.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtNamaSayur.setOnClickListener(clicklistener);
        holder.relaList.setOnClickListener(clicklistener);
        holder.img_iconlistMotor.setOnClickListener(clicklistener);
        holder.toogleSayur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (list_status.get(position).toString().equals("off")){
                   ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(list_key.get(position).toString())
                           .child("statusSayur").setValue("on");
               }else {
                   ref.child("psayur").child(SharedVariable.userID).child("sayurList").child(list_key.get(position).toString())
                           .child("statusSayur").setValue("off");
               }
            }
        });


        holder.txtNamaSayur.setTag(holder);
        holder.txtHarga.setTag(holder);
        holder.img_iconlistMotor.setTag(holder);
        holder.relaList.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHolderListSayur vHolder = (RecycleViewHolderListSayur) v.getTag();
            int position = vHolder.getPosition();
           // Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();

        }
    };



    public int getItemCount() {

        return list_nama == null ? 0 : list_nama.size();
      // return nama.length;

    }



}
