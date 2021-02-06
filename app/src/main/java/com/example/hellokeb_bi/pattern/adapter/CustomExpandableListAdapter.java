package com.example.hellokeb_bi.pattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hellokeb_bi.R;
import com.example.hellokeb_bi.entity.RentRecord;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitulo;
    private HashMap<String, RentRecord> expandableListDetalles;

    public CustomExpandableListAdapter(Context context,
                                       List<String> listTitulo,
                                       HashMap<String, RentRecord> expandableListDetalles) {
        this.context = context;
        this.listTitulo = listTitulo;
        this.expandableListDetalles = expandableListDetalles;
    }


    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final RentRecord rentRecord = (RentRecord) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }


        LinearLayout layoutType = convertView.findViewById(R.id.type_layout);
        LinearLayout layoutMoney = convertView.findViewById(R.id.money_layout);
        LinearLayout layoutTime = convertView.findViewById(R.id.time_layout);
        LinearLayout layoutContent = convertView.findViewById(R.id.content_layout);

        TextView trade_type = convertView.findViewById(R.id.trade_type);
        TextView trade_money = convertView.findViewById(R.id.trade_money);
        TextView trade_time = convertView.findViewById(R.id.trade_time);
        TextView trade_content = convertView.findViewById(R.id.trade_content);

        if (rentRecord.getType() == 1) {
            trade_type.setText("Type: Expenditure");
        } else {
            trade_type.setText("Type: Refund");
        }

        trade_money.setText("Amount: " + rentRecord.getAmount() + " NTD");
        trade_time.setText("Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(rentRecord.getAdd_time()));
        trade_content.setText("Msg: " + rentRecord.getMsg());

        layoutType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Type: "
                        + rentRecord.getType(), Toast.LENGTH_SHORT).show();
            }
        });

        layoutMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Money: "
                        + rentRecord.getAmount(), Toast.LENGTH_SHORT).show();
            }
        });

        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Time: "
                        + rentRecord.getAdd_time(), Toast.LENGTH_SHORT).show();
            }
        });

        layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Content: "
                        + rentRecord.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        convertView.startAnimation(animation);


        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        String OrderNumber = (String) getGroup(groupPosition);
        RentRecord rentRecord = (RentRecord) getChild(groupPosition, 0);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        TextView txtOrderNumber = convertView.findViewById(R.id.txtOrderId);
        TextView trade_title = convertView.findViewById(R.id.textView5);

        txtOrderNumber.setText("ID: " + rentRecord.getId());
        trade_title.setText(rentRecord.getTitle());

        return convertView;
    }


    @Override
    public int getGroupCount() {
        return this.listTitulo.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listTitulo.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetalles.get(this.listTitulo.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
