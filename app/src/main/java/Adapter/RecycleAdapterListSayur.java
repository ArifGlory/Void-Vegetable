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
    public static List<String> list_harga = new ArrayList();
    public static List<String> list_deviceID = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterListSayur(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();




    }


    @Override
    public RecycleViewHolderListSayur onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_listsayur, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListSayur viewHolderChat = new RecycleViewHolderListSayur(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderListSayur holder, final int position) {

        Resources res = context.getResources();

      // holder.txtNamaMotor.setText(nama[position].toString());
        //holder.txtPlatNomor.setText(plat[position].toString());
        //holder.contentWithBackground.setGravity(Gravity.LEFT);
       holder.txtNamaSayur.setText("Sayur ");
       holder.txtHarga.setText("Rp. 1500");


        holder.txtHarga.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtNamaSayur.setOnClickListener(clicklistener);
        holder.relaList.setOnClickListener(clicklistener);
        holder.img_iconlistMotor.setOnClickListener(clicklistener);
        holder.toogleSayur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(context.getApplicationContext(), "Item diklik ke : "+position, Toast.LENGTH_SHORT).show();
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

       // return list_nama == null ? 0 : list_nama.size();
       return nama.length;

    }



}
