package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellokeb_bi.R;

public class OtpSuccessful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_successful);
    }

    public void rentalRecord(View v) {
        Intent intent = new Intent(this, RentHistory.class);
        startActivity(intent);
        finish();
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }
}
