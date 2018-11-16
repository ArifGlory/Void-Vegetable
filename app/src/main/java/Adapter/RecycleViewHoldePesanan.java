package Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import myproject.avoid.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHoldePesanan extends RecyclerView.ViewHolder {

    public TextView txtNamaSayur,txtHarga,txtJumlahPesan,txtRating;
    public ImageView img_iconlistMotor;
    public CardView cardlist_item;
    public RelativeLayout relaList;

    public RecycleViewHoldePesanan(View itemView) {
        super(itemView);

        txtNamaSayur = (TextView) itemView.findViewById(R.id.txt_namaMotor);
        txtHarga = (TextView) itemView.findViewById(R.id.txt_platNomor);
        img_iconlistMotor = (ImageView) itemView.findViewById(R.id.img_iconlistMotor);
        cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
        relaList = (RelativeLayout) itemView.findViewById(R.id.relaList);
        txtJumlahPesan = (TextView) itemView.findViewById(R.id.txtJumlahPesan);

    }
}
