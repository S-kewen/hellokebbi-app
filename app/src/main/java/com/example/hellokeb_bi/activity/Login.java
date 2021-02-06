package com.example.hellokeb_bi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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

public class Login extends AppCompatActivity {
    private TextInputEditText username, password;
    private Button login;
    private ProgressBar progressBar;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        DialogFactory dialogFactory = new AlertDialogFactory(this);
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (editCheck()) {
                    String check = editLengthCheck();
                    if (check.equals("")) {
                        sendpost();
                    }
                } else {
                    dialog = dialogFactory.createWarnDialog();
                    dialog.setTitle("Warnning").setMessage("please check every information has entered!").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    showDialog(dialog);
                }

            }
        });
    }

    public void init() {
        login = (Button) findViewById(R.id.login_login);
        username = (TextInputEditText) findViewById(R.id.login_username);
        password = (TextInputEditText) findViewById(R.id.login_password);
        progressBar = (ProgressBar) findViewById(R.id.login_load);
    }

    public boolean editCheck() {
        if (username.length() == 0 || password.length() == 0) {
            return false;
        }
        return true;
    }

    public String editLengthCheck() {
        DialogFactory dialogFactory = new AlertDialogFactory(this);
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
                    return "please check your password length!(too short)";
                } else if (password.length() > 16) {
                    dialog = dialogFactory.createWarnDialog();
                    dialog.setTitle("Warnning").setMessage("please check your password length!(too long)").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    showDialog(dialog);
                    return "please check your password length!(too long)";
                }
            }
        }
        return "";
    }

    public void sendpost() {
        setProgressStatus(true);
        DialogFactory dialogFactory = new AlertDialogFactory(this);

        FormBody formBody = new FormBody.Builder()
                .add("username", username.getText().toString())
                .add("password", SecureUtil.md5(password.getText().toString()))
                .build();

        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/login", "", formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                setProgressStatus(true);
                dialog = dialogFactory.createWarnDialog();
                dialog.setTitle("Warnning").setMessage("network error!").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                showDialog(dialog);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
                //tvRes.setText("POST回傳：\n" + response.body().string());
                JSONObject json = JSONObject.parseObject(response.body().string());
                if (json.getInteger("code") == 200) {
                    setToken(JSONObject.parseObject(json.getString("data")).getString("token"));
                    setProgressStatus(false);
                    dialog = dialogFactory.createSuccDialog();
                    dialog.setTitle("Successful").setMessage("success").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Login.this, Homepage.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    showDialog(dialog);
                } else {
                    setProgressStatus(false);
                    dialog = dialogFactory.createErrorDialog();
                    dialog.setTitle("Error").setMessage(json.getString("msg")).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    showDialog(dialog);
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

    public void setToken(String token) {
        SharedPreferences.Editor edit = getSharedPreferences("token", MODE_PRIVATE).edit();
        edit.putString("token", token);
        edit.commit();
        Authorization.getInstance().setToken(token);
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
