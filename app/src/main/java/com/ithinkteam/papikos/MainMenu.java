package com.ithinkteam.papikos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class MainMenu extends AppCompatActivity implements AdaptorDB.MClickListener{
    AdaptorDB adaptorDB;
    private ProgressDialog progressDialog;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference sReference;

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        init();
        initFirebase();
    }

    public void init(){
        rv = findViewById(R.id.rc_menu_kos);
        LinearLayout profile = findViewById(R.id.profileMenu);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileUser.class));
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
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for(DataSnapshot mDataSnap : snapshot.getChildren()){
                        ModelDB mp = mDataSnap.getValue(ModelDB.class);
                        adaptorDB.addModelDB(mp);
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

        adaptorDB=new AdaptorDB(this);
        rv.setAdapter(adaptorDB);
    }



    @Override
    public void onClick(int position) {
        ModelDB modelPost = adaptorDB.getModelDB(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Toast.makeText(getApplicationContext(),"ID "+modelPost.getId(),Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplication(),Detail.class);

        intent.putExtra("id_kost",modelPost.getId());
        startActivity(intent);
//        startActivity(new Intent(getApplicationContext(),Detail.class));
    }
}