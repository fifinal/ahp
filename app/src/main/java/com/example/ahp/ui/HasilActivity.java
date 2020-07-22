package com.example.ahp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.adapter.HasilDiagnosaAdapter;
import com.example.ahp.metode.Data;
import com.example.ahp.metode.Diagnosa;
import com.example.ahp.model.HasilDiagnosa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HasilActivity extends AppCompatActivity {

    private RecyclerView rvRank;
    private HasilDiagnosaAdapter hasilDiagnosaAdapter;
    private List<HasilDiagnosa> hasilDiagnosaList;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference penyakit=db.collection("penyakit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        rvRank=findViewById(R.id.rv_rank);
        hasilDiagnosaList=new ArrayList<>();
        try {
            diagnosa();
        }catch (Exception e){
            Toast.makeText(this, "error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        initRecycler();
    }

    private void initRecycler() {
       hasilDiagnosaAdapter=new HasilDiagnosaAdapter(hasilDiagnosaList);
       rvRank.setLayoutManager(new LinearLayoutManager(this));
       rvRank.setAdapter(hasilDiagnosaAdapter);
    }

    public void diagnosa() {
        Diagnosa diagnosa = new Diagnosa();
        diagnosa.calculateAHP();
        diagnosa.topsisMethod();

        Map<String,String> map=new HashMap<>();
        map.put("kode_penyakit",Data.finalRanking.get(0).get("key"));
        db.collection("pasien").document(getIntent().getStringExtra("idPasien")).set(map, SetOptions.merge());

        for (int i=0;i<Data.alternatives.length;i++){
            final int finalI = i;
            penyakit.document(Data.finalRanking.get(i).get("key")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult()!=null){
                        try {
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            double d=numberFormat.parse(Data.finalRanking.get(finalI).get("value")).doubleValue();

                            String nama=task.getResult().getString("nama");
                            HasilDiagnosa hasilDiagnosa=new HasilDiagnosa(
                                    Data.finalRanking.get(finalI).get("key"),
                                    nama,d
                                   );
                            hasilDiagnosaList.add(hasilDiagnosa);
                            hasilDiagnosaAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){
                            Toast.makeText(HasilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
