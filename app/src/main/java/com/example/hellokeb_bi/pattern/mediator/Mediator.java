package com.example.hellokeb_bi.pattern.mediator;

import android.content.Context;

public abstract class Mediator {
    Context context;

    Mediator(Context context) {
        this.context = context;
    }

    public void login() {

    }

    public abstract void handle(String msg);

}
