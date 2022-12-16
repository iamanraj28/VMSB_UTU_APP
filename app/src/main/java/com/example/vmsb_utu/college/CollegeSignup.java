package com.example.vmsb_utu.college;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollegeSignup extends AppCompatActivity {

    EditText collegeEmail, collegePass, collegeName, collegePhone;
    Button collegeSignup, collegeLogin;
    String collegeNameTxt, collegePhoneTxt, collegeEmailTxt, collegePassTxt;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://vmsb-utu-d6d84-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_signup);

        collegeEmail = findViewById(R.id.collegeEmail);
        collegeName = findViewById(R.id.collegeName);
        collegePass = findViewById(R.id.collegePassword);
        collegePhone = findViewById(R.id.collegePhone);

        collegeSignup = findViewById(R.id.collegeSignup);
        collegeLogin = findViewById(R.id.collegeLogin);

        mAuth = FirebaseAuth.getInstance();

        collegeSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                collegeEmailTxt = collegeEmail.getText().toString();
                collegePhoneTxt = collegePhone.getText().toString();
                collegeNameTxt = collegeName.getText().toString();
                collegePassTxt = collegePass.getText().toString();

                if (collegeEmailTxt.isEmpty() || collegePhoneTxt.isEmpty() || collegeNameTxt.isEmpty() || collegePassTxt.isEmpty()){
                    collegeEmail.setError("Email cannot be empty");
                    collegeEmail.requestFocus();
                    collegePhone.setError("Phone cannot be empty");
                    collegePhone.requestFocus();
                    collegeName.setError("Name cannot be empty");
                    collegeName.requestFocus();
                    collegePass.setError("Password cannot be empty");
                    collegePass.requestFocus();
                }
                else{

                    mAuth.createUserWithEmailAndPassword(collegeEmailTxt,collegePassTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CollegeSignup.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CollegeSignup.this, "Email verification failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                databaseReference.child("colleges").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(collegePhoneTxt)){
                                            Toast.makeText(CollegeSignup.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            databaseReference.child("colleges").child(collegePhoneTxt).child("collegeName").setValue(collegeNameTxt);
                                            databaseReference.child("colleges").child(collegePhoneTxt).child("collegeEmail").setValue(collegeEmailTxt);
                                            databaseReference.child("colleges").child(collegePhoneTxt).child("collegePhone").setValue(collegePhoneTxt);
                                            databaseReference.child("colleges").child(collegePhoneTxt).child("collegePassword").setValue(collegePassTxt);
                                            databaseReference.child("colleges").child(collegePhoneTxt).child("as").setValue("college");

                                            startActivity(new Intent(getApplicationContext(), CollegeLogin.class));
                                            overridePendingTransition(0,0);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                collegeEmail.setError("Email already registered. Please Login");
                                collegeEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        collegeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CollegeLogin.class));
                overridePendingTransition(0,0);
            }
        });

    }
}