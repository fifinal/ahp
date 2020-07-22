package com.example.ahp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.model.Pasien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormPasienActivity extends AppCompatActivity {

    public static final String NAMA="NAMA";
    public static final String AlAMAT="ALAMAT";
    public static final String NOHP="NOHP";
    public static final String GENDER="GENDER";

    private EditText edtNama, edtAlamat,edtNoHp;
    private RadioButton rbL,rbP;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pasien);
        progressBar=findViewById(R.id.progressBar);
        edtNama=findViewById(R.id.edt_nama);
        edtAlamat=findViewById(R.id.edt_alamat);
        edtNoHp=findViewById(R.id.edt_no_hp);
        rbL=findViewById(R.id.rb_l);
        rbP=findViewById(R.id.rb_p);
        firestore=FirebaseFirestore.getInstance();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Data Pasien");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String nama = edtNama.getText().toString().trim();
        String alamat = edtAlamat.getText().toString().trim();
        String noHp = edtNoHp.getText().toString().trim();
        if(nama.trim().isEmpty()){
            edtNama.setError("Tidak Boleh Kosong");
        }
        else if(alamat.trim().isEmpty()){
            edtNama.setError("Tidak Boleh Kosong");
        }
        else if(noHp.trim().isEmpty()){
            edtNama.setError("Tidak Boleh Kosong");
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            String gender="Laki-Laki";
            if (rbL.isChecked())
                gender="Laki-Laki";
            else if (rbL.isChecked())
                gender="Perempuan";

            Pasien pasien=new Pasien(nama,alamat,gender,noHp);
            final DocumentReference reference=firestore.collection("pasien").document();
            pasien.setId(reference.getId());
            reference.set(pasien).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"tersimpan",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(FormPasienActivity.this,DiagnosaActivity.class);
                    intent.putExtra("idPasien",reference.getId());
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }
}
