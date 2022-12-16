package com.example.vmsb_utu.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.vmsb_utu.R;
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

import java.util.Random;

public class StudentsSignup extends AppCompatActivity {

    EditText studentEmail, studentPass, studentName, studentPhone, collegeName;
    Button studentSignup, studentLogin;
    String studentNameTxt, studentPhoneTxt, studentEmailTxt, studentPassTxt, collegeNameTxt, genderTxt;
    RadioButton maleRadioButton, femaleRadioButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vmsb-utu-d6d84-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_signup);

        studentEmail = findViewById(R.id.studentEmail);
        studentName = findViewById(R.id.studentName);
        studentPass = findViewById(R.id.studentPassword);
        studentPhone = findViewById(R.id.studentPhone);
        collegeName = findViewById(R.id.collegeName);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        studentSignup = findViewById(R.id.studentSignup);
        studentLogin = findViewById(R.id.studentLogin);

        mAuth = FirebaseAuth.getInstance();

        studentSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                studentEmailTxt = studentEmail.getText().toString();
                collegeNameTxt = collegeName.getText().toString();
                if (maleRadioButton.isChecked()){
                    genderTxt = "Male";
                } else if (femaleRadioButton.isChecked()){
                    genderTxt = "Female";
                }
                studentPhoneTxt = studentPhone.getText().toString();
                studentNameTxt = studentName.getText().toString();
                studentPassTxt = studentPass.getText().toString();

                if (studentEmailTxt.isEmpty() || studentPhoneTxt.isEmpty() || studentNameTxt.isEmpty() || studentPassTxt.isEmpty() || collegeNameTxt.isEmpty() || genderTxt.isEmpty()){
                    studentEmail.setError("Email cannot be empty");
                    studentEmail.requestFocus();
                    collegeName.setError("College Name cannot be empty");
                    collegeName.requestFocus();
                    maleRadioButton.setError("Gender is not selected");
                    maleRadioButton.requestFocus();
                    femaleRadioButton.setError("Gender is not selected");
                    femaleRadioButton.requestFocus();
                    studentPhone.setError("Phone cannot be empty");
                    studentPhone.requestFocus();
                    studentName.setError("Name cannot be empty");
                    studentName.requestFocus();
                    studentPass.setError("Password cannot be empty");
                    studentPass.requestFocus();
                }
                else{

                    mAuth.createUserWithEmailAndPassword(studentEmailTxt,studentPassTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(StudentsSignup.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StudentsSignup.this, "Email verification failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(studentPhoneTxt)){
                                            Toast.makeText(StudentsSignup.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            databaseReference.child("students").child(studentPhoneTxt).child("studentName").setValue(studentNameTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("studentEmail").setValue(studentEmailTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("collegeName").setValue(collegeNameTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("gender").setValue(genderTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("studentPhone").setValue(studentPhoneTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("studentPassword").setValue(studentPassTxt);
                                            databaseReference.child("students").child(studentPhoneTxt).child("as").setValue("student");

                                            startActivity(new Intent(getApplicationContext(),StudentsLogin.class));
                                            overridePendingTransition(0,0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                studentEmail.setError("Email already registered. Please Login");
                                studentEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StudentsLogin.class));
                overridePendingTransition(0,0);
            }
        });

    }

}