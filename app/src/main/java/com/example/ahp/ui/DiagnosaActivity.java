package com.example.ahp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.metode.AHP;
import com.example.ahp.metode.Data;
import com.example.ahp.metode.Fuzzy;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.KombinasiGejala;
import com.example.ahp.model.KombinasiPenyakitGejala;
import com.example.ahp.model.Penyakit;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiagnosaActivity extends AppCompatActivity {

    private ListView lvGejala;
    private TextView tvLoad;
    private Button btnDiagnosa;
    private ProgressBar progressBar;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference gejala=db.collection("gejala");
    private CollectionReference penyakit=db.collection("penyakit");
    private List<Gejala> listGejala=new ArrayList<>();
    private List<Gejala> gejalaTerpilih;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa);
        progressBar= findViewById(R.id.progressBar);
        tvLoad= findViewById(R.id.tv_load);
        lvGejala=findViewById(R.id.lv_gejala);
        btnDiagnosa= findViewById(R.id.btn_diagnosa);
        lvGejala.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        gejalaTerpilih=new ArrayList<>();
        getGejala();
        getPenyakit();
        getKombinasiGejala();
        lvGejala.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Gejala gejala=listGejala.get(position);
                if(((CheckedTextView)view).isChecked()==true){
                    gejalaTerpilih.add(gejala);
                }else{
                    gejalaTerpilih.remove(gejala);
                }
            }
        });

        btnDiagnosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    int panjangGejalaTerpilih=gejalaTerpilih.size();
                    Data.criteria=new String[panjangGejalaTerpilih];
                    int i=0;
                    for (Gejala gejala:gejalaTerpilih){
                        Data.criteria[i]=gejala.getId();
                        i++;
                    }
                Toast.makeText(DiagnosaActivity.this, Data.criteria.length+"", Toast.LENGTH_SHORT).show();
                    start();

            }
        });
    }

    private void getKombinasiGejala(){
        db.collection("kombinasi_gejala").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null){
                    for(DocumentSnapshot snapshot:task.getResult()){

                        KombinasiGejala kombinasiGejala=new KombinasiGejala();
                        kombinasiGejala.setGejala1(snapshot.getString("gejala1"));
                        kombinasiGejala.setGejala2(snapshot.getString("gejala2"));
                        kombinasiGejala.setBobot(snapshot.getDouble("bobot"));
                        Data.criteriaWeight.put(snapshot.getId(),kombinasiGejala);
                    }
                }
            }
        });
    }

    public void start(){
         Data.ahpWeights= new Double[Data.criteria.length];
            final ArrayList<Fuzzy> siteCriteria = new ArrayList<>();

            for (String alternative:Data.alternatives){
                Data.availableSites.put(alternative, new ArrayList<Fuzzy>());
            }
            db.collection("kombinasi_penyakit_gejala").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.getResult()!=null){
                        for (DocumentSnapshot doc:task.getResult()){
//                            String gejala=doc.getString("gejala");
                            KombinasiPenyakitGejala penyakitGejala=doc.toObject(KombinasiPenyakitGejala.class);
                            String penyakit=doc.getString("penyakit");

                            if (ArrayUtils.contains(Data.criteria,penyakitGejala.getGejala().getId())){
                                switch (doc.getString("bobot")){
                                    case "VERY_LOW":
                                        Data.availableSites.get(penyakit).add(Fuzzy.VERY_LOW);
                                        break;
                                    case "LOW":
                                        Data.availableSites.get(penyakit).add(Fuzzy.LOW);
                                        break;
                                    case "GOOD":
                                        Data.availableSites.get(penyakit).add(Fuzzy.GOOD);
                                        break;
                                    case "VERY_HIGH":
                                        Data.availableSites.get(penyakit).add(Fuzzy.VERY_HIGH);
                                        break;
                                    case "HIGH":
                                        Data.availableSites.get(penyakit).add(Fuzzy.HIGH);
                                        break;
                                }
                                progressBar.setVisibility(View.GONE);
                                System.out.println(penyakit+"-"+ Data.availableSites.get(penyakit).toString());
                                Intent intent = new Intent(DiagnosaActivity.this, HasilActivity.class);
                                intent.putExtra("idPasien",getIntent().getStringExtra("idPasien"));
                                startActivity(intent);
                            }

                        }

                    }
                }
            });

    }

    private void getGejala(){
        progressBar.setVisibility(View.VISIBLE);
        gejala.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String items[] =new String[queryDocumentSnapshots.size()];
                int i=0;
                for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    Gejala gejala=doc.toObject(Gejala.class);
                    listGejala.add(gejala);

                    items[i]=doc.getString("nama");
                    i++;
                }
                tvLoad.setVisibility(View.GONE);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.row_checkbox,R.id.cb_tv_gejala,items);
//                adapter.setDropDownViewResource(R.layout.row_checkbox);
                lvGejala.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPenyakit(){
        penyakit.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Data.alternatives=new String[queryDocumentSnapshots.size()];
                int i=0;
                for (QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    Penyakit penyakit=doc.toObject(Penyakit.class);
                    Data.alternatives[i]=penyakit.getId();
                    i++;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}