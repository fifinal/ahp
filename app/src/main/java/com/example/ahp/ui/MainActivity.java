package com.example.ahp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.model.Pasien;
import com.example.ahp.ui.admin.LoginActivity;
import com.example.ahp.ui.admin.fragment.ItemListDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DATA_PASIEN_REQUEST = 1;
    private ImageView btnDiagnosa, btnInfo,btnAplikasi,btnPakar;
    private TextView tvLogin;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        tvLogin=findViewById(R.id.tv_login);
        btnDiagnosa= findViewById(R.id.btn_diagnosa);
        btnInfo= findViewById(R.id.btn_info);
        btnAplikasi= findViewById(R.id.btn_aplikasi);
        btnPakar= findViewById(R.id.btn_pakar);

        firestore=FirebaseFirestore.getInstance();
        tvLogin.setOnClickListener(this);
        btnDiagnosa.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnAplikasi.setOnClickListener(this);
        btnPakar.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_login:
                progressBar.setVisibility(View.VISIBLE);
                intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.btn_diagnosa:
                intent = new Intent(MainActivity.this, FormPasienActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_info:
                intent=new Intent(MainActivity.this,InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_aplikasi:
                intent=new Intent(MainActivity.this,TentangActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_pakar:
                intent=new Intent(MainActivity.this,PakarActivity.class);
                startActivity(intent);
                break;
        }
    }

}
