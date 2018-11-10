package com.example.yarud.yadiupi.adapter;

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

import com.example.yarud.yadiupi.PemilihanKMActivity;
import com.example.yarud.yadiupi.R;
import com.example.yarud.yadiupi.model.ModelPresensi;
import com.example.yarud.yadiupi.network.GetTokenUPI;

import java.util.List;

public class AdapterPemilihanKM extends RecyclerView.Adapter<AdapterPemilihanKM.HolderData> {
    private List<ModelPresensi> item;
    private Context context;

    public AdapterPemilihanKM(Context context, List<ModelPresensi> item){
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public AdapterPemilihanKM.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pemilihan_km,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPemilihanKM.HolderData holder, int position) {
        final ModelPresensi model = item.get(position);
        holder.nimView.setText(model.getNim());
        holder.namaView.setText(model.getNama());
        holder.buttonSetKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setIcon(R.drawable.icon_info)
                        .setTitle("Konfirmasi")
                        .setMessage("Jadikan "+ model.getNama() + " sebagai KM?\nAnda akan diarahkan ke Halaman Presensi")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GetTokenUPI getTokenUPI = new GetTokenUPI(context,"SetKM");
                                PemilihanKMActivity pemilihanKMActivity = (PemilihanKMActivity) context;
                                getTokenUPI.setKM(pemilihanKMActivity.getUsernameDB(),pemilihanKMActivity.getPasswordDB(),pemilihanKMActivity.getIdmk(),model.getNim(),pemilihanKMActivity.getIdrs());
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
        TextView nimView;
        TextView namaView;
        Button buttonSetKM;
        HolderData(View view) {
            super(view);
            nimView = view.findViewById(R.id.textViewPemilihanKMnim);
            namaView = view.findViewById(R.id.textViewPemilihanKMNama);
            buttonSetKM = view.findViewById(R.id.buttonPemilihanKM);
        }
    }
}
