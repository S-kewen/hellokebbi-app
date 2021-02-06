package com.example.hellokeb_bi.pattern.cor;

import android.content.Context;
import android.widget.ImageView;

public abstract class DiscountHandler {
    private DiscountHandler next;

    public DiscountHandler(DiscountHandler next) {
        this.next = next;
    }

    public abstract void grade(int point, ImageView kebbiImg, Context context);

    void doNext(int point, ImageView kebbiImg, Context context) {
        if (next != null) {
            next.grade(point, kebbiImg, context);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
