package com.example.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

     EditText username , password;

     Button button3;
    DatabaseReference userdatabaseref;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button3 = findViewById(R.id.buttonLogin);
        firebaseAuth=FirebaseAuth.getInstance();
        final ArrayList<Users> list=new ArrayList<>();
        userdatabaseref= FirebaseDatabase.getInstance().getReference().child("Users");
        userdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren() ){
                    Users user1=snapshot1.getValue(Users.class);
                    list.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean flag=false;
                EditText mail, password;
                mail = findViewById(R.id.editTextUsername);
                password = findViewById(R.id.editTextPassword);

                if(TextUtils.isEmpty(mail.getText().toString())) {
                    Toast.makeText(Login.this, "Enter mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((password.getText().toString()))) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                        firebaseAuth.signInWithEmailAndPassword(mail.getText().toString(),password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        for(int i=0;i<list.size();i++){
                                            if(list.get(i).email.equals(mail.getText().toString())){
                                                if (list.get(i).role.equals("Civilian")){
                                                    Intent intent = new Intent(Login.this, Index.class);
                                                    startActivity(intent);
                                                }else {
                                                    Intent intent = new Intent(Login.this, CivilProtectionIndex.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

            }

        });

            }


}