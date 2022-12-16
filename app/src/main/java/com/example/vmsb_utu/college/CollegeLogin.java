package com.example.vmsb_utu.college;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.vmsb_utu.R;
import com.example.vmsb_utu.admin.AdminLogin;
import com.example.vmsb_utu.faculty.FacultyLogin;
import com.example.vmsb_utu.preferences;
import com.example.vmsb_utu.students.StudentsLogin;
import com.example.vmsb_utu.students.StudentsMain;
import com.example.vmsb_utu.students.StudentsSignup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollegeLogin extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText phone, password;
    Button login, signup, studentButton, adminBtn, facultyBtn;
    String phoneTxt, passwordTxt;
    Switch active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupButton);
        active = findViewById(R.id.active);
        studentButton = findViewById(R.id.studentButton);
        adminBtn = findViewById(R.id.adminButton);
        facultyBtn = findViewById(R.id.facultyButton);
        active.setChecked(true);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("colleges").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        phoneTxt = phone.getText().toString();
                        passwordTxt = password.getText().toString();

                        if (dataSnapshot.child(phoneTxt).exists()) {
                            if (dataSnapshot.child(phoneTxt).child("collegePassword").getValue(String.class).equals(passwordTxt)) {
                                if (active.isChecked()) {
                                    if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("college")) {
                                        preferences.setDataLogin(CollegeLogin.this, true);
                                        preferences.setDataAs(CollegeLogin.this, "college");
                                        startActivity(new Intent(CollegeLogin.this, CollegeMain.class));
                                        overridePendingTransition(0,0);

                                    } else if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("")){
                                        preferences.setDataLogin(CollegeLogin.this, true);
                                        preferences.setDataAs(CollegeLogin.this, "");
                                        startActivity(new Intent(CollegeLogin.this, CollegeLogin.class));
                                    }
                                } else {
                                    if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("college")) {
                                        preferences.setDataLogin(CollegeLogin.this, false);
                                        startActivity(new Intent(CollegeLogin.this, CollegeMain.class));
                                        overridePendingTransition(0,0);

                                    } else if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("")){
                                        preferences.setDataLogin(CollegeLogin.this, false);
                                        startActivity(new Intent(CollegeLogin.this, CollegeLogin.class));
                                    }
                                }

                            } else {
                                Toast.makeText(CollegeLogin.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CollegeLogin.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CollegeSignup.class));
                overridePendingTransition(0,0);
            }
        });

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StudentsLogin.class));
                overridePendingTransition(0,0);
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                overridePendingTransition(0,0);
            }
        });

        facultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getDataLogin(this)) {
            if (preferences.getDataAs(this).equals("college")) {
                startActivity(new Intent(getApplicationContext(),CollegeMain.class));
                overridePendingTransition(0,0);
                finish();
            }else if(preferences.getDataAs(this).equals("")) {
                startActivity(new Intent(getApplicationContext(),CollegeLogin.class));
                overridePendingTransition(0,0);
                finish();
            }else {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    startActivity(new Intent(getApplicationContext(),CollegeLogin.class));
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        }
    }
}