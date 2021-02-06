package com.example.hellokeb_bi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hellokeb_bi.R;
import com.google.android.material.card.MaterialCardView;

public class AboutHelloKebbi extends AppCompatActivity {
    MaterialCardView version, policy, aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_hello_kebbi);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setTitle("About HelloKebbi");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("v1.0.0");
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("@By OOSE Group3");
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("hello kebbi~");
            }
        });

    }

    private void init() {
        version = (MaterialCardView) findViewById(R.id.about_version);
        policy = (MaterialCardView) findViewById(R.id.about_policy);
        aboutUs = (MaterialCardView) findViewById(R.id.about_aboutUs);
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AboutHelloKebbi.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }
}