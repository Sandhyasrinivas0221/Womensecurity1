package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth.*;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class Registration extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText reg_username, reg_email, reg_phone, reg_password;
    private Button regbutton;
    private TextView login;
    private FirebaseAuth firebaseAuth;
    String user_name, user_mail, user_phone;
    private FirebaseFirestore firestore;
    private String Userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //assign variables

        reg_username = (EditText) findViewById(R.id.user_name);
        reg_email = (EditText) findViewById(R.id.et_mail);
        reg_phone = (EditText) findViewById(R.id.et_mobile);
        reg_password = (EditText) findViewById(R.id.et_password);
        regbutton = (Button) findViewById(R.id.bt_submit);
        login = (TextView) findViewById(R.id.txt_1);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_email = reg_email.getText().toString().trim();
                final String user_password = reg_password.getText().toString().trim();
                final String phone_no=reg_phone.getText().toString();
                final String userName=reg_username.getText().toString();


                if (validate()) {
                    //upload to database

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Userid=firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=firestore.collection("Users").document(Userid);
                                Map<String,Object>user=new HashMap<>();
                                user.put("User Name",userName);
                                user.put("Email Address:",user_email);
                                user.put("Phone no",phone_no);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Log.d(TAG, "user created");

                                    }
                                });
                                sendmailverification();
                            }
                            else {
                                Toast.makeText(Registration.this, "Registration Failed! please ensure your password  length is atleast 6!"
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });
    }

    //function to validate all the fields

    private Boolean validate() {
        Boolean res = false;
        user_name = reg_username.getText().toString();
        user_mail = reg_email.getText().toString();
        user_phone = reg_phone.getText().toString();
        String user_pwd = reg_password.getText().toString();
        if (user_name.isEmpty() || user_mail.isEmpty() || user_phone.isEmpty() || user_pwd.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else res = true;
        return res;
    }
    //function for email verification

    private void sendmailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this, MainActivity.class));
                    } else {
                        Toast.makeText(Registration.this, "Error occured ", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }
}