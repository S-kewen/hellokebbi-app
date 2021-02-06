package com.example.hellokeb_bi.pattern.factoryMethod;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.hellokeb_bi.R;

public class AlertDialogFactory implements DialogFactory {
    private Context context;

    public AlertDialogFactory(Context context) {
        this.context = context;
    }

    @Override
    public AlertDialog.Builder createSimpleDialog() {
        return new AlertDialog.Builder(context);
    }

    @Override
    public AlertDialog.Builder createSuccDialog() {
        AlertDialog.Builder result = new AlertDialog.Builder(context);
        result.setIcon(R.mipmap.ic_success);
        return result;
    }

    @Override
    public AlertDialog.Builder createWarnDialog() {
        AlertDialog.Builder result = new AlertDialog.Builder(context);
        result.setIcon(R.mipmap.ic_warn);
        return result;
    }

    @Override
    public AlertDialog.Builder createErrorDialog() {
        AlertDialog.Builder result = new AlertDialog.Builder(context);
        result.setIcon(R.mipmap.ic_error);
        return result;
    }

}
