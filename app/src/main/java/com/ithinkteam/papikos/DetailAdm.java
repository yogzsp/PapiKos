package com.ithinkteam.papikos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailAdm extends AppCompatActivity {
    public static final String TAG = "TAG";
    String id_kost;
    ImageView iv_gambarKost, tombolBalik;
    TextView tv_namaKost,tv_jenisKost,tv_kotaKost,tv_GMapKost,tv_deskripsiKost,tv_fasilitasKost,tv_hargaKost;
    String namaKost,jenisKost,kotaKost,GMapKost,deskripsiKost,fasilitasKost,hargaKost,gambarKost;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_adm);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id_kost= null;
            } else {
                id_kost= extras.getString("id_kost");
            }
        } else {
            id_kost = (String) savedInstanceState.getSerializable("id_kost");
        }


        init();
        initFirebase();
    }

    public void init(){

        TextView btn_hapus = findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(DetailAdm.this);
                builder2.setMessage("Apakah anda ingin menghapus kost ini?");

                // Jika dia pilih ya
                builder2.setPositiveButton("Ya", (dialogInterface1, i1) -> {
                    if (databaseReference != null) {
                        progressDialog = new ProgressDialog(DetailAdm.this);
                        progressDialog.setMessage("Proses hapus...");
                        progressDialog.setCancelable(false);
                        progressDialog.dismiss();

                        // proses mengambil id dan hapus data pada firebase
                        firebaseDatabase = FirebaseDatabase.getInstance();

                        databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("KOST").child(id_kost).removeValue().addOnSuccessListener(aVoid -> {
//                            sReference.child(modelPost.getId()).delete();
                            Toast.makeText(DetailAdm.this, "Hapus Data Berhasil", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MenuAdmin.class));
                            finish();
                        });
                    }
                });

                // Jika dia pilih tidak
                builder2.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder2.show();
            }
        });



        tv_namaKost = findViewById(R.id.namaKos);
        tv_kotaKost = findViewById(R.id.lokasiKos);
        tv_jenisKost = findViewById(R.id.tipeKos);
        tv_deskripsiKost = findViewById(R.id.deskripsiKost);
        tv_fasilitasKost = findViewById(R.id.fasilitasKos);
        tv_GMapKost = findViewById(R.id.linkGMap);
        iv_gambarKost = findViewById(R.id.gambarKos);

        tombolBalik = findViewById(R.id.tombolBalik);
        tombolBalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("KOST").child(id_kost);

        ArrayList<String> list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ModelDB dataKost = snapshot.getValue(ModelDB.class);
                    String data = dataKost.getNamaKost();
                    gambarKost = dataKost.getImage_url();
                    GMapKost = dataKost.getLinkGMapKost();
                    fasilitasKost = "+ "+dataKost.getFasilitasKost().replaceAll("\n","\n+ ");
                    fasilitasKost = fasilitasKost.substring(0,fasilitasKost.length() - 2);
                    deskripsiKost = dataKost.getDeskripsiKost();
                    jenisKost= dataKost.getJenisKost();
                    kotaKost = dataKost.getKotaKost();
                    namaKost = dataKost.getNamaKost();

                    tv_namaKost.setText(namaKost);
                    tv_kotaKost.setText(kotaKost);
                    tv_jenisKost.setText(jenisKost);
                    tv_deskripsiKost.setText(deskripsiKost);
                    tv_fasilitasKost.setText(fasilitasKost);
//        tv_GMapKost.setText(GMapKost);
                    Picasso.get().load(gambarKost).into(iv_gambarKost);
                    tv_GMapKost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GMapKost)));
                        }
                    });

                    Log.d(TAG, "onDataChange: "+data);
                    Log.d(TAG, "onDataChange: "+snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}