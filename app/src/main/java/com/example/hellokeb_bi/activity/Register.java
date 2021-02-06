package com.example.hellokeb_bi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.factoryMethod.AlertDialogFactory;
import com.example.hellokeb_bi.pattern.factoryMethod.DialogFactory;
import com.example.hellokeb_bi.pattern.singleton.Authorization;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.hutool.crypto.SecureUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    private Button register;
    private TextInputEditText username, password, checkPassword, phone, email, code;
    private DialogFactory dialogFactory;
    private ProgressBar progressBar;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (editCheck()) {
                    String check = editLengthCheck();
                    if (!check.equals("")) {
                        showTip(check);
                    } else {
                        sendpost();
                    }
                } else {
                    showTip("please check every information has entered!");
                }
            }
        });

    }

    public void init() {
        register = (Button) findViewById(R.id.register_next);
        username = (TextInputEditText) findViewById(R.id.register_username);
        password = (TextInputEditText) findViewById(R.id.register_password);
        checkPassword = (TextInputEditText) findViewById(R.id.register_checkpassword);
        phone = (TextInputEditText) findViewById(R.id.register_phone);
        email = (TextInputEditText) findViewById(R.id.register_email);
        code = (TextInputEditText) findViewById(R.id.register_code);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        dialogFactory = new AlertDialogFactory(this);
    }

    public boolean editCheck() {
        if (username.length() == 0 || password.length() == 0 || checkPassword.length() == 0 || phone.length() == 0 || email.length() == 0 || code.length() == 0) {
            return false;
        }
        return true;
    }

    public String editLengthCheck() {
        if (username.length() < 4) {
            dialog = dialogFactory.createWarnDialog();
            dialog.setTitle("Warnning").setMessage("please check your username length!(too short)").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            showDialog(dialog);
            return "please check your username length!(too short)";
        } else {
            if (username.length() > 12) {
                dialog = dialogFactory.createWarnDialog();
                dialog.setTitle("Warnning").setMessage("please check your username length!(too long)").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                showDialog(dialog);
                return "please check your username length!(too long)";
            } else {
                if (password.length() < 4) {
                    dialog = dialogFactory.createWarnDialog();
                    dialog.setTitle("Warnning").setMessage("please check your password length!(too short)").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    showDialog(dialog);
                    return "please check your password length!(4~16)";
                } else if (password.length() > 16) {
                    dialog = dialogFactory.createWarnDialog();
                    dialog.setTitle("Warnning").setMessage("please check your password length!(too long)").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    showDialog(dialog);
                    return "please check your password length!(4~16)";
                }
            }
        }
        return "";
    }

    public void sendpost() {
        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .add("username", username.getText().toString())
                .add("password", SecureUtil.md5(password.getText().toString()))
                .add("phone", phone.getText().toString())
                .add("email", email.getText().toString())
                .add("code", code.getText().toString())
                .build();

        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/register", "", formBody, new Callback() {
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
                    setToken(JSONObject.parseObject(json.getString("data")).getString("token"));
                    showTip("success");
                    Intent intent = new Intent(Register.this, ScanIntro.class);
                    startActivity(intent);
                } else {
                    showTip(json.getString("msg"));
                }
            }
        });
    }

    public void showDialog(AlertDialog.Builder alertDialog) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });

    }

    public void sendcode(View v) {
        if (email.length() == 0) {
            showTip("please enter email first!");
            return;
        }

        setProgressStatus(true);

        FormBody formBody = new FormBody.Builder()
                .add("email", email.getText().toString())
                .build();

        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/sendCode", "", formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
                //tvRes.setText("POST回傳：\n" + response.body().string());
                setProgressStatus(false);
                JSONObject json = JSONObject.parseObject(response.body().string());
                if (json.getInteger("code") == 200) {
                    showTip("send code success");
                } else {
                    showTip(json.getString("msg"));
                }
            }
        });
    }

    public void setToken(String token) {
        SharedPreferences.Editor edit = getSharedPreferences("token", MODE_PRIVATE).edit();
        edit.putString("token", token);
        edit.commit();
        Authorization.getInstance().setToken(token);
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //这里进行UI操作
                Toast.makeText(Register.this, string, Toast.LENGTH_SHORT).show();
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
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });
    }
}
