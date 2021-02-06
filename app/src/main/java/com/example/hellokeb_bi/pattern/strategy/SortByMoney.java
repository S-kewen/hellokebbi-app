package com.example.hellokeb_bi.pattern.strategy;

import com.example.hellokeb_bi.entity.RentRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SortByMoney implements SortRentRecordStrategy {
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
            int targetValue = -1;
            boolean first = true;
            for (int j = i; j < keys.size(); j++) {
                if (ascending) {
                    if (first) {
                        targetValue = values.get(j).getAmount();
                        targetIndex = j;
                        first = false;
                    } else {
                        if (values.get(j).getAmount() < targetValue) {
                            targetValue = values.get(j).getAmount();
                            targetIndex = j;
                        }
                    }
                } else {
                    if (first) {
                        targetValue = values.get(j).getAmount();
                        targetIndex = j;
                        first = false;
                    } else {
                        if (values.get(j).getAmount() > targetValue) {
                            targetValue = values.get(j).getAmount();
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
