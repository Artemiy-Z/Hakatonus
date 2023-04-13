package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText email_registerDri;
    private EditText password_registerDri;
    private AppCompatButton btn_registerDri;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(getApplicationContext());

        final int[] role = {getSharedPreferences("signin", MODE_PRIVATE).getInt("role", 0)};

        if (role[0] == 0) {
            ((TextView) findViewById(R.id.title)).setText("Зарегистрироваться как водитель");
        } else {
            ((TextView) findViewById(R.id.title)).setText("Зарегистрироваться как пассажир");
        }

        email_registerDri = findViewById(R.id.email_registerDri);
        password_registerDri = findViewById(R.id.password_registerDri);
        btn_registerDri = findViewById(R.id.btn_registerDri);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        findViewById(R.id.txt_roleDri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role[0] == 0) {
                    getSharedPreferences("signin", MODE_PRIVATE).edit().putInt("role", 1).apply();
                    role[0] = 1;
                    ((TextView) findViewById(R.id.title)).setText("Зарегистрироваться как пассажир");
                } else {
                    getSharedPreferences("signin", MODE_PRIVATE).edit().putInt("role", 0).apply();
                    role[0] = 0;
                    role[0] = 0;((TextView) findViewById(R.id.title)).setText("Зарегистрироваться как водитель");
                }
            }
        });

        btn_registerDri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email_registerDri.getText().toString().equals("") || password_registerDri.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email_registerDri.getText().toString(), password_registerDri.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String email = email_registerDri.getText().toString();

                            HashMap<String, String> newUser = new HashMap<>();
                            newUser.put("email", email);

                            mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Register.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, IntroActivity.class));
        finish();
    }
}