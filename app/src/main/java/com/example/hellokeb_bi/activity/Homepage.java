package com.example.hellokeb_bi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.pattern.adapter.IboxListAdapter;
import com.example.hellokeb_bi.pattern.factoryMethod.AlertDialogFactory;
import com.example.hellokeb_bi.pattern.factoryMethod.DialogFactory;
import com.example.hellokeb_bi.pattern.mediator.HomePageMediator;
import com.example.hellokeb_bi.pattern.mediator.Mediator;
import com.yongbeom.aircalendar.AirCalendarDatePickerActivity;
import com.yongbeom.aircalendar.core.AirCalendarIntent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Homepage extends AppCompatActivity {
    private final static int REQUEST_CODE = 1;

    private Spinner mSpn;
    private Spinner mSpn2;
    private TextView startTime_tv, endTime_tv;
    private ListView kebbiList;
    private Button homepage_search;
    private ProgressBar progressBar;
    private IboxListAdapter iboxListAdapter;
    private boolean checkInfo = false;
    private Mediator mediator;
    private AlertDialog.Builder dialog;

    private AdapterView.OnItemSelectedListener spnOnItemSelected
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            // TODO Auto-generated method stub
            mediator.handle("country_select");
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        init();

    }

    public void init() {
        homepage_search = (Button) findViewById(R.id.homepage_search);

        mSpn = (Spinner) findViewById(R.id.spn);
        mSpn.setOnItemSelectedListener(spnOnItemSelected);
        mSpn.setSelection(0, false);

        mSpn2 = (Spinner) findViewById(R.id.spn2);

        startTime_tv = (TextView) findViewById(R.id.start_time_tv);
        endTime_tv = (TextView) findViewById(R.id.end_time_tv);
        kebbiList = (ListView) findViewById(R.id.kebbilist);
        progressBar = (ProgressBar) findViewById(R.id.homepage_load);
        homepage_search = (Button) findViewById(R.id.homepage_search);

        DialogFactory dialogFactory = new AlertDialogFactory(this);

        dialog = dialogFactory.createErrorDialog();

        mediator = new HomePageMediator(this, mSpn, mSpn2, startTime_tv, endTime_tv, dialog, progressBar, homepage_search);
    }

    public void selectTime(View v) {
        AirCalendarIntent intent = new AirCalendarIntent(this);
        intent.isBooking(false);
        intent.isSelect(false);
        intent.isMonthLabels(false);
        intent.setSelectButtonText("Select"); //the select button text
        intent.setResetBtnText("Reset"); //the reset button text
        intent.setWeekStart(Calendar.MONDAY);
        intent.setWeekDaysLanguage(AirCalendarIntent.Language.EN); //language for the weekdays

        ArrayList<String> weekDay = new ArrayList<>();
        weekDay.add("M");
        weekDay.add("T");
        weekDay.add("W");
        weekDay.add("T");
        weekDay.add("F");
        weekDay.add("S");
        weekDay.add("S");
        intent.setCustomWeekDays(weekDay); //custom weekdays

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                checkInfo = true;
                startTime_tv.setText(data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_START_DATE));
                endTime_tv.setText(data.getStringExtra(AirCalendarDatePickerActivity.RESULT_SELECT_END_DATE));
            }
        }
    }

    public void onHomepageMainClick(View view) {
        showTip("Hello I'm Kebbi~");

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
                        intent.setClass(Homepage.this, AccountManager.class);
                        startActivity(intent);
                        break;

                    case R.id.credit:
                        popup.dismiss();
                        Intent intent2 = new Intent();
                        intent2.setClass(Homepage.this, CreditCard.class);
                        startActivity(intent2);
                        break;

                    case R.id.record:
                        popup.dismiss();
                        Intent intent3 = new Intent();
                        intent3.setClass(Homepage.this, RentHistory.class);
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
                        intent.setClass(Homepage.this, TradeDetail.class);
                        startActivity(intent);
                        break;

                    case R.id.customerser:
                        popup.dismiss();
                        Intent intent2 = new Intent();
                        intent2.setClass(Homepage.this, CustomerService.class);
                        startActivity(intent2);
                        break;

                    case R.id.hellokebbi:
                        popup.dismiss();
                        Intent intent3 = new Intent();
                        intent3.setClass(Homepage.this, AboutHelloKebbi.class);
                        startActivity(intent3);
                        break;

                }
                // TODO Auto-generated method stub
                return false;
            }

        });
        popup.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return true;
    }

    public void insertList(String country, String town, String rid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
                String[] ibox = null;
                try {
                    Field idField = null;
                    idField = R.array.class.getDeclaredField(town);
                    ibox = getResources().getStringArray(idField.getInt(idField));
                } catch (Exception e) {
                }

                for (int i = 0; i < ibox.length; i++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("country", country);
                    item.put("town", town);
                    item.put("ibox", ibox[i]);
                    item.put("startTime", startTime_tv.getText().toString());
                    item.put("endTime", endTime_tv.getText().toString());
                    item.put("rid", rid);
                    itemList.add(item);
                    System.out.println(item);
                }


                iboxListAdapter = new IboxListAdapter(Homepage.this, itemList);
                kebbiList.setAdapter(iboxListAdapter);
            }
        });
    }

    public void insertEmptyList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();

                iboxListAdapter = new IboxListAdapter(Homepage.this, itemList);
                kebbiList.setAdapter(iboxListAdapter);
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

    private void showTip(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Homepage.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
