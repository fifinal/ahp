package com.example.ahp.ui.admin.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.adapter.GejalaAdapter;
import com.example.ahp.adapter.KombinasiGejalaAdapter;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.KombinasiGejala;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KombinasiGejalaFragment extends Fragment {
    private RecyclerView rvGejala;
    private ProgressBar progressBar;

    private Context context;
    private KombinasiGejalaAdapter adapter;
    private List<Gejala> gejalaList;
    private List<KombinasiGejala> kombinasiGejalaList;
    private KombinasiGejala gejalaTerpilih;

    private FirebaseFirestore firestore;

    public KombinasiGejalaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=container.getContext();
        View root= inflater.inflate(R.layout.fragment_kombinasi_gejala, container, false);
        firestore=FirebaseFirestore.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGejala=view.findViewById(R.id.rv_kombinasi_gejala);
        progressBar=view.findViewById(R.id.progressBar);
        gejalaList=new ArrayList<>();
        kombinasiGejalaList=new ArrayList<>();

        initRecyclerView();
        getGejala();

    }
    private void initRecyclerView() {
        adapter=new KombinasiGejalaAdapter(kombinasiGejalaList);
        rvGejala.setLayoutManager(new LinearLayoutManager(context));
        rvGejala.setAdapter(adapter);
        adapter.setOnItemClickListener(new KombinasiGejalaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, KombinasiGejala gejala, int position) {

                firestore.collection("kombinasi_gejala")
                        .document(gejala.getGejala1()+"-"+gejala.getGejala2())
                        .set(gejala).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "berhasil", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onItemLongClick(KombinasiGejala kombinasiGejala) {

            }
        });
    }

    private void getGejala() {
        firestore.collection("gejala").get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult()!=null){
                    for (DocumentSnapshot doc:task.getResult()){
                        Gejala gejala=doc.toObject(Gejala.class);
                        gejalaList.add(gejala);
                    }
                    setKombinasi();
                }
            }
        });
    }

    private void setKombinasi(){
        for (int i=0;i<gejalaList.size();i++){
            for (int j=i+1;j<gejalaList.size();j++) {
                KombinasiGejala kombinasiGejala=new KombinasiGejala();
                kombinasiGejala.setGejala1(gejalaList.get(i).getId());
                kombinasiGejala.setGejala2(gejalaList.get(j).getId());
                kombinasiGejalaList.add(kombinasiGejala);
            }
        }
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

}
