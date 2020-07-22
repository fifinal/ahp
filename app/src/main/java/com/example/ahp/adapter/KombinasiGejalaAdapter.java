package com.example.ahp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.model.KombinasiGejala;

import java.util.ArrayList;
import java.util.List;

public class KombinasiGejalaAdapter extends RecyclerView.Adapter<KombinasiGejalaAdapter.ViewHolder> {

    List<KombinasiGejala> gejalaList=new ArrayList<>();
    private OnItemClickListener listener;

    public KombinasiGejalaAdapter(List<KombinasiGejala> gejalaList) {
        this.gejalaList = gejalaList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kombinasi_gejala_list_item,parent,false);

        KombinasiGejalaAdapter.ViewHolder holder=new KombinasiGejalaAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvGejala1.setText(gejalaList.get(position).getGejala1());
        holder.tvGejala2.setText(gejalaList.get(position).getGejala2());
        holder.edtBobot.setText(gejalaList.get(position).getBobot()+"");
    }

    @Override
    public int getItemCount() {
        return gejalaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGejala1, tvGejala2;
        EditText edtBobot;
        ImageButton btnSimpan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGejala1=itemView.findViewById(R.id.tv_g1);
            tvGejala2=itemView.findViewById(R.id.tv_g2);
            edtBobot=itemView.findViewById(R.id.edt_bobot);
            btnSimpan=itemView.findViewById(R.id.btn_simpan);
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        gejalaList.get(position).setBobot(Double.parseDouble(edtBobot.getText().toString()));
                        listener.onItemClick(v,gejalaList.get(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,KombinasiGejala kombinasiGejala,int position);
        void onItemLongClick(KombinasiGejala kombinasiGejala);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
