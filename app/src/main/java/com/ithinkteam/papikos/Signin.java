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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signin extends AppCompatActivity {

    TextView btn_masuk, btn_signup,btn_lupaPass;
    EditText et_email, et_password;
    boolean v_email = false, v_pass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        btn_masuk = findViewById(R.id.btn_masuk);
        btn_signup = findViewById(R.id.signUP);
        btn_lupaPass = findViewById(R.id.forgotPass);
        btn_masuk.setEnabled(false);
        btn_masuk.setBackgroundResource(R.color.hijau_tua);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);

        //Klik Tombol
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Masih coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        btn_lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
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
                    if (v_email == true && v_pass == true) {
                        btn_masuk.setEnabled(true);
                        btn_masuk.setBackgroundResource(R.color.hijau);
                    } else {
                        btn_masuk.setEnabled(false);
                        btn_masuk.setBackgroundResource(R.color.hijau_tua);
                    }
                } else {
                    v_email = false;
                    et_email.setError("Masukan email dengan benar!");
                }
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
                        et_password.setError("Password harus terdiri dari 8 karakter!");
                    } else {
                        v_pass = true;
                        if (v_email == true && v_pass == true) {
                            btn_masuk.setEnabled(true);
                            btn_masuk.setBackgroundResource(R.color.hijau);
                        } else {
                            btn_masuk.setEnabled(false);
                            btn_masuk.setBackgroundResource(R.color.hijau_tua);
                        }
                    }
                } else {
                    v_pass = false;
                    et_password.setError("Password harus terdiri dari huruf besar, kecil, angkat dan karakter spesial!");
                }
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
}