package com.example.hellokeb_bi.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.entity.RentRecord;
import com.example.hellokeb_bi.network.Okhttp;
import com.example.hellokeb_bi.pattern.adapter.CustomExpandableListAdapter;
import com.example.hellokeb_bi.pattern.singleton.Authorization;
import com.example.hellokeb_bi.pattern.strategy.SortByMoney;
import com.example.hellokeb_bi.pattern.strategy.SortByTime;
import com.example.hellokeb_bi.pattern.strategy.SortRentRecordStrategy;

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


public class TradeDetail extends AppCompatActivity {

    ProgressBar progressBar;
    private ExpandableListView expandableListView;
    private BaseExpandableListAdapter expandableListAdapter;
    private List<String> recordKey;
    private HashMap<String, RentRecord> historyRecord;
    private int lastExpandedPosition = -1;
    private Context context;
    private SortRentRecordStrategy sortStrategy;
    private Spinner sortSpn;
    private AdapterView.OnItemSelectedListener sortAdapter = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e("spn", Integer.toString(position));
            switch (position) {
                case 0: // by time desc
                    sortStrategy = new SortByTime();
                    recordKey = sortStrategy.sort(historyRecord, false);
                    break;
                case 1: // by time asc
                    sortStrategy = new SortByTime();
                    recordKey = sortStrategy.sort(historyRecord, true);
                    break;
                case 2: // by money desc
                    sortStrategy = new SortByMoney();
                    recordKey = sortStrategy.sort(historyRecord, false);
                    break;
                case 3: // by money asc
                    sortStrategy = new SortByMoney();
                    recordKey = sortStrategy.sort(historyRecord, true);
                    break;
            }
            expandableListAdapter = new CustomExpandableListAdapter(context, recordKey, historyRecord);
            expandableListView.setAdapter(expandableListAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.trans_toolbar);
        toolbar.setTitle("Transaction Record");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = this;

        sortSpn = findViewById(R.id.spnSort);
        sortSpn.setOnItemSelectedListener(sortAdapter);

        init();

        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    private void init() {
        this.expandableListView = findViewById(R.id.expandableListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar10);
        historyRecord = getHistoryRecord();
    }

   /* private void inserEmptyList() {
        historyRecord = new HashMap<>();
        this.recordKey = new ArrayList<>(historyRecord.keySet());
        this.expandableListAdapter = new CustomExpandableListAdapter(this,
                recordKey, historyRecord);
    }*/

    private HashMap<String, RentRecord> getHistoryRecord() {
        HashMap<String, RentRecord> tmp = new HashMap<>();
        setProgressStatus(true);
        FormBody formBody = new FormBody.Builder()
                .add("type", "0")
                .add("sortName", "id")
                .add("sortType", "desc")
                .build();
        Okhttp.post("https://hellokebbi-api.iskwen.com/v1/api/transaction/getTransactionList", Authorization.getInstance().getToken(), formBody, new Callback() {
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
                    List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
                    int i = 1;
                    if (jsonArray.size() > 0) {
                        while (iterator.hasNext()) {
                            jsonObject = (JSONObject) iterator.next();
                            try {
                                tmp.put(String.valueOf(i), new RentRecord(jsonObject.getInteger("id"), jsonObject.getInteger("type"), jsonObject.getInteger("state"), jsonObject.getInteger("amount"), jsonObject.getString("title"), jsonObject.getString("msg"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.TAIWAN).parse(jsonObject.getString("add_time"))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sortStrategy = new SortByTime();
                                recordKey = sortStrategy.sort(historyRecord, false);
                                expandableListAdapter = new CustomExpandableListAdapter(TradeDetail.this,
                                        recordKey, historyRecord);
                                expandableListView.setAdapter(expandableListAdapter);
                            }
                        });
                        showTip("success");
                    } else {
                        showTip("no order");
                    }
                } else {
                    showTip(jsonObject.getString("msg"));
                }
            }
        });
        return tmp;
    }

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TradeDetail.this, string, Toast.LENGTH_SHORT).show();
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
