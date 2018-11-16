package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import myproject.avoid.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterDetailPesanan extends RecyclerView.Adapter<RecycleViewHoldePesanan> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_harga = new ArrayList();
    public static List<String> list_jumlah = new ArrayList();
    public static List<String> list_key = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterDetailPesanan(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

    }



    @Override
    public RecycleViewHoldePesanan onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_detailpesanan, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHoldePesanan viewHolderChat = new RecycleViewHoldePesanan(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(RecycleViewHoldePesanan holder, final int position) {

        Resources res = context.getResources();


       holder.txtNamaSayur.setText("sayur kangkung");
       holder.txtHarga.setText("Rp. 4000");
        holder.txtJumlahPesan.setText("2"+" unit");
        //pake library glide buat load gambar dari URL
        Glide.with(context.getApplicationContext())
                .load(R.drawable.kangkung)
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_iconlistMotor);


        holder.txtHarga.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtNamaSayur.setOnClickListener(clicklistener);
        holder.relaList.setOnClickListener(clicklistener);


        holder.txtNamaSayur.setTag(holder);
        holder.txtHarga.setTag(holder);
        holder.relaList.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHoldePesanan vHolder = (RecycleViewHoldePesanan) v.getTag();
            int position = vHolder.getPosition();
           // Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();

        }
    };



    public int getItemCount() {

     //  return list_nama == null ? 0 : list_nama.size();
        return 1;

    }



}
