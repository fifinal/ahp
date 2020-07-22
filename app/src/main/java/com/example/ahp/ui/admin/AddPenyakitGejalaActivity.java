package com.example.ahp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.adapter.KombinasiPenyakitGejalaAdapter;
import com.example.ahp.metode.Fuzzy;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.KombinasiPenyakitGejala;
import com.example.ahp.model.Penyakit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;


public class AddPenyakitGejalaActivity extends AppCompatActivity {

    private TextView tvPenyakit;
    private RecyclerView rvPenyakitGejala;
    private ProgressBar progressBar;

    private KombinasiPenyakitGejalaAdapter adapter;
    private List<Gejala> gejalaList;
    private List<Penyakit> penyakitList;
    private List<KombinasiPenyakitGejala> penyakitGejalaList;

    private String idPenyakit,namaPenyakit;

    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penyakit_gejala);
        tvPenyakit=findViewById(R.id.tv_penyakit);
        rvPenyakitGejala=findViewById(R.id.rv_kombinasi_penyakit_gejala);
        progressBar=findViewById(R.id.progressBar);
        gejalaList=new ArrayList<>();
        penyakitList=new ArrayList<>();
        penyakitGejalaList=new ArrayList<>();
        firestore=FirebaseFirestore.getInstance();

        idPenyakit=getIntent().getStringExtra("ID_PENYAKIT");
        namaPenyakit=getIntent().getStringExtra("NAMA_PENYAKIT");

        tvPenyakit.setText(namaPenyakit);
        initRecyclerView();
        getGejala();
    }
    private void initRecyclerView() {
        adapter=new KombinasiPenyakitGejalaAdapter(penyakitGejalaList,this);
        rvPenyakitGejala.setLayoutManager(new LinearLayoutManager(this));
        rvPenyakitGejala.setAdapter(adapter);
        adapter.setOnItemClickListener(new KombinasiPenyakitGejalaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, KombinasiPenyakitGejala penyakitGejala, int position) {

                firestore.collection("kombinasi_penyakit_gejala")
                        .document(penyakitGejala.getPenyakit()+"-"+penyakitGejala.getGejala().getId())
                        .set(penyakitGejala,SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddPenyakitGejalaActivity.this, "simpan", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            @Override
            public void onItemLongClick(KombinasiPenyakitGejala kombinasiGejala) {

            }
        });

    }
    private void getGejala() {
        firestore.collection("gejala").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null){
                    for (DocumentSnapshot snapshot:task.getResult()){
                        Gejala gejala=snapshot.toObject(Gejala.class);
                        gejalaList.add(gejala);
                    }
                    getKombinasi();
                }
            }
        });
    }


    private void getKombinasi(){
        firestore.collection("kombinasi_penyakit_gejala").whereEqualTo("penyakit",idPenyakit).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null) {
                    penyakitGejalaList.clear();
                    if (task.getResult().size() ==gejalaList.size()){
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            KombinasiPenyakitGejala penyakitGejala = snapshot.toObject(KombinasiPenyakitGejala.class);
                            penyakitGejalaList.add(penyakitGejala);
                        }
                    }
                    else{
                        setKombinasi();
                    }
                    Toast.makeText(AddPenyakitGejalaActivity.this, "not null", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();

            }
        });
    }
    private void setKombinasi(){
            penyakitGejalaList.clear();
            for (int i=0;i<gejalaList.size();i++){
                KombinasiPenyakitGejala penyakitGejala=new KombinasiPenyakitGejala();
                penyakitGejala.setPenyakit(idPenyakit);
                penyakitGejala.setGejala(gejalaList.get(i));
                penyakitGejala.setBobot(Fuzzy.LOW);
                penyakitGejalaList.add(penyakitGejala);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                finish();
                break;
            case R.id.action_save:
                simpan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void simpan() {
        for (int i=0;i<penyakitGejalaList.size();i++){
            final int finalI = i;
            firestore.collection("kombinasi_penyakit_gejala")
                    .document(idPenyakit+"-"+penyakitGejalaList.get(i).getGejala().getId())
                    .set(penyakitGejalaList.get(i),SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddPenyakitGejalaActivity.this, penyakitGejalaList.get(finalI).getGejala().getId()+" berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            });
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
