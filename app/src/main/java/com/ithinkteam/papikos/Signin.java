package com.ithinkteam.papikos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signin extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "TAG";
    TextView btn_masuk, btn_signup,btn_lupaPass;
    FirebaseAuth firebaseAuth;
    EditText et_email, et_password;

    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPreferences;
    List<ModelDB> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        sharedPreferences = getSharedPreferences(SesiAkun.SHARED_PREF_NAME,MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btn_masuk = findViewById(R.id.btn_masuk);
        btn_signup = findViewById(R.id.signUP);
        btn_lupaPass = findViewById(R.id.forgotPass);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);

        //Klik Tombol
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if(!email.isEmpty() && !password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                firebaseFirestore.collection("users")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(!queryDocumentSnapshots.isEmpty()){

                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                                    for(DocumentSnapshot d : list){
                                                        String emailDicari = d.getString("email");
                                                        String usernameDicari = d.getString("username");
                                                        if(email.equals(emailDicari)){
                                                            Toast.makeText(getApplicationContext(), "Berhasil membuat akun", Toast.LENGTH_SHORT).show();
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString(SesiAkun.KEY_EMAIL,email);
                                                            editor.putString(SesiAkun.KEY_USERNAME,usernameDicari);
                                                            editor.apply();

                                                            startActivity(new Intent(getApplicationContext(), MainMenu.class));
                                                            finish();
                                                        }
                                                        Log.d(TAG, "onSuccess: "+d.getString("username"));
                                                    }

                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: ", e);
                                            }
                                        });
                            }else {
                                Toast.makeText(getApplicationContext(),"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainMenu.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUP:
                startActivity(new Intent(Signin.this,SignUp.class));
                break;
        }
    }
}