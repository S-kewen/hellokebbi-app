package com.example.hellokeb_bi.pattern.mediator;

import android.view.View;
import android.widget.Button;

public class aButton extends Component {

    private Button button;

    public aButton(Button button, Mediator m) {
        super(m);
        this.button = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediator.handle("press btn");
            }
        });
    }

}
