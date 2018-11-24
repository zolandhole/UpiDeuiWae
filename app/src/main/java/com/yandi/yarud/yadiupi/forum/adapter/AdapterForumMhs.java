package com.yandi.yarud.yadiupi.forum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.forum.DataForumActivity;
import com.yandi.yarud.yadiupi.forum.model.ModelForumMhs;

import java.util.ArrayList;
import java.util.List;

public class AdapterForumMhs extends RecyclerView.Adapter<AdapterForumMhs.HolderData> {
    private Context context;
    private List<ModelForumMhs> item;
    public AdapterForumMhs(Context context, List<ModelForumMhs> item){
        this.context = context;
        this.item = item;
    }
    @NonNull
    @Override
    public AdapterForumMhs.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_forum_mhs,parent,false);
        return new AdapterForumMhs.HolderData(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterForumMhs.HolderData holder, int position) {
        final Intent intent = new Intent(context, DataForumActivity.class);
        ModelForumMhs model = item.get(position);
        holder.idView.setText(model.getIDMK());
        holder.kodeMK.setText(model.getKODEMK());
        holder.namamatakuliahView.setText(model.getNAMAMK());

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
        TextView kodeMK;
        CardView btnDetail;
        HolderData(View view) {
            super(view);
            idView = view.findViewById(R.id.TextViewForumMhsIDMK);
            namamatakuliahView = view.findViewById(R.id.TextViewForumMhsNAMAMK);
            kodeMK = view.findViewById(R.id.TextViewForumMhsKodeMK);
            btnDetail = view.findViewById(R.id.CardViewForumMhs);
        }
    }

    public void setFilter(ArrayList<ModelForumMhs> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
