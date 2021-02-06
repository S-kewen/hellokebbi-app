package com.example.hellokeb_bi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.singleton.Authorization;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CreditCard extends AppCompatActivity {
    EditText cardName, cardNum, cardExpireDate, cardCcv;
    Button store, remove;
    TextView previewId, previewName, previewNum, previewExpire;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        init();
        store.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editCheck()) {
                    showTip("please check every information has entered!");
                    return;
                }
                if (previewId.getText().toString().length() > 0) {//update
                    setProgressStatus(true);
                    FormBody formBody = new FormBody.Builder()
                            .add("id", previewId.getText().toString())
                            .add("name", cardName.getText().toString())
                            .add("cardNum", cardNum.getText().toString())
                            .add("goodThru", cardExpireDate.getText().toString())
                            .add("password", cardCcv.getText().toString())
                            .build();

                    Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/card/modifyCardInfo", Authorization.getInstance().getToken(), formBody, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            setProgressStatus(false);
                            showTip("network error!");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            /**取得回傳*/
                            //tvRes.setText("POST回傳：\n" + response.body().string());
                            setProgressStatus(false);
                            JSONObject json = JSONObject.parseObject(response.body().string());
                            if (json.getInteger("code") == 200) {
                                showTip("success");
                                getCardInfo();
                            } else {
                                showTip(json.getString("msg"));
                            }
                        }
                    });
                } else {//add
                    progressBar.setVisibility(View.VISIBLE);
                    FormBody formBody = new FormBody.Builder()
                            .add("name", cardName.getText().toString())
                            .add("cardNum", cardNum.getText().toString())
                            .add("goodThru", cardExpireDate.getText().toString())
                            .add("password", cardCcv.getText().toString())
                            .build();

                    Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/card/addCard", Authorization.getInstance().getToken(), formBody, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            /**如果傳送過程有發生錯誤*/
                            //tvRes.setText(e.getMessage());
                            setProgressStatus(false);
                            new AlertDialog.Builder(CreditCard.this)
                                    .setTitle("Error!")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            /**取得回傳*/
                            //tvRes.setText("POST回傳：\n" + response.body().string());
                            setProgressStatus(false);
                            JSONObject json = JSONObject.parseObject(response.body().string());
                            if (json.getInteger("code") == 200) {
                                showTip("success");
                                getCardInfo();
                            } else {
                                showTip(json.getString("msg"));
                            }
                        }
                    });
                }

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (previewId.getText().toString().length() == 0) {
                    showTip("please add a card first");
                    return;
                }
                setProgressStatus(true);
                FormBody formBody = new FormBody.Builder()
                        .add("id", previewId.getText().toString())
                        .build();

                Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/card/deleteCard", Authorization.getInstance().getToken(), formBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        setProgressStatus(false);
                        new AlertDialog.Builder(CreditCard.this)
                                .setTitle("Error!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        setProgressStatus(false);
                        JSONObject json = JSONObject.parseObject(response.body().string());
                        if (json.getInteger("code") == 200) {
                            showTip("success");
                            setCardInfo("", "", "", "", "");
                            getCardInfo();
                        } else {
                            showTip(json.getString("msg"));
                        }
                    }
                });
            }
        });
    }

    public void init() {
        store = (Button) findViewById(R.id.card_store);
        remove = (Button) findViewById(R.id.card_remove);
        cardName = (EditText) findViewById(R.id.card_name);
        cardNum = (EditText) findViewById(R.id.card_number);
        cardExpireDate = (EditText) findViewById(R.id.card_expireDate);
        cardCcv = (EditText) findViewById(R.id.card_ccv);
        previewName = (TextView) findViewById(R.id.card_preview_name);
        previewNum = (TextView) findViewById(R.id.card_preview_number);
        previewExpire = (TextView) findViewById(R.id.card_preview_expire);
        previewId = (TextView) findViewById(R.id.card_preview_id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.credit_toolbar);
        toolbar.setTitle("Credit Card");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCardInfo();
    }

    public boolean editCheck() {
        if (cardName.length() == 0 || cardNum.length() == 0 || cardExpireDate.length() == 0 || cardCcv.length() == 0) {
            return false;
        }
        return true;
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CreditCard.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCardInfo(String _id, String _cardName, String _cardNum, String _cardExpireDate, String _cardCcv) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewId.setText(_id);
                cardName.setText(_cardName);
                cardNum.setText(_cardNum);
                cardExpireDate.setText(_cardExpireDate);
                cardCcv.setText(_cardCcv);
                previewName.setText(_cardName);
                previewNum.setText(_cardNum);
                previewExpire.setText(_cardExpireDate);
            }
        });
    }

    private void getCardInfo() {
        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .add("sortName", "id")
                .add("sortType", "desc")
                .build();
        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/card/getCardList", Authorization.getInstance().getToken(), formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                setProgressStatus(false);
                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                System.out.println(jsonObject.toJSONString());
                if (jsonObject.getInteger("code") == 200) {
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                    Iterator<Object> iterator = jsonArray.iterator();
                    if (jsonArray.size() > 0) {
                        while (iterator.hasNext()) {
                            jsonObject = (JSONObject) iterator.next();
                            setCardInfo(String.valueOf(jsonObject.getInteger("id")), jsonObject.getString("name"), jsonObject.getString("card_num"), jsonObject.getString("good_thru"), jsonObject.getString("password"));
                            break;
                        }
                        showTip("success");
                    } else {
                        showTip("no card");
                    }
                } else {
                    showTip(jsonObject.getString("msg"));
                }
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
