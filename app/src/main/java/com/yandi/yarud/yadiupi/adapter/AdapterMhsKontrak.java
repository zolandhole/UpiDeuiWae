package com.yandi.yarud.yadiupi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.ApproveKehadiranDosenActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.model.ModelMhsKontrak;

import java.util.List;

public class AdapterMhsKontrak extends RecyclerView.Adapter<AdapterMhsKontrak.HolderData> {
    private Context context;
    private List<ModelMhsKontrak> item;
    public AdapterMhsKontrak(Context context, List<ModelMhsKontrak> item){
        this.context = context;
        this.item = item;
    }
    @NonNull
    @Override
    public AdapterMhsKontrak.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mahasiswa_kontrak,parent,false);
        return new HolderData(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterMhsKontrak.HolderData holder, int position) {
        final Intent intent = new Intent(context, ApproveKehadiranDosenActivity.class);
        final ModelMhsKontrak model = item.get(position);
        holder.textViewMhsKontrakIDMK.setText(model.getIDMK());
        holder.textViewMhsKontrakKODEMK.setText(model.getKODEMK());
        holder.textViewMhsKontrakNAMAMK.setText(model.getNAMAMK());
        holder.textViewMhsKontrakSKS.setText(model.getSKS()+" SKS");

        if (model.getKM().equals("1")){
            holder.mhsKontrakview.setVisibility(View.VISIBLE);
            holder.textViewMhsKontrakKM.setText("Anda sebagai KM");
            if (model.getNEEDAPPROVE().equals("1")){
                holder.textViewMhsKontrakNEEDAPPROVE.setVisibility(View.VISIBLE);
                holder.buttonMhsKontrakApprove.setVisibility(View.VISIBLE);

                holder.textViewMhsKontrakNEEDAPPROVE.setText(model.getNEEDAPPROVE() + " Risalah perlu konfirmasi");
                holder.buttonMhsKontrakApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("IDMK",model.getIDMK());
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.textViewMhsKontrakNEEDAPPROVE.setVisibility(View.GONE);
                holder.buttonMhsKontrakApprove.setVisibility(View.GONE);
            }
        } else {
            holder.mhsKontrakview.setVisibility(View.GONE);
            holder.textViewMhsKontrakKM.setVisibility(View.GONE);
            holder.buttonMhsKontrakApprove.setVisibility(View.GONE);
            holder.textViewMhsKontrakNEEDAPPROVE.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return item.size();
    }
    class HolderData extends RecyclerView.ViewHolder{
        private TextView textViewMhsKontrakIDMK, textViewMhsKontrakKODEMK, textViewMhsKontrakNAMAMK, textViewMhsKontrakSKS, textViewMhsKontrakKM, textViewMhsKontrakNEEDAPPROVE;
        private Button buttonMhsKontrakApprove;
        private View mhsKontrakview;
        HolderData(View view) {
            super(view);
            textViewMhsKontrakIDMK = view.findViewById(R.id.MhsKontrakTextViewIDMK);
            textViewMhsKontrakKODEMK = view.findViewById(R.id.MhsKontrakTextViewKODEMK);
            textViewMhsKontrakNAMAMK = view.findViewById(R.id.TextViewMhsKontrakNAMAMK);
            textViewMhsKontrakSKS = view.findViewById(R.id.TextViewMhsKontrakSKS);
            textViewMhsKontrakKM = view.findViewById(R.id.MhsKontrakTextViewKM);
            textViewMhsKontrakNEEDAPPROVE = view.findViewById(R.id.MhsKontrakTextViewNEEDAPPROVE);
            buttonMhsKontrakApprove = view.findViewById(R.id.MhsKontrakButtonApprove);
            mhsKontrakview = view.findViewById(R.id.MhsKontrakview);
        }
    }
}
