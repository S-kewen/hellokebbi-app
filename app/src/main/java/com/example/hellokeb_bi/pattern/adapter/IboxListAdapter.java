package com.example.hellokeb_bi.pattern.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.activity.ConfirmOrder;

import java.util.List;
import java.util.Map;

public class IboxListAdapter extends BaseAdapter {
    List<Map<String, Object>> mItemList;
    Context context;
    private LayoutInflater mLayInf;

    public IboxListAdapter(Context context, List<Map<String, Object>> itemList) {
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
        view = mLayInf.inflate(R.layout.kebbilist_view, viewGroup, false);
        TextView town = (TextView) view.findViewById(R.id.town_tv);
        TextView ibox = (TextView) view.findViewById(R.id.ibox_tv);

        town.setText(mItemList.get(position).get("town").toString());
        ibox.setText(mItemList.get(position).get("ibox").toString());
        Button reserve = (Button) view.findViewById(R.id.kebbiList_reserve);
        reserve.setTag(position);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfirmOrder.class);
                intent.putExtra("startTime", mItemList.get((Integer) v.getTag()).get("startTime").toString());
                intent.putExtra("endTime", mItemList.get((Integer) v.getTag()).get("endTime").toString());
                intent.putExtra("country", mItemList.get((Integer) v.getTag()).get("country").toString());
                intent.putExtra("town", mItemList.get((Integer) v.getTag()).get("town").toString());
                intent.putExtra("ibox", mItemList.get((Integer) v.getTag()).get("ibox").toString());
                intent.putExtra("rid", mItemList.get((Integer) v.getTag()).get("rid").toString());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
