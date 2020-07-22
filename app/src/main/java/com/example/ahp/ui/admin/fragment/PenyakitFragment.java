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

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahp.R;
import com.example.ahp.adapter.GejalaAdapter;
import com.example.ahp.adapter.PenyakitAdapter;
import com.example.ahp.model.Gejala;
import com.example.ahp.model.Penyakit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * A simple {@link Fragment} subclass.
 */
public class PenyakitFragment extends Fragment {

    private RecyclerView rvPenyakit;
    private Context context;
    private PenyakitAdapter adapter;
    private List<Penyakit> penyakitList;
    private Penyakit penyakitTerpilih;

    private FirebaseFirestore firestore;

    public PenyakitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=container.getContext();
        View root = inflater.inflate(R.layout.fragment_penyakit, container, false);
        firestore=FirebaseFirestore.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPenyakit=view.findViewById(R.id.rv_penyakit);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        penyakitList=new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formPenyakit("tambah");
            }
        });

        initRecyclerView();
        getPenyakit();
        swipe();

    }
    private void initRecyclerView() {
        adapter=new PenyakitAdapter(penyakitList);
        rvPenyakit.setLayoutManager(new LinearLayoutManager(context));
        rvPenyakit.setAdapter(adapter);
        adapter.setOnItemClickListener(new PenyakitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Penyakit penyakit, int position) {
                penyakitTerpilih=penyakit;
                formPenyakit("update");
            }

            @Override
            public void onItemLongClick(Penyakit penyakit) {

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

                final Penyakit penyakit=penyakitList.get(viewHolder.getAdapterPosition());
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(viewHolder.itemView.getContext());
                builder.setTitle("Hapus penyakit");
                builder.setMessage("Yakin ingin menghapus "+penyakit.getNama()+" ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id=penyakit.getId();
                        hapusGejala(id);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        }).attachToRecyclerView(rvPenyakit);
    }

    private void hapusGejala(String id) {
        firestore.collection("penyakit").document(id).delete().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "penyakit berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPenyakit() {

        firestore.collection("penyakit").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                penyakitList.clear();
                if (queryDocumentSnapshots.size()>0){
                    for (DocumentSnapshot doc:queryDocumentSnapshots.getDocuments()){
                        Penyakit penyakit=doc.toObject(Penyakit.class);
                        penyakitList.add(penyakit);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
    private void formPenyakit(final String state) {
        final EditText edtid = new EditText(context);
        final EditText edtNama = new EditText(context);
        edtid.setInputType(InputType.TYPE_CLASS_TEXT);
        edtNama.setInputType(InputType.TYPE_CLASS_TEXT);

        String title="";
        String button="";
        if (state.equals("tambah")){
            edtid.setHint("id penyakit");
            edtNama.setHint("nama penyakit");
            title="Tambah penyakit";
            button="Tambah";

        }else{
            edtid.setText(penyakitTerpilih.getId());
            edtNama.setText(penyakitTerpilih.getNama());
            edtid.setEnabled(false);
            title="Update penyakit";
            button="Update";
        }

        LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setPadding(10,10,10,10);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.addView(edtid,0);
        linearLayout.addView(edtNama,1);

        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(title);
        builder.setView(linearLayout);

        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id="";
                if(state.equals("tambah")){
                    id=edtid.getText().toString().trim();
                }else{
                    id=penyakitTerpilih.getId();
                }
                String gejala=edtNama.getText().toString().trim();

                if (isEmpty(gejala)) edtid.setHint("id penyakit tidak boleh kosong");
                else if (isEmpty(gejala)) edtNama.setHint("Nama penyakit tidak boleh kosong");
                else tambahProduk(new Gejala(id,gejala));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void tambahProduk(Gejala gejala) {
        firestore.collection("penyakit").document(gejala.getId()).set(gejala, SetOptions.merge()).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "penyakit berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
