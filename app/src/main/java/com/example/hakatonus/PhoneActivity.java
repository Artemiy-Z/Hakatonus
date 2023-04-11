package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText phone;
    private FirebaseAuth auth;
    private String mVerificationId;
    private View content;
    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        FirebaseApp.initializeApp(getApplicationContext());

        phone = findViewById(R.id.editTextPhone);

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("ru");

        progressBar = findViewById(R.id.progress);

        content = findViewById(R.id.content);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber("+7 "+phone.getText().toString().replace("(", "").replace(")", "-"))
                        .setActivity(PhoneActivity.this)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.d("Verf", "onVerificationCompleted:" + phoneAuthCredential);

                                signInWithPhoneCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);

                                mVerificationId = verificationId;

                                progressBar.setVisibility(View.INVISIBLE);

                                TranslateAnimation anim = new TranslateAnimation(getResources().getDisplayMetrics().widthPixels, 0, 0, 0);
                                anim.setFillAfter(true);
                                anim.setDuration(500);
                                content.startAnimation(anim);
                            }
                        })
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);

                TranslateAnimation anim = new TranslateAnimation(0, -getResources().getDisplayMetrics().widthPixels, 0, 0);
                anim.setFillAfter(true);
                anim.setDuration(500);
                content.startAnimation(anim);
            }
        });
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(PhoneActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(PhoneActivity.this, "Введенный код неверный!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}