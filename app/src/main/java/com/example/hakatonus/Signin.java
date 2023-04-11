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

public class Signin extends AppCompatActivity {

    private EditText email_signin;
    private EditText password_signin;
    private AppCompatButton btn_signin;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email_signin = findViewById(R.id.email_signin);
        password_signin = findViewById(R.id.password_signin);
        btn_signin = findViewById(R.id.btn_signin);
        mauth = FirebaseAuth.getInstance();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_signin.getText().toString().isEmpty() || password_signin.getText().toString().isEmpty()){
                    Toast.makeText(Signin.this, "поля не заполнены", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.signInWithEmailAndPassword(email_signin.getText().toString(),
                                    password_signin.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent());
                                    } else {
                                        Toast.makeText(Signin.this, "такой пользователь уже существует ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

    }
}