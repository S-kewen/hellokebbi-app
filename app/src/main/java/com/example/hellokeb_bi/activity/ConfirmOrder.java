package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.singleton.Authorization;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ConfirmOrder extends AppCompatActivity {
    private String startTime, endTime, country, town, ibox, rid;
    private double money;
    private ProgressBar progressBar;
    private Date date1, date2;
    private TextView confirm_date_text, confirm_location_text, confirm_box_text, confirm_money_text;
    private double discount = 1;
    private long day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        overridePendingTransition(0, 0);

        init();

        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) :
                (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        money = (day) * 100;


        FormBody formBody = new FormBody.Builder()
                .build();
        setProgressStatus(true);
        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/user/getUserDiscount", Authorization.getInstance().getToken(), formBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                setProgressStatus(false);
                showTip("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                setProgressStatus(false);
                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                System.out.println(jsonObject.toJSONString() + "    1231321321321321");
                if (jsonObject.getInteger("code") == 200) {
                    discount = JSONObject.parseObject(jsonObject.getString("data")).getDouble("discount");
                    setInfo(JSONObject.parseObject(jsonObject.getString("data")).getString("level"));
                } else {
                    showTip(jsonObject.getString("msg"));
                }
            }
        });
    }

    public void setInfo(String grade) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                money = Math.round(money * discount);

                confirm_date_text.setText(startTime + "  ~  " + endTime);
                confirm_location_text.setText(country + "-" + town);
                confirm_box_text.setText(ibox);
                confirm_money_text.setText(String.valueOf(money) + "(you're " + grade + "member ! price * " + discount);
            }
        });
    }

    public void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar11);
        confirm_date_text = (TextView) findViewById(R.id.confirmOrder_date);
        confirm_location_text = (TextView) findViewById(R.id.confirmOrder_location);
        confirm_box_text = (TextView) findViewById(R.id.confirmOrder_box);
        confirm_money_text = (TextView) findViewById(R.id.confirm_money);
        Intent intent = this.getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        country = intent.getStringExtra("country");
        town = intent.getStringExtra("town");
        ibox = intent.getStringExtra("ibox");
        rid = intent.getStringExtra("rid");

    }

    public void placeOrder(View v) throws ParseException {
        Intent intent = new Intent(ConfirmOrder.this, Otp.class);
        intent.putExtra("startTime", new SimpleDateFormat("yyyy-MM-dd").format(date1) + " 12:00:00");
        intent.putExtra("endTime", new SimpleDateFormat("yyyy-MM-dd").format(date2) + " 23:59:59");
        intent.putExtra("country", country);
        intent.putExtra("town", town);
        intent.putExtra("ibox", ibox);
        intent.putExtra("rid", rid);
        intent.putExtra("money", String.valueOf(money));
        intent.putExtra("day", String.valueOf(day));
        startActivity(intent);
    }

    public void cancel(View v) {
        finish();
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

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ConfirmOrder.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onPopupButtonClick(View button) {

        final PopupMenu popup = new PopupMenu(this, button);
        getMenuInflater().inflate(R.menu.customer_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account:
                        popup.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(ConfirmOrder.this, AccountManager.class);
                        startActivity(intent);
                        break;

                    case R.id.credit:
                        popup.dismiss();
                        Intent intent2 = new Intent();
                        intent2.setClass(ConfirmOrder.this, CreditCard.class);
                        startActivity(intent2);
                        break;

                    case R.id.record:
                        popup.dismiss();
                        Intent intent3 = new Intent();
                        intent3.setClass(ConfirmOrder.this, RentHistory.class);
                        startActivity(intent3);
                        break;

                }
                // TODO Auto-generated method stub
                return false;
            }

        });
        popup.show();
    }

    public void onPopupButtonClick2(View button) {

        final PopupMenu popup = new PopupMenu(this, button);
        getMenuInflater().inflate(R.menu.info_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user:
                        popup.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(ConfirmOrder.this, TradeDetail.class);
                        startActivity(intent);
                        break;

                    case R.id.customerser:
                        popup.dismiss();
                        Intent intent2 = new Intent();
                        intent2.setClass(ConfirmOrder.this, CustomerService.class);
                        startActivity(intent2);
                        break;

                    case R.id.hellokebbi:
                        popup.dismiss();
                        Intent intent3 = new Intent();
                        intent3.setClass(ConfirmOrder.this, AboutHelloKebbi.class);
                        startActivity(intent3);
                        break;

                }
                // TODO Auto-generated method stub
                return false;
            }

        });
        popup.show();
    }
}
