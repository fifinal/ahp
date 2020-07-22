package com.example.ahp.ui.admin.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahp.R;
import com.example.ahp.adapter.GejalaAdapter;
import com.example.ahp.model.Gejala;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.text.TextUtils.isEmpty;

public class GejalaFragment extends Fragment {

    private RecyclerView rvGejala;
    private Context context;
    private GejalaAdapter adapter;
    private List<Gejala> gejalaList;
    private Gejala gejalaTerpilih;

    private FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context=container.getContext();
        View root = inflater.inflate(R.layout.fragment_gejala, container, false);
        firestore=FirebaseFirestore.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGejala=view.findViewById(R.id.rv_gejala);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        gejalaList=new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formGejala("tambah");
            }
        });

        initRecyclerView();
        getGejala();
        swipe();

    }

    private void initRecyclerView() {
        adapter=new GejalaAdapter(gejalaList);
        rvGejala.setLayoutManager(new LinearLayoutManager(context));
        rvGejala.setAdapter(adapter);
        adapter.setOnItemClickListener(new GejalaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Gejala gejala, int position) {
                gejalaTerpilih=gejala;
                formGejala("update");
            }

            @Override
            public void onItemLongClick(Gejala gejala) {

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

                final Gejala gejala=gejalaList.get(viewHolder.getAdapterPosition());
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(viewHolder.itemView.getContext());
                builder.setTitle("Hapus Gejala");
                builder.setMessage("Yakin ingin menghapus "+gejala.getNama()+" ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id=gejala.getId();
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
        }).attachToRecyclerView(rvGejala);
    }

    private void hapusGejala(String id) {
        firestore.collection("gejala").document(id).delete().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "gejala berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGejala() {

        firestore.collection("gejala").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                gejalaList.clear();
                if (queryDocumentSnapshots!=null){
                    for (DocumentSnapshot doc:queryDocumentSnapshots.getDocuments()){
                        Gejala gejala=doc.toObject(Gejala.class);
                        gejalaList.add(gejala);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void formGejala(final String state) {
        final EditText edtid = new EditText(context);
        final EditText edtNama = new EditText(context);
        edtid.setInputType(InputType.TYPE_CLASS_TEXT);
        edtNama.setInputType(InputType.TYPE_CLASS_TEXT);

        String title="";
        String button="";
        if (state.equals("tambah")){
            edtid.setHint("id gejala");
            edtNama.setHint("nama gejala");
            title="Tambah gejala";
            button="Tambah";

        }else{
            edtid.setText(gejalaTerpilih.getId());
            edtNama.setText(gejalaTerpilih.getNama());
            edtid.setEnabled(false);
            title="Update Gejala";
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
                    id=gejalaTerpilih.getId();
                }
                String gejala=edtNama.getText().toString().trim();

                if (isEmpty(gejala)) edtid.setHint("id gejala tidak boleh kosong");
                else if (isEmpty(gejala)) edtNama.setHint("Nama gejala tidak boleh kosong");
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
        firestore.collection("gejala").document(gejala.getId()).set(gejala,SetOptions.merge()).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Gejala berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}