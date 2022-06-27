package com.ithinkteam.papikos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {
    public static final String TAG = "TAG";
    String id_kost;
    ImageView iv_gambarKost, tombolBalik;
    TextView tv_namaKost,tv_jenisKost,tv_kotaKost,tv_GMapKost,tv_deskripsiKost,tv_fasilitasKost,tv_hargaKost;
    String namaKost,jenisKost,kotaKost,GMapKost,deskripsiKost,fasilitasKost,hargaKost,gambarKost;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

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
        initFirbase();
    }

    public void init(){
        tv_namaKost = findViewById(R.id.namaKos);
        tv_kotaKost = findViewById(R.id.lokasiKos);
        tv_jenisKost = findViewById(R.id.tipeKos);
        tv_hargaKost = findViewById(R.id.hargaKost);
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

    public void initFirbase(){
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
                    hargaKost = dataKost.getHargaKost();
                    deskripsiKost = dataKost.getDeskripsiKost();
                    jenisKost= dataKost.getJenisKost();
                    kotaKost = dataKost.getKotaKost();
                    namaKost = dataKost.getNamaKost();

                    tv_namaKost.setText(namaKost);
                    tv_hargaKost.setText("Rp. "+hargaKost+"/bln");
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