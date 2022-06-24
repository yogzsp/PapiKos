package com.ithinkteam.papikos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView btn_daftar, btn_signin, errEmail, errPass,errUsername;
    EditText et_email, et_password, et_username;
    boolean v_email = false, v_pass = false,v_username=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        btn_daftar = findViewById(R.id.btn_daftar);
        btn_signin = findViewById(R.id.signIn);
        errEmail = findViewById(R.id.errorEmail);
        errPass = findViewById(R.id.errorPassword);
        errUsername = findViewById(R.id.errorUsername);

        btn_daftar.setEnabled(false);
        btn_daftar.setBackgroundResource(R.color.hijau_tua);

        et_email = findViewById(R.id.email);
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();






        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainMenu.class));
            finish();
        }


        //Klik Tombol
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String username = et_username.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Berhasil membuat akun",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainMenu.class));
                        }else {
                            Toast.makeText(getApplicationContext(),"Error!" + task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //check input
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = et_email.getText().toString();

                if (checkEmail(email)) {
                    v_email = true;
                    errEmail.setVisibility(View.INVISIBLE);
                } else {
                    v_email = false;
                    errEmail.setVisibility(View.VISIBLE);
                }

                checkValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = et_username.getText().toString();

                if (username.length() >= 4) {
                    v_username = true;
                    errUsername.setVisibility(View.INVISIBLE);
                } else {
                    v_username = false;
                    errUsername.setVisibility(View.VISIBLE);
                }

                checkValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = et_password.getText().toString();
                if (checkPassword(password)) {
                    if (password.length() < 8) {
                        v_pass = false;
                        errPass.setText("*password terdiri dari 8 karakter!");
                        errPass.setVisibility(View.VISIBLE);
                    } else {
                        v_pass = true;
                        errPass.setVisibility(View.INVISIBLE);
                    }
                } else {
                    v_pass = false;
                    errPass.setText("*password terdiri dari huruf besar,kecil, angka dan spesial!");
                    errPass.setVisibility(View.VISIBLE);
                }

                checkValue();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static boolean checkPassword(String Password) {
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(Password);

        return matcher.matches();
    }

    public static boolean checkEmail(String Email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    public void checkValue(){
        if (v_email == true && v_pass == true && v_username == true) {
            btn_daftar.setEnabled(true);
            btn_daftar.setBackgroundResource(R.color.hijau);
        } else {
            btn_daftar.setEnabled(false);
            btn_daftar.setBackgroundResource(R.color.hijau_tua);
        }
    }
}