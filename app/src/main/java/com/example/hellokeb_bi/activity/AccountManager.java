package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.cor.DiamondMember;
import com.example.hellokeb_bi.pattern.cor.DiscountHandler;
import com.example.hellokeb_bi.pattern.cor.GoldMember;
import com.example.hellokeb_bi.pattern.cor.OrdinaryMember;
import com.example.hellokeb_bi.pattern.cor.SilverMember;
import com.example.hellokeb_bi.pattern.singleton.Authorization;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AccountManager extends AppCompatActivity {
    TextInputEditText username, idCard, phone, email;
    TextInputLayout phoneFixed, emailFixed;
    ImageView kebbiImg;
    Button store, logout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        init();
        getUserInfo();
        store.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editCheck()) {
                    showTip("please check every information has entered!");
                    return;
                }
                setProgressStatus(true);
                FormBody formBody = new FormBody.Builder()
                        .add("email", email.getText().toString())
                        .add("phone", phone.getText().toString())
                        .add("idCard", idCard.getText().toString())
                        .build();

                Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/modifyInfo", Authorization.getInstance().getToken(), formBody, new Callback() {
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
                        } else {
                            showTip(json.getString("msg"));
                        }
                    }

                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setToken("");
                Intent intent = new Intent(AccountManager.this, Login.class);
                startActivity(intent);
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkPhone(phone.getText().toString())) {
                    phoneFixed.setEndIconActivated(true);
                } else {
                    phoneFixed.setEndIconActivated(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkEmail(email.getText().toString())) {
                    emailFixed.setEndIconActivated(true);
                } else {
                    emailFixed.setEndIconActivated(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        store = (Button) findViewById(R.id.accountManager_store);
        logout = (Button) findViewById(R.id.accountManager_logout);
        username = (TextInputEditText) findViewById(R.id.accountManager_username);
        idCard = (TextInputEditText) findViewById(R.id.accountManager_idCard);
        phone = (TextInputEditText) findViewById(R.id.accountManager_phone);
        email = (TextInputEditText) findViewById(R.id.accountManager_email);
        username.setInputType(InputType.TYPE_NULL);
        phoneFixed = (TextInputLayout) findViewById(R.id.accountManager_phoneField);
        emailFixed = (TextInputLayout) findViewById(R.id.accountManager_emailField);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        kebbiImg = (ImageView) findViewById(R.id.kebbiimg);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        toolbar.setTitle("Account Manager");
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

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AccountManager.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToken(String token) {
        SharedPreferences.Editor edit = getSharedPreferences("token", MODE_PRIVATE).edit();
        edit.putString("token", token);
        System.out.println(token);
        edit.commit();
    }

    public void getUserInfo() {
        FormBody formBody = new FormBody.Builder()
                .build();
        setProgressStatus(true);
        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/getUserInfo", Authorization.getInstance().getToken(), formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                setProgressStatus(false);
                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                if (jsonObject.getInteger("code") == 200) {
                    jsonObject = JSONObject.parseObject(jsonObject.getString("data"));
                    setUserInfo(jsonObject.getString("username"), jsonObject.getString("id_card"), jsonObject.getString("phone"), jsonObject.getString("email"), jsonObject.getInteger("point"));
                    showTip("success");
                } else {
                    showTip(jsonObject.getString("msg"));
                }
            }
        });
    }

    private void setUserInfo(String _username, String _idCard, String _phone, String _email, int _point) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DiscountHandler discountHandler = new DiamondMember(new GoldMember(new SilverMember(new OrdinaryMember(null))));
                discountHandler.grade(_point, kebbiImg, AccountManager.this);
                System.out.println(kebbiImg);
                username.setText(_username);
                idCard.setText(_idCard);
                phone.setText(_phone);
                email.setText(_email);
            }
        });
    }

    public boolean editCheck() {
        if (!checkPhone(phone.getText().toString()) || !checkEmail(email.getText().toString()) || !checkIdCard(idCard.getText().toString())) {
            return false;
        }
        return true;
    }

    private boolean checkPhone(String _phone) {
        return _phone.length() == 10;
    }

    private boolean checkEmail(String _email) {
        if ((Pattern.matches("\\w+@(\\w+.)+[a-z]{2,3}", _email))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIdCard(String _idCard) {
        return idCard.length() == 10;
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
