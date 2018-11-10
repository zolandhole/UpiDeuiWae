package com.example.yarud.yadiupi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yarud.yadiupi.PresensiActivity;
import com.example.yarud.yadiupi.R;
import com.example.yarud.yadiupi.model.ModelPresensi;
import com.example.yarud.yadiupi.network.GetTokenUPI;

import java.util.ArrayList;
import java.util.List;

public class AdapterPresensi extends RecyclerView.Adapter<AdapterPresensi.HolderData>{
    private List<ModelPresensi> item;
    private Context context;
    private AlertDialog alert;

    public AdapterPresensi(Context context, List<ModelPresensi> item){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPresensi.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_presensi,parent,false);
        return new HolderData(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterPresensi.HolderData holder, int position) {
        final ModelPresensi model = item.get(position);
        holder.nimView.setText(model.getNim());
        holder.namaView.setText(model.getNama());
        holder.txtKeterangan.setText(model.getKeterangan());
        if(model.getStatus().equals("1")){
            holder.boxPresensi.setChecked(true);
            holder.txtKeterangan.setVisibility(View.GONE);
            holder.namaView.setTextColor(Color.parseColor("#808080"));
        }else{
            holder.boxPresensi.setChecked(false);
            holder.txtKeterangan.setText("/ " + model.getKeterangan());
            holder.namaView.setTextColor(Color.RED);
        }
        holder.boxPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.boxPresensi.isChecked()) {
                    PresensiActivity presensi = (PresensiActivity) context;
                    presensi.displayLoading();
                    GetTokenUPI updateabsensi = new GetTokenUPI(context,"Presensi");
                    updateabsensi.getTokenAbsensi(presensi.getUsernameDB(),presensi.getPasswordDB(),model.getIdrs(),model.getNim(),"1","", model.getNama());
                    holder.txtKeterangan.setVisibility(View.GONE);
                    holder.namaView.setTextColor(Color.parseColor("#808080"));
                }else{
                    PresensiActivity presensi = (PresensiActivity) context;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    @SuppressLint("InflateParams") View mView = presensi.getLayoutInflater().inflate(R.layout.popup_input_presensi,null);
                    final Spinner mSpinner = mView.findViewById(R.id.SpinnerInputPresensiTidakHadir);
                    Button mButtonUpdate = mView.findViewById(R.id.ButtonInputPresensiUpdate);
                    Button mButtonCancel = mView.findViewById(R.id.ButtonInputPresensiCancel);
                    List<String> keterangan = new ArrayList<>();
                    keterangan.add("Pilih ...");
                    keterangan.add("Izin");
                    keterangan.add("Sakit");
                    keterangan.add("Tanpa Keterangan");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, keterangan);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(dataAdapter);
                    mButtonUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String string = String.valueOf(mSpinner.getSelectedItem());
                            if (string.equals("Pilih ...")){
                                Toast.makeText(context,"Anda belum memilih keterangan tidak hadir",Toast.LENGTH_LONG).show();
                            }else {
                                PresensiActivity presensi = (PresensiActivity) context;
                                presensi.displayLoading();
                                GetTokenUPI updateabsensi = new GetTokenUPI(context,"Presensi");
                                updateabsensi.getTokenAbsensi(presensi.getUsernameDB(),presensi.getPasswordDB(),model.getIdrs(),model.getNim(),"0",string, model.getNama());
                                holder.txtKeterangan.setText(string);
                                holder.txtKeterangan.setVisibility(View.VISIBLE);
                                holder.namaView.setTextColor(Color.RED);
                                alert.dismiss();
                            }
                        }
                    });
                    mButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            holder.boxPresensi.setChecked(true);
                        }
                    });
                    builder.setView(mView);
                    builder.setCancelable(false);
                    alert = builder.create();
                    alert.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView nimView;
        TextView namaView;
        TextView txtKeterangan;
        CheckBox boxPresensi;

        private HolderData (View view){
            super(view);
            nimView = view.findViewById(R.id.textViewPresensinim);
            namaView = view.findViewById(R.id.textViewPresensiNama);
            txtKeterangan = view.findViewById(R.id.TextViewPresensiket);
            boxPresensi = view.findViewById(R.id.checkBoxPresensi);
        }
    }
}
