package com.example.womensecurity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Emergencycontacts extends AppCompatActivity {
    private EditText firstname,first_mail,first_phone,secoundname,secound_mail,secound_phone;
    private Button submit;
    private EditText thirdname,third_mail,third_phone;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencycontacts);

        //assign variables

        firstname= findViewById(R.id.et_firstusername);
        first_mail= findViewById(R.id.first_mail);
        first_phone= findViewById(R.id.first_mobile);
        secoundname= findViewById(R.id.second_username);
        secound_mail= findViewById(R.id.second_mail);
        secound_phone= findViewById(R.id.second_mobile);
        thirdname= findViewById(R.id.et_thirdusername);
        third_mail= findViewById(R.id.third_mail);
        third_phone= findViewById(R.id.third_mobile);
        submit=findViewById(R.id.bt_submit);

        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname,fmail,fphone,sname,smail,sphone,tname,tmail,tphone;
                fname=firstname.getText().toString().trim();
                fmail=first_mail.getText().toString().trim();
                fphone=first_phone.getText().toString().trim();
                sname=secoundname.getText().toString().trim();
                smail=secound_mail.getText().toString().trim();
                sphone=secound_phone.getText().toString().trim();
                tname=thirdname.getText().toString().trim();
                tmail=third_mail.getText().toString().trim();
                tphone=third_phone.getText().toString().trim();


                if (validate()){
                    String userid=firebaseAuth.getCurrentUser().getUid();
                  DocumentReference documentReference=firestore.collection("Users").document(userid).collection("Emergency Contacts").document(userid);
                    Map<String,Object>user=new HashMap<>();
                    user.put("First person name",fname);
                    user.put("First Email",fmail);
                    user.put("First  Phone no",fphone);
                    user.put("Secound person name",sname);
                    user.put("Secound Email",smail);
                    user.put("secound Phone no",sphone);
                    user.put("Third person name",fname);
                    user.put("Third Email",fmail);
                    user.put("Third Phone no",fphone);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "user contacts created");
                            finish();
                            //move to information page
                            startActivity(new Intent(Emergencycontacts.this,Information.class));

                        }
                    });

                }

            }
        });


    }

    private Boolean validate()
    {
        boolean res=false;
        String fname,fmail,fphone,sname,smail,sphone,tname,tmail,tphone;
        fname=firstname.getText().toString().trim();
        fmail=first_mail.getText().toString().trim();
        fphone=first_phone.getText().toString().trim();
        sname=secoundname.getText().toString().trim();
        smail=secound_mail.getText().toString().trim();
        sphone=secound_phone.getText().toString().trim();
        tname=thirdname.getText().toString().trim();
        tmail=third_mail.getText().toString().trim();
        tphone=third_phone.getText().toString().trim();

        if(fname.isEmpty() || fmail.isEmpty()||fphone.isEmpty()||sname.isEmpty()||sphone.isEmpty()||smail.isEmpty()||tname.isEmpty()||tmail.isEmpty()||tphone.isEmpty()) {
            Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(fmail).matches()||!Patterns.EMAIL_ADDRESS.matcher(smail).matches()||!Patterns.EMAIL_ADDRESS.matcher(tmail).matches())
            Toast.makeText(this, "please enter valid Email Address", Toast.LENGTH_SHORT).show();
        else if(!Patterns.PHONE.matcher(sphone).matches()||!Patterns.PHONE.matcher(sphone).matches()||!Patterns.PHONE.matcher(tphone).matches())
            Toast.makeText(this, "please enter valid phone numbers", Toast.LENGTH_SHORT).show();
        else
            res=true;

        return res;
    }
}