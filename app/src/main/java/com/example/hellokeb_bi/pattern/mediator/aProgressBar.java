package com.example.hellokeb_bi.pattern.mediator;

import android.view.View;
import android.widget.ProgressBar;

public class aProgressBar extends Component {

    private ProgressBar progressBar;

    public aProgressBar(ProgressBar progressBar, Mediator m) {
        super(m);
        this.progressBar = progressBar;
    }

    void show(boolean state) {
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                if (state) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}
