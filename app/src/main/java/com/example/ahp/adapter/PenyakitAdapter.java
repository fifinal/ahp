package com.example.ahp.adapter;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.Penyakit;

import java.util.ArrayList;
import java.util.List;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.ViewHolder> {
    List<Penyakit> penyakitList=new ArrayList<>();
    private OnItemClickListener listener;

    public PenyakitAdapter(List<Penyakit> penyakitList) {
        this.penyakitList = penyakitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_penyakit_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvId.setText(penyakitList.get(position).getId());
        holder.tvNama.setText(penyakitList.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return penyakitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId,tvNama;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId=itemView.findViewById(R.id.tv_id);
            tvNama=itemView.findViewById(R.id.tv_nama);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v,penyakitList.get(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, Penyakit penyakit, int position);
        void onItemLongClick(Penyakit penyakit);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
