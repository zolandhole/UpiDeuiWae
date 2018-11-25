package com.yandi.yarud.yadiupi.forum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yandi.yarud.yadiupi.R;
import com.yandi.yarud.yadiupi.forum.DiskusiActivity;
import com.yandi.yarud.yadiupi.forum.model.ModelDiskusi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @SuppressLint("SetTextI18n")
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
        DiskusiActivity diskusiActivity = (DiskusiActivity) context;
        String userId = diskusiActivity.getUser_id();
        if (userId.equals(model.getUser_id())){
            holder.textViewDiskusinama.setText("Anda");
            holder.textViewDiskusinama.setTextColor(Color.rgb(10, 168, 57));
            holder.cardViewDiskusi.setCardBackgroundColor(Color.rgb(255, 239, 204));
            holder.textViewDiskusiisi.setTextColor(Color.BLACK);
            holder.textViewDiskusiwaktu.setTextColor(Color.rgb(155, 155, 155));
        }

        java.util.Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String saatIni = dateFormat.format(c);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM d yy");

        Calendar kamari = Calendar.getInstance();
        kamari.add(Calendar.DATE, -1);
        kamari.getTime();
        Date kemarin = kamari.getTime();
        String sebelumHariIni = dateFormat.format(kemarin);

        try {
            Date waktuKomentar = dateFormat.parse(model.getWaktu());
            dateFormat.applyPattern("yyyy-MM-dd");

            String jam = model.getWaktu().substring(model.getWaktu().lastIndexOf(" ")+1);
            String jamMenit = jam.substring(0,jam.length()-3);

            dateFormat2.applyPattern("d MMMyy");

            if (dateFormat.format(waktuKomentar).compareTo(saatIni) == 0){
                holder.textViewDiskusiwaktu.setText(jamMenit);
            } else if (dateFormat.format(waktuKomentar).compareTo(sebelumHariIni) == 0) {
                holder.textViewDiskusiwaktu.setText("Kemarin "+ jamMenit);
            } else {
                holder.textViewDiskusiwaktu.setText(dateFormat2.format(waktuKomentar)+ " " + jamMenit);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView textViewDiskusiid_frm,textViewDiskusiid_pn,textViewDiskusiuser_id,textViewDiskusiisi,textViewDiskusiinduk,textViewDiskusiwaktu,textViewDiskusinama;
        CardView cardViewDiskusi;
        HolderData(View view){
            super(view);
            textViewDiskusiid_frm = view.findViewById(R.id.TextViewDiskusiid_frm);
            textViewDiskusiid_pn = view.findViewById(R.id.TextViewDiskusiid_pn);
            textViewDiskusiuser_id = view.findViewById(R.id.TextViewDiskusiuser_id);
            textViewDiskusiisi = view.findViewById(R.id.TextViewDiskusiisi);
            textViewDiskusiinduk = view.findViewById(R.id.TextViewDiskusiinduk);
            textViewDiskusiwaktu = view.findViewById(R.id.TextViewDiskusiwaktu);
            textViewDiskusinama = view.findViewById(R.id.TextViewDiskusinama);
            cardViewDiskusi = view.findViewById(R.id.cardViewDiskusi);
        }
    }

    public void setFilter(ArrayList<ModelDiskusi> newList){
        item = new ArrayList<>();
        item.addAll(newList);
        notifyDataSetChanged();
    }
}
