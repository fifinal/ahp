package com.example.ahp.ui.admin.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.ahp.adapter.KombinasiPenyakitGejalaAdapter;
import com.example.ahp.adapter.PenyakitAdapter;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.KombinasiPenyakitGejala;
import com.example.ahp.model.Penyakit;
import com.example.ahp.ui.admin.AddPenyakitGejalaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KombinasiPenyakitGejalaFragment extends Fragment {

    private RecyclerView rvPenyakitGejala;
    private ProgressBar progressBar;

    private Context context;
    private List<Gejala> gejalaList;
    private List<Penyakit> penyakitList;
    private List<KombinasiPenyakitGejala> penyakitGejalaList;

    private PenyakitAdapter adapter;
    private Penyakit penyakitTerpilih;
    private FirebaseFirestore firestore;

    public KombinasiPenyakitGejalaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=container.getContext();
        View root= inflater.inflate(R.layout.fragment_kombinasi_penyakit_gejala, container, false);
        firestore=FirebaseFirestore.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPenyakitGejala=view.findViewById(R.id.rv_kombinasi_penyakit_gejala);
        progressBar=view.findViewById(R.id.progressBar);
        gejalaList=new ArrayList<>();
        penyakitList=new ArrayList<>();
        penyakitGejalaList=new ArrayList<>();

        initRecyclerView();
        getPenyakit();

    }
    private void initRecyclerView() {
        adapter=new PenyakitAdapter(penyakitList);
        rvPenyakitGejala.setLayoutManager(new LinearLayoutManager(context));
        rvPenyakitGejala.setAdapter(adapter);
        adapter.setOnItemClickListener(new PenyakitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Penyakit penyakit, int position) {
                penyakitTerpilih=penyakit;
                Intent intent=new Intent(getContext(), AddPenyakitGejalaActivity.class);
                intent.putExtra("ID_PENYAKIT",penyakit.getId());
                intent.putExtra("NAMA_PENYAKIT",penyakit.getNama());
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(Penyakit penyakit) {

            }
        });
    }

    private void getPenyakit() {
        firestore.collection("penyakit").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                penyakitList.clear();
                if (queryDocumentSnapshots.size()>0){
                    for (DocumentSnapshot doc:queryDocumentSnapshots.getDocuments()){
                        Penyakit penyakit=doc.toObject(Penyakit.class);
                        penyakitList.add(penyakit);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

//    private void initRecyclerView() {
//        adapter=new KombinasiPenyakitGejalaAdapter(penyakitGejalaList,getContext());
//        rvPenyakitGejala.setLayoutManager(new LinearLayoutManager(context));
//        rvPenyakitGejala.setAdapter(adapter);
//        adapter.setOnItemClickListener(new KombinasiPenyakitGejalaAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(final View view, KombinasiPenyakitGejala penyakitGejala, int position) {
//                progressBar.setVisibility(View.VISIBLE);
//                firestore.collection("kombinasi_penyakit_gejala")
//                        .document(penyakitGejala.getPenyakit()+"-"+penyakitGejala.getGejala())
//                        .set(penyakitGejala, SetOptions.merge()).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(context, "berhasil", Toast.LENGTH_SHORT).show();
//                        view.setBackgroundColor(Color.GREEN);
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }).addOnFailureListener(getActivity(), new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "GAGAL "+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//
//            @Override
//            public void onItemLongClick(KombinasiPenyakitGejala kombinasiGejala) {
//
//            }
//
//        });
//    }
//
//    private void getGejala() {
//        firestore.collection("gejala").get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.getResult()!=null){
//                    for (DocumentSnapshot doc:task.getResult()){
//                        Gejala gejala=doc.toObject(Gejala.class);
//                        gejalaList.add(gejala);
//                    }
//                    getPenyakit();
//                }
//            }
//        });
//    }
//    private void getPenyakit() {
//        firestore.collection("penyakit").get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.getResult()!=null){
//                    for (DocumentSnapshot doc:task.getResult()){
//                        Penyakit penyakit=doc.toObject(Penyakit.class);
//                        penyakitList.add(penyakit);
//                    }
////                    setKombinasi();
//                }
//            }
//        });
//    }

//    private void setKombinasi(){
//        for (int i=0;i<penyakitList.size();i++){
//            for (int j=0;j<gejalaList.size();j++) {
//               KombinasiPenyakitGejala penyakitGejala=new KombinasiPenyakitGejala();
//                penyakitGejala.setPenyakit(penyakitList.get(i).getId());
//                penyakitGejala.setGejala(gejalaList.get(j).getId());
//                penyakitGejalaList.add(penyakitGejala);
//            }
//        }
//        adapter.notifyDataSetChanged();
//        progressBar.setVisibility(View.GONE);
//    }

}
