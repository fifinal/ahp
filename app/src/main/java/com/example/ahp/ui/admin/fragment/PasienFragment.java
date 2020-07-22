package com.example.ahp.ui.admin.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.adapter.PasienAdapter;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.Pasien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasienFragment extends Fragment {

    private RecyclerView rvPasien;
    private Context context;
    private PasienAdapter adapter;
    private List<Pasien> pasienList;
    private Pasien pasienTerpilih;

    private FirebaseFirestore firestore;
    public PasienFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=container.getContext();
        View root = inflater.inflate(R.layout.fragment_pasien, container, false);
        firestore=FirebaseFirestore.getInstance();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPasien=view.findViewById(R.id.rv_pasien);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        pasienList=new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        initRecyclerView();
        getPasien();
        swipe();

    }

    private void initRecyclerView() {
        adapter=new PasienAdapter(pasienList);
        rvPasien.setLayoutManager(new LinearLayoutManager(context));
        rvPasien.setAdapter(adapter);
        adapter.setOnItemClickListener(new PasienAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Pasien pasien, int position) {
//                firestore.collection("pasien").document("").delete();
            }

            @Override
            public void onItemLongClick(Pasien pasien) {

            }
        });
    }

    private void swipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                Pasien pasien=pasienList.get(viewHolder.getAdapterPosition());
                hapusPasien(pasien.getId());

            }
        }).attachToRecyclerView(rvPasien);
    }

    private void hapusPasien(String id) {
        firestore.collection("pasien").document(id).delete();
    }


    private void getPasien() {

        firestore.collection("pasien").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pasienList.clear();
                if (queryDocumentSnapshots!=null){
                    for (DocumentSnapshot doc:queryDocumentSnapshots.getDocuments()){
                        Pasien pasien=doc.toObject(Pasien.class);
                        pasienList.add(pasien);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
