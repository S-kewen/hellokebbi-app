package com.example.hellokeb_bi.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hellokeb_bi.R;

public class CustomerService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_toolbar);
        toolbar.setTitle("Customer Service");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
