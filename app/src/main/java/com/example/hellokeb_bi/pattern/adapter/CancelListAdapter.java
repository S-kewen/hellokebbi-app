package com.example.hellokeb_bi.pattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hellokeb_bi.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CancelListAdapter extends BaseAdapter {
    private List<Map<String, Object>> mItemList;
    private Context context;
    private LayoutInflater mLayInf;

    public CancelListAdapter(Context context, List<Map<String, Object>> itemList) {
        this.context = context;
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        //取得 ListView 列表 Item 的數量
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        //取得 ListView 列表於 position 位置上的 Item
        return position;
    }

    @Override
    public long getItemId(int position) {
        //取得 ListView 列表於 position 位置上的 Item 的 ID
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //設定與回傳 convertView 作為顯示在這個 position 位置的 Item 的 View。
        view = mLayInf.inflate(R.layout.cancel_list, viewGroup, false);
        TextView order_num = (TextView) view.findViewById(R.id.order_num_3);
        TextView locationshow = (TextView) view.findViewById(R.id.locationshow_3);
        TextView dateshow = (TextView) view.findViewById(R.id.dateshow_3);

        order_num.setText("OrderId： " + mItemList.get(position).get("id").toString());
        locationshow.setText(mItemList.get(position).get("address").toString());
        dateshow.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(mItemList.get(position).get("pickup_time")));

        return view;
    }
}
