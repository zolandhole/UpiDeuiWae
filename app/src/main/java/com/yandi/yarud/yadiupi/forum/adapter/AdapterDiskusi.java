package com.yandi.yarud.yadiupi.forum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.forum.model.ModelDiskusi;

import java.util.ArrayList;
import java.util.List;

public class AdapterDiskusi extends RecyclerView.Adapter<AdapterDiskusi.HolderData>{
    private List<ModelDiskusi> item;
    private Context context;

    public AdapterDiskusi(Context context, List<ModelDiskusi> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDiskusi.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_diskusi,parent,false);
        return new AdapterDiskusi.HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDiskusi.HolderData holder, int position) {
        final ModelDiskusi model = item.get(position);
        holder.textViewDiskusiid_frm.setText(model.getId_frm());
        holder.textViewDiskusiid_pn.setText(model.getId_pn());
        holder.textViewDiskusiuser_id.setText(model.getUser_id());
        holder.textViewDiskusiisi.setText(model.getIsi());
        holder.textViewDiskusiinduk.setText(model.getIndu());
        holder.textViewDiskusiwaktu.setText(model.getWaktu());
        holder.textViewDiskusinama.setText(model.getNama());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView textViewDiskusiid_frm,textViewDiskusiid_pn,textViewDiskusiuser_id,textViewDiskusijudul,textViewDiskusiisi,textViewDiskusiinduk,textViewDiskusiwaktu,textViewDiskusinama;
        HolderData(View view){
            super(view);
            textViewDiskusiid_frm = view.findViewById(R.id.TextViewDiskusiid_frm);
            textViewDiskusiid_pn = view.findViewById(R.id.TextViewDiskusiid_pn);
            textViewDiskusiuser_id = view.findViewById(R.id.TextViewDiskusiuser_id);
            textViewDiskusiisi = view.findViewById(R.id.TextViewDiskusiisi);
            textViewDiskusiinduk = view.findViewById(R.id.TextViewDiskusiinduk);
            textViewDiskusiwaktu = view.findViewById(R.id.TextViewDiskusiwaktu);
            textViewDiskusinama = view.findViewById(R.id.TextViewDiskusinama);
        }
    }

    public void setFilter(ArrayList<ModelDiskusi> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
