package com.example.ahp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.model.Gejala;

import java.util.ArrayList;
import java.util.List;

public class GejalaAdapter extends RecyclerView.Adapter<GejalaAdapter.ViewHolder> {

    List<Gejala> gejalaList=new ArrayList<>();
    private OnItemClickListener listener;

    public GejalaAdapter(List<Gejala> gejalaList) {
        this.gejalaList = gejalaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gejala_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvId.setText(gejalaList.get(position).getId());
        holder.tvNama.setText(gejalaList.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return gejalaList.size();
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
                        listener.onItemClick(v,gejalaList.get(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view,Gejala gejala,int position);
        void onItemLongClick(Gejala gejala);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
