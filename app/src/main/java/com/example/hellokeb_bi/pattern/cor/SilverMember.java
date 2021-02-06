package com.example.hellokeb_bi.pattern.cor;

import android.content.Context;
import android.widget.ImageView;

public class SilverMember extends DiscountHandler {

    public SilverMember(DiscountHandler next) {
        super(next);
    }

    @Override
    public void grade(int point, ImageView kebbiImg, Context context) {
        if (point >= 1000) {
            kebbiImg.setImageResource(context.getResources().getIdentifier("kebbicor_silver", "drawable", context.getPackageName()));
        } else {
            doNext(point, kebbiImg, context);
        }
    }
}
