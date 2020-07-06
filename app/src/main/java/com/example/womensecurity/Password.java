package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Password extends AppCompatActivity {

    private EditText resetmail;
    private Button resetpwd;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        firebaseAuth=FirebaseAuth.getInstance();

        //assign variables
        resetmail=(EditText) findViewById(R.id.EmailAddress);
        resetpwd=(Button) findViewById(R.id.password_reset);


        resetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail=resetmail.getText().toString().trim();
                if(e_mail.equals("")){
                    Toast.makeText(Password.this, "please enter your registered mail", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Password.this, "password reset mail has been sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Password.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(Password.this, "Error in sending password link", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}