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

public class RegisterToDriver extends AppCompatActivity {

    private EditText email_registerDri;
    private EditText password_registerDri;
    private AppCompatButton btn_registerDri;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_driver);

        email_registerDri = findViewById(R.id.email_registerDri);
        password_registerDri = findViewById(R.id.password_registerDri);
        btn_registerDri = findViewById(R.id.btn_registerDri);
        mauth = FirebaseAuth.getInstance();

        btn_registerDri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_registerDri.getText().toString().isEmpty() || password_registerDri.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterToDriver.this, "поля не заполнены", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.signInWithEmailAndPassword(email_registerDri.getText().toString(),
                                    password_registerDri.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent());
                                    } else {
                                        Toast.makeText(RegisterToDriver.this, "такой пользователь уже существует ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterToDriver.this, IntroActivity.class));
        finish();
    }
}