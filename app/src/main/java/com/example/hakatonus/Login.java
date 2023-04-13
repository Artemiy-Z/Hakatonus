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

public class Login extends AppCompatActivity {



    private EditText email_login;
    private EditText password_login;
    private AppCompatButton btn_login;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final int[] role = new int[] {getSharedPreferences("signin", MODE_PRIVATE).getInt("role", 0)};

        if(role[0] == 0) {
            ((TextView) findViewById(R.id.title)).setText("Войти как водитель");
        } else {
            ((TextView) findViewById(R.id.title)).setText("Войти как пассажир");
        }

        FirebaseApp.initializeApp(getApplicationContext());

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);
        mauth = FirebaseAuth.getInstance();

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_login.getText().toString().isEmpty() || password_login.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "поля не заполнены", Toast.LENGTH_SHORT).show();
                } else {
                    mauth.signInWithEmailAndPassword(email_login.getText().toString(),
                            password_login.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "ошибка", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        findViewById(R.id.txt_roleDri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role[0] == 0) {
                    getSharedPreferences("signin", MODE_PRIVATE).edit().putInt("role", 1).apply();
                    role[0] = 1;
                    ((TextView) findViewById(R.id.title)).setText("Войти как пассажир");
                } else {
                    getSharedPreferences("signin", MODE_PRIVATE).edit().putInt("role", 0).apply();
                    role[0] = 0;
                    role[0] = 0;((TextView) findViewById(R.id.title)).setText("Войти как водитель");
                }
            }
        });

    }
}