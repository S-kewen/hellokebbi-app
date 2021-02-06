package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.singleton.Authorization;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Authorization.getInstance().setToken(getSharedPreferences("token", MODE_PRIVATE).getString("token", ""));
        String token = Authorization.getInstance().getToken();

        if (token.length() != 0) {
            setProgressStatus(true);
            showTip("logging in...");
            Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/checkToken", token, new FormBody.Builder().build(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    setProgressStatus(false);
                    showTip("network error");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    setProgressStatus(false);
                    JSONObject json = JSONObject.parseObject(response.body().string());
                    if (json.getInteger("code") == 200) {
                        showTip("success");
                        Intent intent = new Intent(MainActivity.this, Homepage.class);
                        startActivity(intent);
                    } else {
                        showTip(json.getString("msg"));
                    }
                }
            });
        }
    }

    public void init() {
        progressBar = (ProgressBar) findViewById(R.id.main_load);
    }

    public void register(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void login(View v) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
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
