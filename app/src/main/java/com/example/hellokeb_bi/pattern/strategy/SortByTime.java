package com.example.hellokeb_bi.pattern.strategy;

import com.example.hellokeb_bi.entity.RentRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SortByTime implements SortRentRecordStrategy {
    @Override
    public List<String> sort(HashMap<String, RentRecord> records, boolean ascending) {
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<RentRecord> values = new ArrayList<>();

        for (String key : records.keySet()) {
            keys.add(key);
            values.add(records.get(key));
        }

        for (int i = 0; i < keys.size(); i++) {
            int targetIndex = -1;
            String targetValue = "";
            boolean first = true;
            for (int j = i; j < keys.size(); j++) {
                if (ascending) {
                    //new Date().after(values.get(j).getAdd_time());
                    if (first) {
                        targetValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time());
                        targetIndex = j;
                        first = false;
                    } else {
                        if (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time()).compareTo(targetValue) < 0) {
                            targetValue = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time()));
                            targetIndex = j;
                        }
                    }
                } else {
                    if (first) {
                        targetValue = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time()));
                        targetIndex = j;
                        first = false;
                    } else {
                        if (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time()).compareTo(targetValue) > 0) {
                            targetValue = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.TAIWAN).format(values.get(j).getAdd_time()));
                            targetIndex = j;
                        }
                    }
                }
            }

            String tmp = keys.get(i);
            keys.set(i, keys.get(targetIndex));
            keys.set(targetIndex, tmp);

            RentRecord tmp2 = values.get(i);
            values.set(i, values.get(targetIndex));
            values.set(targetIndex, tmp2);
        }

        return keys;
    }

}
