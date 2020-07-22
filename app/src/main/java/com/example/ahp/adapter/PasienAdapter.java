package com.example.ahp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.model.Pasien;

import java.util.ArrayList;
import java.util.List;

public class PasienAdapter extends RecyclerView.Adapter<PasienAdapter.ViewHolder> {
    List<Pasien> pasienList=new ArrayList<>();
    private OnItemClickListener listener;

    public PasienAdapter(List<Pasien> pasienList) {
        this.pasienList = pasienList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pasien_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNama.setText(pasienList.get(position).getNama());
        holder.tvAlamat.setText(pasienList.get(position).getAlamat());
        holder.tvGender.setText(pasienList.get(position).getGender());
//        holder.tvNoHp.setText(pasienList.get(position).getNo_telp());
    }

    @Override
    public int getItemCount() {
        return pasienList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvAlamat, tvGender, tvNoHp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama=itemView.findViewById(R.id.tv_nama);
            tvAlamat=itemView.findViewById(R.id.tv_alamat);
            tvGender=itemView.findViewById(R.id.tv_gender);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v,pasienList.get(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,Pasien pasien,int position);
        void onItemLongClick(Pasien pasien);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
