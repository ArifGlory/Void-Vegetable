package Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import Kelas.Keranjang;
import Kelas.KeranjangTampil;
import myproject.avoid.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterKeranjang extends RecyclerView.Adapter<AdapterKeranjang.MyViewHolder> {

    private Context mContext;
    private List<KeranjangTampil> keranjangTampilList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNamaSayur, txtHarga,txtJumlah;
        public ImageView thumbnail;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            txtNamaSayur = (TextView) view.findViewById(R.id.txt_namaSayur);
            txtHarga = (TextView) view.findViewById(R.id.txt_Harga);
            txtJumlah = (TextView) view.findViewById(R.id.txt_jumlah);
            thumbnail = (ImageView) view.findViewById(R.id.img_menu);
            cv_main = (CardView) view.findViewById(R.id.cv_main);
        }
    }


    public AdapterKeranjang(Context mContext, List<KeranjangTampil> keranjangTampilList) {
        this.mContext = mContext;
        this.keranjangTampilList = keranjangTampilList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_rv_keranjang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        KeranjangTampil keranjangTampil = keranjangTampilList.get(position);

        holder.txtNamaSayur.setText(keranjangTampil.getNamaSayur());
        holder.txtHarga.setText("Rp. "+keranjangTampil.getHargaSayur());
        holder.txtJumlah.setText(keranjangTampil.getJumlah());

        Glide.with(mContext)
                .load(keranjangTampil.getUrlGambar())
                .into(holder.thumbnail);

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();
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
