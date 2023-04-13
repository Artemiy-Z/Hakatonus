package com.example.hakatonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText email_registerDri;
    private EditText password_registerDri;
    private AppCompatButton btn_registerDri;
    private DatabaseReference mDatabase;
    private String USER_KEY = "User";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSharedPreferences("signin", ""))

        email_registerDri = findViewById(R.id.email_registerDri);
        password_registerDri = findViewById(R.id.password_registerDri);
        btn_registerDri = findViewById(R.id.btn_registerDri);

        mDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        btn_registerDri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_registerDri.getText().toString();

                HashMap<String, String> newUser = new HashMap<>();
                newUser.put("email", email);

                mDatabase.child(FirebaseAuth.getInstance().getUid()).setValue(newUser);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, IntroActivity.class));
        finish();
    }
}