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

public class RegisterToPassenger extends AppCompatActivity {

    private EditText email_registerPas;
    private EditText password_registerPas;
    private AppCompatButton btn_registerPas;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_passenger);

        email_registerPas = findViewById(R.id.email_registerPas);
        password_registerPas = findViewById(R.id.password_registerPas);
        btn_registerPas = findViewById(R.id.btn_registerPas);
        mauth = FirebaseAuth.getInstance();

        btn_registerPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_registerPas.getText().toString().isEmpty() || password_registerPas.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterToPassenger.this, "поля не заполнены", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.createUserWithEmailAndPassword(email_registerPas.getText().toString(),
                                    password_registerPas.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent());
                                    } else {
                                        Toast.makeText(RegisterToPassenger.this, "такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(RegisterToPassenger.this,IntroActivity.class));
        finish();
    }

}