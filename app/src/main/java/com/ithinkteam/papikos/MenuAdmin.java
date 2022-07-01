package com.ithinkteam.papikos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class MenuAdmin extends AppCompatActivity implements AdminDB.MClickListener{
    public static final String TAG = "TAG";
    AdminDB adaptorDB;
    String pencarian;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference sReference;

//

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_adm);

        init();
        initFirebase();
    }

    public void init(){
        rv = findViewById(R.id.rc_menu_kos);

        EditText cariKost = findViewById(R.id.cariKos);

        cariKost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                pencarian = cariKost.getText().toString();
                readCari();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initFirebase(){
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference("KOST");

        read();
    }

    private void read(){
        config();
        sharedPreferences = getSharedPreferences(SesiAkun.SHARED_PREF_NAME,MODE_PRIVATE);

        String EmailShared = sharedPreferences.getString(SesiAkun.KEY_EMAIL,null);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for(DataSnapshot mDataSnap : snapshot.getChildren()){
                        ModelDB mp = mDataSnap.getValue(ModelDB.class);
                        if(mp.getEmail().contains(EmailShared)) {
                            adaptorDB.addModelDB(mp);
                            Log.d(TAG, "onDataChange: "+mp.getEmail());
                        }else {
                            Log.d(TAG, "onDataChange-salah: "+mp.getEmail());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readCari(){
        config();
        sharedPreferences = getSharedPreferences(SesiAkun.SHARED_PREF_NAME,MODE_PRIVATE);

        String EmailShared = sharedPreferences.getString(SesiAkun.KEY_EMAIL,null);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for(DataSnapshot mDataSnap : snapshot.getChildren()){
                        ModelDB mp = mDataSnap.getValue(ModelDB.class);
                        if(mp.getNamaKost().toLowerCase(Locale.ROOT).contains(pencarian) && mp.getEmail().contains(EmailShared)) {
                            adaptorDB.addModelDB(mp);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void config(){
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adaptorDB=new AdminDB(this);
        rv.setAdapter(adaptorDB);
    }

    @Override
    public void onClick(int position) {
        ModelDB modelPost = adaptorDB.getModelDB(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Intent intent = new Intent(getApplication(),DetailAdm.class);

        intent.putExtra("id_kost",modelPost.getId());
        startActivity(intent);
        finish();
    }
}