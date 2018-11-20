package com.yandi.yarud.yadiupi.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.absensi.DetilMKActivity;
import com.yandi.yarud.yadiupi.forum.DataForumActivity;
import com.yandi.yarud.yadiupi.forum.model.ModelForum;

import java.util.ArrayList;
import java.util.List;

public class AdapterForum extends RecyclerView.Adapter<AdapterForum.HolderData>{
    private List<ModelForum> item;
    private Context context;

    public AdapterForum(Context context, List<ModelForum> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterForum.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_forum,parent,false);
        return new AdapterForum.HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForum.HolderData holder, int position) {
        final Intent intent = new Intent(context, DataForumActivity.class);
        ModelForum model = item.get(position);
        holder.idView.setText(model.getIdMK());
        holder.namaPST.setText(model.getNamaPST());
        holder.namamatakuliahView.setText(model.getNamaMK());

        intent.putExtra("IDMK",holder.idView.getText().toString());
        intent.putExtra("MK",holder.namamatakuliahView.getText().toString());
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
        TextView namaPST;
        CardView btnDetail;

        HolderData(View view){
            super(view);
            idView = view.findViewById(R.id.TextViewForumIDMK);
            namamatakuliahView = view.findViewById(R.id.TextViewForumNAMAMK);
            namaPST = view.findViewById(R.id.TextViewForumNAMAPST);
            btnDetail = view.findViewById(R.id.CardViewForum);
        }
    }

    public void setFilter(ArrayList<ModelForum> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}

