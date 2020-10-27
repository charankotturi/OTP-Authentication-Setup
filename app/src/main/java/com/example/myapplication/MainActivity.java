package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.hbb20.CountryCodePicker;



public class MainActivity extends AppCompatActivity {

    private EditText editTxtPhoneNumber;
    private Button btnLogin;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        getSupportActionBar().hide();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTxtPhoneNumber.getText().toString().isEmpty() && editTxtPhoneNumber.getText().toString().length() == 10){

                    String phoneNum = ccp.getSelectedCountryCodeWithPlus()+ editTxtPhoneNumber.getText().toString();

                    Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                    intent.putExtra("NUMBER", phoneNum);
                    startActivity(intent);

                }
            }
        });
    }

    private void initView(){
        btnLogin = findViewById(R.id.login_btn);
        editTxtPhoneNumber = findViewById(R.id.editTxtPhoneNumber);
        ccp = findViewById(R.id.ccp);
    }

}