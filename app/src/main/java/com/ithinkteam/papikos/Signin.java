package com.ithinkteam.papikos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signin extends AppCompatActivity implements View.OnClickListener{

    TextView btn_masuk, btn_signup,btn_lupaPass;
    FirebaseAuth firebaseAuth;
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_masuk = findViewById(R.id.btn_masuk);
        btn_signup = findViewById(R.id.signUP);
        btn_lupaPass = findViewById(R.id.forgotPass);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);

        //Klik Tombol
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Masih coming soon",Toast.LENGTH_SHORT).show();
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