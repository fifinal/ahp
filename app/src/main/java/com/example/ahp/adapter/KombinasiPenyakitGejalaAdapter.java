package com.example.ahp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.metode.Fuzzy;
import com.example.ahp.model.KombinasiPenyakitGejala;

import java.util.ArrayList;
import java.util.List;

public class KombinasiPenyakitGejalaAdapter extends RecyclerView.Adapter<KombinasiPenyakitGejalaAdapter.ViewHolder> {
    List<KombinasiPenyakitGejala> penyakitGejalaList=new ArrayList<>();
    private OnItemClickListener listener;
    Context context;
    Fuzzy bobot;

    public KombinasiPenyakitGejalaAdapter(List<KombinasiPenyakitGejala> penyakitGejalaList, Context context) {
        this.penyakitGejalaList = penyakitGejalaList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kombinasi_penyakit_gejala_list_item,parent,false);

        KombinasiPenyakitGejalaAdapter.ViewHolder holder=new KombinasiPenyakitGejalaAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvIdGejala.setText(penyakitGejalaList.get(position).getGejala().getId());
        holder.tvNamaGejala.setText(penyakitGejalaList.get(position).getGejala().getNama());
        String bobot=(penyakitGejalaList.get(position).getBobot().name()!=null)?penyakitGejalaList.get(position).getBobot().name():"VERY_HIGH";
        holder.spinBobot.setSelection(Fuzzy.valueOf(bobot).ordinal());
//        holder.edtBobot.setText(penyakitGejalaList.get(position).getBobot()+"");
    }

    @Override
    public int getItemCount() {
        return penyakitGejalaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvIdGejala,tvNamaGejala;
        Spinner spinBobot;
        Button btnSimpan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdGejala=itemView.findViewById(R.id.tv_id_gejala);
            tvNamaGejala=itemView.findViewById(R.id.tv_nama_gejala);
            spinBobot=itemView.findViewById(R.id.edt_bobot);
            btnSimpan=itemView.findViewById(R.id.btn_simpan);
            spinBobot.setAdapter(new ArrayAdapter<Fuzzy>(context,android.R.layout.simple_list_item_1,Fuzzy.values()));

            spinBobot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int adapterPosition= getAdapterPosition();
                    bobot=Fuzzy.values()[position];
                    penyakitGejalaList.get(adapterPosition).setBobot(bobot);
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view,penyakitGejalaList.get(adapterPosition),adapterPosition);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,KombinasiPenyakitGejala penyakitGejala,int position);
        void onItemLongClick(KombinasiPenyakitGejala kombinasiGejala);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}