package com.example.ahp.ui.admin.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {


    private AutoCompleteTextView edtTentang,edtInfo;
    private Button btnSimpan;

    private FirebaseFirestore firestore;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore=FirebaseFirestore.getInstance();
        edtTentang=view.findViewById(R.id.edt_tentng);
        edtInfo=view.findViewById(R.id.edt_info);
        btnSimpan=view.findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(this);
        getSetting();

    }

    private void getSetting() {

        firestore.collection("setting").document("setting").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null){
                            edtTentang.setText(task.getResult().getString("tentang"));
                            edtInfo.setText(task.getResult().getString("info"));
                    }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_simpan:
                simpanSetting();
                break;
        }
    }

    private void simpanSetting() {

        Map<String,String> map=new HashMap<>();

        String tentang =edtTentang.getText().toString().trim();
        String info=edtInfo.getText().toString().trim();
        map.put("tentang",tentang);
        map.put("info",info);
        firestore.collection("setting").document("setting").set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Berhasil di simpan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
