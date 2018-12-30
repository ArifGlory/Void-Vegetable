package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.List;

import Kelas.KeranjangTampil;
import Kelas.SharedVariable;
import myproject.avoid.DetailPesananActivity;
import myproject.avoid.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterKeranjangPenjual extends RecyclerView.Adapter<AdapterKeranjangPenjual.MyViewHolder> {

    private Context mContext;
    private List<KeranjangTampil> keranjangTampilList;
    DialogInterface.OnClickListener listener;
    DatabaseReference ref,refSayur;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private int total = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNamaSayur, txtHarga,txtJumlah;
        public ImageView thumbnail;
        public CardView cv_main;
        public ImageButton ibCancel;

        public MyViewHolder(View view) {
            super(view);
            txtNamaSayur = (TextView) view.findViewById(R.id.txt_namaSayur);
            txtHarga = (TextView) view.findViewById(R.id.txt_Harga);
            txtJumlah = (TextView) view.findViewById(R.id.txt_jumlah);
            thumbnail = (ImageView) view.findViewById(R.id.img_menu);
            cv_main = (CardView) view.findViewById(R.id.cv_main);
            ibCancel = (ImageButton) view.findViewById(R.id.btnCancel);
        }
    }


    public AdapterKeranjangPenjual(Context mContext, List<KeranjangTampil> keranjangTampilList) {
        this.mContext = mContext;
        this.keranjangTampilList = keranjangTampilList;
        Firebase.setAndroidContext(mContext);
        FirebaseApp.initializeApp(mContext);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_rv_detailpesanan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final KeranjangTampil keranjangTampil = keranjangTampilList.get(position);

        holder.txtNamaSayur.setText(keranjangTampil.getNamaSayur());
        holder.txtHarga.setText("Rp. "+keranjangTampil.getHargaSayur());
        holder.txtJumlah.setText("Jumlah Pesan : "+keranjangTampil.getJumlah());

        if (DetailPesananActivity.status.equals("1")){
            holder.ibCancel.setVisibility(View.GONE);
        }else if (DetailPesananActivity.status.equals("2")){
            holder.ibCancel.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(keranjangTampil.getUrlGambar())
                .into(holder.thumbnail);

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Apakah anda ingin menghapus pesanan sayur ini ?");
                builder.setCancelable(false);

                listener = new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){

                            ref.child("order_detail").child(keranjangTampil.idOrder).setValue(null);

                            //ubah total harga
                            final int totalHargaDihapus = Integer.parseInt(keranjangTampil.getJumlah().toString()) * Integer.parseInt(keranjangTampil.getHargaSayur().toString());

                           int total = Integer.parseInt(DetailPesananActivity.total);
                           total = total - totalHargaDihapus;
                            String totalNew = String.valueOf(total);
                            ref.child("order").child(DetailPesananActivity.keyOrder).child("total").setValue(totalNew);

                            Toast.makeText(mContext.getApplicationContext(), "Berhasil Dihapus ! : ", Toast.LENGTH_SHORT).show();


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

    }


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
        return keranjangTampilList.size();
    }
}
