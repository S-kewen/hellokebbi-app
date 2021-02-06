package com.example.hellokeb_bi.pattern.cor;

import android.content.Context;
import android.widget.ImageView;

public class DiamondMember extends DiscountHandler {

    public DiamondMember(DiscountHandler next) {
        super(next);
    }

    @Override
    public void grade(int point, ImageView kebbiImg, Context context) {
        if (point >= 5000) {
            kebbiImg.setImageResource(context.getResources().getIdentifier("kebbi_dimand", "drawable", context.getPackageName()));
        } else {
            doNext(point, kebbiImg, context);
        }
    }
}
