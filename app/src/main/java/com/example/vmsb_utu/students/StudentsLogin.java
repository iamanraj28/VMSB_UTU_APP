package com.example.vmsb_utu.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.vmsb_utu.MainActivity;
import com.example.vmsb_utu.R;
import com.example.vmsb_utu.admin.AdminLogin;
import com.example.vmsb_utu.admin.AdminMain;
import com.example.vmsb_utu.college.CollegeLogin;
import com.example.vmsb_utu.college.CollegeMain;
import com.example.vmsb_utu.faculty.FacultyLogin;
import com.example.vmsb_utu.faculty.FacultyMain;
import com.example.vmsb_utu.preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentsLogin extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText phone, password;
    Button login, signup, collegeButton, adminButton, facultyButton;
    String phoneTxt, passwordTxt;
    Switch active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupButton);
        active = findViewById(R.id.active);
        collegeButton = findViewById(R.id.collegeButton);
        adminButton = findViewById(R.id.adminButton);
        facultyButton = findViewById(R.id.facultyButton);
        active.setChecked(true);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        phoneTxt = phone.getText().toString();
                        passwordTxt = password.getText().toString();

                        if (dataSnapshot.child(phoneTxt).exists()) {
                            if (dataSnapshot.child(phoneTxt).child("Student Password").getValue(String.class).equals(passwordTxt)) {
                                if (active.isChecked()) {
                                    if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("student")) {
                                        preferences.setDataLogin(StudentsLogin.this, true);
                                        preferences.setDataAs(StudentsLogin.this, "student");
                                        startActivity(new Intent(StudentsLogin.this, StudentsMain.class));
                                        overridePendingTransition(0,0);

                                    } else if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("")){
                                        preferences.setDataLogin(StudentsLogin.this, true);
                                        preferences.setDataAs(StudentsLogin.this, "");
                                        startActivity(new Intent(StudentsLogin.this, StudentsLogin.class));
                                    }
                                } else {
                                    if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("student")) {
                                        preferences.setDataLogin(StudentsLogin.this, false);
                                        startActivity(new Intent(StudentsLogin.this, StudentsMain.class));
                                        overridePendingTransition(0,0);

                                    } else if (dataSnapshot.child(phoneTxt).child("as").getValue(String.class).equals("")){
                                        preferences.setDataLogin(StudentsLogin.this, false);
                                        startActivity(new Intent(StudentsLogin.this, StudentsLogin.class));
                                    }
                                }

                            } else {
                                Toast.makeText(StudentsLogin.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StudentsLogin.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminLogin.class));
                overridePendingTransition(0,0);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentsSignup.class));
                overridePendingTransition(0,0);
            }
        });

        collegeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CollegeLogin.class));
                overridePendingTransition(0,0);
            }
        });

        facultyButton.setOnClickListener(new View.OnClickListener() {
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
            if (preferences.getDataAs(this).equals("student")) {
                startActivity(new Intent(getApplicationContext(),StudentsMain.class));
                overridePendingTransition(0,0);
                finish();
            }else if(preferences.getDataAs(this).equals("college")) {
                startActivity(new Intent(getApplicationContext(), CollegeMain.class));
                overridePendingTransition(0,0);
                finish();
            }else if(preferences.getDataAs(this).equals("faculty")) {
                startActivity(new Intent(getApplicationContext(), FacultyMain.class));
                overridePendingTransition(0,0);
                finish();
            }else if(preferences.getDataAs(this).equals("admin")) {
                startActivity(new Intent(getApplicationContext(), AdminMain.class));
                overridePendingTransition(0,0);
                finish();
            }else {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    startActivity(new Intent(getApplicationContext(),StudentsLogin.class));
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        }
    }
}