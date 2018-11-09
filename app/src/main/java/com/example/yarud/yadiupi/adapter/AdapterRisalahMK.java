package com.example.yarud.yadiupi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yarud.yadiupi.R;
import com.example.yarud.yadiupi.model.ModelRisalahMK;

import java.util.List;

public class AdapterRisalahMK extends RecyclerView.Adapter<AdapterRisalahMK.HolderData>{
    private List<ModelRisalahMK> item;
    private Context context;

    public AdapterRisalahMK(Context context, List<ModelRisalahMK> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_risalah_mk,parent,false);
        return new HolderData(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
//        final Intent intentPemilihanKM = new Intent(context,PemilihanKMActivity.class);
//        final Intent intentPresensi = new Intent(context,PresensiActivity.class);
        ModelRisalahMK model = item.get(position);
        holder.idrsView.setText(model.getIdrs());
        holder.idpnView.setText(model.getIdpn());
        holder.pertemuanView.setText(model.getPertemuan());
        holder.topikView.setText(model.getTopik());
        holder.subtopikView.setText(model.getSubtopik());

        if (model.getSesuai().equals("Y")){
            holder.sesuaiView.setText("Ya");
            holder.sesuaiView.setTextColor(Color.rgb(88, 164, 94));
        } else {
            holder.sesuaiView.setText("Tidak");
        }

        holder.waktuView.setText(model.getWaktu());
        holder.useridView.setText(model.getUserid());

        if (model.getApprove().equals("1")){
            holder.approveView.setText("Sudah Approve");
            holder.approveView.setTextColor(Color.rgb(88, 164, 94));
        } else {
            holder.approveView.setText("Belum Approve");
        }

//        intentPemilihanKM.putExtra("IDRS",holder.idrsView.getText().toString());
//        intentPemilihanKM.putExtra("IDMK", Objects.requireNonNull(holder.idpnView.getText().toString()));
//        intentPresensi.putExtra("IDRS",holder.idrsView.getText().toString());
//        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RisalahMKActivity risalah = (RisalahMKActivity) context;
//                if(risalah.getStatusKM().equals("1")){
//                    risalah.kePresensi(intentPresensi);
//                }else{
//                    risalah.kePresensi(intentPemilihanKM);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return item.size();
        //test
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView idrsView;
        TextView idpnView;
        TextView pertemuanView;
        TextView topikView;
        TextView subtopikView;
        TextView sesuaiView;
        TextView waktuView;
        TextView useridView;
        TextView approveView;
        Button btnDetail;

        HolderData(View view){
            super(view);

            idrsView = view.findViewById(R.id.textViewRisalahMKid_rs);
            idpnView =  view.findViewById(R.id.textViewRisalahMKid_pn);
            pertemuanView = view.findViewById(R.id.TextViewRisalahMKpertemuan);
            topikView =  view.findViewById(R.id.TextViewRisalahMKtopik);
            subtopikView = view.findViewById(R.id.TextViewRisalahMKsubtopik);
            sesuaiView =  view.findViewById(R.id.TextViewRisalahMKsesuai);
            waktuView = view.findViewById(R.id.TextViewRisalahMKwaktu);
            useridView = view.findViewById(R.id.textViewRisalahMKuser_id);
            approveView = view.findViewById(R.id.textViewRisalahMKapprove);
            btnDetail = view.findViewById(R.id.buttonPresensi);
        }
    }
}
