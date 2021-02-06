package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellokeb_bi.R;

public class Successfully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully);

        Button btn1 = (Button) findViewById(R.id.registerSuccessful_login);

        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Successfully.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
