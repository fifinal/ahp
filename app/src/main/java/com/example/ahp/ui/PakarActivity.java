package com.example.ahp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ahp.R;
import com.example.ahp.ui.admin.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PakarActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private TextView tvNama,tvAlamat,tvtelp;
    private ImageView imgProfile;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakar);
        firestore=FirebaseFirestore.getInstance();
        tvNama=findViewById(R.id.tv_nama);
        tvAlamat=findViewById(R.id.tv_alamat);
        tvtelp=findViewById(R.id.tv_telp);
        imgProfile=findViewById(R.id.img_profile);
        progressBar=findViewById(R.id.progressBar);
        setPakar();
    }
    public void setPakar(){
        firestore.collection("pakar").limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult()!=null){
                    try {
                        for (DocumentSnapshot doc: task.getResult()){
                            tvNama.setText(doc.getString("nama"));
                            tvAlamat.setText(doc.getString("alamat"));
                            tvtelp.setText(doc.getString("no_hp"));
                            RequestOptions requestOptions = new RequestOptions()
                                    .error(R.drawable.default_image)
                                    .placeholder(R.drawable.default_image);
                            Glide.with(PakarActivity.this)
                                    .setDefaultRequestOptions(requestOptions)
                                    .load(doc.getString("foto_profile"))
                                    .into(imgProfile);
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                    catch (Exception e){
                        Toast.makeText(PakarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
