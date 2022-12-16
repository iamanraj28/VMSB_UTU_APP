package com.example.vmsb_utu.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.vmsb_utu.R;
import com.example.vmsb_utu.preferences;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMain extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView uploadNotice, allColleges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawableLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        uploadNotice = findViewById(R.id.addNotice);
        allColleges = findViewById(R.id.allColleges);

        uploadNotice.setOnClickListener(this);
        allColleges.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id==R.id.logout){

                    startActivity(new Intent(getApplicationContext(),AdminLogin.class));
                    overridePendingTransition(0,0);
                    preferences.clearData(AdminMain.this);
                    finish();

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.addNotice:
                startActivity(new Intent(getApplicationContext(), UploadNotice.class));
                overridePendingTransition(0,0);
                break;

            case R.id.allColleges:
                startActivity(new Intent(getApplicationContext(), AllColleges.class));
                overridePendingTransition(0,0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}