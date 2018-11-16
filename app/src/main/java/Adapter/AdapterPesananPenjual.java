package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Kelas.Order;
import myproject.avoid.DetailOrderActivity;
import myproject.avoid.DetailPesananActivity;
import myproject.avoid.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AdapterPesananPenjual extends RecyclerView.Adapter<AdapterPesananPenjual.MyViewHolder> {

    private Context mContext;
    private List<Order> orderList;
    private List<String> keyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_Faktur;
        TextView Txt_Tanggal;
        TextView Txt_Harga,Txt_Status;
        public CardView cv_main;

        public MyViewHolder(View view) {
            super(view);
            Txt_Faktur = (TextView) view.findViewById(R.id.txt_faktur);
            Txt_Tanggal = (TextView) view.findViewById(R.id.txt_tanggal);
            Txt_Harga = (TextView) view.findViewById(R.id.txt_total);
            Txt_Status = (TextView) view.findViewById(R.id.txt_status);
            cv_main = (CardView) view.findViewById(R.id.cv_main);
        }
    }


    public AdapterPesananPenjual(Context mContext, List<Order> orderList, List<String> keyList) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.keyList = keyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist_listpesanan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Order order  = orderList.get(position);
        String status = order.getStatus();
        if (status.equals("0")){
            status = "Menunggu konfirmasi";
        }else if (status.equals("1")){
            status = "Pesanan diterima, sedang diproses";
        }else if (status.equals("2")){
            status = "Pesanan ditolak";
        }
        int posisiPesanan = position + 1;
        holder.Txt_Faktur.setText("Pesanan - "+posisiPesanan);
        holder.Txt_Tanggal.setText("Tanggal : "+order.getTanggal());
        holder.Txt_Harga.setText("Total Bayar : Rp. "+order.getTotal());
        holder.Txt_Status.setText(status);


        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext.getApplicationContext(), "Di klik", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext.getApplicationContext(), DetailPesananActivity.class);
                intent.putExtra("tanggal",order.getTanggal());
                intent.putExtra("total",order.getTotal());
                intent.putExtra("status",order.getStatus());
                intent.putExtra("keyOrder",keyList.get(position).toString());
                intent.putExtra("keyPembeli",order.getUidPembeli());
                mContext.startActivity(intent);
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
        return orderList.size();
    }
}
