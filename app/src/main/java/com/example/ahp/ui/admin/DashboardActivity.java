package com.example.ahp.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ahp.R;
import com.example.ahp.ui.MainActivity;
import com.example.ahp.ui.admin.fragment.ItemListDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity implements ItemListDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private TextView tvNama,tvEmail;
    private ImageView imgProfile;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

//        progressBar=findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        imgProfile=header.findViewById(R.id.img_header);
        tvNama=header.findViewById(R.id.tv_header_nama);
        tvEmail=header.findViewById(R.id.tv_header_email);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gejala,R.id.nav_penyakit,R.id.nav_kombinasi_penyakit_gejala,R.id.nav_pasien,R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setPakar();
    }

    public void setPakar(){
        firestore.collection("pakar").document(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null){
                    try {
                        tvNama.setText(task.getResult().getString("nama"));
                        tvEmail.setText(task.getResult().getString("email"));
                        RequestOptions requestOptions = new RequestOptions()
                                .error(R.drawable.default_image)
                                .placeholder(R.drawable.default_image);
                        Glide.with(DashboardActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(task.getResult().getString("foto_profile"))
                                .into(imgProfile);
//                        progressBar.setVisibility(View.GONE);
                    }
                    catch (Exception e){
                        Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_home:
                intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_profile:
                intent=new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                auth.signOut();
                intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
