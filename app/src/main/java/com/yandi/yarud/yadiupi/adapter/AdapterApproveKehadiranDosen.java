package com.yandi.yarud.yadiupi.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.ApproveKehadiranDosenActivity;
import com.yandi.yarud.yadiupi.PemilihanKMActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.model.ModelRisalahMK;
import com.yandi.yarud.yadiupi.network.GetTokenUPI;

import java.util.List;

public class AdapterApproveKehadiranDosen extends RecyclerView.Adapter<AdapterApproveKehadiranDosen.HolderData> {
    private List<ModelRisalahMK> item;
    private Context context;
    public AdapterApproveKehadiranDosen(Context context, List<ModelRisalahMK> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterApproveKehadiranDosen.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_approve_kehadiran_dosen,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApproveKehadiranDosen.HolderData holder, int position) {
        final ModelRisalahMK model = item.get(position);
        holder.textViewApproveKDid_rs.setText(model.getIdrs());
        holder.textViewApproveKDid_pn.setText(model.getIdpn());
        holder.textViewApproveKDpertemuan.setText(model.getPertemuan());
        holder.textViewApproveKDtopik.setText(model.getTopik());
        holder.textViewApproveKDsubtopik.setText(model.getSubtopik());
        holder.textViewApproveKDsesuai.setText(model.getSesuai());
        holder.textViewApproveKDwaktu.setText(model.getWaktu());
        holder.textViewApproveKDuser_id.setText(model.getUserid());
        holder.textViewApproveKDapprove.setText(model.getApprove());
        holder.textViewApproveKDtgl.setText(model.getTgl());
        holder.textViewApproveKDfinish.setText(model.getFinish());
        holder.textViewApproveKDtgl_finish.setText(model.getTgl_finish());
        holder.buttonApproveKDKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setIcon(R.drawable.icon_info)
                        .setTitle("Konfirmasi")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GetTokenUPI getTokenUPI = new GetTokenUPI(context,"PostApproveKM");
                                ApproveKehadiranDosenActivity approveKehadiranDosenActivity = (ApproveKehadiranDosenActivity) context;
                                getTokenUPI.approveKM(approveKehadiranDosenActivity.getUsername(),approveKehadiranDosenActivity.getPassword(),model.getIdrs());
                            }
                        })
                        .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button no = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                Button yes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                no.setTextColor(Color.rgb(143, 4, 4));
                yes.setTextColor(Color.rgb(29,145,36));
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        private TextView textViewApproveKDid_rs, textViewApproveKDid_pn, textViewApproveKDpertemuan,
                textViewApproveKDtopik, textViewApproveKDsubtopik, textViewApproveKDsesuai, textViewApproveKDwaktu,
                textViewApproveKDuser_id, textViewApproveKDapprove, textViewApproveKDtgl, textViewApproveKDfinish,
                textViewApproveKDtgl_finish;
        private Button buttonApproveKDKonfirmasi;
        HolderData(View itemView) {
            super(itemView);
            textViewApproveKDid_rs = itemView.findViewById(R.id.TextViewApproveKDid_rs);
            textViewApproveKDid_pn = itemView.findViewById(R.id.TextViewApproveKDid_pn);
            textViewApproveKDpertemuan = itemView.findViewById(R.id.TextViewApproveKDpertemuan);
            textViewApproveKDtopik = itemView.findViewById(R.id.TextViewApproveKDtopik);
            textViewApproveKDsubtopik = itemView.findViewById(R.id.TextViewApproveKDsubtopik);
            textViewApproveKDsesuai = itemView.findViewById(R.id.TextViewApproveKDsesuai);
            textViewApproveKDwaktu = itemView.findViewById(R.id.TextViewApproveKDwaktu);
            textViewApproveKDuser_id = itemView.findViewById(R.id.TextViewApproveKDuser_id);
            textViewApproveKDapprove = itemView.findViewById(R.id.TextViewApproveKDapprove);
            textViewApproveKDtgl = itemView.findViewById(R.id.textViewApproveKDtgl);
            textViewApproveKDfinish = itemView.findViewById(R.id.textViewApproveKDfinish);
            textViewApproveKDtgl_finish = itemView.findViewById(R.id.textViewApproveKDtgl_finish);
            buttonApproveKDKonfirmasi = itemView.findViewById(R.id.ButtonApproveKDKonfirmasi);
        }
    }
}
