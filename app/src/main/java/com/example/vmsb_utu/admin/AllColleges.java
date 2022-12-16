package com.example.vmsb_utu.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.vmsb_utu.R;
import com.example.vmsb_utu.preferences;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class AllColleges extends AppCompatActivity {

    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView allCollegesRecyclerView;
    AllCollegesAdapter allCollegesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_colleges);

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawableLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        allCollegesRecyclerView = (RecyclerView) findViewById(R.id.allCollegesRecyclerView);
        allCollegesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AllCollegesData> options =
                new FirebaseRecyclerOptions.Builder<AllCollegesData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("colleges"),AllCollegesData.class)
                        .build();

        allCollegesAdapter = new AllCollegesAdapter(options);
        allCollegesRecyclerView.setAdapter(allCollegesAdapter);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id==R.id.logout){

                    startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                    overridePendingTransition(0,0);
                    preferences.clearData(AllColleges.this);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        allCollegesAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        allCollegesAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdminMain.class));
        overridePendingTransition(0,0);
        finish();
    }
}