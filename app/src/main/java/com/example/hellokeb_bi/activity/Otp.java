package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.singleton.Authorization;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class Otp extends AppCompatActivity {
    ProgressBar progressBar;
    private Button btnGetcode;
    private TextInputEditText otp_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        init();
        sendpost();
    }

    public void init() {
        btnGetcode = (Button) findViewById(R.id.otp_sendCode);
        progressBar = (ProgressBar) findViewById(R.id.otp_load);
        otp_code = (TextInputEditText) findViewById(R.id.otp_code);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.otp_toolbar);
        toolbar.setTitle("OTP Verification");
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

    public void sendPassword(View v) {
        countDownTimer();
        sendpost();
    }

    public void sendpost() {

        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .build();

        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/order/sendSmsCode", Authorization.getInstance().getToken(), formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                setProgressStatus(false);
                JSONObject json = JSONObject.parseObject(response.body().string());
                System.out.println(json.toJSONString());
                if (json.getInteger("code") == 200) {
                    showTip("success");
                } else {
                    showTip(json.getString("msg"));
                }
            }
        });
    }

    public void countDownTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnGetcode.setBackgroundColor(Color.parseColor("#919191"));
                btnGetcode.setClickable(false);
                btnGetcode.setText(String.format(Locale.getDefault(), "Resend after  %d seconds", millisUntilFinished / 1000L));
            }

            public void onFinish() {
                btnGetcode.setText("Get the verification code again");
                btnGetcode.setClickable(true);
                btnGetcode.setBackgroundColor(Color.parseColor("#1D2655"));
            }
        }.start();
    }

    public void submit(View v) {
        if (otp_code.length() == 0) {
            showTip("please enter the otp code!");
            return;
        } else if (otp_code.length() != 6) {
            showTip("Enter the wrong length!(6 words)");
            return;
        }

        String startTime, endTime, country, town, ibox, money, rid, day;

        Intent intent = this.getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        country = intent.getStringExtra("country");
        town = intent.getStringExtra("town");
        ibox = intent.getStringExtra("ibox");
        rid = intent.getStringExtra("rid");
        day = intent.getStringExtra("day");
        System.out.println(intent.getStringExtra("startTime"));
        System.out.println(intent.getStringExtra("endTime"));
        System.out.println(intent.getStringExtra("country"));
        System.out.println(intent.getStringExtra("town"));
        System.out.println(intent.getStringExtra("ibox"));
        System.out.println(intent.getStringExtra("rid"));
        System.out.println(intent.getStringExtra("day"));

        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .add("rid", rid)
                .add("day", day)
                .add("pickupTime", startTime)
                .add("address", country + "-" + ibox)
                .add("expireTime", endTime)
                .add("code", otp_code.getText().toString())
                .build();

        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/order/addOrder", Authorization.getInstance().getToken(), formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                setProgressStatus(false);
                JSONObject json = JSONObject.parseObject(response.body().string());
                if (json.getInteger("code") == 200) {
                    showTip("success");
                    Intent intent = new Intent(Otp.this, OtpSuccessful.class);
                    startActivity(intent);
                    finish();
                } else {
                    showTip(json.getString("msg"));
                }
            }
        });

    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Otp.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProgressStatus(boolean val) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (val) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}


