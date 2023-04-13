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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterToDriver extends AppCompatActivity {

    private EditText email_registerDri;
    private EditText password_registerDri;
    private AppCompatButton btn_registerDri;
    private DatabaseReference mDatabase;
    private String USER_KEY = "User";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_driver);

        email_registerDri = findViewById(R.id.email_registerDri);
        password_registerDri = findViewById(R.id.password_registerDri);
        btn_registerDri = findViewById(R.id.btn_registerDri);

        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        btn_registerDri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mDatabase.getKey();
                String email = email_registerDri.getText().toString();

                User newUser = new User(id,email);
                mDatabase.push().setValue(newUser);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterToDriver.this, IntroActivity.class));
        finish();
    }
}