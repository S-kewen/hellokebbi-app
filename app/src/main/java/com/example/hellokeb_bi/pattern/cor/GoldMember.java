package com.example.hellokeb_bi.pattern.cor;

import android.content.Context;
import android.widget.ImageView;

public class GoldMember extends DiscountHandler {

    public GoldMember(DiscountHandler next) {
        super(next);
    }

    @Override
    public void grade(int point, ImageView kebbiImg, Context context) {
        if (point >= 2000) {
            kebbiImg.setImageResource(context.getResources().getIdentifier("kebbicor_gold", "drawable", context.getPackageName()));
        } else {
            doNext(point, kebbiImg, context);
        }
    }
}
