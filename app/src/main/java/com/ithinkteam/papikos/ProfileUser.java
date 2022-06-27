package com.ithinkteam.papikos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileUser extends AppCompatActivity {
    TextView btn_logout,daftarKost,namaUser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        btn_logout = findViewById(R.id.btn_logout);
        daftarKost = findViewById(R.id.daftarKost);
        namaUser = findViewById(R.id.namaUser);

        sharedPreferences = getSharedPreferences(SesiAkun.SHARED_PREF_NAME,MODE_PRIVATE);

        String UsernameShared = sharedPreferences.getString(SesiAkun.KEY_USERNAME,null);

        namaUser.setText("Selamat datang "+UsernameShared+"!");

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(),Signin.class));
                finishAffinity();
            }
        });

        daftarKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddKost.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profileNavbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeNavbar:
                        startActivity(new Intent(getApplicationContext(),MainMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.profileNavbar:
                        return true;
                    case  R.id.favoriteNavbar:
//                        startActivity(new Intent(getApplicationContext(),ProfileUser.class));
//                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}