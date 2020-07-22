package com.example.ahp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.model.HasilDiagnosa;

import java.util.ArrayList;
import java.util.List;

public class HasilDiagnosaAdapter extends RecyclerView.Adapter<HasilDiagnosaAdapter.ViewHolder> {
    List<HasilDiagnosa> hasilDiagnosaList=new ArrayList<>();

    public HasilDiagnosaAdapter(List<HasilDiagnosa> hasilDiagnosaList) {
        this.hasilDiagnosaList = hasilDiagnosaList;
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hasil_diagnosa_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvKode.setText(hasilDiagnosaList.get(position).getKodePenyakit());
        holder.tvNama.setText(hasilDiagnosaList.get(position).getNamaPenyakit());
        holder.tvNilai.setText(hasilDiagnosaList.get(position).getNilai()+"");
    }



    @Override
    public int getItemCount() {
        return hasilDiagnosaList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKode,tvNama, tvNilai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKode=itemView.findViewById(R.id.tv_kode_penyakit);
            tvNama=itemView.findViewById(R.id.tv_nama_penyakit);
            tvNilai=itemView.findViewById(R.id.tv_nilai);

        }
    }
}
