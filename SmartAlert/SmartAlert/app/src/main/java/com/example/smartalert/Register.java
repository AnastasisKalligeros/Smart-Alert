package com.example.smartalert;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button button2;
    private Spinner spinner;
    
    DatabaseReference userdatabaseref;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button2 = findViewById(R.id.buttonRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fullname,password,email,phone;
                fullname = findViewById(R.id.editTextTextUsername);
                password = findViewById(R.id.editTextTextPassword);
                email = findViewById(R.id.editTextTextEmailAddress);
                phone=findViewById(R.id.editTextPhone);
                userdatabaseref= FirebaseDatabase.getInstance().getReference().child("Users");
                if(TextUtils.isEmpty(fullname.getText().toString())) {
                    Toast.makeText(Register.this, "Enter fullname", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((password.getText().toString()))) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((email.getText().toString()))) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(Register.this, "Enter phone", Toast.LENGTH_SHORT).show();

                }else {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Users users= new Users();
                                    users.fullname=fullname.getText().toString();
                                    users.password=password.getText().toString();
                                    users.role=spinner.getSelectedItem().toString();
                                    users.email=email.getText().toString();
                                    users.Phone=phone.getText().toString();
                                    userdatabaseref.push().setValue(users);
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();    
                                }
                            });
                    
                }


            }
        });







        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}

