package com.example.hellokeb_bi.pattern.mediator;

import android.widget.TextView;

public class aTextView extends Component {

    private TextView textView;

    public aTextView(TextView textView, Mediator m) {
        super(m);
        this.textView = textView;
    }

    String getText() {
        return this.textView.getText().toString();
    }

}
