package com.example.womensecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText pwd;
    private Button Login;
    private FirebaseAuth firebaseAuth;
    private TextView userregister, forgetpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variables

        Username = (EditText) findViewById(R.id.etext1);
        pwd = (EditText) findViewById(R.id.etext2);
        Login = (Button) findViewById(R.id.button);
        userregister = (TextView) findViewById(R.id.txt);
        forgetpwd = (TextView) findViewById(R.id.forgetpwd);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            Toast.makeText(this, "exisiting user", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Information.class));
        }


        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Password.class));
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Username.getText().toString(), pwd.getText().toString());
            }
        });

        userregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registration.class));
            }
        });


    }

    private void validate(String user_name, String password) {
        firebaseAuth.signInWithEmailAndPassword(user_name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Toast.makeText(MainActivity.this, "login Successfully", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this,Information.class));
                    checkmailverification();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
//function check user verified

    private void checkmailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean mail_flag = firebaseUser.isEmailVerified();
        if (mail_flag) {
            finish();
            startActivity(new Intent(MainActivity.this, Information.class));
        } else {
            Toast.makeText(this, "Please verify your Email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}