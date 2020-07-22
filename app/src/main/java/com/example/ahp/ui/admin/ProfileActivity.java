package com.example.ahp.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ahp.R;
import com.example.ahp.model.Pakar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNama, edtAlamat ,edtEmail,edtTelp;
    private Button btnSimpan;
    private ImageView imgProfile;
    private ProgressBar setupProgress;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Uri mainImageURI = null;
    private Bitmap compressedImageFile;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        imgProfile= findViewById(R.id.img_profile);
        edtNama= findViewById(R.id.edt_nama);
        edtEmail= findViewById(R.id.edt_email);
        edtAlamat= findViewById(R.id.edt_alamat);
        edtTelp = findViewById(R.id.edt_telp);
        btnSimpan = findViewById(R.id.btn_simpan);
        setupProgress = findViewById(R.id.setup_progress);
        btnSimpan.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        setupProgress.setVisibility(View.VISIBLE);
        setPakar();
    }
    public void setPakar(){
        firebaseFirestore.collection("pakar").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null){
                    try {
                        edtNama.setText(task.getResult().getString("nama"));
                        edtEmail.setText(task.getResult().getString("email"));
                        edtAlamat.setText(task.getResult().getString("alamat"));
                        edtTelp.setText(task.getResult().getString("no_hp"));
                        setTitle(task.getResult().getString("nama"));
                        RequestOptions requestOptions = new RequestOptions()
                                .error(R.drawable.default_image)
                                .placeholder(R.drawable.default_image);
                        Glide.with(ProfileActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(task.getResult().getString("foto_profile"))
                                .into(imgProfile);
                        setupProgress.setVisibility(View.GONE);
                    }
                    catch (Exception e){
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4, 3)
                .start(this);
    }
    private void simpanDataPakar() {
        final String nama= edtNama.getText().toString().trim();
        final String email= edtEmail.getText().toString().trim();
        final String alamat= edtAlamat.getText().toString().trim();
        final String telp = edtTelp.getText().toString().trim();
        if (TextUtils.isEmpty(nama)){
            edtNama.setError("nama harus didisi");
        }
        else if (TextUtils.isEmpty(alamat)){
            edtAlamat.setError("alamatharus didisi");
        }
        else if (TextUtils.isEmpty(telp)){
            edtTelp.setError("telp bengkel harus didisi");
        }
        else if (mainImageURI == null){
            Toast.makeText(this, "pilih dulu gambar bengkel", Toast.LENGTH_SHORT).show();
        }
        else {
            setupProgress.setVisibility(View.VISIBLE);
            btnSimpan.setEnabled(false);
            user_id = firebaseAuth.getCurrentUser().getUid();
            File newImageFile = new File(mainImageURI.getPath());
            try {
                compressedImageFile = new Compressor(this)
                        .setMaxHeight(125)
                        .setMaxWidth(125)
                        .setQuality(50)
                        .compressToBitmap(newImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] thumbData = baos.toByteArray();
            final StorageReference ref=storageReference.child("images").child(user_id + ".jpg");
            UploadTask image_path = ref.putBytes(thumbData);
            image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri;
                    if (task.isSuccessful()){
                        downloadUri=task.getResult();
                        if (downloadUri==null) downloadUri= mainImageURI;
                        Pakar pakar=new Pakar(nama,email,alamat,telp,downloadUri.toString());
                        storeFirestore(pakar);
                    }
                    else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                        setupProgress.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
    private void storeFirestore(Pakar pakar){
        firebaseFirestore.collection("pakar").document(user_id).set(pakar).addOnCompleteListener(new OnCompleteListener<Void>() {@Override
        public void onComplete(@NonNull Task<Void> task) {
            setupProgress.setVisibility(View.INVISIBLE);
            Toast.makeText(ProfileActivity.this, "Berhasil ", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ProfileActivity.this,DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        });
    }
    private void loadGambar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else BringImagePicker();
        }
        else BringImagePicker();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                imgProfile.setImageURI(mainImageURI);
                //                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_simpan:
                simpanDataPakar();
                break;
            case R.id.img_profile:
                loadGambar();
                break;
        }
    }
}
