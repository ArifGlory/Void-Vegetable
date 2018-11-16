package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import myproject.avoid.R;

/**
 * Created by fujimiya on 10/8/18.
 */




public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private ArrayList<String> Faktur;
    private ArrayList<String> Tanggal;
    private ArrayList<String> Total;
    Context context;
    public AdapterOrder(Context getcontext, ArrayList<String> getFaktur, ArrayList<String> getTanggal, ArrayList<String> getTotal){
        Faktur =  getFaktur;
        Tanggal = getTanggal;
        Total = getTotal;
        context = getcontext;
    }

    @Override
    public AdapterOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_order, parent, false);
        AdapterOrder.ViewHolder vh = new AdapterOrder.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterOrder.ViewHolder holder, final int position) {
        holder.Txt_Faktur.setText("FAKTUR/"+"-LKWIDNWpawk");
        holder.Txt_Tanggal.setText("Tanggal : "+"13 November 2018");
        holder.Txt_Harga.setText("Rp. 4000");

        holder.Txt_Faktur.setText("FAKTUR/"+Faktur.get(position));
        holder.Txt_Tanggal.setText("Tanggal : "+Tanggal.get(position));
        holder.Txt_Harga.setText(Total.get(position));
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.putExtra("faktur", Faktur.get(position));
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return Faktur.size();
     //  return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_Faktur;
        TextView Txt_Tanggal;
        TextView Txt_Harga;
        CardView cvMain;
        public ViewHolder(View itemView) {
            super(itemView);
            Txt_Faktur = itemView.findViewById(R.id.txt_faktur);
            Txt_Tanggal = itemView.findViewById(R.id.txt_tanggal);
            Txt_Harga = itemView.findViewById(R.id.total_pesanan);
            cvMain = (CardView) itemView.findViewById(R.id.cv_main);

        }
    }


}
