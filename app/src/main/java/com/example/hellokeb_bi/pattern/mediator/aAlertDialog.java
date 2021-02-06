package com.example.hellokeb_bi.pattern.mediator;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class aAlertDialog extends Component {
    private AlertDialog.Builder alertDialog;

    public aAlertDialog(AlertDialog.Builder alertDialog, Mediator m) {
        super(m);
        this.alertDialog = alertDialog;
    }

    void show(String title, String message) {
        alertDialog.setTitle(title).setMessage(message).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
