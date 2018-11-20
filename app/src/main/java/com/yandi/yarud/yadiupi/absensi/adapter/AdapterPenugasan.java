package com.yandi.yarud.yadiupi.absensi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.absensi.DetilMKActivity;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.model.ModelPenugasan;

import java.util.ArrayList;
import java.util.List;

public class AdapterPenugasan extends RecyclerView.Adapter<AdapterPenugasan.HolderData>{
    private List<ModelPenugasan> item;
    private Context context;

    public AdapterPenugasan(Context context, List<ModelPenugasan> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_penugasan,parent,false);
        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        final Intent intent = new Intent(context, DetilMKActivity.class);
        ModelPenugasan model = item.get(position);
        holder.idView.setText(model.getIdMK());
        holder.namaPST.setText(model.getNamaPST());
        holder.namamatakuliahView.setText(model.getNamaMK());
        holder.sksView.setText(model.getSks());
        holder.namakelasView.setText(model.getNamaKelas());
        holder.semesterView.setText(model.getSemester());
        holder.tahunajarView.setText(model.getTahunajar());
        holder.jenjangView.setText(model.getJenjang());
        holder.namadsnView.setText(model.getNamadsn());
        if(model.getNamadsn2().equals("")){
            holder.namadsn2View.setVisibility(View.GONE);
        }else{
            holder.namadsn2View.setText(model.getNamadsn2());
        }
        if(model.getNamadsn3().equals("")){
            holder.namadsn3View.setVisibility(View.GONE);
        }else{
            holder.namadsn3View.setText(model.getNamadsn3());
        }

        intent.putExtra("IDMK",holder.idView.getText().toString());
        intent.putExtra(("KODEMK"),holder.idView.getText().toString());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView idView;
        TextView namamatakuliahView;
        TextView sksView;
        TextView namakelasView;
        TextView semesterView;
        TextView tahunajarView;
        TextView namaPST;
        TextView jenjangView;
        TextView namadsn2View;
        TextView namadsn3View;
        TextView namadsnView;
        Button btnDetail;

        HolderData(View view){
            super(view);
            idView = view.findViewById(R.id.TextViewIDMK);
            namamatakuliahView = view.findViewById(R.id.TextViewForumNAMAMK);
            namaPST = view.findViewById(R.id.TextViewForumNAMAPST);
            sksView = view.findViewById(R.id.TextViewSKS);
            namakelasView = view.findViewById(R.id.TextViewNAMAKELAS);
            semesterView = view.findViewById(R.id.TextViewSMTAJAR);
            tahunajarView = view.findViewById(R.id.TextViewTHNAJAR);
            jenjangView = view.findViewById(R.id.TextViewJENJANG);
            namadsn2View = view.findViewById(R.id.TextViewNAMADSN2);
            namadsn3View = view.findViewById(R.id.TextViewNAMADSN3);
            namadsnView = view.findViewById(R.id.TextViewNAMADSN);
            btnDetail = view.findViewById(R.id.buttonPenugasanDetil);
        }
    }

    public void setFilter(ArrayList<ModelPenugasan> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
