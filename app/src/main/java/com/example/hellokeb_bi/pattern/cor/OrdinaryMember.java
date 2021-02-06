package com.example.hellokeb_bi.pattern.cor;

import android.content.Context;
import android.widget.ImageView;

public class OrdinaryMember extends DiscountHandler {

    public OrdinaryMember(DiscountHandler next) {
        super(next);
    }

    @Override
    public void grade(int point, ImageView kebbiImg, Context context) {
        if (point >= 0) {
            kebbiImg.setImageResource(context.getResources().getIdentifier("kebbicor_bronze", "drawable", context.getPackageName()));
        } else {
            doNext(point, kebbiImg, context);
        }
    }
}
