package com.example.vmsb_utu.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vmsb_utu.R;
import com.example.vmsb_utu.students.StudentsLogin;
import com.example.vmsb_utu.students.StudentsSignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultySignup extends AppCompatActivity {

    EditText facultyEmail, facultyPass, facultyName, facultyPhone, collegeName;
    Button facultySignup, facultyLogin;
    String facultyNameTxt, facultyPhoneTxt, facultyEmailTxt, facultyPassTxt, collegeNameTxt, genderTxt;
    RadioButton maleRadioButton, femaleRadioButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vmsb-utu-d6d84-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_signup);

        facultyEmail = findViewById(R.id.facultyEmail);
        facultyName = findViewById(R.id.facultyName);
        facultyPass = findViewById(R.id.facultyPassword);
        facultyPhone = findViewById(R.id.facultyPhone);
        collegeName = findViewById(R.id.facultyCollegeName);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        facultySignup = findViewById(R.id.facultySignup);
        facultyLogin = findViewById(R.id.facultyLogin);

        mAuth = FirebaseAuth.getInstance();

        facultySignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                facultyEmailTxt = facultyEmail.getText().toString();
                facultyPhoneTxt = facultyPhone.getText().toString();
                facultyNameTxt = facultyName.getText().toString();
                if (maleRadioButton.isChecked()){
                    genderTxt = "Male";
                } else if (femaleRadioButton.isChecked()){
                    genderTxt = "Female";
                }
                facultyPassTxt = facultyPass.getText().toString();
                collegeNameTxt = collegeName.getText().toString();

                if (facultyEmailTxt.isEmpty() || facultyPhoneTxt.isEmpty() || facultyNameTxt.isEmpty() || facultyPassTxt.isEmpty() || collegeNameTxt.isEmpty() || genderTxt.isEmpty()){
                    facultyEmail.setError("Email cannot be empty");
                    facultyEmail.requestFocus();
                    collegeName.setError("College Name cannot be empty");
                    collegeName.requestFocus();
                    maleRadioButton.setError("Gender is not selected");
                    maleRadioButton.requestFocus();
                    femaleRadioButton.setError("Gender is not selected");
                    femaleRadioButton.requestFocus();
                    facultyPhone.setError("Phone cannot be empty");
                    facultyPhone.requestFocus();
                    facultyName.setError("Name cannot be empty");
                    facultyName.requestFocus();
                    facultyPass.setError("Password cannot be empty");
                    facultyPass.requestFocus();
                }
                else{

                    mAuth.createUserWithEmailAndPassword(facultyEmailTxt,facultyPassTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(FacultySignup.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FacultySignup.this, "Email verification failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                databaseReference.child("faculties").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(facultyPhoneTxt)){
                                            Toast.makeText(FacultySignup.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("facultyName").setValue(facultyNameTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("facultyEmail").setValue(facultyEmailTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("collegeName").setValue(collegeNameTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("gender").setValue(genderTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("facultyPhone").setValue(facultyPhoneTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("facultyPassword").setValue(facultyPassTxt);
                                            databaseReference.child("faculties").child(facultyPhoneTxt).child("as").setValue("faculty");

                                            startActivity(new Intent(getApplicationContext(), FacultyLogin.class));
                                            overridePendingTransition(0,0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                facultyEmail.setError("Email already registered. Please Login");
                                facultyEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        facultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FacultyLogin.class));
                overridePendingTransition(0,0);
            }
        });

    }

}