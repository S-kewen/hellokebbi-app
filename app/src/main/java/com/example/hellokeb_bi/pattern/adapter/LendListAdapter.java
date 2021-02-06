package com.example.hellokeb_bi.pattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.hellokeb_bi.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LendListAdapter extends BaseAdapter {
    List<Map<String, Object>> mItemList;
    private LayoutInflater mLayInf;
    private CallBack callBack;

    public LendListAdapter(Context context, List<Map<String, Object>> itemList, LendListAdapter.CallBack callBack) {
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemList = itemList;
        this.callBack = callBack;
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
        view = mLayInf.inflate(R.layout.lend_list, viewGroup, false);
        TextView order_num = (TextView) view.findViewById(R.id.order_num_2);
        TextView locationshow = (TextView) view.findViewById(R.id.locationshow_2);
        TextView dateshow = (TextView) view.findViewById(R.id.dateshow_2);
        TextView moneyshow = (TextView) view.findViewById(R.id.moneyshow);

        order_num.setText("OrderId： " + mItemList.get(position).get("id").toString());
        locationshow.setText(mItemList.get(position).get("address").toString());
        dateshow.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(mItemList.get(position).get("pickup_time")) + "  ~  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(mItemList.get(position).get("expire_time")));
        moneyshow.setText(mItemList.get(position).get("amount").toString());

        Button lendList_return = (Button) view.findViewById(R.id.lendList_return);
        lendList_return.setTag(position);
        lendList_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onClick(mItemList.get((Integer) v.getTag()).get("id").toString());
            }
        });

        return view;
    }

    /**
     * 自定義回调接口
     *
     * @return
     */
    public interface CallBack {
        void onClick(String order_num);
    }


}
