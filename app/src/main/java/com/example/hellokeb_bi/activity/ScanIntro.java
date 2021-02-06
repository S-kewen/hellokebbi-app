package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellokeb_bi.R;

public class ScanIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanintro);

        Button btn1 = (Button) findViewById(R.id.scanintro_scan);

        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(ScanIntro.this, Scan.class);
                startActivity(intent);
            }
        });
    }
}
