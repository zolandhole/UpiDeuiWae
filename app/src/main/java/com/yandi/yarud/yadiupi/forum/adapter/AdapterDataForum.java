package com.yandi.yarud.yadiupi.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.forum.DiskusiActivity;
import com.yandi.yarud.yadiupi.forum.model.ModelDataForum;

import java.util.ArrayList;
import java.util.List;

public class AdapterDataForum extends RecyclerView.Adapter<AdapterDataForum.HolderData>{

    private List<ModelDataForum> item;
    private Context context;

    public AdapterDataForum(Context context, List<ModelDataForum> item){
        this.item = item;
        this.context = context;
    }



    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_forum,parent,false);
        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        final ModelDataForum model = item.get(position);
        holder.textViewDFid_frm.setText(model.getId_frm());
        holder.textViewDFid_pn.setText(model.getId_pn());
        holder.textViewDFuser_id.setText(model.getUser_id());
        holder.textViewDFjudul.setText(model.getJudul());
        holder.textViewDFisi.setText(model.getIsi());
        holder.textViewDFinduk.setText(model.getInduk());
        holder.textViewDFwaktu.setText(model.getWaktu());
        holder.textViewDFnama.setText(model.getNama());

        holder.buttonDFDiskusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDiskusi = new Intent(context, DiskusiActivity.class);
                intentDiskusi.putExtra("JUDUL",model.getJudul());
                intentDiskusi.putExtra("NAMA",model.getNama());
                intentDiskusi.putExtra("WAKTU",model.getWaktu());
                intentDiskusi.putExtra("ISI",model.getIsi());
                intentDiskusi.putExtra("IDMK",model.getId_pn());
                intentDiskusi.putExtra("IDFRM",model.getId_frm());
                context.startActivity(intentDiskusi);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView textViewDFid_frm,textViewDFid_pn,textViewDFuser_id,textViewDFjudul,textViewDFisi,textViewDFinduk,textViewDFwaktu,textViewDFnama;
        Button buttonDFDiskusi;
        HolderData(View view){
            super(view);
            textViewDFid_frm = view.findViewById(R.id.TextViewDFid_frm);
            textViewDFid_pn = view.findViewById(R.id.TextViewDFid_pn);
            textViewDFuser_id = view.findViewById(R.id.TextViewDFuser_id);
            textViewDFjudul = view.findViewById(R.id.TextViewDFjudul);
            textViewDFisi = view.findViewById(R.id.TextViewDFisi);
            textViewDFinduk = view.findViewById(R.id.TextViewDFinduk);
            textViewDFwaktu = view.findViewById(R.id.TextViewDFwaktu);
            textViewDFnama = view.findViewById(R.id.TextViewDFnama);
            buttonDFDiskusi = view.findViewById(R.id.ButtonDFDiskusi);
        }
    }

    public void setFilter(ArrayList<ModelDataForum> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
