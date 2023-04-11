package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText email_register;
    private EditText password_register;
    private AppCompatButton btn_register;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_register = findViewById(R.id.email_register);
        password_register = findViewById(R.id.password_register);
        btn_register = findViewById(R.id.btn_register);
        mauth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_register.getText().toString().isEmpty() || password_register.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "поля не заполнены", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.signInWithEmailAndPassword(email_register.getText().toString(),
                                    password_register.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent());
                                    } else {
                                        Toast.makeText(Register.this, "такой пользователь уже существует ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

    }
}