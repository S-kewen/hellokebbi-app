package com.example.hellokeb_bi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.adapter.CancelListAdapter;
import com.example.hellokeb_bi.pattern.adapter.LendListAdapter;
import com.example.hellokeb_bi.pattern.adapter.ProcessingListAdapter;
import com.example.hellokeb_bi.pattern.adapter.RentListAdapter;
import com.example.hellokeb_bi.pattern.singleton.Authorization;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class RentHistory extends AppCompatActivity {
    TabItem procesing, lend, compeleted;
    TabLayout tab;
    ListView list;
    ProgressBar progressBar;
    private BaseAdapter lendListAdapter;
    private BaseAdapter rentListAdapter;
    private BaseAdapter processingListAdapter;
    private BaseAdapter cancelListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_history);
        setTitle("Rent Record");
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.rental_toolbar);
        toolbar.setTitle("Rent History");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        procesing = (TabItem) findViewById(R.id.rentHistory_processing);
        lend = (TabItem) findViewById(R.id.rentHistory_lend);
        compeleted = (TabItem) findViewById(R.id.rentHistory_compeleted);
        list = (ListView) findViewById(R.id.rentHistory_list);
        tab = (TabLayout) findViewById(R.id.rentHistory_tab);
        progressBar = (ProgressBar) findViewById(R.id.progressBar6);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                getOrderList(index + 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getOrderList(1);
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RentHistory.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrderList(int state) {
        clearList();
        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .add("state", String.valueOf(state))
                .add("sortName", "id")
                .add("sortType", "desc")
                .build();
        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/order/getOrderList", Authorization.getInstance().getToken(), formBody, new Callback() {
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
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                    Iterator<Object> iterator = jsonArray.iterator();
                    List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
                    if (jsonArray.size() > 0) {
                        while (iterator.hasNext()) {
                            jsonObject = (JSONObject) iterator.next();
                            try {
                                Map<String, Object> item = new HashMap<String, Object>();
                                item.put("id", jsonObject.getInteger("id"));
                                item.put("pickup_password", jsonObject.getString("pickup_password"));
                                item.put("pickup_time", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.TAIWAN).parse(jsonObject.getString("pickup_time")));
                                item.put("expire_time", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.TAIWAN).parse(jsonObject.getString("expire_time")));
                                item.put("address", jsonObject.getString("address"));
                                item.put("amount", jsonObject.getString("amount"));
                                itemList.add(item);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (state == 1) {
                            insertProcessingList(itemList);
                        } else if (state == 2) {
                            insertLendList(itemList);
                        } else if (state == 3) {
                            insertList(itemList);
                        } else if (state == 4) {
                            insertCancelList(itemList);
                        }

                        showTip("success");
                    } else {
                        showTip("no order");
                    }
                } else {
                    showTip(jsonObject.getString("msg"));
                }
            }
        });
    }

    private void insertList(List<Map<String, Object>> itemList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearList();
                rentListAdapter = new RentListAdapter(RentHistory.this, itemList);
                list.setAdapter(rentListAdapter);
            }
        });
    }

    private void insertCancelList(List<Map<String, Object>> itemList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearList();
                cancelListAdapter = new CancelListAdapter(RentHistory.this, itemList);
                list.setAdapter(cancelListAdapter);
            }
        });
    }

    private void insertLendList(List<Map<String, Object>> itemList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearList();
                lendListAdapter = new LendListAdapter(RentHistory.this, itemList, new LendListAdapter.CallBack() {
                    @Override
                    public void onClick(String order_num) {
                        setProgressStatus(true);
                        //藍芽開門
                        FormBody formBody = new FormBody.Builder()
                                .add("id", order_num)
                                .build();
                        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/order/completeOrder", Authorization.getInstance().getToken(), formBody, new Callback() {
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
                                    showTip("success");
                                    getOrderList(2);
                                } else {
                                    showTip(jsonObject.getString("msg"));
                                }
                            }
                        });
                    }
                });
                list.setAdapter(lendListAdapter);
            }
        });
    }

    private void insertProcessingList(List<Map<String, Object>> itemList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearList();
                processingListAdapter = new ProcessingListAdapter(RentHistory.this, itemList, new ProcessingListAdapter.CallBack() {
                    @Override
                    public void onClick(String order_num) {
                        setProgressStatus(true);
                        FormBody formBody = new FormBody.Builder()
                                .add("id", order_num)
                                .build();
                        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/order/cancelOrder", Authorization.getInstance().getToken(), formBody, new Callback() {
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
                                    showTip("success");
                                    getOrderList(1);
                                } else {
                                    showTip(jsonObject.getString("msg"));
                                }
                            }
                        });
                    }
                });
                list.setAdapter(processingListAdapter);
            }
        });
    }

    private void clearList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(new RentListAdapter(RentHistory.this, new ArrayList<Map<String, Object>>()));
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
