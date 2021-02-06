package com.example.hellokeb_bi.pattern.strategy;

import com.example.hellokeb_bi.entity.RentRecord;

import java.util.HashMap;
import java.util.List;

public interface SortRentRecordStrategy {
    public List<String> sort(HashMap<String, RentRecord> records, boolean ascending);
}
