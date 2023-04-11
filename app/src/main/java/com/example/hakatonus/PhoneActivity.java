package com.example.hakatonus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.PhoneAuthOptions;

public class PhoneActivity extends AppCompatActivity {

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        phone = findViewById(R.id.editTextPhone);

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(phone.getText().toString())
                        .build();
            }
        });
    }
}