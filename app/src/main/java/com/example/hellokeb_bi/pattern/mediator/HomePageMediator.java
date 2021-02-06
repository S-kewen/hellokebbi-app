package com.example.hellokeb_bi.pattern.mediator;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hellokeb_bi.activity.Homepage;
import com.example.hellokeb_bi.pattern.singleton.Authorization;

import okhttp3.FormBody;

public class HomePageMediator extends Mediator {
    int[] townlist = {1000, 1001, 1002, 1003, 1004};
    String[] _town = {"Douliu", "Huwei", "Citong", "Linnei", "Gukeng"};
    private aSpinner country;
    private aSpinner town;
    private aTextView startTime;
    private aTextView endTime;
    private aAlertDialog alertDialog;
    private aProgressBar progressBar;
    private aButton button;
    private aNetwork network;


    public HomePageMediator(Context context, Spinner country, Spinner town, TextView startTime, TextView endTime, AlertDialog.Builder alertDialog, ProgressBar progressBar, Button button) {
        super(context);
        this.country = new aSpinner(country, this);
        this.town = new aSpinner(town, this);
        this.startTime = new aTextView(startTime, this);
        this.endTime = new aTextView(endTime, this);
        this.alertDialog = new aAlertDialog(alertDialog, this);
        this.progressBar = new aProgressBar(progressBar, this);
        this.button = new aButton(button, this);
        this.network = new aNetwork(this);
    }

    @Override
    public void handle(String msg) {
        switch (msg) {
            case "country_select":
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, context.getResources().getIdentifier(country.getSelectionText() + "_list", "array", context.getPackageName()), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                town.setAdapter(adapter);
                town.setSelection(0);
                break;
            case "network error!":
                alertDialog.show("Error", "network error!");
                progressBar.show(false);
                break;
            case "network response":
                progressBar.show(false);
                JSONObject jsonObject = network.getJsonObject();
                if (jsonObject.getInteger("code") == 200) {
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                    if (jsonArray.size() > 0) {
                        ((Homepage) context).insertList("Yunlin", _town[town.getSelectionPosition()], ((JSONObject) jsonArray.get(0)).getString("id"));
                    } else {
                        ((Homepage) context).insertEmptyList();
                    }
                } else {
                    alertDialog.show("Error!", jsonObject.getString("msg"));
                }
                break;
            case "press btn":
                if (startTime.getText().equals("Start  time")) {
                    alertDialog.show("Error!", "please select the start time first!");
                    break;
                }
                if (endTime.getText().equals("End  time")) {
                    alertDialog.show("Error!", "please select the end time first!");
                    break;
                }
                if (country.getSelectionText().length() == 0) {
                    alertDialog.show("Error!", "please select the country!");
                    break;
                }
                if (town.getSelectionText().length() == 0) {
                    alertDialog.show("Error!", "please select the town!");
                    break;
                }

                FormBody formBody = new FormBody.Builder()
                        .add("state", "1")
                        .add("region", String.valueOf(townlist[town.getSelectionPosition()]))
                        .build();
                progressBar.show(true);
                network.post("https://hellokebbi-api.iskwen.com/v1/api/robot/getRobotList", Authorization.getInstance().getToken(),
                        formBody);
                break;
        }
    }

}
