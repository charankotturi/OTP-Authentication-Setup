package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";
    private FirebaseAuth fAuth;
    private TextView txtGivenPhoneNumber;
    private EditText editTxtOTP;
    private Button btnNext;
    private String code;
    private TextView countDown;
    private int i;
    private TextView txtResend;
    private boolean verification = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        initView();

        Intent intent = getIntent();
        String number = intent.getStringExtra("NUMBER");
        System.out.println(">>>>>>>>>>>>>>>"+number);

        txtGivenPhoneNumber.setText(number);

        requestOTP(number);
        i=60;
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000){
            @Override
            public void onTick(long l) {
                countDown.setText(String.valueOf(i));
                i--;
            }

            @Override
            public void onFinish() {
                if(verification){
                    requestOTP(number);
                }

            }
        };

        countDownTimer.start();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTxtOTP.getText().toString().length() == 6){
                    Log.d(TAG, "onClick: " + editTxtOTP.getText());
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(code, editTxtOTP.getText().toString());
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                    verification = false;
                }else{

                    Log.d(TAG, "onClick: " + editTxtOTP.getText());
                    Toast.makeText(ThirdActivity.this, "Enter valid code!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 60;
                countDownTimer.start();
            }
        });

    }


    private void requestOTP(String phoneNum){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60L,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        code = s;

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ThirdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            System.out.print(user);
                            Intent intent = new Intent(ThirdActivity.this, Second_Activity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void initView(){
        txtGivenPhoneNumber = findViewById(R.id.txtGivenPhoneNumber);
        editTxtOTP = findViewById(R.id.editTxtCode);
        btnNext = findViewById(R.id.otp_submit);
        countDown = findViewById(R.id.textResendCount);
        fAuth = FirebaseAuth.getInstance();
        txtResend = findViewById(R.id.txt2);
    }
}