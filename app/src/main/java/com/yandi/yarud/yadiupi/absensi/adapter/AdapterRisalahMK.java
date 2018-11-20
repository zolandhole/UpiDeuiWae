package com.yandi.yarud.yadiupi.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.absensi.PemilihanKMActivity;
import com.yandi.yarud.yadiupi.absensi.PresensiActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.RisalahMKActivity;
import com.yandi.yarud.yadiupi.absensi.model.ModelRisalahMK;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdapterRisalahMK extends RecyclerView.Adapter<AdapterRisalahMK.HolderData>{
    private List<ModelRisalahMK> item;
    private Context context;
    private String kodekls, namakelas;
    public AdapterRisalahMK(Context context, List<ModelRisalahMK> item, String kodekls, String namakelas){
        this.item = item;
        this.context = context;
        this.kodekls = kodekls;
        this.namakelas = namakelas;
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
        final Intent intentPemilihanKM = new Intent(context,PemilihanKMActivity.class);
        final Intent intentPresensi = new Intent(context,PresensiActivity.class);
        final ModelRisalahMK model = item.get(position);
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

        intentPemilihanKM.putExtra("IDRS",holder.idrsView.getText().toString());
        intentPemilihanKM.putExtra("IDMK", Objects.requireNonNull(holder.idpnView.getText().toString()));
        intentPemilihanKM.putExtra("NAMAKELAS",namakelas);
        intentPresensi.putExtra("IDRS",holder.idrsView.getText().toString());
        intentPresensi.putExtra("KODEKLS",kodekls);
        intentPresensi.putExtra("NAMAKELAS",namakelas);

        if (model.getFinish().equals("1")){
            holder.btnDetail.setText("Edit");
        } else {
            holder.btnDetail.setText("Presensi");
        }
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RisalahMKActivity risalah = (RisalahMKActivity) context;
                if(risalah.getStatusKM().equals("1")){
                    if (model.getApprove().equals("1")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.todoDialogLight);
                        builder.setIcon(R.drawable.icon_info)
                                .setTitle("Pertemuan sudah Approve KM")
                                .setMessage("Apakah anda akan merubah data presensi?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        context.startActivity(intentPresensi);
                                    }
                                })
                                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        AlertDialog alert1 = builder.create();
                        alert1.show();
                        Button yes = alert1.getButton(DialogInterface.BUTTON_POSITIVE);
                        yes.setTextColor(Color.rgb(29,145,36));
                    } else {
                        context.startActivity(intentPresensi);
                    }
                }else{
                    risalah.konfirmasiSetKM(intentPemilihanKM);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return item.size();
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

    public void setFilter(ArrayList<ModelRisalahMK> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
