package com.example.hellokeb_bi.pattern.mediator;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class aSpinner extends Component {

    private Spinner spinner;

    public aSpinner(Spinner spinner, Mediator m) {
        super(m);
        this.spinner = spinner;
    }

    int getSelectionPosition() {
        return this.spinner.getSelectedItemPosition();
    }

    String getSelectionText() {
        return spinner.getSelectedItem().toString();
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSelection(int i) {
        spinner.setSelection(i);
    }
}
